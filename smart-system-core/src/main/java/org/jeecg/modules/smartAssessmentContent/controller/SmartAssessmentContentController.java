package org.jeecg.modules.smartAssessmentContent.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Joiner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.system.vo.SysDepartModel;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.common.service.CommonService;
import org.jeecg.modules.smartAssessmentContent.entity.SmartAssessmentContent;
import org.jeecg.modules.smartAssessmentContent.service.ISmartAssessmentContentService;
import org.jeecg.modules.smartAssessmentDepartment.entity.SmartAssessmentDepartment;
import org.jeecg.modules.smartAssessmentDepartment.service.ISmartAssessmentDepartmentService;
import org.jeecg.modules.smartAssessmentMission.entity.SmartAssessmentMission;
import org.jeecg.modules.smartAssessmentMission.service.ISmartAssessmentMissionService;
import org.jeecg.modules.smartAssessmentTeam.service.ISmartAssessmentTeamService;
import org.jeecg.modules.smartFinanceResult.entity.SmartFinanceResult;
import org.jeecg.modules.smartRankVisible.entity.SmartRankVisible;
import org.jeecg.modules.smartRankVisible.service.ISmartRankVisibleService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @Description: ???????????????
 * @Author: jeecg-boot
 * @Date: 2022-02-12
 * @Version: V1.0
 */
@Api(tags = "???????????????")
@RestController
@RequestMapping("/smartAssessmentContent/smartAssessmentContent")
@Slf4j
public class SmartAssessmentContentController extends JeecgController<SmartAssessmentContent, ISmartAssessmentContentService> {
    @Autowired
    private ISmartAssessmentContentService smartAssessmentContentService;

    @Autowired
    private ISmartAssessmentMissionService smartAssessmentMissionService;

    @Autowired
    private ISmartRankVisibleService smartRankVisibleService;

    @Autowired
    private ISmartAssessmentDepartmentService smartAssessmentDepartmentService;

    @Autowired
    private ISmartAssessmentTeamService smartAssessmentTeamService;

    @Autowired
    ISysBaseAPI iSysBaseAPI;

    @Value("${jeecg.path.upload}")
    private String upLoadPath;

