package org.jeecg.modules.smartAssessmentMission.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Joiner;
//import io.github.classgraph.utils.Join;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtil;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.SqlInjectionUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.smartAnswerAssContent.entity.SmartAnswerAssContent;
import org.jeecg.modules.smartAnswerAssContent.service.ISmartAnswerAssContentService;
import org.jeecg.modules.smartAnswerInfo.entity.SmartAnswerInfo;
import org.jeecg.modules.smartAnswerInfo.service.ISmartAnswerInfoService;
import org.jeecg.modules.smartAssessmentContent.entity.SmartAssessmentContent;
import org.jeecg.modules.smartAssessmentContent.service.ISmartAssessmentContentService;
import org.jeecg.modules.smartAssessmentDepartment.entity.SmartAssessmentDepartment;
import org.jeecg.modules.smartAssessmentDepartment.service.ISmartAssessmentDepartmentService;
import org.jeecg.modules.smartAssessmentMission.entity.SmartAssessmentDepart;
import org.jeecg.modules.smartAssessmentMission.entity.SmartAssessmentMission;
import org.jeecg.modules.smartAssessmentMission.service.ISmartAssessmentDepartService;
import org.jeecg.modules.smartAssessmentMission.service.ISmartAssessmentMissionService;
import org.jeecg.modules.smartRankVisible.entity.SmartRankVisible;
import org.jeecg.modules.smartRankVisible.service.ISmartRankVisibleService;
import org.jeecg.modules.smartAssessmentTeam.entity.SmartAssessmentTeam;
import org.jeecg.modules.smartAssessmentTeam.service.ISmartAssessmentTeamService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description: 考核任务表
 * @Author: jeecg-boot
 * @Date: 2022-02-12
 * @Version: V1.0
 */
@Api(tags = "考核任务表")
@RestController
@RequestMapping("/smartAssessmentMission/smartAssessmentMission")
@Slf4j
public class SmartAssessmentMissionController extends JeecgController<SmartAssessmentMission, ISmartAssessmentMissionService> {

    @Autowired
    private ISmartAssessmentMissionService smartAssessmentMissionService;

    @Autowired
    private ISmartAssessmentDepartService smartAssessmentDepartService;

    @Autowired
    private ISmartAnswerInfoService smartAnswerInfoService;

    @Autowired
    private ISmartAnswerAssContentService smartAnswerAssContentService;

    @Autowired
    private ISmartAssessmentContentService smartAssessmentContentService;

    @Autowired
    private ISmartAssessmentTeamService smartAssessmentTeamService;

    @Autowired
    private ISmartAssessmentDepartmentService smartAssessmentDepartmentService;

    @Autowired
    private ISmartRankVisibleService smartRankVisibleService;


    /*---------------------------------主表处理-begin-------------------------------------*/

