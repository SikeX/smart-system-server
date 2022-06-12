package org.jeecg.modules.smartFinanceResult.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.modules.common.service.CommonService;
import org.jeecg.modules.common.util.ParamsUtil;
import org.jeecg.modules.smartFinanceResult.entity.SmartFinanceAnnex;
import org.jeecg.modules.smartFinanceResult.entity.SmartFinanceResult;
import org.jeecg.modules.smartFinanceResult.service.ISmartFinanceAnnexService;
import org.jeecg.modules.smartFinanceResult.service.ISmartFinanceResultService;
import org.jeecg.modules.smartFinanceResult.vo.SmartFinanceResultPage;
import org.jeecg.modules.tasks.smartVerifyTask.service.SmartVerify;
import org.jeecg.modules.tasks.taskType.service.ISmartVerifyTypeService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 8项规定财物收支表
 * @Author: jeecg-boot
 * @Date: 2021-11-10
 * @Version: V1.0
 */
@Api(tags = "8项规定财物收支表")
@RestController
@RequestMapping("/smartFinanceResult/smartFinanceResult")
@Slf4j
public class SmartFinanceResultController {
    @Autowired
    private ISmartFinanceResultService smartFinanceResultService;
    @Autowired
    private ISmartFinanceAnnexService smartFinanceAnnexService;
    /**
     * 审核
     */
    @Autowired
    private SmartVerify smartVerify;
    @Autowired
    private ISmartVerifyTypeService smartVerifyTypeService;
    public String verifyType = "财务收支";
    @Autowired
    CommonService commonService;
    @Autowired
    private ISysBaseAPI sysBaseAPI;
    @Autowired
    private BaseCommonService baseCommonService;

