package org.jeecg.modules.smartPremaritalFiling.controller;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.ObjectUtil;
import org.jeecg.common.api.CommonAPI;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.modules.common.service.CommonService;
import org.jeecg.modules.common.util.ParamsUtil;
import org.jeecg.modules.smartPostMarriage.service.ISmartPostMarriageReportService;
import org.jeecg.modules.smartExportWord.util.WordUtils;
import org.jeecg.modules.tasks.smartVerifyTask.service.SmartVerify;
import org.jeecg.modules.tasks.taskType.service.ISmartVerifyTypeService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.smartPremaritalFiling.entity.SmartPremaritalFilingApp;
import org.jeecg.modules.smartPremaritalFiling.entity.SmartPremaritalFiling;
import org.jeecg.modules.smartPremaritalFiling.vo.SmartPremaritalFilingPage;
import org.jeecg.modules.smartPremaritalFiling.service.ISmartPremaritalFilingService;
import org.jeecg.modules.smartPremaritalFiling.service.ISmartPremaritalFilingAppService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 8项规定婚前报备表
 * @Author: jeecg-boot
 * @Date: 2021-11-13
 * @Version: V1.0
 */
@Api(tags = "8项规定婚前报备表")
@RestController
@RequestMapping("/smartPremaritalFiling/smartPremaritalFiling")
@Slf4j
public class SmartPremaritalFilingController {
    @Autowired
    private ISysBaseAPI sysBaseAPI;
    @Autowired
    private ISmartPremaritalFilingService smartPremaritalFilingService;
    @Autowired
    private ISmartPremaritalFilingAppService smartPremaritalFilingAppService;
    @Autowired
    private BaseCommonService baseCommonService;
    @Autowired
    CommonService commonService;

    @Autowired
    ISmartPostMarriageReportService smartPostMarriageReportService;

    @Autowired
    private SmartVerify smartVerify;
    @Autowired
    private ISmartVerifyTypeService smartVerifyTypeService;

    public String verifyType = "婚前报备";


    @AutoLog(value = "更新文件下载次数")
    @ApiOperation(value = "更新文件下载次数", notes = "更新文件下载次数")
    @PutMapping(value = "/downloadCount")
    public Result<?> edit(@RequestBody SmartPremaritalFilingApp testAttachedFile) {
        SmartPremaritalFilingApp newTestAttachedFile = smartPremaritalFilingAppService.getById(testAttachedFile.getId());
        int currentCount = newTestAttachedFile.getDownloadCount();
        newTestAttachedFile.setDownloadCount(currentCount + 1);
        smartPremaritalFilingAppService.updateById(newTestAttachedFile);
        return Result.OK("更新成功!");
    }

