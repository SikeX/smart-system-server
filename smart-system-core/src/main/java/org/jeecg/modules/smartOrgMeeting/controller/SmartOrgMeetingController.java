package org.jeecg.modules.smartOrgMeeting.controller;

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
import org.jeecg.modules.smartOrgMeeting.entity.SmartOrgMeeting;
import org.jeecg.modules.smartOrgMeeting.entity.SmartOrgMeetingPacpa;
import org.jeecg.modules.smartOrgMeeting.service.ISmartOrgMeetingPacpaService;
import org.jeecg.modules.smartOrgMeeting.service.ISmartOrgMeetingService;
import org.jeecg.modules.smartOrgMeeting.vo.SmartOrgMeetingPage;
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
 * @Description: 组织生活会
 * @Author: jeecg-boot
 * @Date: 2021-11-10
 * @Version: V1.0
 */
@Api(tags = "组织生活会")
@RestController
@RequestMapping("/smartOrgMeeting/smartOrgMeeting")
@Slf4j
public class SmartOrgMeetingController {
    @Autowired
    private ISmartOrgMeetingService smartOrgMeetingService;
    @Autowired
    private ISmartOrgMeetingPacpaService smartOrgMeetingPacpaService;
    /**
     * 审核
     */
    @Autowired
    private SmartVerify smartVerify;
    @Autowired
    private ISmartVerifyTypeService smartVerifyTypeService;
    public String verifyType = "组织生活会";
    @Autowired
    CommonService commonService;
    @Autowired
    private ISysBaseAPI sysBaseAPI;
    @Autowired
    private BaseCommonService baseCommonService;