    /**
     * ??????????????????
     *
     * @param id
     * @return
     */
    @AutoLog(value = "???????????????-??????????????????")
    @ApiOperation(value = "???????????????-??????????????????", notes = "???????????????-??????????????????")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(@RequestParam("id") String id) {
        try {
            QueryWrapper<SmartAssessmentContent> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", id);
            List<SmartAssessmentContent> list = smartAssessmentContentService.list(queryWrapper);
            IPage<SmartAssessmentContent> pageList = new Page<>(1, 10, list.size());
            pageList.setRecords(list);
            return Result.OK(pageList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("?????????????????????" + e.getMessage());
        }
    }

    /**
     * ??????????????????
     *
     * @param smartAssessmentContent
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "???????????????-??????????????????")
    @ApiOperation(value = "???????????????-??????????????????", notes = "???????????????-??????????????????")
    @GetMapping(value = "/rootList")
    public Result<?> queryRootList(SmartAssessmentContent smartAssessmentContent,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        if (oConvertUtils.isEmpty(smartAssessmentContent.getMissionId())) {
            return Result.error("????????????id????????????");
        }
        String hasQuery = req.getParameter("hasQuery");
        if (hasQuery != null && "true".equals(hasQuery)) {
            QueryWrapper<SmartAssessmentContent> queryWrapper = QueryGenerator.initQueryWrapper(smartAssessmentContent, req.getParameterMap());
            List<SmartAssessmentContent> list = smartAssessmentContentService.queryTreeListNoPage(queryWrapper);
            IPage<SmartAssessmentContent> pageList = new Page<>(1, 10, list.size());
            pageList.setRecords(list);
            return Result.OK(pageList);
        } else {
            String parentId = smartAssessmentContent.getPid();
            if (oConvertUtils.isEmpty(parentId)) {
                parentId = "0";
            }
            smartAssessmentContent.setPid(null);
            QueryWrapper<SmartAssessmentContent> queryWrapper = QueryGenerator.initQueryWrapper(smartAssessmentContent, req.getParameterMap());
            // ?????? eq ??????????????????
            queryWrapper.eq("pid", parentId);
            Page<SmartAssessmentContent> page = new Page<SmartAssessmentContent>(pageNo, pageSize);
            IPage<SmartAssessmentContent> pageList = smartAssessmentContentService.page(page, queryWrapper);
            return Result.OK(pageList);
        }
    }

    /**
     * ???????????????
     *
     * @param smartAssessmentContent
     * @param req
     * @return
     */
    @AutoLog(value = "???????????????-???????????????")
    @ApiOperation(value = "???????????????-???????????????", notes = "???????????????-???????????????")
    @GetMapping(value = "/childList")
    public Result<?> queryPageList(SmartAssessmentContent smartAssessmentContent, HttpServletRequest req) {
        QueryWrapper<SmartAssessmentContent> queryWrapper = QueryGenerator.initQueryWrapper(smartAssessmentContent, req.getParameterMap());
        List<SmartAssessmentContent> list = smartAssessmentContentService.list(queryWrapper);
        IPage<SmartAssessmentContent> pageList = new Page<>(1, 10, list.size());
        pageList.setRecords(list);
        return Result.OK(pageList);
    }

    /**
     * ?????????????????????
     *
     * @param parentIds ???ID????????????????????????????????????
     * @param parentIds
     * @return ?????? IPage
     * @return
     */
    @AutoLog(value = "???????????????-?????????????????????")
    @ApiOperation(value = "???????????????-?????????????????????", notes = "???????????????-?????????????????????")
    @GetMapping("/getChildListBatch")
    public Result getChildListBatch(@RequestParam("parentIds") String parentIds) {
        try {
            QueryWrapper<SmartAssessmentContent> queryWrapper = new QueryWrapper<>();
            List<String> parentIdList = Arrays.asList(parentIds.split(","));
            queryWrapper.in("pid", parentIdList);
            List<SmartAssessmentContent> list = smartAssessmentContentService.list(queryWrapper);
            IPage<SmartAssessmentContent> pageList = new Page<>(1, 10, list.size());
            pageList.setRecords(list);
            return Result.OK(pageList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("??????????????????????????????" + e.getMessage());
        }
    }

    /**
     * ???????????????
     *
     * @param req
     * @return
     */
    @AutoLog(value = "???????????????????????????-???????????????")
    @ApiOperation(value="???????????????????????????-???????????????", notes="???????????????????????????-???????????????")
    @GetMapping(value = "/dict")
    public Result<?> queryDicList(@RequestParam("level") String level,
                                  @RequestParam("missionId") String missionId,
                                  HttpServletRequest req) {
        QueryWrapper<SmartAssessmentContent> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "name");
        queryWrapper.eq("is_key", "0");
        queryWrapper.eq("mission_id", missionId);
        if ("1".equals(level)) {
            queryWrapper.eq("pid", "0");
        } else if ("2".equals(level)) {
            queryWrapper.ne("pid", "0");
        } else {
            return Result.error("??????????????????????????????????????????");
        }
        List<SmartAssessmentContent> list = smartAssessmentContentService.list(queryWrapper);
        return Result.OK(list);
    }

    /**
     * ??????
     *
     * @param smartAssessmentContent
     * @return
     */
    @AutoLog(value = "???????????????-??????")
    @ApiOperation(value = "???????????????-??????", notes = "???????????????-??????")
    @PostMapping(value = "/add")
    @Transactional(rollbackFor = Exception.class)
    public Result<?> add(@RequestBody SmartAssessmentContent smartAssessmentContent) {
        smartAssessmentContentService.addSmartAssessmentContent(smartAssessmentContent);
        // ????????????????????????????????????
        Integer sortNum = 1;
        if (Objects.equals(smartAssessmentContent.getPid(), "0")) {
            SmartRankVisible smartRankVisible = new SmartRankVisible();
            smartRankVisible.setMissionId(smartAssessmentContent.getMissionId());
            smartRankVisible.setContentId(smartAssessmentContent.getId());
            smartRankVisible.setContentName(smartAssessmentContent.getName());
            smartRankVisible.setVisible("1");
            smartRankVisible.setTag("content");
            smartRankVisible.setSort(sortNum);
            smartRankVisibleService.save(smartRankVisible);
            sortNum++;
        }
        // ?????????????????????,??????????????????????????????
        Integer point = smartAssessmentContent.getPoint();
        QueryWrapper<SmartAssessmentContent> queryWrapper = new QueryWrapper<>();

        if (smartAssessmentContent.getIsKey() == 1) {
            updateSuperiorPoint(smartAssessmentContent, point);
        }
        return Result.OK("???????????????");
    }

    /**
     * ????????????
     *
     * @param missionId
     * @return
     */
    @AutoLog(value = "???????????????-????????????")
    @ApiOperation(value = "???????????????-????????????", notes = "???????????????-????????????")
    @GetMapping(value = "/checkPoint")
    @Transactional(rollbackFor = Exception.class)
    public Result<?> checkPoint(@RequestParam(name = "missionId", required = true) String missionId) {
        // ???????????????????????????
        QueryWrapper<SmartAssessmentContent> queryWrapper = new QueryWrapper<>();
        SmartAssessmentContent updateContent = new SmartAssessmentContent();
        updateContent.setPoint(0);
        queryWrapper.eq("mission_id", missionId).eq("is_key", 0);
        smartAssessmentContentService.update(updateContent, queryWrapper);

        // ???????????????????????????
        SmartAssessmentMission mission = smartAssessmentMissionService.getById(missionId);
        mission.setTotalPoint(0);
        smartAssessmentMissionService.updateById(mission);

        queryWrapper.clear();
        queryWrapper.eq("mission_id", missionId).eq("is_key", 1);
        List<SmartAssessmentContent> assessmentContentList = smartAssessmentContentService.list(queryWrapper);
        assessmentContentList.forEach(smartAssessmentContent -> {
            updateSuperiorPoint(smartAssessmentContent, smartAssessmentContent.getPoint());
        });

        return Result.OK("????????????!");
    }

    /**
     * ??????
     *
     * @param smartAssessmentContent
     * @return
     */
    @AutoLog(value = "???????????????-??????")
    @ApiOperation(value = "???????????????-??????", notes = "???????????????-??????")
    @PutMapping(value = "/edit")
    @Transactional(rollbackFor = Exception.class)
    public Result<?> edit(@RequestBody SmartAssessmentContent smartAssessmentContent) {
        // ???????????????????????????
        QueryWrapper<SmartAssessmentContent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", smartAssessmentContent.getId());
        SmartAssessmentContent lastOne = smartAssessmentContentService.getOne(queryWrapper);

        if (lastOne.getIsKey() - smartAssessmentContent.getIsKey() == 1) {
            // ????????????????????????????????????????????????
            smartAssessmentContent.setInstructions(null);
            smartAssessmentContent.setPoint(0);
            smartAssessmentContent.setAssDepart(null);
            smartAssessmentContent.setAssTeam(null);
        }

        // ????????????????????????,????????????
        int increment = smartAssessmentContent.getPoint() - lastOne.getPoint();

        // ???????????????????????????
        smartAssessmentContentService.updateSmartAssessmentContent(smartAssessmentContent);

        // ???????????????????????????????????????????????????
        if (increment != 0) {
            updateSuperiorPoint(smartAssessmentContent, increment);
        }
        return Result.OK("????????????!");
    }

    /**
     * ??????????????????
     *
     * @param smartAssessmentContent
     * @param increment              ????????????
     */
    @Transactional(rollbackFor = Exception.class)
    void updateSuperiorPoint(SmartAssessmentContent smartAssessmentContent, int increment) {
        // ????????????????????????
        SmartAssessmentMission mission = smartAssessmentMissionService.getById(smartAssessmentContent.getMissionId());
        mission.setTotalPoint(mission.getTotalPoint() + increment);
        smartAssessmentMissionService.updateById(mission);

        QueryWrapper<SmartAssessmentContent> queryWrapper = new QueryWrapper<>();
        while (oConvertUtils.isNotEmpty(smartAssessmentContent.getPid())) {
            // ??????????????????????????????
            queryWrapper.clear();
            queryWrapper.eq("id", smartAssessmentContent.getPid());
            smartAssessmentContent = smartAssessmentContentService.getOne(queryWrapper);
            if (oConvertUtils.isEmpty(smartAssessmentContent)) {
                break;
            }
            smartAssessmentContent.setPoint(smartAssessmentContent.getPoint() + increment);
            smartAssessmentContentService.updateSmartAssessmentContent(smartAssessmentContent);
        }
    }

    /**
     * ??????id??????
     *
     * @param id
     * @return
     */
    @AutoLog(value = "???????????????-??????id??????")
    @ApiOperation(value = "???????????????-??????id??????", notes = "???????????????-??????id??????")
    @DeleteMapping(value = "/delete")
    @Transactional(rollbackFor = Exception.class)
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        // ???????????????????????????
        QueryWrapper<SmartAssessmentContent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        SmartAssessmentContent smartAssessmentContent = smartAssessmentContentService.getOne(queryWrapper);
        if (oConvertUtils.isNotEmpty(smartAssessmentContent)) {
            updateSuperiorPoint(smartAssessmentContent, smartAssessmentContent.getPoint() * -1);
        }
        smartAssessmentContentService.deleteSmartAssessmentContent(id);
        // ?????????????????????????????????????????????
        QueryWrapper<SmartRankVisible> rankVisibleQueryWrapper = new QueryWrapper<>();
        rankVisibleQueryWrapper.eq("content_id", id);
        smartRankVisibleService.remove(rankVisibleQueryWrapper);
        return Result.OK("????????????!");
    }