    /**
     * 分页列表查询
     *
     * @param smartPremaritalFiling
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "8项规定婚前报备表-分页列表查询")
    @ApiOperation(value = "8项规定婚前报备表-分页列表查询", notes = "8项规定婚前报备表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SmartPremaritalFiling smartPremaritalFiling,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        // 获取登录用户信息，可以用来查询单位部门信息
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String username = sysUser.getUsername();
        // 获取用户角色
        List<String> role = sysBaseAPI.getRolesByUsername(username);
        Page<SmartPremaritalFiling> page = new Page<SmartPremaritalFiling>(pageNo, pageSize);
        // 如果是普通用户，则只能看到自己创建的数据
        if (role.contains("CommonUser")) {
            QueryWrapper<SmartPremaritalFiling> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("create_by", username);
            IPage<SmartPremaritalFiling> pageList = smartPremaritalFilingService.page(page, queryWrapper);
            return Result.OK(pageList);
        } else {
            // 1. 规则，下面是 以**开始
            String rule = "in";
            // 2. 查询字段
            String field = "departId";
            // 获取子单位ID
            String childrenIdString = commonService.getChildrenIdStringByOrgCode(sysUser.getOrgCode());

            HashMap<String, String[]> map = new HashMap<>(req.getParameterMap());
            // 获取请求参数中的superQueryParams
            List<String> paramsList = ParamsUtil.getSuperQueryParams(req.getParameterMap());

            // 添加额外查询条件，用于权限控制
            paramsList.add("%5B%7B%22rule%22:%22" + rule + "%22,%22type%22:%22string%22,%22dictCode%22:%22%22,%22val%22:%22"
                    + childrenIdString
                    + "%22,%22field%22:%22" + field + "%22%7D%5D");
            String[] params = new String[paramsList.size()];
            paramsList.toArray(params);
            map.put("superQueryParams", params);
            params = new String[]{"and"};
            map.put("superQueryMatchType", params);

            // TODO：3. 修改自己函数中这一部门，封装查询参数修改为我们的 map
            QueryWrapper<SmartPremaritalFiling> queryWrapper = QueryGenerator.initQueryWrapper(smartPremaritalFiling, map);
            //QueryWrapper<SmartPremaritalFiling> queryWrapper = QueryGenerator.initQueryWrapper(smartPremaritalFiling, req.getParameterMap());
            IPage<SmartPremaritalFiling> pageList = smartPremaritalFilingService.page(page, queryWrapper);
            // 请同步修改edit函数中，将departId变为null，不然会更新成名称
            List<String> departIds = pageList.getRecords().stream().map(SmartPremaritalFiling::getDepartId).collect(Collectors.toList());
            if (departIds != null && departIds.size() > 0) {
                Map<String, String> useDepNames = commonService.getDepNamesByIds(departIds);
                pageList.getRecords().forEach(item -> {
                    item.setDepartId(useDepNames.get(item.getDepartId()));
                });
            }
            log.info("新的开始" + String.valueOf(pageList));
            return Result.OK(pageList);
        }
    }

    /**
     * 添加
     *
     * @param smartPremaritalFilingPage
     * @return
     */
    @AutoLog(value = "8项规定婚前报备表-添加")
    @ApiOperation(value = "8项规定婚前报备表-添加", notes = "8项规定婚前报备表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SmartPremaritalFilingPage smartPremaritalFilingPage) {
        //审核功能
        smartVerify.addVerifyRecord(smartPremaritalFilingPage.getId(), verifyType);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String orgCode = sysUser.getOrgCode();
        if ("".equals(orgCode)) {
            return Result.error("本用户没有操作权限！");
        }
        String id = smartPremaritalFilingService.getDepartIdByOrgCode(orgCode);
        smartPremaritalFilingPage.setDepartId(id);
        SmartPremaritalFiling smartPremaritalFiling = new SmartPremaritalFiling();
        BeanUtils.copyProperties(smartPremaritalFilingPage, smartPremaritalFiling);
//        smartPremaritalFilingService.saveMain(smartPremaritalFiling, smartPremaritalFilingPage.getSmartPremaritalFilingAppList());
//        smartVerify.addVerifyRecord(smartPremaritalFiling.getId(), verifyType);


        //审核状态
        Boolean isVerify = smartVerifyTypeService.getIsVerifyStatusByType(verifyType);
        if (isVerify) {
            smartPremaritalFilingService.saveMain(smartPremaritalFiling, smartPremaritalFilingPage.getSmartPremaritalFilingAppList());
            String recordId = smartPremaritalFiling.getId();
            smartVerify.addVerifyRecord(recordId, verifyType);
            smartPremaritalFiling.setVerifyStatus(smartVerify.getFlowStatusById(recordId).toString());
            smartPremaritalFilingService.updateById(smartPremaritalFiling);
        } else { // 设置审核状态为免审
            smartPremaritalFiling.setVerifyStatus("3"); // 直接添加，不走审核流程
            smartPremaritalFilingService.saveMain(smartPremaritalFiling, smartPremaritalFilingPage.getSmartPremaritalFilingAppList());
        }
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param smartPremaritalFilingPage
     * @return
     */
    @AutoLog(value = "8项规定婚前报备表-编辑")
    @ApiOperation(value = "8项规定婚前报备表-编辑", notes = "8项规定婚前报备表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SmartPremaritalFilingPage smartPremaritalFilingPage) {
        SmartPremaritalFiling smartPremaritalFiling = new SmartPremaritalFiling();
        BeanUtils.copyProperties(smartPremaritalFilingPage, smartPremaritalFiling);
        SmartPremaritalFiling smartPremaritalFilingEntity = smartPremaritalFilingService.getById(smartPremaritalFiling.getId());
        if (smartPremaritalFilingEntity == null) {
            return Result.error("未找到对应数据");
        }

        smartPremaritalFiling.setDepartId(null);
        smartPremaritalFiling.setCreateTime(null);
        smartPremaritalFilingService.updateMain(smartPremaritalFiling, smartPremaritalFilingPage.getSmartPremaritalFilingAppList());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "8项规定婚前报备表-通过id删除")
    @ApiOperation(value = "8项规定婚前报备表-通过id删除", notes = "8项规定婚前报备表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {

        //根据preId设置婚后del_flag = "1"
        smartPostMarriageReportService.setDelFlagByPreId(id);

        //删除数据
        smartPremaritalFilingService.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "8项规定婚前报备表-批量删除")
    @ApiOperation(value = "8项规定婚前报备表-批量删除", notes = "8项规定婚前报备表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {

        //根据preId设置婚后del_flag = "1"
        List<String> tem = Arrays.asList(ids.split(","));
        for(String s : tem){
            smartPostMarriageReportService.setDelFlagByPreId(s);
        }

        this.smartPremaritalFilingService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "8项规定婚前报备表-通过id查询")
    @ApiOperation(value = "8项规定婚前报备表-通过id查询", notes = "8项规定婚前报备表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SmartPremaritalFiling smartPremaritalFiling = smartPremaritalFilingService.getById(id);
        if (smartPremaritalFiling == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(smartPremaritalFiling);

    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "8项规定婚前报备表附表通过主表ID查询")
    @ApiOperation(value = "8项规定婚前报备表附表主表ID查询", notes = "8项规定婚前报备表附表-通主表ID查询")
    @GetMapping(value = "/querySmartPremaritalFilingAppByMainId")
    public Result<?> querySmartPremaritalFilingAppListByMainId(@RequestParam(name = "id", required = true) String
                                                                       id) {
        List<SmartPremaritalFilingApp> smartPremaritalFilingAppList = smartPremaritalFilingAppService.selectByMainId(id);
        return Result.OK(smartPremaritalFilingAppList);
    }


    /**
     * 导出excel
     *
     * @param req
     * @param smartPremaritalFiling
     */
//    @RequestMapping(value = "/exportXls")
//    public ModelAndView exportXls(HttpServletRequest req, HttpServletResponse response, SmartPremaritalFiling smartPremaritalFiling) throws Exception {
//        // Step.1 组装查询条件查询数据
//        //QueryWrapper<SmartPremaritalFiling> queryWrapper = QueryGenerator.initQueryWrapper(smartPremaritalFiling, request.getParameterMap());
//        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
//        String username = sysUser.getUsername();
//
//        //获取用户角色
//        List<String> role = sysBaseAPI.getRolesByUsername(username);
//        List<SmartPremaritalFiling> queryList = new ArrayList<SmartPremaritalFiling>();
//
//        // 如果是普通用户，则只能看到自己创建的数据
//        if (role.contains("CommonUser")) {
//            QueryWrapper<SmartPremaritalFiling> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("create_by", username);
//            queryList = smartPremaritalFilingService.list(queryWrapper);
//        } else {
//            // 1. 规则，下面是 以**开始
//            String rule = "in";
//            // 2. 查询字段
//            String field = "departId";
//            // 获取子单位ID
//            String childrenIdString = commonService.getChildrenIdStringByOrgCode(sysUser.getOrgCode());
//            HashMap<String, String[]> map = new HashMap<>(req.getParameterMap());
//            // 获取请求参数中的superQueryParams
//            List<String> paramsList = ParamsUtil.getSuperQueryParams(req.getParameterMap());
//            // 添加额外查询条件，用于权限控制
//            paramsList.add("%5B%7B%22rule%22:%22" + rule + "%22,%22type%22:%22string%22,%22dictCode%22:%22%22,%22val%22:%22 " + childrenIdString + "%22,%22field%22:%22" + field + "%22%7D%5D");
//            String[] params = new String[paramsList.size()];
//            paramsList.toArray(params);
//            map.put("superQueryParams", params);
//            params = new String[]{"and"};
//            map.put("superQueryMatchType", params);
//            QueryWrapper<SmartPremaritalFiling> queryWrapper = QueryGenerator.initQueryWrapper(smartPremaritalFiling, map);
//            queryList = smartPremaritalFilingService.list(queryWrapper);
//        }
//
//
//        //Step.2 获取导出数据
//        // 过滤选中数据
//        String selections = req.getParameter("selections");
//        log.info(String.valueOf(req));
//        List<SmartPremaritalFiling> smartPremaritalFilingList = new ArrayList<SmartPremaritalFiling>();
//        if (oConvertUtils.isEmpty(selections)) {
//            smartPremaritalFilingList = queryList;
//
//        } else {
//            List<String> selectionList = Arrays.asList(selections.split(","));
//            smartPremaritalFilingList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
//
//        }
//
//
//        // Step.3 组装pageList
//        List<SmartPremaritalFilingPage> pageList = new ArrayList<SmartPremaritalFilingPage>();
//        for (SmartPremaritalFiling main : smartPremaritalFilingList) {
//            SmartPremaritalFilingPage vo = new SmartPremaritalFilingPage();
//            BeanUtils.copyProperties(main, vo);
//            List<SmartPremaritalFilingApp> smartPremaritalFilingAppList = smartPremaritalFilingAppService.selectByMainId(main.getId());
//            vo.setSmartPremaritalFilingAppList(smartPremaritalFilingAppList);
//            pageList.add(vo);
//        }
//
//        // Step.4 AutoPoi 导出Excel
//        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
//        mv.addObject(NormalExcelConstants.FILE_NAME, "8项规定婚前报备表列表");
//        mv.addObject(NormalExcelConstants.CLASS, SmartPremaritalFilingPage.class);
//        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("8项规定婚前报备表数据", "导出人:" + sysUser.getRealname(), "8项规定婚前报备表"));
//        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
//
//        // List深拷贝，否则返回前端会没数据
//        List<SmartPremaritalFilingPage> newPageList = ObjectUtil.cloneByStream(pageList);
//        baseCommonService.addExportLog(mv.getModel(), "8项规定婚前报备", req, response);
//        mv.addObject(NormalExcelConstants.DATA_LIST, newPageList);
//
//        return mv;
//    }
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest req, HttpServletResponse response, SmartPremaritalFiling smartPremaritalFiling) throws Exception {
        // 获取登录用户信息，可以用来查询单位部门信息
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String username = sysUser.getUsername();
        // 获取用户角色
        List<String> role = sysBaseAPI.getRolesByUsername(username);


        List<SmartPremaritalFiling> queryList = new ArrayList<SmartPremaritalFiling>();
        // 如果是普通用户，则只能看到自己创建的数据
        if (role.contains("CommonUser")) {
            QueryWrapper<SmartPremaritalFiling> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("create_by", username);
            queryList = smartPremaritalFilingService.list(queryWrapper);
        } else {
            // 1. 规则，下面是 以**开始
            String rule = "in";
            // 2. 查询字段
            String field = "departId";
            // 获取子单位ID
            String childrenIdString = commonService.getChildrenIdStringByOrgCode(sysUser.getOrgCode());

            HashMap<String, String[]> map = new HashMap<>(req.getParameterMap());
            // 获取请求参数中的superQueryParams
            List<String> paramsList = ParamsUtil.getSuperQueryParams(req.getParameterMap());

            // 添加额外查询条件，用于权限控制
            paramsList.add("%5B%7B%22rule%22:%22" + rule + "%22,%22type%22:%22string%22,%22dictCode%22:%22%22,%22val%22:%22"
                    + childrenIdString
                    + "%22,%22field%22:%22" + field + "%22%7D%5D");
            String[] params = new String[paramsList.size()];
            paramsList.toArray(params);
            map.put("superQueryParams", params);
            params = new String[]{"and"};
            map.put("superQueryMatchType", params);

            // TODO：3. 修改自己函数中这一部门，封装查询参数修改为我们的 map
            QueryWrapper<SmartPremaritalFiling> queryWrapper = QueryGenerator.initQueryWrapper(smartPremaritalFiling, map);
            //QueryWrapper<SmartPremaritalFiling> queryWrapper = QueryGenerator.initQueryWrapper(smartPremaritalFiling, req.getParameterMap());
            queryList = smartPremaritalFilingService.list(queryWrapper);
        }
// Step.1 组装查询条件查询数据
// Step.2 获取导出数据
// 过滤选中数据
            String selections = req.getParameter("selections");
            List<SmartPremaritalFiling> smartPremaritalFilingList = new ArrayList<SmartPremaritalFiling>();
            if (oConvertUtils.isEmpty(selections)) {
                smartPremaritalFilingList = queryList;
            } else {
                List<String> selectionList = Arrays.asList(selections.split(","));
                smartPremaritalFilingList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
            }// Step.3 组装pageList
            List<SmartPremaritalFilingPage> pageList = new ArrayList<SmartPremaritalFilingPage>();
            for (
                    SmartPremaritalFiling main : smartPremaritalFilingList) {
                SmartPremaritalFilingPage vo = new SmartPremaritalFilingPage();
                BeanUtils.copyProperties(main, vo);
                List<SmartPremaritalFilingApp> smartCreateAdviceAnnexList = smartPremaritalFilingAppService.selectByMainId(main.getId());
                vo.setSmartPremaritalFilingAppList(smartCreateAdviceAnnexList);
                pageList.add(vo);
            }
            // Step.4 AutoPoi 导出Excel
            ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
            mv.addObject(NormalExcelConstants.FILE_NAME, "八项规定婚前报备表");
            mv.addObject(NormalExcelConstants.CLASS, SmartPremaritalFilingPage.class);
            mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("八项规定婚前报备表数据", "导出人:" + sysUser.getRealname(), "八项规定婚前报备表"));
            mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
// List深拷贝，否则返回前端会没数据
            List<SmartPremaritalFilingPage> newPageList = ObjectUtil.cloneByStream(pageList);
            baseCommonService.addExportLog(mv.getModel(), "八项规定婚前报备", req, response);
            mv.addObject(NormalExcelConstants.DATA_LIST, newPageList);
            return mv;
        }


