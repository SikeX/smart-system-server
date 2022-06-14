package org.jeecg.modules.smartPostMarriage.controller;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.ObjectUtil;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.modules.common.service.CommonService;
import org.jeecg.modules.common.util.ParamsUtil;
import org.jeecg.modules.constant.VerifyConstant;
import org.jeecg.modules.smartExportWord.util.WordUtils;
import org.jeecg.modules.smartJob.entity.SysUser;
import org.jeecg.modules.smartJob.service.ISmartJobService;
import org.jeecg.modules.smartPostMarriage.util.RedisGetVal;
import org.jeecg.modules.smartPremaritalFiling.entity.SmartPremaritalFiling;
import org.jeecg.modules.smartPremaritalFiling.service.ISmartPremaritalFilingService;
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
import org.jeecg.modules.smartPostMarriage.entity.SmartPostMarriageReportFile;
import org.jeecg.modules.smartPostMarriage.entity.SmartPostMarriageReport;
import org.jeecg.modules.smartPostMarriage.vo.SmartPostMarriageReportPage;
import org.jeecg.modules.smartPostMarriage.service.ISmartPostMarriageReportService;
import org.jeecg.modules.smartPostMarriage.service.ISmartPostMarriageReportFileService;
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
 * @Description: 8项规定婚后报备表
 * @Author: jeecg-boot
 * @Date: 2021-11-10
 * @Version: V1.0
 */
@Api(tags = "8项规定婚后报备表")
@RestController
@RequestMapping("/smartPostMarriage/smartPostMarriageReport")
@Slf4j
public class SmartPostMarriageReportController {
    @Autowired
    private ISmartPostMarriageReportService smartPostMarriageReportService;
    @Autowired
    private ISmartPostMarriageReportFileService smartPostMarriageReportFileService;

    @Autowired
    private BaseCommonService baseCommonService;

    @Autowired
    ISmartPremaritalFilingService smartPremaritalFilingService;

    @Autowired
    CommonService commonService;

    @Autowired
    private SmartVerify smartVerify;
    public String verifyType = "婚后报备";

    //审核状态
    @Autowired
    private ISmartVerifyTypeService smartVerifyTypeService;

    @Autowired
    private ISysBaseAPI sysBaseAPI;

    @Autowired
    private RedisGetVal redisGetVal;

    /**
     * 分页列表查询
     *
     * @param smartPostMarriageReport
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "8项规定婚后报备表-分页列表查询")
    @ApiOperation(value = "8项规定婚后报备表-分页列表查询", notes = "8项规定婚后报备表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SmartPostMarriageReport smartPostMarriageReport,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {

        // 获取登录用户信息，可以用来查询单位部门信息
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        System.out.println(sysUser.getOrgCode() + "此用户的部门");
        List<String> role = sysBaseAPI.getRolesByUsername(sysUser.getUsername());
        Page<SmartPostMarriageReport> page = new Page<SmartPostMarriageReport>(pageNo, pageSize);

        if (role.contains("CommonUser")) {
            //普通用户权限控制，只能看到自己的数据
            QueryWrapper<SmartPostMarriageReport> queryWrapper = new QueryWrapper<SmartPostMarriageReport>();
            queryWrapper.eq("create_by", sysUser.getUsername());
            IPage<SmartPostMarriageReport> pageList = smartPostMarriageReportService.page(page, queryWrapper);
            return Result.OK(pageList);
        } else {
            // TODO：规则，下面是 以＊*开始
            String rule = "in";
            // TODO：查询字段
            String field = "workDepartment";


            // 获取子单位ID
            String childrenIdString = commonService.getChildrenIdStringByOrgCode(sysUser.getOrgCode());

            // 添加查询参数，下面的参数是查询以用户所在部门编码开头的的所有单位数据，即用户所在单位和子单位的信息
            // superQueryParams=[{"rule":"right_like","type":"string","dictCode":"","val":"用户所在的部门","field":"departId"}]
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

            QueryWrapper<SmartPostMarriageReport> queryWrapper = QueryGenerator.initQueryWrapper(smartPostMarriageReport, map);
//			Page<SmartPostMarriageReport> page = new Page<SmartPostMarriageReport>(pageNo, pageSize);
            IPage<SmartPostMarriageReport> pageList = smartPostMarriageReportService.page(page, queryWrapper);

            List<String> departIds = pageList.getRecords().stream().map(SmartPostMarriageReport::getWorkDepartment).collect(Collectors.toList());
            if (departIds != null && departIds.size() > 0) {
                Map<String, String> useDepNames = commonService.getDepNamesByIds(departIds);
                pageList.getRecords().forEach(item -> {
                    item.setWorkDepartment(useDepNames.get(item.getWorkDepartment()));
                });
            }

            return Result.OK(pageList);
        }

    }

    /**
     * 添加
     *
     * @param smartPostMarriageReport
     * @return
     */
    @AutoLog(value = "8项规定婚后报备表-提交审核")
    @ApiOperation(value = "8项规定婚后报备表-提交审核", notes = "8项规定婚后报备表-提交审核")
    @PostMapping(value = "/submitVerify")
    public Result<?> submitVerify(@RequestBody SmartPostMarriageReport smartPostMarriageReport) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        String orgCode = sysUser.getOrgCode();
        if ("".equals(orgCode)) {
            return Result.error("本用户没有操作权限！");
        }