    /**
     * ????????????
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "???????????????-????????????")
    @ApiOperation(value = "???????????????-????????????", notes = "???????????????-????????????")
    @DeleteMapping(value = "/deleteBatch")
    @Transactional(rollbackFor = Exception.class)
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.smartAssessmentContentService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("?????????????????????");
    }

    /**
     * ??????id??????
     *
     * @param id
     * @return
     */
    @AutoLog(value = "???????????????-??????id??????")
    @ApiOperation(value = "???????????????-??????id??????", notes = "???????????????-??????id??????")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SmartAssessmentContent smartAssessmentContent = smartAssessmentContentService.getById(id);
        if (smartAssessmentContent == null) {
            return Result.error("?????????????????????");
        }
        return Result.OK(smartAssessmentContent);
    }

    /**
     * ??????excel
     *
     * @param request
     * @param smartAssessmentContent
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartAssessmentContent smartAssessmentContent) {
        // Step.1 ??????????????????
        QueryWrapper<SmartAssessmentContent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mission_id", smartAssessmentContent.getMissionId());
        queryWrapper.orderByAsc("pid", "is_key");
//        QueryWrapper<SmartAssessmentContent> queryWrapper = QueryGenerator.initQueryWrapper(smartAssessmentContent, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
//        queryWrapper.orderByAsc("pid");
//        queryWrapper.orderByAsc("is_key");

        // Step.2 ??????????????????
        List<SmartAssessmentContent> pageList = service.list(queryWrapper);
        List<SmartAssessmentContent> exportList = null;

        // ??????????????????
        String selections = request.getParameter("selections");
        if (oConvertUtils.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            exportList = pageList.stream().filter(item -> selectionList.contains(getId(item))).collect(Collectors.toList());
        } else {
            exportList = pageList;
        }


        for (SmartAssessmentContent item : exportList) {
            if (oConvertUtils.isNotEmpty(item.getAssDepart())) {
                List<String> departList = Arrays.asList(item.getAssDepart().split(","));
                List<String> departNameList = new ArrayList<>();
                for (String departId : departList) {
                    SmartAssessmentDepartment assessmentDepartment = smartAssessmentDepartmentService.getById(departId);
                    if (oConvertUtils.isEmpty(assessmentDepartment)) {
                        continue;
                    }
                    SysDepartModel sysDepartModel = iSysBaseAPI.selectAllById(assessmentDepartment.getDepartId());
                    if (oConvertUtils.isNotEmpty(sysDepartModel)) {
                        departNameList.add(sysDepartModel.getDepartName());
                    }
                }
                item.setAssDepart(Joiner.on(",").join(departNameList));
            }
        }

        // Step.3 AutoPoi ??????Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "???????????????"); //???????????????filename?????? ,??????????????????????????????
        mv.addObject(NormalExcelConstants.CLASS, SmartAssessmentContent.class);
        //update-begin--Author:liusq  Date:20210126 for????????????????????????ImageBasePath?????????--------------------
        ExportParams exportParams = new ExportParams("???????????????" + "??????", "?????????:" + sysUser.getRealname(), "???????????????");
        exportParams.setImageBasePath(upLoadPath);
        //update-end--Author:liusq  Date:20210126 for????????????????????????ImageBasePath?????????----------------------
        mv.addObject(NormalExcelConstants.PARAMS, exportParams);
        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        return mv;
    }

    /**
     * ??????excel????????????
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        String missionId = request.getParameter("missionId");
        System.out.println(missionId);
        if (oConvertUtils.isEmpty(missionId)) {
            return Result.error("?????????????????????????????????ID");
        }
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// ????????????????????????
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<SmartAssessmentContent> list = ExcelImportUtil.importExcel(file.getInputStream(), SmartAssessmentContent.class, params);
                //update-begin-author:taoyan date:20190528 for:??????????????????
                long start = System.currentTimeMillis();
                List<SmartAssessmentContent> successList = new ArrayList<>();
                List<SmartRankVisible> rankVisibleList = new ArrayList<>();

                for (SmartAssessmentContent item : list) {
                    if (!item.getMissionId().equals(missionId)) {
                        return Result.error("?????????????????????????????????????????????????????????!");
                    }
                }

                for (SmartAssessmentContent item : list) {
                    SmartAssessmentContent content = new SmartAssessmentContent();
                    BeanUtils.copyProperties(item, content);

                    // 4. ????????????
                    if (oConvertUtils.isNotEmpty(content.getPid()) && !"0".equals(content.getPid())) {
                        SmartAssessmentContent temp = smartAssessmentContentService.getById(content.getPid());

                        for (SmartAssessmentContent successItem : successList) {
                            if (StringUtils.equals(successItem.getName(), temp.getName())) {
                                content.setPid(successItem.getId());
                                break;
                            }
                        }
                    } else {
                        content.setPid("0");
                        Integer sortNum = 1;
                        SmartRankVisible smartRankVisible = new SmartRankVisible();
                        smartRankVisible.setMissionId(content.getMissionId());
                        smartRankVisible.setContentId(content.getId());
                        smartRankVisible.setContentName(content.getName());
                        smartRankVisible.setVisible("1");
                        smartRankVisible.setTag("content");
                        smartRankVisible.setSort(sortNum);
                        rankVisibleList.add(smartRankVisible);
                    }
                    // ????????????
                    if (oConvertUtils.isNotEmpty(content.getAssDepart())) {
                        List<String> departList = Arrays.asList(content.getAssDepart().split(","));
                        List<String> departIdList = new ArrayList<>();
                        for (String departId : departList) {
                            String assessmentDepartmentId = smartAssessmentDepartmentService.getAssessmentDepartmentIdByDepartName(departId);
                            if (oConvertUtils.isEmpty(assessmentDepartmentId)) {
                                continue;
                            }
                            departIdList.add(assessmentDepartmentId);
                        }
                        content.setAssDepart(Joiner.on(",").join(departIdList));
                    }

                    smartAssessmentContentService.addSmartAssessmentContent(content);
                    successList.add(content);

                }
                smartRankVisibleService.saveBatch(rankVisibleList);

                //400??? saveBatch????????????1592??????  ????????????????????????1947??????
                //1200???  saveBatch????????????3687?????? ????????????????????????5212??????
                log.info("????????????" + (System.currentTimeMillis() - start) + "??????");

                //update-end-author:taoyan date:20190528 for:??????????????????
                return Result.ok("????????????????????????????????????" + list.size());

            } catch (Exception e) {
                //update-begin-author:taoyan date:20211124 for: ??????????????????????????????
                String msg = e.getMessage();
                log.error(msg, e);
                if (msg != null && msg.indexOf("Duplicate entry") >= 0) {
                    return Result.error("??????????????????:??????????????????");
                } else {
                    return Result.error("??????????????????:" + e.getMessage());
                }
                //update-end-author:taoyan date:20211124 for: ??????????????????????????????
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.error("?????????????????????");
//        return super.importExcel(request, response, SmartAssessmentContent.class);
    }

    /**
     * ????????????ID
     *
     * @return
     */
    private String getId(SmartAssessmentContent item) {
        try {
            return PropertyUtils.getProperty(item, "id").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
