package org.jeecg.modules.smartPremaritalFiling.controller;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.modules.common.service.CommonService;
import org.jeecg.modules.common.util.ParamsUtil;
import org.jeecg.modules.smartJob.entity.SmartJob;
import org.jeecg.modules.smartJob.service.ISmartJobService;
import org.jeecg.modules.smartJob.service.imp.SmartJobServiceImpl;
import org.jeecg.modules.smartJob.util.LoopTask;
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
import com.alibaba.fastjson.JSON;
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
    private ISmartPremaritalFilingService smartPremaritalFilingService;
    @Autowired
    private ISmartPremaritalFilingAppService smartPremaritalFilingAppService;

    @Autowired
    CommonService commonService;

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

        // 1. 规则，下面是 以**开始
        String rule = "in";
        // 2. 查询字段
        String field = "departId";
        // 获取登录用户信息，可以用来查询单位部门信息
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

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

        Page<SmartPremaritalFiling> page = new Page<SmartPremaritalFiling>(pageNo, pageSize);
        IPage<SmartPremaritalFiling> pageList = smartPremaritalFilingService.page(page, queryWrapper);
        // 请同步修改edit函数中，将departId变为null，不然会更新成名称
        List<String> departIds = pageList.getRecords().stream().map(SmartPremaritalFiling::getDepartId).collect(Collectors.toList());
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
     * @param smartPremaritalFilingPage
     * @return
     */
    @AutoLog(value = "8项规定婚前报备表-添加")
    @ApiOperation(value = "8项规定婚前报备表-添加", notes = "8项规定婚前报备表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SmartPremaritalFilingPage smartPremaritalFilingPage) {
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

        //开启婚后报备提醒
//        LoopTask loopTask = LoopTask.getInstance();
//        loopTask.addPostMarray(
//                smartPremaritalFiling.getPeopleId(),
//                smartPremaritalFiling.getWeddingTime(),
//                smartPremaritalFiling.getId()
//        );

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

        //婚后报备提醒任务编辑
//        LoopTask loopTask = LoopTask.getInstance();
//        loopTask.editPostMarray(smartPremaritalFiling);

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
        smartPremaritalFilingService.delMain(id);

        //婚后报备提醒任务删除
//        LoopTask loopTask = LoopTask.getInstance();
//        loopTask.deleteJob(id);
//        ISmartJobService smartJobService = new SmartJobServiceImpl();
//        smartJobService.updateStatus(id);

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
    public Result<?> querySmartPremaritalFilingAppListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<SmartPremaritalFilingApp> smartPremaritalFilingAppList = smartPremaritalFilingAppService.selectByMainId(id);
        return Result.OK(smartPremaritalFilingAppList);
    }


    /**
     * 导出excel
     *
     * @param request
     * @param smartPremaritalFiling
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartPremaritalFiling smartPremaritalFiling) {
        // Step.1 组装查询条件查询数据
        QueryWrapper<SmartPremaritalFiling> queryWrapper = QueryGenerator.initQueryWrapper(smartPremaritalFiling, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //Step.2 获取导出数据
        List<SmartPremaritalFiling> queryList = smartPremaritalFilingService.list(queryWrapper);
        // 过滤选中数据
        String selections = request.getParameter("selections");
        List<SmartPremaritalFiling> smartPremaritalFilingList = new ArrayList<SmartPremaritalFiling>();
        if (oConvertUtils.isEmpty(selections)) {
            smartPremaritalFilingList = queryList;
        } else {
            List<String> selectionList = Arrays.asList(selections.split(","));
            smartPremaritalFilingList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        }

        // Step.3 组装pageList
        List<SmartPremaritalFilingPage> pageList = new ArrayList<SmartPremaritalFilingPage>();
        for (SmartPremaritalFiling main : smartPremaritalFilingList) {
            SmartPremaritalFilingPage vo = new SmartPremaritalFilingPage();
            BeanUtils.copyProperties(main, vo);
            List<SmartPremaritalFilingApp> smartPremaritalFilingAppList = smartPremaritalFilingAppService.selectByMainId(main.getId());
            vo.setSmartPremaritalFilingAppList(smartPremaritalFilingAppList);
            pageList.add(vo);
        }

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "8项规定婚前报备表列表");
        mv.addObject(NormalExcelConstants.CLASS, SmartPremaritalFilingPage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("8项规定婚前报备表数据", "导出人:" + sysUser.getRealname(), "8项规定婚前报备表"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
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

}
