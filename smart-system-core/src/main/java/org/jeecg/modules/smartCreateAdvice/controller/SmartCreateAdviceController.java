package org.jeecg.modules.smartCreateAdvice.controller;

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
import org.jeecg.modules.smartCreateAdvice.entity.SmartCreateAdviceAnnex;
import org.jeecg.modules.smartCreateAdvice.entity.SmartCreateAdvice;
import org.jeecg.modules.smartCreateAdvice.vo.SmartCreateAdvicePage;
import org.jeecg.modules.smartCreateAdvice.service.ISmartCreateAdviceService;
import org.jeecg.modules.smartCreateAdvice.service.ISmartCreateAdviceAnnexService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
 * @Description: 制发建议表
 * @Author: jeecg-boot
 * @Date: 2021-11-13
 * @Version: V1.0
 */
@Api(tags = "制发建议表")
@RestController
@RequestMapping("/smartCreateAdvice/smartCreateAdvice")
@Slf4j
public class SmartCreateAdviceController {
    @Autowired
    private ISmartCreateAdviceService smartCreateAdviceService;
    @Autowired
    private ISmartCreateAdviceAnnexService smartCreateAdviceAnnexService;
    @Autowired
    CommonService commonService;
    @Autowired
    private SmartVerify smartVerify;
    @Autowired
    private ISmartVerifyTypeService smartVerifyTypeService;
    @Autowired
    private ISysBaseAPI sysBaseAPI;
    @Autowired
    private BaseCommonService baseCommonService;

    public String verifyType = "制发建议";

    /**
     * 分页列表查询
     *
     * @param smartCreateAdvice
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "制发建议表-分页列表查询")
    @ApiOperation(value = "制发建议表-分页列表查询", notes = "制发建议表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SmartCreateAdvice smartCreateAdvice,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {

        // 获取登录用户信息，可以用来查询单位部门信息
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        String username = sysUser.getUsername();

        // 获取用户角色
        List<String> role = sysBaseAPI.getRolesByUsername(username);

        Page<SmartCreateAdvice> page = new Page<SmartCreateAdvice>(pageNo, pageSize);

        // 如果是普通用户，则只能看到自己创建的数据
        if (role.contains("CommonUser")) {
            QueryWrapper<SmartCreateAdvice> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("create_by", username);
            IPage<SmartCreateAdvice> pageList = smartCreateAdviceService.page(page, queryWrapper);
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
            QueryWrapper<SmartCreateAdvice> queryWrapper = QueryGenerator.initQueryWrapper(smartCreateAdvice, map);

            IPage<SmartCreateAdvice> pageList = smartCreateAdviceService.page(page, queryWrapper);
            List<String> departIds = pageList.getRecords().stream().map(SmartCreateAdvice::getDepartId).collect(Collectors.toList());
            if (departIds != null && departIds.size() > 0) {
                Map<String, String> useDepNames = commonService.getDepNamesByIds(departIds);
                pageList.getRecords().forEach(item -> {
                    item.setDepartId(useDepNames.get(item.getDepartId()));
                });
            }
            return Result.OK(pageList);
        }
    }

    /**
     * 添加
     *
     * @param smartCreateAdvice
     * @return
     */
    @AutoLog(value = "制发建议表-提交审核")
    @ApiOperation(value = "制发建议表-提交审核", notes = "制发建议表-提交审核")
    @PostMapping(value = "/add")
    @Transactional
    public Result<?> add(@RequestBody SmartCreateAdvice smartCreateAdvice) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        String orgCode = sysUser.getOrgCode();
        if ("".equals(orgCode)) {
            return Result.error("本用户没有操作权限！");
        }
        String id = commonService.getDepartIdByOrgCode(orgCode);
        if (id == null) {
            return Result.error("没有找到部门！");
        }
        smartCreateAdvice.setDepartId(id);
        smartCreateAdviceService.save(smartCreateAdvice);

        Boolean isVerify = smartVerifyTypeService.getIsVerifyStatusByType(verifyType);
        if (isVerify) {
            // 如果任务需要审核，则设置任务为待提交状态
            smartCreateAdvice.setVerifyStatus(VerifyConstant.VERIFY_STATUS_TOSUBMIT);
        } else {
            // 设置审核状态为免审
            smartCreateAdvice.setVerifyStatus(VerifyConstant.VERIFY_STATUS_FREE);
        }

        smartCreateAdviceService.updateById(smartCreateAdvice);