        /**
         * 通过excel导入数据
         *
         * @param request
         * @param response
         * @return
         */
        @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
        public Result<?> importExcel (HttpServletRequest request, HttpServletResponse response){
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
            for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
                MultipartFile file = entity.getValue();// 获取上传文件对象
                ImportParams params = new ImportParams();
                params.setTitleRows(2);
                params.setHeadRows(1);
                params.setNeedSave(true);
                try {
                    List<SmartPremaritalFilingPage> list = ExcelImportUtil.importExcel(file.getInputStream(), SmartPremaritalFilingPage.class, params);
                    for (SmartPremaritalFilingPage page : list) {
                        SmartPremaritalFiling po = new SmartPremaritalFiling();
                        BeanUtils.copyProperties(page, po);
                        smartPremaritalFilingService.saveMain(po, page.getSmartPremaritalFilingAppList());
                    }
                    return Result.OK("文件导入成功！数据行数:" + list.size());
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    return Result.error("文件导入失败:" + e.getMessage());
                } finally {
                    try {
                        file.getInputStream().close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return Result.OK("文件导入失败！");
        }

    @GetMapping(value = "/exportWord")
    public void test01(@RequestParam(name = "ids", required = true) String ids, HttpServletResponse response, HttpServletRequest request) {
//        ids = "1476047132190715905";

        //获取需要的数据
        List<String> idsList = Arrays.asList(ids.split(","));
        List<SmartPremaritalFiling> smartPremaritalFilings = smartPremaritalFilingService.listByIds(idsList);

        //存放数据map
        List<Map<String, Object>> dataList = new ArrayList<>();
        //存放文件名
        List<String> fileNamesList = new ArrayList<>();
        for (int i = 0; i < smartPremaritalFilings.size(); i++) {
            //数据完善
            if (smartPremaritalFilings.get(i).getPeopleSex() != null && smartPremaritalFilings.get(i).getPeopleSex().equals("2")) {
                smartPremaritalFilings.get(i).setPeopleSex("女");
            } else if (smartPremaritalFilings.get(i).getPeopleSex() != null && smartPremaritalFilings.get(i).getPeopleSex().equals("1")) {
                smartPremaritalFilings.get(i).setPeopleSex("男");
            }

            //部门名称
            List<String> departids = new ArrayList<>();
            departids.add(smartPremaritalFilings.get(i).getDepartId());
            Map<String, String> departNames = commonService.getDepNamesByIds(departids);
            smartPremaritalFilings.get(i).setDepartId(departNames.get(smartPremaritalFilings.get(i).getDepartId()));

            //设置数据
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("obj", smartPremaritalFilings.get(i));
            dataList.add(dataMap);

            //文件名，注意于数据对应
            String fileName = smartPremaritalFilings.get(i).getPeopleName();
            fileNamesList.add(fileName);
        }

        //设置模板
        String ftlTemplateName = "/templates/SmartPremaritalFiling.ftl";
        WordUtils.preExportWordBatch(dataList, fileNamesList, ftlTemplateName, response, request);
    }

    }