        if(!smartVerifyTypeService.getIsVerifyStatusByType(verifyType)){
            return Result.error("免审任务，无需提交审核！");
        }

        String recordId = smartPostMarriageReport.getId();
        smartVerify.addVerifyRecord(recordId, verifyType);
        smartPostMarriageReport.setVerifyStatus(smartVerify.getFlowStatusById(recordId).toString());
        smartPostMarriageReportService.updateById(smartPostMarriageReport);

        return Result.OK("提交成功！");

//        //审核功能
//        smartVerify.addVerifyRecord(smartPostMarriageReportPage.getId(), verifyType);
//
//
//        System.out.println(orgCode + "此用户的orgcode");
//        String id = smartPostMarriageReportService.getDepartIdByOrgCode(orgCode);
//        smartPostMarriageReportPage.setWorkDepartment(id);
//        System.out.println(id + "id等于");
//
//        SmartPostMarriageReport smartPostMarriageReport = new SmartPostMarriageReport();
//        BeanUtils.copyProperties(smartPostMarriageReportPage, smartPostMarriageReport);
//
//        //审核判断
//        Boolean isVerify = smartVerifyTypeService.getIsVerifyStatusByType(verifyType);
//        if (isVerify) {
//            smartPostMarriageReportService.saveMain(smartPostMarriageReport, smartPostMarriageReportPage.getSmartPostMarriageReportFileList());
//            String recordId = smartPostMarriageReport.getId();
//            smartVerify.addVerifyRecord(recordId, verifyType);
//            smartPostMarriageReport.setVerifyStatus(smartVerify.getFlowStatusById(recordId).toString());
//            smartPostMarriageReportService.updateById(smartPostMarriageReport);
//        } else {
//            // 设置审核状态为免审
//            smartPostMarriageReport.setVerifyStatus("3");
//            // 直接添加，不走审核流程
//            smartPostMarriageReportService.saveMain(smartPostMarriageReport, smartPostMarriageReportPage.getSmartPostMarriageReportFileList());
//        }
//
//        //更改婚前报备isReport为"1"
//        smartPostMarriageReportService.editPreIsReport(smartPostMarriageReportPage.getPreId());
//
//        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param smartPostMarriageReportPage
     * @return
     */
    @AutoLog(value = "8项规定婚后报备表-编辑")
    @ApiOperation(value = "8项规定婚后报备表-编辑", notes = "8项规定婚后报备表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SmartPostMarriageReportPage smartPostMarriageReportPage) {
        SmartPostMarriageReport smartPostMarriageReport = new SmartPostMarriageReport();
        BeanUtils.copyProperties(smartPostMarriageReportPage, smartPostMarriageReport);
        SmartPostMarriageReport smartPostMarriageReportEntity = smartPostMarriageReportService.getById(smartPostMarriageReport.getId());
        if (smartPostMarriageReportEntity == null) {
            return Result.error("未找到对应数据");
        }
        if(smartPostMarriageReportEntity.getVerifyStatus().equals(VerifyConstant.VERIFY_STATUS_TOSUBMIT) || !smartPostMarriageReportEntity.getVerifyStatus().equals(VerifyConstant.VERIFY_STATUS_FREE)) {
            return Result.error("该任务已提交审核，不能修改！");
        }
        smartPostMarriageReport.setWorkDepartment(null);
        smartPostMarriageReport.setCreateTime(null);
        smartPostMarriageReportService.updateMain(smartPostMarriageReport, smartPostMarriageReportPage.getSmartPostMarriageReportFileList());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "8项规定婚后报备表-通过id删除")
    @ApiOperation(value = "8项规定婚后报备表-通过id删除", notes = "8项规定婚后报备表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        System.out.println(id);

        //修改preId为0
        //查询婚后数据，获取婚前id
        QueryWrapper<SmartPostMarriageReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        SmartPostMarriageReport smartPostMarriageReport = smartPostMarriageReportService.getOne(queryWrapper);
        smartPostMarriageReportService.setPreIsReport(smartPostMarriageReport.getPreId());

        smartPostMarriageReportService.delMain(id);

        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "8项规定婚后报备表-批量删除")
    @ApiOperation(value = "8项规定婚后报备表-批量删除", notes = "8项规定婚后报备表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        System.out.println(ids);

        //更新pre表的is_report为”0“
        List<String> tem = Arrays.asList(ids.split(","));
        List<SmartPostMarriageReport> list = smartPostMarriageReportService.listByIds(tem);

        for (SmartPostMarriageReport s : list) {
            //修改preId为0
            //查询婚后数据，获取婚前id
            if (null != s.getPreId()) {
                smartPostMarriageReportService.setPreIsReport(s.getPreId());
            }
        }

        //删除
        this.smartPostMarriageReportService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "8项规定婚后报备表-通过id查询")
    @ApiOperation(value = "8项规定婚后报备表-通过id查询", notes = "8项规定婚后报备表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id, @RequestParam(name = "preId", required = false) String preId) {
        System.out.println(id);
        System.out.println(preId);
        SmartPostMarriageReport smartPostMarriageReport = smartPostMarriageReportService.getById(id);
        if (smartPostMarriageReport == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(smartPostMarriageReport);

    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "8项规定婚后报备宴请发票与附件表通过主表ID查询")
    @ApiOperation(value = "8项规定婚后报备宴请发票与附件表主表ID查询", notes = "8项规定婚后报备宴请发票与附件表-通主表ID查询")
    @GetMapping(value = "/querySmartPostMarriageReportFileByMainId")
    public Result<?> querySmartPostMarriageReportFileListByMainId(@RequestParam(name = "id", required = true) String id) {
        System.out.println(id);
        List<SmartPostMarriageReportFile> smartPostMarriageReportFileList = smartPostMarriageReportFileService.selectByMainId(id);
        return Result.OK(smartPostMarriageReportFileList);
    }

    /**
     * 导出excel
     *
     * @param req
     * @param smartPremaritalFiling
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest req, HttpServletResponse response, SmartPremaritalFiling smartPremaritalFiling) throws IOException {
        System.out.println(smartPremaritalFiling);
        // Step.1 组装查询条件查询数据
//		 QueryWrapper<SmartPostMarriageReport> queryWrapper = QueryGenerator.initQueryWrapper(smartPostMarriageReport, request.getParameterMap());

        //获取登录用户信息
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String username = sysUser.getUsername();

        // 获取用户角色
        List<String> role = sysBaseAPI.getRolesByUsername(username);
        List<SmartPremaritalFiling> queryList1 = new ArrayList<SmartPremaritalFiling>();

        if (role.contains("CommonUser")) {
            QueryWrapper<SmartPremaritalFiling> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("create_by", username);
            queryList1 = smartPremaritalFilingService.list(queryWrapper);
        } else {
            // TODO：规则，下面是 以＊*开始
            String rule = "in";
            // TODO：查询字段
            String field = "departId";


            // 获取子单位ID
            String childrenIdString = commonService.getChildrenIdStringByOrgCode(sysUser.getOrgCode());

            // 添加查询参数，下面的参数是查询以用户所在部门编码开头的的所有单位数据，即用户所在单位和子单位的信息
            // superQueryParams=[{"rule":"right_like","type":"string","dictCode":"","val":"用户所在的部门","field":"departId"}]
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

            QueryWrapper<SmartPremaritalFiling> queryWrapper = QueryGenerator.initQueryWrapper(smartPremaritalFiling, map);
            queryList1 = smartPremaritalFilingService.list(queryWrapper);
        }

        //将婚前报备转换为婚后报备数据。。。
        List<String> preIdList = new ArrayList<>();
        for(int i = 0; i < queryList1.size(); i++){
            preIdList.add(queryList1.get(i).getId());
        }
        QueryWrapper<SmartPostMarriageReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("pre_id", preIdList);
        List<SmartPostMarriageReport> queryList = smartPostMarriageReportService.list(queryWrapper);


        // Step.1 组装查询条件查询数据

        //Step.2 获取导出数据
        // 过滤选中数据
        String selections = req.getParameter("selections");
        List<SmartPostMarriageReport> smartPostMarriageReportList = new ArrayList<SmartPostMarriageReport>();
        if (oConvertUtils.isEmpty(selections)) {
            //已选择的id为空
            smartPostMarriageReportList = queryList;
        } else {
            //已选择的id不空。。。
            List<String> selectionList1 = Arrays.asList(selections.split(","));
            //将婚前id转换为婚后id
            QueryWrapper<SmartPostMarriageReport> covert = new QueryWrapper<>();
            covert.in("pre_id", selectionList1);
            List<SmartPostMarriageReport> tem = smartPostMarriageReportService.list(queryWrapper);
            List<String> selectionList = new ArrayList<>();
            for(int i = 0; i < tem.size(); i++){
                selectionList.add(tem.get(i).getId());
            }

            smartPostMarriageReportList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        }

        // Step.3 组装pageList
        List<SmartPostMarriageReportPage> pageList = new ArrayList<SmartPostMarriageReportPage>();
        for (SmartPostMarriageReport main : smartPostMarriageReportList) {
            SmartPostMarriageReportPage vo = new SmartPostMarriageReportPage();
            BeanUtils.copyProperties(main, vo);
            List<SmartPostMarriageReportFile> smartPostMarriageReportFileList = smartPostMarriageReportFileService.selectByMainId(main.getId());
            vo.setSmartPostMarriageReportFileList(smartPostMarriageReportFileList);
            pageList.add(vo);
        }

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "8项规定婚后报备表列表");
        mv.addObject(NormalExcelConstants.CLASS, SmartPostMarriageReportPage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("8项规定婚后报备表数据", "导出人:" + sysUser.getRealname(), "8项规定婚后报备表"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);

        // List深拷贝，否则返回前端会没数据
        List<SmartPostMarriageReportPage> newPageList = ObjectUtil.cloneByStream(pageList);

        baseCommonService.addExportLog(mv.getModel(), "婚后报备", req, response);

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
                List<SmartPostMarriageReportPage> list = ExcelImportUtil.importExcel(file.getInputStream(), SmartPostMarriageReportPage.class, params);
                for (SmartPostMarriageReportPage page : list) {
                    SmartPostMarriageReport po = new SmartPostMarriageReport();
                    BeanUtils.copyProperties(page, po);
                    smartPostMarriageReportService.saveMain(po, page.getSmartPostMarriageReportFileList());
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
    public Result<?> edit(@RequestBody SmartPostMarriageReportFile smartPostMarriageReportFile) {
        SmartPostMarriageReportFile newSmartPostMarriageReportFile =
                smartPostMarriageReportFileService.getById(smartPostMarriageReportFile.getId());
        int currentCount = newSmartPostMarriageReportFile.getDownloadCount();
        newSmartPostMarriageReportFile.setDownloadCount(currentCount + 1);
        smartPostMarriageReportFileService.updateById(newSmartPostMarriageReportFile);
        return Result.OK("更新成功!");
    }

    @GetMapping(value = "/exportWord")
    public void test01(@RequestParam(name = "ids", required = true) String ids, HttpServletResponse response, HttpServletRequest request) {

        //获取需要的数据
        List<String> idsList = Arrays.asList(ids.split(","));
        System.out.println(ids);

        QueryWrapper<SmartPostMarriageReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id").in("pre_id", idsList);
        List<SmartPostMarriageReport> list = smartPostMarriageReportService.list(queryWrapper);

        if(list.size() == 0){
            return;
        }

        List<String> idList = new ArrayList<>();

        for(int i = 0; i < list.size(); i++){
            idList.add(list.get(i).getId());
        }

        List<SmartPostMarriageReport> smartPostMarriageReports = smartPostMarriageReportService.listByIds(idList);

        //存放数据map
        List<Map<String, Object>> dataList = new ArrayList<>();
        //存放文件名
        List<String> fileNamesList = new ArrayList<>();
        for (int i = 0; i < smartPostMarriageReports.size(); i++) {
            //数据完善
            //性别
            String sex = redisGetVal.getValue("sex", smartPostMarriageReports.get(i).getSex());
            //政治面貌
            String politicsStatus = redisGetVal.getValue("political_status", smartPostMarriageReports.get(i).getPoliticsStatus());
            //职务
            //String job = redisGetVal.getValue("sys_position", smartPostMarriageReports.get(i).getJob());
            String job = null;
            //职级
            String jobLevel = redisGetVal.getValue("position_rank", smartPostMarriageReports.get(i).getJobLevel());

            smartPostMarriageReports.get(i).setSex(sex);
            smartPostMarriageReports.get(i).setPoliticsStatus(politicsStatus);
            smartPostMarriageReports.get(i).setJob(job);
            smartPostMarriageReports.get(i).setJobLevel(jobLevel);

            //部门名称
            List<String> departids = new ArrayList<>();
            departids.add(smartPostMarriageReports.get(i).getWorkDepartment());
            Map<String, String> departNames = commonService.getDepNamesByIds(departids);
            smartPostMarriageReports.get(i).setWorkDepartment(departNames.get(smartPostMarriageReports.get(i).getWorkDepartment()));

            //设置数据
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("obj", smartPostMarriageReports.get(i));
            dataList.add(dataMap);

            //文件名，注意于数据对应
            String fileName = smartPostMarriageReports.get(i).getName();
            fileNamesList.add(fileName);
        }

        //设置模板
        String ftlTemplateName = "/templates/SmartPostMarriageReport.ftl";
        WordUtils.exportWordBatch(dataList, fileNamesList, ftlTemplateName, response, request);

    }

    //根据婚前id查找婚后报备记录
    @GetMapping(value = "/queryByPreId")
    public Result<?> queryByPreId(@RequestParam(name = "preId", required = true) String preId) {
        System.out.println(preId);

        SmartPostMarriageReport smartPostMarriageReport = smartPostMarriageReportService.getByPreId(preId);
        if (smartPostMarriageReport == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(smartPostMarriageReport);

    }

    @GetMapping("/getUserInfo")
    public Result<?> getUserInfo(){
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        System.out.println(sysUser.getId());
        String id = sysUser.getId();

        SysUser ret = smartPostMarriageReportService.getSysUser(id);
        System.out.println(ret);
        return Result.OK(ret);
    }

    /**
     * 添加
     *
     * @param smartPostMarriageReportPage
     * @return
     */
    @AutoLog(value = "8项规定婚后报备表-添加")
    @ApiOperation(value = "8项规定婚后报备表-添加", notes = "8项规定婚后报备表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SmartPostMarriageReportPage smartPostMarriageReportPage) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        String orgCode = sysUser.getOrgCode();
        if ("".equals(orgCode)) {
            return Result.error("本用户没有操作权限！");
        }

        String departId = commonService.getDepartIdByOrgCode(orgCode);
        if (departId == null) {
            return Result.error("没有找到部门！");
        }

        //根据婚前报备id判断有没有婚后填报
        SmartPostMarriageReport smartPostMarriageReport1 = smartPostMarriageReportService.getByPreId(
                smartPostMarriageReportPage.getPreId()
        );
        if (null != smartPostMarriageReport1) {
            return Result.error("请勿重复报备！");
        }

        //审核功能
        SmartPostMarriageReport smartPostMarriageReport = new SmartPostMarriageReport();
        BeanUtils.copyProperties(smartPostMarriageReportPage, smartPostMarriageReport);
        smartPostMarriageReportService.saveMain(smartPostMarriageReport, smartPostMarriageReportPage.getSmartPostMarriageReportFileList());
        smartPostMarriageReport.setWorkDepartment(departId);
        Boolean isVerify = smartVerifyTypeService.getIsVerifyStatusByType(verifyType);
        if (isVerify) {
            // 如果任务需要审核，则设置任务为待提交状态
            smartPostMarriageReport.setVerifyStatus(VerifyConstant.VERIFY_STATUS_TOSUBMIT);
        } else {
            // 设置审核状态为免审
            smartPostMarriageReport.setVerifyStatus(VerifyConstant.VERIFY_STATUS_FREE);
        }

        smartPostMarriageReportService.updateById(smartPostMarriageReport);


//        //审核功能
//        smartVerify.addVerifyRecord(smartPostMarriageReportPage.getId(), verifyType);
//
//        smartPostMarriageReportPage.setWorkDepartment(departId);
//
//        SmartPostMarriageReport smartPostMarriageReport = new SmartPostMarriageReport();
//        BeanUtils.copyProperties(smartPostMarriageReportPage, smartPostMarriageReport);

        //审核判断
//        Boolean isVerify = smartVerifyTypeService.getIsVerifyStatusByType(verifyType);
//        if (isVerify) {
//            smartPostMarriageReportService.saveMain(smartPostMarriageReport, smartPostMarriageReportPage.getSmartPostMarriageReportFileList());
//            String recordId = smartPostMarriageReport.getId();
//            smartVerify.addVerifyRecord(recordId, verifyType);
//            smartPostMarriageReport.setVerifyStatus(smartVerify.getFlowStatusById(recordId).toString());
//            smartPostMarriageReportService.updateById(smartPostMarriageReport);
//        } else {
//            // 设置审核状态为免审
//            smartPostMarriageReport.setVerifyStatus("3");
//            // 直接添加，不走审核流程
//            smartPostMarriageReportService.saveMain(smartPostMarriageReport, smartPostMarriageReportPage.getSmartPostMarriageReportFileList());
//        }

        //更改婚前报备isReport为"1"
        smartPostMarriageReportService.editPreIsReport(smartPostMarriageReportPage.getPreId());

        return Result.OK("添加成功！");
    }
}