        return Result.OK("添加成功！");
    }

    /**
     * 提交审核
     *
     * @param smartCreateAdvice
     * @return
     */
    @AutoLog(value = "制发建议表-提交审核")
    @ApiOperation(value = "制发建议表-提交审核", notes = "制发建议表-提交审核")
    @PostMapping(value = "/submitVerify")
    @Transactional
    public Result<?> submitVerify(@RequestBody SmartCreateAdvice smartCreateAdvice) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        String orgCode = sysUser.getOrgCode();
        if ("".equals(orgCode)) {
            return Result.error("本用户没有操作权限！");
        }

        if(!smartVerifyTypeService.getIsVerifyStatusByType(verifyType)){
            return Result.error("免审任务，无需提交审核！");
        }

        String recordId = smartCreateAdvice.getId();
        smartVerify.addVerifyRecord(recordId, verifyType);
        smartCreateAdvice.setVerifyStatus(smartVerify.getFlowStatusById(recordId).toString());
        smartCreateAdviceService.updateById(smartCreateAdvice);

        return Result.OK("提交成功！");
    }

    /**
     * 编辑
     *
     * @param smartCreateAdvice
     * @return
     */
    @AutoLog(value = "制发建议表-编辑")
    @ApiOperation(value = "制发建议表-编辑", notes = "制发建议表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SmartCreateAdvice smartCreateAdvice) {
        SmartCreateAdvice smartCreateAdviceEntity = smartCreateAdviceService.getById(smartCreateAdvice.getId());
        if (smartCreateAdviceEntity == null) {
            return Result.error("未找到对应数据");
        }
        if(!smartCreateAdviceEntity.getVerifyStatus().equals(VerifyConstant.VERIFY_STATUS_TOSUBMIT) || !smartCreateAdviceEntity.getVerifyStatus().equals(VerifyConstant.VERIFY_STATUS_FREE)){
            return Result.error("该任务已提交审核，不能修改！");
        }

        smartCreateAdviceService.updateById(smartCreateAdvice);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "制发建议表-通过id删除")
    @ApiOperation(value = "制发建议表-通过id删除", notes = "制发建议表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        smartCreateAdviceService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "制发建议表-批量删除")
    @ApiOperation(value = "制发建议表-批量删除", notes = "制发建议表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.smartCreateAdviceService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "制发建议表-通过id查询")
    @ApiOperation(value = "制发建议表-通过id查询", notes = "制发建议表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SmartCreateAdvice smartCreateAdvice = smartCreateAdviceService.getById(id);
        if (smartCreateAdvice == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(smartCreateAdvice);

    }

    /**
     * 导出excel
     *
     * @param req
     * @param smartCreateAdvice
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest req,
                                  HttpServletResponse response, SmartCreateAdvice smartCreateAdvice) throws Exception {

        // 获取登录用户信息，可以用来查询单位部门信息
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        String username = sysUser.getUsername();

        // 获取用户角色
        List<String> role = sysBaseAPI.getRolesByUsername(username);

        List<SmartCreateAdvice> queryList = new ArrayList<SmartCreateAdvice>();


        // 如果是普通用户，则只能看到自己创建的数据
        if (role.contains("CommonUser")) {
            QueryWrapper<SmartCreateAdvice> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("create_by", username);
            queryList = smartCreateAdviceService.list(queryWrapper);
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
            QueryWrapper<SmartCreateAdvice> queryWrapper = QueryGenerator.initQueryWrapper(smartCreateAdvice, map);

            queryList = smartCreateAdviceService.list(queryWrapper);
        }


        // Step.1 组装查询条件查询数据

        //Step.2 获取导出数据
        // 过滤选中数据
        String selections = req.getParameter("selections");
        List<SmartCreateAdvice> smartCreateAdviceList = new ArrayList<SmartCreateAdvice>();
        if (oConvertUtils.isEmpty(selections)) {
            smartCreateAdviceList = queryList;
        } else {
            List<String> selectionList = Arrays.asList(selections.split(","));
            smartCreateAdviceList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        }

        // Step.3 组装pageList
        List<SmartCreateAdvicePage> pageList = new ArrayList<SmartCreateAdvicePage>();
        for (SmartCreateAdvice main : smartCreateAdviceList) {
            SmartCreateAdvicePage vo = new SmartCreateAdvicePage();
            BeanUtils.copyProperties(main, vo);
            List<SmartCreateAdviceAnnex> smartCreateAdviceAnnexList = smartCreateAdviceAnnexService.selectByMainId(main.getId());
            vo.setSmartCreateAdviceAnnexList(smartCreateAdviceAnnexList);
            pageList.add(vo);
        }

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "制发建议表列表");
        mv.addObject(NormalExcelConstants.CLASS, SmartCreateAdvicePage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("制发建议表数据", "导出人:" + sysUser.getRealname(), "制发建议表"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);

        // List深拷贝，否则返回前端会没数据
        List<SmartCreateAdvicePage> newPageList = ObjectUtil.cloneByStream(pageList);

        baseCommonService.addExportLog(mv.getModel(), "制发建议", req, response);


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
                List<SmartCreateAdvicePage> list = ExcelImportUtil.importExcel(file.getInputStream(), SmartCreateAdvicePage.class, params);
                for (SmartCreateAdvicePage page : list) {
                    SmartCreateAdvice po = new SmartCreateAdvice();
                    BeanUtils.copyProperties(page, po);
                    smartCreateAdviceService.saveMain(po, page.getSmartCreateAdviceAnnexList());
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
    public Result<?> edit(@RequestBody SmartCreateAdviceAnnex smartCreateAdviceAnnex) {
        SmartCreateAdviceAnnex newSmartCreateAdviceAnnex = smartCreateAdviceAnnexService.getById(smartCreateAdviceAnnex.getId());
        int currentCount = newSmartCreateAdviceAnnex.getDownCount();
        newSmartCreateAdviceAnnex.setDownCount(currentCount + 1);
        smartCreateAdviceAnnexService.updateById(newSmartCreateAdviceAnnex);
        return Result.OK("更新成功!");
    }

}