    /**
     * 分页列表查询
     *
     * @param smartFinanceResult
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "8项规定财物收支表-分页列表查询")
    @ApiOperation(value = "8项规定财物收支表-分页列表查询", notes = "8项规定财物收支表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SmartFinanceResult smartFinanceResult,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        // 1. 规则，下面是 以**开始
        String rule = "in";
        // 2. 查询字段
        String field = "departId";
        // 获取登录用户信息，可以用来查询单位部门信息
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        if ("".equals(sysUser.getOrgCode())) {
            return Result.error("没有权限");
        }

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


        QueryWrapper<SmartFinanceResult> queryWrapper = QueryGenerator.initQueryWrapper(smartFinanceResult, map);
        Page<SmartFinanceResult> page = new Page<SmartFinanceResult>(pageNo, pageSize);
        IPage<SmartFinanceResult> pageList = smartFinanceResultService.page(page, queryWrapper);
        List<String> departIds = pageList.getRecords().stream().map(SmartFinanceResult::getDepartId).collect(Collectors.toList());
        if (departIds != null && departIds.size() > 0) {
            Map<String, String> useDepNames = commonService.getDepNamesByIds(departIds);
            pageList.getRecords().forEach(item -> {
                item.setDepartId(useDepNames.get(item.getDepartId()));
            });
        }
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param smartFinanceResultPage
     * @return
     */
    @AutoLog(value = "8项规定财物收支表-添加")
    @ApiOperation(value = "8项规定财物收支表-添加", notes = "8项规定财物收支表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SmartFinanceResultPage smartFinanceResultPage) {
        SmartFinanceResult smartFinanceResult = new SmartFinanceResult();
        BeanUtils.copyProperties(smartFinanceResultPage, smartFinanceResult);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String orgCode = sysUser.getOrgCode();
        if ("".equals(orgCode)) {
            return Result.error("本用户没有操作权限！");
        }
        String id = commonService.getDepartIdByOrgCode(orgCode);
        if (id == null) {
            return Result.error("没有找到部门！");
        }
        smartFinanceResult.setDepartId(id);
        smartFinanceResult.setCreatorId(sysUser.getId());
        smartFinanceResult.setCreator(sysUser.getRealname());

        Boolean isVerify = smartVerifyTypeService.getIsVerifyStatusByType(verifyType);
        if (isVerify) {
            smartFinanceResultService.saveMain(smartFinanceResult, smartFinanceResultPage.getSmartFinanceAnnexList());
            String recordId = smartFinanceResult.getId();
            smartVerify.addVerifyRecord(recordId, verifyType);
            smartFinanceResult.setVerifyStatus(smartVerify.getFlowStatusById(recordId).toString());
            smartFinanceResultService.updateById(smartFinanceResult);
        } else {
            smartFinanceResult.setVerifyStatus("3");
            smartFinanceResultService.saveMain(smartFinanceResult, smartFinanceResultPage.getSmartFinanceAnnexList());
        }
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param smartFinanceResultPage
     * @return
     */
    @AutoLog(value = "8项规定财物收支表-编辑")
    @ApiOperation(value = "8项规定财物收支表-编辑", notes = "8项规定财物收支表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SmartFinanceResultPage smartFinanceResultPage) {
        SmartFinanceResult smartFinanceResult = new SmartFinanceResult();
        BeanUtils.copyProperties(smartFinanceResultPage, smartFinanceResult);
        SmartFinanceResult smartFinanceResultEntity = smartFinanceResultService.getById(smartFinanceResult.getId());
        if (smartFinanceResultEntity == null) {
            return Result.error("未找到对应数据");
        }
        smartFinanceResult.setDepartId(null);
        smartFinanceResult.setCreateTime(null);
        smartFinanceResultService.updateMain(smartFinanceResult, smartFinanceResultPage.getSmartFinanceAnnexList());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "8项规定财物收支表-通过id删除")
    @ApiOperation(value = "8项规定财物收支表-通过id删除", notes = "8项规定财物收支表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        smartFinanceResultService.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "8项规定财物收支表-批量删除")
    @ApiOperation(value = "8项规定财物收支表-批量删除", notes = "8项规定财物收支表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.smartFinanceResultService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "8项规定财物收支表-通过id查询")
    @ApiOperation(value = "8项规定财物收支表-通过id查询", notes = "8项规定财物收支表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SmartFinanceResult smartFinanceResult = smartFinanceResultService.getById(id);
        if (smartFinanceResult == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(smartFinanceResult);

    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "8项规定财物收支附件通过主表ID查询")
    @ApiOperation(value = "8项规定财物收支附件主表ID查询", notes = "8项规定财物收支附件-通主表ID查询")
    @GetMapping(value = "/querySmartFinanceAnnexByMainId")
    public Result<?> querySmartFinanceAnnexListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<SmartFinanceAnnex> smartFinanceAnnexList = smartFinanceAnnexService.selectByMainId(id);
        return Result.OK(smartFinanceAnnexList);
    }

    /**
     * 导出excel
     *
     * @param req
     * @param smartFinanceResult
     * @param response
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest req, SmartFinanceResult smartFinanceResult, HttpServletResponse response) throws Exception {
        // 获取登录用户信息，可以用来查询单位部门信息
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        String username = sysUser.getUsername();

        // 获取用户角色
        List<String> role = sysBaseAPI.getRolesByUsername(username);

        List<SmartFinanceResult> queryList = new ArrayList<SmartFinanceResult>();

        // 如果是普通用户，则只能看到自己创建的数据
        if(role.contains("CommonUser")) {
            QueryWrapper<SmartFinanceResult> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("create_by",username);
            queryList = smartFinanceResultService.list(queryWrapper);
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
            QueryWrapper<SmartFinanceResult> queryWrapper = QueryGenerator.initQueryWrapper(smartFinanceResult, map);

            queryList = smartFinanceResultService.list(queryWrapper);
        }

        // Step.1 组装查询条件查询数据

        //Step.2 获取导出数据
        // 过滤选中数据
        String selections = req.getParameter("selections");
        List<SmartFinanceResult> smartFinanceResultList = new ArrayList<SmartFinanceResult>();
        if(oConvertUtils.isEmpty(selections)) {
            smartFinanceResultList = queryList;
        }else {
            List<String> selectionList = Arrays.asList(selections.split(","));
            smartFinanceResultList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        }

        // Step.3 组装pageList
        List<SmartFinanceResultPage> pageList = new ArrayList<SmartFinanceResultPage>();
        for (SmartFinanceResult main : smartFinanceResultList) {
            SmartFinanceResultPage vo = new SmartFinanceResultPage();
            BeanUtils.copyProperties(main, vo);
            List<SmartFinanceAnnex> smartFinanceAnnexList = smartFinanceAnnexService.selectByMainId(main.getId());
            vo.setSmartFinanceAnnexList(smartFinanceAnnexList);
            pageList.add(vo);
        }

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "8项规定财物收支表列表");
        mv.addObject(NormalExcelConstants.CLASS, SmartFinanceResultPage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("8项规定财物收支表数据", "导出人:" + sysUser.getRealname(), "8项规定财物收支表"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);

        // List深拷贝，否则返回前端会没数据
        List<SmartFinanceResultPage> newPageList = ObjectUtil.cloneByStream(pageList);

        baseCommonService.addExportLog(mv.getModel(), "财物收支", req, response);

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
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<SmartFinanceResultPage> list = ExcelImportUtil.importExcel(file.getInputStream(), SmartFinanceResultPage.class, params);
                for (SmartFinanceResultPage page : list) {
                    SmartFinanceResult po = new SmartFinanceResult();
                    BeanUtils.copyProperties(page, po);
                    smartFinanceResultService.saveMain(po, page.getSmartFinanceAnnexList());
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

    @AutoLog(value = "更新文件下载次数")
    @ApiOperation(value = "更新文件下载次数", notes = "更新文件下载次数")
    @PutMapping(value = "/downloadCount")
    public Result<?> downloadCount(@RequestBody SmartFinanceAnnex smartFinanceAnnex) {
        SmartFinanceAnnex newSmartFinanceAnnex = smartFinanceAnnexService.getById(smartFinanceAnnex.getId());
        if (newSmartFinanceAnnex == null) {
            return Result.error("未找到对应数据");
        }
        Integer downloadCount = newSmartFinanceAnnex.getDownloadCount();
        newSmartFinanceAnnex.setDownloadCount(downloadCount + 1);
        smartFinanceAnnexService.updateById(newSmartFinanceAnnex);
        return Result.OK("更新成功!");
    }

}