    /**
     * 分页列表查询
     *
     * @param smartAssessmentMission
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "考核任务表-分页列表查询")
    @ApiOperation(value = "考核任务表-分页列表查询", notes = "考核任务表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SmartAssessmentMission smartAssessmentMission,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<SmartAssessmentMission> queryWrapper = QueryGenerator.initQueryWrapper(smartAssessmentMission, req.getParameterMap());
        Page<SmartAssessmentMission> page = new Page<SmartAssessmentMission>(pageNo, pageSize);
        IPage<SmartAssessmentMission> pageList = smartAssessmentMissionService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 最终评分页面分页列表查询
     *
     * @param smartAssessmentMission
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "考核任务表-分页列表查询")
    @ApiOperation(value = "考核任务表-分页列表查询", notes = "考核任务表-分页列表查询")
    @GetMapping(value = "/finalScoreList")
    public Result<?> queryFinalScorePageList(SmartAssessmentMission smartAssessmentMission,
                                             @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                             @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                             HttpServletRequest req) {
        QueryWrapper<SmartAssessmentMission> queryWrapper = QueryGenerator.initQueryWrapper(smartAssessmentMission, req.getParameterMap());
        queryWrapper.eq("mission_status", "已发布");
        Page<SmartAssessmentMission> page = new Page<SmartAssessmentMission>(pageNo, pageSize);
        IPage<SmartAssessmentMission> pageList = smartAssessmentMissionService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 首页考核正在进行任务列表
     *
     * @param smartAssessmentMission
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "考核任务表-分页列表查询")
    @ApiOperation(value = "考核任务表-分页列表查询", notes = "考核任务表-分页列表查询")
    @GetMapping(value = "/indexList")
    public Result<?> queryIndexPageList(SmartAssessmentMission smartAssessmentMission,
                                        @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                        HttpServletRequest req) {
        QueryWrapper<SmartAssessmentMission> queryWrapper = QueryGenerator.initQueryWrapper(smartAssessmentMission, req.getParameterMap());
        // TODO： 权限控制
        // 只查看正在考核的任务
        queryWrapper.eq("mission_status", "已发布");
        Page<SmartAssessmentMission> page = new Page<SmartAssessmentMission>(pageNo, pageSize);
        IPage<SmartAssessmentMission> pageList = smartAssessmentMissionService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询历史考核任务
     *
     * @param smartAssessmentMission
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "答题信息表-分页列表查询历史考核任务")
    @ApiOperation(value = "答题信息表-分页列表查询历史考核任务", notes = "答题信息表-分页列表查询历史考核任务")
    @GetMapping(value = "/historyList")
    public Result<?> queryPageHistoryList(SmartAssessmentMission smartAssessmentMission,
                                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                          HttpServletRequest req) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String hasQuery = req.getParameter("hasQuery");
        if (hasQuery != null && "true".equals(hasQuery)) {
            QueryWrapper<SmartAssessmentMission> queryWrapper = QueryGenerator.initQueryWrapper(smartAssessmentMission, req.getParameterMap());
            queryWrapper.select("distinct id").eq("mission_status", "发布评分结果");
            List<SmartAssessmentMission> missionList = smartAssessmentMissionService.list(queryWrapper);
            List<String> missionIdList = new ArrayList<>();
            missionList.forEach(mission -> {
                missionIdList.add(mission.getId());
            });

            if (missionList.size() == 0) {
                return Result.error("没有找到符合条件的考核任务!");
            }

            // 查询上面所有考核任务信息
            QueryWrapper<SmartAnswerInfo> answerInfoQueryWrapper = new QueryWrapper<>();
            // 包含本单位的任务
            answerInfoQueryWrapper.eq("depart", sysUser.getDepartId())
                    .in("mission_id", missionIdList)
                    .eq("mission_status", "发布评分结果");
            Page<SmartAnswerInfo> page = new Page<SmartAnswerInfo>(pageNo, pageSize);
            IPage<SmartAnswerInfo> pageList = smartAnswerInfoService.page(page, answerInfoQueryWrapper);
            return Result.OK(pageList);
        } else {
            QueryWrapper<SmartAnswerInfo> queryWrapper = new QueryWrapper<>();
            // 包含本单位的任务
            queryWrapper.eq("depart", sysUser.getDepartId())
                    .eq("mission_status", "发布评分结果");
            Page<SmartAnswerInfo> page = new Page<SmartAnswerInfo>(pageNo, pageSize);
            IPage<SmartAnswerInfo> pageList = smartAnswerInfoService.page(page, queryWrapper);
            return Result.OK(pageList);
        }

    }

    /**
     * 分页列表查询用户考核组参与评分的考核任务
     *
     * @param smartAssessmentMission
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "考核任务表-分页列表查询考核组参与的考核任务")
    @ApiOperation(value = "考核任务表-分页列表查询考核组参与的考核任务", notes = "考核任务表-分页列表查询考核组参与的考核任务")
    @GetMapping(value = "/teamMissionList")
    public Result<?> queryTeamPageList(SmartAssessmentMission smartAssessmentMission,
                                       @RequestParam(name = "scoreRoleId") String scoreRoleId,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                       HttpServletRequest req) {
        // 查询考核组参与的考核任务ID
        QueryWrapper<SmartAssessmentContent> contentQueryWrapper = new QueryWrapper<>();
        contentQueryWrapper.select("distinct mission_id").like("ass_team", scoreRoleId);
        List<SmartAssessmentContent> contentList = smartAssessmentContentService.list(contentQueryWrapper);
        if (contentList.size() == 0) {
            return Result.error("没有找到符合条件的考核任务!");
        }
        List<String> missionIdList = new ArrayList<>();
        contentList.forEach(smartAssessmentContent -> missionIdList.add(smartAssessmentContent.getMissionId()));

        QueryWrapper<SmartAssessmentMission> queryWrapper = QueryGenerator.initQueryWrapper(smartAssessmentMission, req.getParameterMap());
        queryWrapper.eq("mission_status", "已发布").in("id", missionIdList);
        Page<SmartAssessmentMission> page = new Page<SmartAssessmentMission>(pageNo, pageSize);
        IPage<SmartAssessmentMission> pageList = smartAssessmentMissionService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询用户考核单位参与评分的考核任务
     *
     * @param smartAssessmentMission
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "考核任务表-分页列表查询考核组参与的考核任务")
    @ApiOperation(value = "考核任务表-分页列表查询考核组参与的考核任务", notes = "考核任务表-分页列表查询考核组参与的考核任务")
    @GetMapping(value = "/departmentMissionList")
    public Result<?> queryDepartmentPageList(SmartAssessmentMission smartAssessmentMission,
                                             @RequestParam(name = "scoreRoleId") String scoreRoleId,
                                             @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                             @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                             HttpServletRequest req) {

        // 查询考核单位参与的考核任务ID
        QueryWrapper<SmartAssessmentContent> contentQueryWrapper = new QueryWrapper<>();
        contentQueryWrapper.select("distinct mission_id").like("ass_depart", scoreRoleId);
        List<SmartAssessmentContent> contentList = smartAssessmentContentService.list(contentQueryWrapper);
        List<String> missionIdList = new ArrayList<>();
        contentList.forEach(smartAssessmentContent -> missionIdList.add(smartAssessmentContent.getMissionId()));

        QueryWrapper<SmartAssessmentMission> queryWrapper = QueryGenerator.initQueryWrapper(smartAssessmentMission, req.getParameterMap());
        queryWrapper.eq("mission_status", "已发布").in("id", missionIdList);
        Page<SmartAssessmentMission> page = new Page<SmartAssessmentMission>(pageNo, pageSize);
        IPage<SmartAssessmentMission> pageList = smartAssessmentMissionService.page(page, queryWrapper);
        return Result.OK(pageList);
    }


    /**
     * 添加
     *
     * @param smartAssessmentMission
     * @return
     */
    @AutoLog(value = "考核任务表-添加")
    @ApiOperation(value = "考核任务表-添加", notes = "考核任务表-添加")
    @PostMapping(value = "/add")
    @Transactional(rollbackFor = Exception.class)
    public Result<?> add(@RequestBody SmartAssessmentMission smartAssessmentMission) {
        smartAssessmentMissionService.save(smartAssessmentMission);
        // 新增每个任务排名总分、排名，去年排名字段的可见性 @author: sike
        Map<String, String> rankMap = new HashMap<>();
        rankMap.put("totalScore", "总分");
        rankMap.put("rank", "排名");
        rankMap.put("lastRank", "上一年度排名");

        Integer sortNum = 900;
        List<SmartRankVisible> rankVisibleList = new ArrayList<>();
        for (Map.Entry<String, String> vo : rankMap.entrySet()) {
            SmartRankVisible smartRankVisible = new SmartRankVisible();
            smartRankVisible.setMissionId(smartAssessmentMission.getId());
            smartRankVisible.setContentId(vo.getKey());
            smartRankVisible.setContentName(vo.getValue());
            smartRankVisible.setVisible("1");
            smartRankVisible.setSort(sortNum);
            smartRankVisible.setTag("stable");
            rankVisibleList.add(smartRankVisible);
            sortNum++;
        }
        smartRankVisibleService.saveBatch(rankVisibleList);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param smartAssessmentMission
     * @return
     */
    @AutoLog(value = "考核任务表-编辑")
    @ApiOperation(value = "考核任务表-编辑", notes = "考核任务表-编辑")
    @PutMapping(value = "/edit")
    @Transactional(rollbackFor = Exception.class)
    public Result<?> edit(@RequestBody SmartAssessmentMission smartAssessmentMission) {
        SmartAssessmentMission mission = smartAssessmentMissionService.getById(smartAssessmentMission.getId());
        if (mission.getAssessmentTime().equals(smartAssessmentMission.getAssessmentTime())) {
            smartAssessmentMissionService.updateById(smartAssessmentMission);
        } else {
            smartAssessmentMissionService.updateById(smartAssessmentMission);
            // 考核时间变更，修改所有被考核单位的时间
            UpdateWrapper<SmartAssessmentDepart> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("deadline", smartAssessmentMission.getAssessmentTime());
            updateWrapper.eq("mission_id", smartAssessmentMission.getId());
            smartAssessmentDepartService.update(updateWrapper);

            // 考核时间变更，修改考核信息表中的考核时间
            UpdateWrapper<SmartAnswerInfo> infoUpdateWrapper = new UpdateWrapper<>();
            infoUpdateWrapper.set("end_time", smartAssessmentMission.getAssessmentTime());
            infoUpdateWrapper.eq("mission_id", smartAssessmentMission.getId());
            smartAnswerInfoService.update(infoUpdateWrapper);
        }
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "考核任务表-通过id删除")
    @ApiOperation(value = "考核任务表-通过id删除", notes = "考核任务表-通过id删除")
    @DeleteMapping(value = "/delete")
    @Transactional(rollbackFor = Exception.class)
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        // 删除考核任务下的考核内容
        QueryWrapper<SmartAssessmentContent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mission_id", id);
        smartAssessmentContentService.remove(queryWrapper);
        smartAssessmentMissionService.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     * 更新全区完成度
     *
     * @param smartAssessmentMission
     * @return
     */
    @AutoLog(value = "考核任务表-更新全区完成度")
    @ApiOperation(value = "考核任务表-更新全区完成度", notes = "考核任务表-更新全区完成度")
    @PutMapping(value = "/updateCompletionDegree")
    @Transactional(rollbackFor = Exception.class)
    public Result<?> updateCompletionDegree(@RequestBody SmartAssessmentMission smartAssessmentMission) {
        if (smartAssessmentMission.getKeyPointsAmount() == 0) {
            return Result.error("该考核任务总要点个数为0，无法计算！");
        }

        QueryWrapper<SmartAnswerInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mission_id", smartAssessmentMission.getId());
        List<SmartAnswerInfo> answerInfoList = smartAnswerInfoService.list(queryWrapper);
        if (answerInfoList.size() == 0) {
            return Result.error("该考核任务没有被考核单位，无法计算！");
        }
        double allFinishedCount = 0;

        for (SmartAnswerInfo smartAnswerInfo : answerInfoList) {
            if ("未签收".equals(smartAnswerInfo.getMissionStatus())) {
                continue;
            }
            // 查询上传文件数目大于0的要点个数
            QueryWrapper<SmartAnswerAssContent> assContentQueryWrapper = new QueryWrapper<>();
            assContentQueryWrapper.eq("main_id", smartAnswerInfo.getId())
                    .eq("is_key", 1)
                    .eq("has_child", "0")
                    .ne("upload_count", 0);
            double count = smartAnswerAssContentService.count(assContentQueryWrapper);
            allFinishedCount += count;

            // 如果完成要点数没有改变则不进行更新
            if (smartAnswerInfo.getFinishedKeyPointAmount() != (int) count) {
                smartAnswerInfo.setFinishedKeyPointAmount((int) count);

                smartAnswerInfo.setCompletionDegree(count / smartAnswerInfo.getTotalKeyPointAmount());

                smartAnswerInfoService.updateById(smartAnswerInfo);
            }
        }

        smartAssessmentMission.setCompletionDegree(allFinishedCount / (answerInfoList.size() * smartAssessmentMission.getKeyPointsAmount()));


        smartAssessmentMissionService.updateById(smartAssessmentMission);
        return Result.OK("编辑成功!");
    }

    /**
     * 撤销任务发布
     *
     * @param smartAssessmentMission
     * @return
     */
    @AutoLog(value = "考核任务表-撤销任务发布")
    @ApiOperation(value = "考核任务表-撤销任务发布", notes = "考核任务表-撤销任务发布")
    @PutMapping(value = "/reset")
    @Transactional(rollbackFor = Exception.class)
    public Result<?> reset(@RequestBody SmartAssessmentMission smartAssessmentMission) {

        // 查询答题信息表相关数据的ID
        QueryWrapper<SmartAnswerInfo> answerInfoQueryWrapper = new QueryWrapper<>();
        answerInfoQueryWrapper.select("id").eq("mission_id", smartAssessmentMission.getId());
        List<SmartAnswerInfo> answerInfoList = smartAnswerInfoService.list(answerInfoQueryWrapper);
        // 如果没有被考核单位答题记录，则直接更新任务状态返回
        if (answerInfoList.size() == 0) {
            // 更新任务状态
            smartAssessmentMission.setMissionStatus("未发布");
            smartAssessmentMissionService.updateById(smartAssessmentMission);
            return Result.OK("撤销任务成功!");
        }
        List<String> answerInfoIdsList = new ArrayList<>();
        answerInfoList.forEach(smartAnswerInfo -> {
            answerInfoIdsList.add(smartAnswerInfo.getId());
        });

        // 删除答题考核节点相关数据
        QueryWrapper<SmartAnswerAssContent> contentQueryWrapper = new QueryWrapper<>();
        contentQueryWrapper.select("distinct id").in("main_id", answerInfoIdsList);
        List<SmartAnswerAssContent> answerAssContentList = smartAnswerAssContentService.list(contentQueryWrapper);
        List<String> answerAssContentIdList = new ArrayList<>();
        answerAssContentList.forEach(smartAnswerAssContent -> answerAssContentIdList.add(smartAnswerAssContent.getId()));
        smartAnswerAssContentService.delBatchMain(answerAssContentIdList);

        // 删除答题信息表中相关数据
        smartAnswerInfoService.removeByIds(answerInfoIdsList);

        // 更新被考核单位签收信息
        SmartAssessmentDepart smartAssessmentDepart = new SmartAssessmentDepart();
        smartAssessmentDepart.setSignTime(null);
        smartAssessmentDepart.setSignStatus(null);
        smartAssessmentDepart.setSignUser(null);
        QueryWrapper<SmartAssessmentDepart> departQueryWrapper = new QueryWrapper<>();
        departQueryWrapper.eq("mission_id", smartAssessmentMission.getId());
        smartAssessmentDepartService.update(smartAssessmentDepart, departQueryWrapper);

        // 更新任务状态
        smartAssessmentMission.setMissionStatus("未发布");
        smartAssessmentMissionService.updateById(smartAssessmentMission);
        return Result.OK("撤销任务成功!");
    }

    /**
     * 考核任务被考核单位的重复校验接口
     *
     * @return
     */
    @GetMapping(value = "/duplicateCheck")
    @ApiOperation("考核任务被考核单位的重复校验接口")
    public Result<Object> doDuplicateCheckWithDelFlag(@RequestParam(name = "departId", required = true) String departId,
                                                      @RequestParam(name = "missionId", required = true) String missionId,
                                                      @RequestParam(name = "dataId", required = false) String dataId,
                                                      HttpServletRequest request) {
        Long num = null;

        log.info("----duplicate check------："+ departId);
        QueryWrapper<SmartAssessmentDepart> queryWrapper = new QueryWrapper<>();
        List<String> departIds = Arrays.asList(departId.split(","));
        queryWrapper.eq("mission_id", missionId).in("assessment_depart", departIds);
        if (StringUtils.isNotBlank(dataId)) {
            // [2].编辑页面校验
            queryWrapper.ne("id", dataId);
            num = smartAssessmentDepartService.count(queryWrapper);
        } else {
            // [1].添加页面校验
            num = smartAssessmentDepartService.count(queryWrapper);
        }

        if (num == null || num == 0) {
            // 该值可用
            return Result.ok("该值可用！");
        } else {
            // 该值不可用
            log.info("该值不可用，系统中已存在！");
            return Result.error("考核任务已添加该被考核单位！");
        }
    }

    /**
     * 发布
     *
     * @param smartAssessmentMission
     * @return
     */
    @AutoLog(value = "考核任务表-发布")
    @ApiOperation(value = "考核任务表-发布", notes = "考核任务表-发布")
    @PutMapping(value = "/publish")
    @Transactional(rollbackFor = Exception.class)
    public Result<?> publish(@RequestBody SmartAssessmentMission smartAssessmentMission) {
        // 统计考核要点数目
        QueryWrapper<SmartAssessmentContent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mission_id", smartAssessmentMission.getId()).eq("is_key", 1).eq("has_child", "0");
        Integer count = Math.toIntExact(smartAssessmentContentService.count(queryWrapper));
        smartAssessmentMission.setKeyPointsAmount(count);

        List<SmartAssessmentDepart> smartAssessmentDeparts = smartAssessmentDepartService.selectByMainId(smartAssessmentMission.getId());
        // 答题信息表生成记录
        List<SmartAnswerInfo> list = new ArrayList<>();
        for (SmartAssessmentDepart smartAssessmentDepart : smartAssessmentDeparts) {
            SmartAnswerInfo smartAnswerInfo = new SmartAnswerInfo();
            smartAnswerInfo.setMissionId(smartAssessmentMission.getId());
            smartAnswerInfo.setMissionStatus("未签收");
            smartAnswerInfo.setEndTime(smartAssessmentDepart.getDeadline());
            smartAnswerInfo.setDepart(smartAssessmentDepart.getAssessmentDepart());
            smartAnswerInfo.setTotalKeyPointAmount(count);
            smartAnswerInfo.setIsShowScore(smartAssessmentDepart.getIsShowScore());
            smartAnswerInfo.setMarkedContent("");
            list.add(smartAnswerInfo);
        }
        if (list.size() > 0) {
            smartAnswerInfoService.saveBatch(list);
        }

        smartAssessmentMission.setMissionStatus("已发布");
        smartAssessmentMissionService.updateById(smartAssessmentMission);

        return Result.OK("发布成功");
    }

    /**
     * 发布评分结果
     *
     * @param smartAssessmentMission
     * @return
     */
    @AutoLog(value = "考核任务表-发布评分结果")
    @ApiOperation(value = "考核任务表-发布评分结果", notes = "考核任务表-发布评分结果")
    @PutMapping(value = "/publishScore")
    @Transactional(rollbackFor = Exception.class)
    public Result<?> publishScore(@RequestBody SmartAssessmentMission smartAssessmentMission) {
        if (oConvertUtils.isEmpty(smartAssessmentMission.getId())) {
            return Result.error("数据错误!");
        }

        QueryWrapper<SmartAnswerInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mission_status", "未签收").eq("mission_id", smartAssessmentMission.getId());
        long count = smartAnswerInfoService.count(queryWrapper);
        if (count != 0) {
            return Result.error("存在未签收的单位,无法发布评分结果!");
        }
        // 生成排名
        generateRank(smartAssessmentMission.getId());
        // 修改任务状态
        smartAnswerInfoService.updateMissionStatus(smartAssessmentMission.getId(), "发布评分结果");
        smartAssessmentMission.setMissionStatus("发布评分结果");
        smartAssessmentMissionService.updateById(smartAssessmentMission);

        return Result.OK("发布成功");
    }

    /**
     * 生成排名
     *
     * @param smartAssessmentMission
     * @return
     */
    @AutoLog(value = "考核任务表-生成排名")
    @ApiOperation(value = "考核任务表-生成排名", notes = "考核任务表-生成排名")
    @PutMapping(value = "/generateRank")
    @Transactional(rollbackFor = Exception.class)
    public Result<?> generateRank(@RequestBody SmartAssessmentMission smartAssessmentMission) {
        if (oConvertUtils.isEmpty(smartAssessmentMission.getId())) {
            return Result.error("数据错误!");
        }

        QueryWrapper<SmartAnswerInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mission_status", "未签收").eq("mission_id", smartAssessmentMission.getId());
        long count = smartAnswerInfoService.count(queryWrapper);
        if (count != 0) {
            return Result.error("存在未签收的单位!");
        }
        // 生成排名
        generateRank(smartAssessmentMission.getId());

        return Result.OK("生成成功!");
    }

    @Transactional(rollbackFor = Exception.class)
    void generateRank(String missionId) {
        QueryWrapper<SmartAnswerInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mission_id", missionId).orderByDesc("total_points");
        List<SmartAnswerInfo> list = smartAnswerInfoService.list(queryWrapper);
        for (int i = 0; i < list.size(); i++) {
            SmartAnswerInfo smartAnswerInfo = list.get(i);
            smartAnswerInfo.setRanking(i + 1);
        }

        smartAnswerInfoService.updateBatchById(list);
    }

    /**
     * 取消发布评分结果
     *
     * @param smartAssessmentMission
     * @return
     */
    @AutoLog(value = "考核任务表-取消发布评分结果")
    @ApiOperation(value = "考核任务表-取消发布评分结果", notes = "考核任务表-取消发布评分结果")
    @PutMapping(value = "/recallScoreResult")
    @Transactional(rollbackFor = Exception.class)
    public Result<?> recallScoreResult(@RequestBody SmartAssessmentMission smartAssessmentMission) {
        if (oConvertUtils.isEmpty(smartAssessmentMission.getId())) {
            return Result.error("数据错误!");
        }
        smartAnswerInfoService.updateMissionStatus(smartAssessmentMission.getId(), "已签收");
        // 更新任务状态
        smartAssessmentMission.setMissionStatus("已发布");
        smartAssessmentMissionService.updateById(smartAssessmentMission);
        return Result.OK("取消发布评分结果成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "考核任务表-批量删除")
    @ApiOperation(value = "考核任务表-批量删除", notes = "考核任务表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    @Transactional(rollbackFor = Exception.class)
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.smartAssessmentMissionService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 导出
     *
     * @return
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartAssessmentMission smartAssessmentMission) {
        return super.exportXls(request, smartAssessmentMission, SmartAssessmentMission.class, "考核任务表");
    }

    /**
     * 导入
     *
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, SmartAssessmentMission.class);
    }
    /*---------------------------------主表处理-end-------------------------------------*/


    /*--------------------------------子表处理-考核任务被考核单位-begin----------------------------------------------*/

    /**
     * 通过主表ID查询
     *
     * @return
     */
    @AutoLog(value = "考核任务被考核单位-通过主表ID查询")
    @ApiOperation(value = "考核任务被考核单位-通过主表ID查询", notes = "考核任务被考核单位-通过主表ID查询")
    @GetMapping(value = "/listSmartAssessmentDepartByMainId")
    public Result<?> listSmartAssessmentDepartByMainId(SmartAssessmentDepart smartAssessmentDepart,
                                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                       HttpServletRequest req) {
        String signStatus = smartAssessmentDepart.getSignStatus();
        smartAssessmentDepart.setSignStatus(null);
        QueryWrapper<SmartAssessmentDepart> queryWrapper = QueryGenerator.initQueryWrapper(smartAssessmentDepart, req.getParameterMap());
        if (oConvertUtils.isNotEmpty(signStatus)) {
            if (signStatus.equals("0")) {
                queryWrapper.isNull("sign_status");
            } else if (signStatus.equals("1")) {
                queryWrapper.eq("sign_status", "已签收");
            }
        }
        Page<SmartAssessmentDepart> page = new Page<SmartAssessmentDepart>(pageNo, pageSize);
        IPage<SmartAssessmentDepart> pageList = smartAssessmentDepartService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "考核任务被考核单位通过主表ID查询")
    @ApiOperation(value = "考核任务被考核单位主表ID查询", notes = "考核任务被考核单位-通主表ID查询")
    @GetMapping(value = "/querySmartAssessmentDepartByMainId")
    public Result<?> querySmartAssessmentDepartListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<SmartAssessmentDepart> smartAssessmentDepartList = smartAssessmentDepartService.selectByMainId(id);
        return Result.OK(smartAssessmentDepartList);
    }

    /**
     * 添加
     *
     * @param smartAssessmentDepart
     * @return
     */
    @AutoLog(value = "考核任务被考核单位-添加")
    @ApiOperation(value = "考核任务被考核单位-添加", notes = "考核任务被考核单位-添加")
    @PostMapping(value = "/addSmartAssessmentDepart")
    @Transactional(rollbackFor = Exception.class)
    public Result<?> addSmartAssessmentDepart(@RequestBody SmartAssessmentDepart smartAssessmentDepart) {
        // 前端传过来的单位ID是多选
        String[] departIds = smartAssessmentDepart.getAssessmentDepart().split(",");
        List<SmartAssessmentDepart> departList = new ArrayList<>();
        for (String departId : departIds) {
            SmartAssessmentDepart depart = new SmartAssessmentDepart();
            BeanUtils.copyProperties(smartAssessmentDepart, depart);
            depart.setAssessmentDepart(departId);
            departList.add(depart);
        }
        smartAssessmentDepartService.saveBatch(departList);
//        smartAssessmentDepartService.save(smartAssessmentDepart);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param smartAssessmentDepart
     * @return
     */
    @AutoLog(value = "考核任务被考核单位-编辑")
    @ApiOperation(value = "考核任务被考核单位-编辑", notes = "考核任务被考核单位-编辑")
    @PutMapping(value = "/editSmartAssessmentDepart")
    @Transactional(rollbackFor = Exception.class)
    public Result<?> editSmartAssessmentDepart(@RequestBody SmartAssessmentDepart smartAssessmentDepart) {
        // 更新答题信息表中的截止时间
        QueryWrapper<SmartAnswerInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mission_id", smartAssessmentDepart.getMissionId()).eq("depart", smartAssessmentDepart.getAssessmentDepart());
        SmartAnswerInfo answerInfo = smartAnswerInfoService.getOne(queryWrapper);

        // 如果还没有发布任务则不更新
        if (oConvertUtils.isNotEmpty(answerInfo)) {
            answerInfo.setEndTime(smartAssessmentDepart.getDeadline());
            answerInfo.setIsShowScore(smartAssessmentDepart.getIsShowScore());
            smartAnswerInfoService.updateById(answerInfo);
        }

        smartAssessmentDepartService.updateById(smartAssessmentDepart);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "考核任务被考核单位-通过id删除")
    @ApiOperation(value = "考核任务被考核单位-通过id删除", notes = "考核任务被考核单位-通过id删除")
    @DeleteMapping(value = "/deleteSmartAssessmentDepart")
    @Transactional(rollbackFor = Exception.class)
    public Result<?> deleteSmartAssessmentDepart(@RequestParam(name = "id", required = true) String id) {
        smartAssessmentDepartService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "考核任务被考核单位-批量删除")
    @ApiOperation(value = "考核任务被考核单位-批量删除", notes = "考核任务被考核单位-批量删除")
    @DeleteMapping(value = "/deleteBatchSmartAssessmentDepart")
    @Transactional(rollbackFor = Exception.class)
    public Result<?> deleteBatchSmartAssessmentDepart(@RequestParam(name = "ids", required = true) String ids) {
        this.smartAssessmentDepartService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 导出
     *
     * @return
     */
    @RequestMapping(value = "/exportSmartAssessmentDepart")
    public ModelAndView exportSmartAssessmentDepart(HttpServletRequest request, SmartAssessmentDepart smartAssessmentDepart) {
        // Step.1 组装查询条件
        QueryWrapper<SmartAssessmentDepart> queryWrapper = QueryGenerator.initQueryWrapper(smartAssessmentDepart, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        // Step.2 获取导出数据
        List<SmartAssessmentDepart> pageList = smartAssessmentDepartService.list(queryWrapper);
        List<SmartAssessmentDepart> exportList = null;

        // 过滤选中数据
        String selections = request.getParameter("selections");
        if (oConvertUtils.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            exportList = pageList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        } else {
            exportList = pageList;
        }

        // Step.3 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "考核任务被考核单位"); //此处设置的filename无效 ,前端会重更新设置一下
        mv.addObject(NormalExcelConstants.CLASS, SmartAssessmentDepart.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("考核任务被考核单位报表", "导出人:" + sysUser.getRealname(), "考核任务被考核单位"));
        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        return mv;
    }

    /**
     * 导入
     *
     * @return
     */
    @RequestMapping(value = "/importSmartAssessmentDepart/{mainId}")
    @Transactional(rollbackFor = Exception.class)
    public Result<?> importSmartAssessmentDepart(HttpServletRequest request, HttpServletResponse response, @PathVariable("mainId") String mainId) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<SmartAssessmentDepart> list = ExcelImportUtil.importExcel(file.getInputStream(), SmartAssessmentDepart.class, params);
                for (SmartAssessmentDepart temp : list) {
                    temp.setMissionId(mainId);
                }
                long start = System.currentTimeMillis();
                smartAssessmentDepartService.saveBatch(list);
                log.info("消耗时间" + (System.currentTimeMillis() - start) + "毫秒");
                return Result.OK("文件导入成功！数据行数：" + list.size());
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
        return Result.error("文件导入失败！");
    }

    /*--------------------------------子表处理-考核任务被考核单位-end----------------------------------------------*/


}