    /**
     * 分页列表查询
     *
     * @param smartOrgMeeting
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "组织生活会-分页列表查询")
    @ApiOperation(value = "组织生活会-分页列表查询", notes = "组织生活会-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SmartOrgMeeting smartOrgMeeting,
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

        QueryWrapper<SmartOrgMeeting> queryWrapper = QueryGenerator.initQueryWrapper(smartOrgMeeting, map);
        Page<SmartOrgMeeting> page = new Page<SmartOrgMeeting>(pageNo, pageSize);
        IPage<SmartOrgMeeting> pageList = smartOrgMeetingService.page(page, queryWrapper);
        // 请同步修改edit函数中，将departId变为null，不然会更新成名称
        List<String> departIds = pageList.getRecords().stream().map(SmartOrgMeeting::getDepartId).collect(Collectors.toList());
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
     * @param smartOrgMeetingPage
     * @return
     */
    @AutoLog(value = "组织生活会-添加")
    @ApiOperation(value = "组织生活会-添加", notes = "组织生活会-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SmartOrgMeetingPage smartOrgMeetingPage) {
        SmartOrgMeeting smartOrgMeeting = new SmartOrgMeeting();
        BeanUtils.copyProperties(smartOrgMeetingPage, smartOrgMeeting);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String orgCode = sysUser.getOrgCode();
        if ("".equals(orgCode)) {
            return Result.error("本用户没有操作权限！");
        }
        String id = commonService.getDepartIdByOrgCode(orgCode);
        if (id == null) {
            return Result.error("没有找到部门！");
        }
        smartOrgMeeting.setDepartId(id);
        smartOrgMeeting.setCreatorId(sysUser.getId());

        Boolean isVerify = smartVerifyTypeService.getIsVerifyStatusByType(verifyType);
        if (isVerify) {
            smartOrgMeetingService.saveMain(smartOrgMeeting, smartOrgMeetingPage.getSmartOrgMeetingPacpaList());
            String recordId = smartOrgMeeting.getId();
            log.info("recordId is " + recordId);
            smartVerify.addVerifyRecord(recordId, verifyType);
            smartOrgMeeting.setVerifyStatus(smartVerify.getFlowStatusById(recordId).toString());
            smartOrgMeetingService.updateById(smartOrgMeeting);
        } else {
            smartOrgMeeting.setVerifyStatus("3");
            smartOrgMeetingService.saveMain(smartOrgMeeting, smartOrgMeetingPage.getSmartOrgMeetingPacpaList());
        }
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param smartOrgMeetingPage
     * @return
     */
    @AutoLog(value = "组织生活会-编辑")
    @ApiOperation(value = "组织生活会-编辑", notes = "组织生活会-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SmartOrgMeetingPage smartOrgMeetingPage) {
        SmartOrgMeeting smartOrgMeeting = new SmartOrgMeeting();
        BeanUtils.copyProperties(smartOrgMeetingPage, smartOrgMeeting);
        SmartOrgMeeting smartOrgMeetingEntity = smartOrgMeetingService.getById(smartOrgMeeting.getId());
        if (smartOrgMeetingEntity == null) {
            return Result.error("未找到对应数据");
        }
        smartOrgMeeting.setDepartId(null);
        smartOrgMeeting.setCreateTime(null);
        smartOrgMeetingService.updateMain(smartOrgMeeting, smartOrgMeetingPage.getSmartOrgMeetingPacpaList());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "组织生活会-通过id删除")
    @ApiOperation(value = "组织生活会-通过id删除", notes = "组织生活会-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        smartOrgMeetingService.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "组织生活会-批量删除")
    @ApiOperation(value = "组织生活会-批量删除", notes = "组织生活会-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.smartOrgMeetingService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "组织生活会-通过id查询")
    @ApiOperation(value = "组织生活会-通过id查询", notes = "组织生活会-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SmartOrgMeeting smartOrgMeeting = smartOrgMeetingService.getById(id);
        if (smartOrgMeeting == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(smartOrgMeeting);

    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "组织生活会参会人员表通过主表ID查询")
    @ApiOperation(value = "组织生活会参会人员表主表ID查询", notes = "组织生活会参会人员表-通主表ID查询")
    @GetMapping(value = "/querySmartOrgMeetingPacpaByMainId")
    public Result<?> querySmartOrgMeetingPacpaListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<SmartOrgMeetingPacpa> smartOrgMeetingPacpaList = smartOrgMeetingPacpaService.selectByMainId(id);
        return Result.OK(smartOrgMeetingPacpaList);
    }


    /**
     * 导出excel
     *
     * @param req
     * @param smartOrgMeeting
     * @param response
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest req, SmartOrgMeeting smartOrgMeeting, HttpServletResponse response) throws Exception {
        // 获取登录用户信息，可以用来查询单位部门信息
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        String username = sysUser.getUsername();

        // 获取用户角色
        List<String> role = sysBaseAPI.getRolesByUsername(username);

        List<SmartOrgMeeting> queryList = new ArrayList<SmartOrgMeeting>();

        // 如果是普通用户，则只能看到自己创建的数据
        if(role.contains("CommonUser")) {
            QueryWrapper<SmartOrgMeeting> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("create_by",username);
            queryList = smartOrgMeetingService.list(queryWrapper);
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
            QueryWrapper<SmartOrgMeeting> queryWrapper = QueryGenerator.initQueryWrapper(smartOrgMeeting, map);

            queryList = smartOrgMeetingService.list(queryWrapper);
        }

        // Step.1 组装查询条件查询数据

        //Step.2 获取导出数据
        // 过滤选中数据
        String selections = req.getParameter("selections");
        List<SmartOrgMeeting> smartOrgMeetingList = new ArrayList<SmartOrgMeeting>();
        if(oConvertUtils.isEmpty(selections)) {
            smartOrgMeetingList = queryList;
        }else {
            List<String> selectionList = Arrays.asList(selections.split(","));
            smartOrgMeetingList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        }

        // Step.3 组装pageList
        List<SmartOrgMeetingPage> pageList = new ArrayList<SmartOrgMeetingPage>();
        for (SmartOrgMeeting main : smartOrgMeetingList) {
            SmartOrgMeetingPage vo = new SmartOrgMeetingPage();
            BeanUtils.copyProperties(main, vo);
            List<SmartOrgMeetingPacpa> smartOrgMeetingPacpaList = smartOrgMeetingPacpaService.selectByMainId(main.getId());
            vo.setSmartOrgMeetingPacpaList(smartOrgMeetingPacpaList);
            pageList.add(vo);
        }

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "组织生活会列表");
        mv.addObject(NormalExcelConstants.CLASS, SmartOrgMeetingPage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("组织生活会数据", "导出人:" + sysUser.getRealname(), "组织生活会"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);

        // List深拷贝，否则返回前端会没数据
        List<SmartOrgMeetingPage> newPageList = ObjectUtil.cloneByStream(pageList);

        baseCommonService.addExportLog(mv.getModel(), "组织生活会", req, response);

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
            //方法方法
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<SmartOrgMeetingPage> list = ExcelImportUtil.importExcel(file.getInputStream(), SmartOrgMeetingPage.class, params);
                for (SmartOrgMeetingPage page : list) {
                    SmartOrgMeeting po = new SmartOrgMeeting();
                    BeanUtils.copyProperties(page, po);
                    smartOrgMeetingService.saveMain(po, page.getSmartOrgMeetingPacpaList());
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