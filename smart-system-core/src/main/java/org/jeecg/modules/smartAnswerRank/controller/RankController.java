package org.jeecg.modules.smartAnswerRank.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Joiner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparableComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtil;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.smartAnswerAssContent.entity.SmartAnswerAssContent;
import org.jeecg.modules.smartAnswerAssContent.entity.SmartAnswerFile;
import org.jeecg.modules.smartAnswerAssContent.service.ISmartAnswerAssContentService;
import org.jeecg.modules.smartAnswerAssContent.service.ISmartAnswerFileService;
import org.jeecg.modules.smartAnswerInfo.entity.SmartAnswerInfo;
import org.jeecg.modules.smartAnswerInfo.service.ISmartAnswerInfoService;
import org.jeecg.modules.smartAnswerRank.vo.Rank;
import org.jeecg.modules.smartAssessmentContent.entity.SmartAssessmentContent;
import org.jeecg.modules.smartAssessmentContent.service.ISmartAssessmentContentService;
import org.jeecg.modules.smartAssessmentMission.entity.SmartAssessmentDepart;
import org.jeecg.modules.smartAssessmentMission.entity.SmartAssessmentMission;
import org.jeecg.modules.smartAssessmentMission.service.ISmartAssessmentDepartService;
import org.jeecg.modules.smartAssessmentMission.service.ISmartAssessmentMissionService;
import org.jeecg.modules.smartAssessmentTeam.service.ISmartAssessmentTeamService;
import org.jeecg.modules.smartExportWord.util.WordUtils;
import org.jeecg.modules.smartRankVisible.entity.SmartRankVisible;
import org.jeecg.modules.smartRankVisible.service.ISmartRankVisibleService;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecgframework.poi.excel.view.JeecgTemplateWordView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Description: ??????????????????
 * @Author: sike
 * @Date: 2022-03-04
 * @Version: V1.0
 */
@Api(tags = "??????????????????")
@RestController
@RequestMapping("/smartAnswerInfo/rank")
@Slf4j
public class RankController extends JeecgController<SmartAnswerInfo, ISmartAnswerInfoService> {
    @Autowired
    private ISmartAnswerInfoService smartAnswerInfoService;

    @Autowired
    private ISmartAnswerAssContentService smartAnswerAssContentService;

    @Autowired
    private ISmartAssessmentContentService smartAssessmentContentService;

    @Autowired
    private ISmartAssessmentTeamService smartAssessmentTeamService;

    @Autowired
    private ISmartAssessmentDepartService smartAssessmentDepartService;

    @Autowired
    private ISysBaseAPI sysBaseAPI;

    @Autowired
    private ISmartAssessmentMissionService smartAssessmentMissionService;

    @Autowired
    private ISmartRankVisibleService smartRankVisibleService;

    /**
     * ????????????ID??????
     * @return
     */
    @AutoLog(value = "??????????????????-????????????ID??????")
    @ApiOperation(value="??????????????????-????????????ID??????", notes="??????????????????-????????????ID??????")
    @GetMapping(value = "/getRankById")
    public Result<?> getRankByMainId(@RequestParam(name = "id") String id,
                                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                 HttpServletRequest req) {

        List<Rank> rankList = new ArrayList<>();

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        List <String> roleList = sysBaseAPI.getRolesByUsername(sysUser.getUsername());

        log.info(String.valueOf(roleList));


        String userDepartId = sysBaseAPI.getDepartIdByUserId(sysUser.getId());


        // ??????????????????
        QueryWrapper<SmartAssessmentContent> missionQueryWrapper = new QueryWrapper<>();

        missionQueryWrapper.eq("mission_id",id).eq("pid", 0);

        List<SmartAssessmentContent> missionList =  smartAssessmentContentService.list(missionQueryWrapper);

        // ????????????????????????????????????????????????
        QueryWrapper<SmartAnswerInfo> answerInfoQueryWrapper = new QueryWrapper<>();

        answerInfoQueryWrapper.eq("mission_id", id);

        List<SmartAnswerInfo> answerInfoList = smartAnswerInfoService.list(answerInfoQueryWrapper);

        if(roleList.contains("CCDIAdmin")) {

            answerInfoList.forEach(info -> {
                Rank rank = new Rank();

                rank.setDepartId(info.getDepart());
                rank.setDepartName(sysBaseAPI.translateDictFromTable("sys_depart","depart_name","id",info.getDepart()));
                log.info(sysBaseAPI.translateDictFromTable("sys_depart","depart_name","id",info.getDepart()));
                rank.setTotalScore(info.getTotalPoints());

                List scoreList = new ArrayList();

                // ?????????????????? id:score
                Map<String, Double> scoreMap = new HashMap<>();

                missionList.forEach(mission -> {

                    // ????????????????????????????????????
                    QueryWrapper<SmartAnswerAssContent> answerAssContentQueryWrapper = new QueryWrapper<>();

                    answerAssContentQueryWrapper.eq("main_id",info.getId())
                            .eq("ass_content_id",mission.getMissionId())
                            .eq("pid",0);

                    SmartAnswerAssContent smartAnswerAssContent =
                            smartAnswerAssContentService.getOne(answerAssContentQueryWrapper);
                    if(smartAnswerAssContent == null) {
                        scoreMap.put(mission.getId(), 0.0);
                    }
                    else {
                        scoreMap.put(mission.getId(), smartAnswerAssContent.getFinalScore());
                    }
                });

                rank.setScoreMap(scoreMap);
                rank.setRank(info.getRanking());

                rankList.add(rank);
            });

        } else {
            answerInfoList.forEach(info -> {

                if(!StrUtil.equals(userDepartId, info.getDepart())) {
                    return;
                }

                Rank rank = new Rank();

                rank.setDepartId(info.getDepart());
                rank.setDepartName(sysBaseAPI.translateDictFromTable("sys_depart","depart_name","id",info.getDepart()));
                log.info(sysBaseAPI.translateDictFromTable("sys_depart","depart_name","id",info.getDepart()));
                rank.setTotalScore(info.getTotalPoints());

                List scoreList = new ArrayList();

                // ?????????????????? id:score
                Map<String, Double> scoreMap = new HashMap<>();

                missionList.forEach(mission -> {

                    // ????????????????????????????????????
                    QueryWrapper<SmartAnswerAssContent> answerAssContentQueryWrapper = new QueryWrapper<>();

                    answerAssContentQueryWrapper.eq("main_id",info.getId())
                            .eq("ass_content_id",mission.getMissionId())
                            .eq("pid",0);

                    SmartAnswerAssContent smartAnswerAssContent =
                            smartAnswerAssContentService.getOne(answerAssContentQueryWrapper);
                    if(smartAnswerAssContent == null) {
                        scoreMap.put(mission.getId(), 0.0);
                    }
                    else {
                        scoreMap.put(mission.getId(), smartAnswerAssContent.getFinalScore());
                    }
                });

                rank.setScoreMap(scoreMap);
                rank.setRank(info.getRanking());

                rankList.add(rank);
            });
        }

        Comparator mycmp1 = ComparableComparator.getInstance();
        ArrayList<Object> sortFields = new ArrayList<>();
        sortFields.add(new BeanComparator("totalScore", mycmp1));
        // ?????????????????????
        ComparatorChain multiSort = new ComparatorChain(sortFields);
        // ??????????????????????????????????????????????????????
        Collections.sort(rankList, multiSort);

        return Result.OK(rankList);
    }

    @AutoLog(value = "??????????????????-????????????ID??????????????????")
    @ApiOperation(value="??????????????????-????????????ID??????????????????", notes="??????????????????-????????????ID??????????????????")
    @GetMapping(value = "/getScoreTypeById")
    public Result<?> getScoreTypeById(@RequestParam(name = "id") String id,
                                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                 HttpServletRequest req) {
        // ??????????????????
        QueryWrapper<SmartAssessmentContent> missionQueryWrapper = new QueryWrapper<>();

        missionQueryWrapper.eq("mission_id",id).eq("pid", 0);

        return Result.OK(smartAssessmentContentService.list(missionQueryWrapper));
    }

    @AutoLog(value = "???????????????-??????????????????")
    @ApiOperation(value="???????????????-??????????????????", notes="???????????????-??????????????????")
    @GetMapping(value = "/getMissionList")
    public Result<?> getMissionList(SmartAssessmentMission smartAssessmentMission,
                                   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<SmartAssessmentMission> queryWrapper = QueryGenerator.initQueryWrapper(smartAssessmentMission, req.getParameterMap());
        queryWrapper.eq("mission_status","??????????????????");
        Page<SmartAssessmentMission> page = new Page<SmartAssessmentMission>(pageNo, pageSize);
        IPage<SmartAssessmentMission> pageList = smartAssessmentMissionService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * ???????????????
     *
     * @param smartAnswerInfo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "???????????????-??????????????????")
    @ApiOperation(value = "???????????????-??????????????????", notes = "???????????????-??????????????????")
    @GetMapping(value = "/listInCharge")
    public Result<?> queryChargePageList(SmartAnswerInfo smartAnswerInfo,
                                         @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                         HttpServletRequest req) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        QueryWrapper<SmartAnswerInfo> queryWrapper = QueryGenerator.initQueryWrapper(smartAnswerInfo, req.getParameterMap());
        queryWrapper.eq("depart", sysUser.getDepartId());
        Page<SmartAnswerInfo> page = new Page<SmartAnswerInfo>(pageNo, pageSize);
        IPage<SmartAnswerInfo> pageList = smartAnswerInfoService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * ??????????????????
     *
     * @param smartAnswerInfo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "???????????????-??????????????????")
    @ApiOperation(value = "???????????????-??????????????????", notes = "???????????????-??????????????????")
    @GetMapping(value = "/listAll")
    public Result<?> queryAllPageList(SmartAnswerInfo smartAnswerInfo,
                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                      HttpServletRequest req) {
        QueryWrapper<SmartAnswerInfo> queryWrapper = QueryGenerator.initQueryWrapper(smartAnswerInfo, req.getParameterMap());
        Page<SmartAnswerInfo> page = new Page<SmartAnswerInfo>(pageNo, pageSize);
        IPage<SmartAnswerInfo> pageList = smartAnswerInfoService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * ??????
     *
     * @param smartAnswerInfo
     * @return
     */
    @AutoLog(value = "???????????????-??????")
    @ApiOperation(value = "???????????????-??????", notes = "???????????????-??????")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SmartAnswerInfo smartAnswerInfo) {
        smartAnswerInfoService.save(smartAnswerInfo);
        return Result.OK("???????????????");
    }

    /**
     * ??????
     *
     * @param smartAnswerInfo
     * @return
     */
    @AutoLog(value = "???????????????-??????")
    @ApiOperation(value = "???????????????-??????", notes = "???????????????-??????")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SmartAnswerInfo smartAnswerInfo) {
        smartAnswerInfoService.updateById(smartAnswerInfo);
        return Result.OK("????????????!");
    }


    /**
     * ??????excel
     *
     * @param request
     * @param smartAnswerInfo
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartAnswerInfo smartAnswerInfo) {
        return super.exportXls(request, smartAnswerInfo, SmartAnswerInfo.class, "???????????????");
    }

    @GetMapping(value = "/exportWord")
    public void exportWord(HttpServletResponse response, HttpServletRequest request, String ids) {

        String id = ids;

        List<Rank> rankList = new ArrayList<>();

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        // ????????????????????????
        List <String> roleList = sysBaseAPI.getRolesByUsername(sysUser.getUsername());

        // ????????????????????????
        String userDepartId = sysBaseAPI.getDepartIdByUserId(sysUser.getId());


        // ??????????????????
        QueryWrapper<SmartAssessmentContent> missionQueryWrapper = new QueryWrapper<>();

        missionQueryWrapper.eq("mission_id",id).eq("pid", 0);

        List<SmartAssessmentContent> missionList =  smartAssessmentContentService.list(missionQueryWrapper);

        // ????????????????????????????????????????????????
        QueryWrapper<SmartAnswerInfo> answerInfoQueryWrapper = new QueryWrapper<>();

        answerInfoQueryWrapper.eq("mission_id", id);

        List<SmartAnswerInfo> answerInfoList = smartAnswerInfoService.list(answerInfoQueryWrapper);

        if(roleList.contains("CCDIAdmin")) {

            answerInfoList.forEach(info -> {
                Rank rank = new Rank();

                Map<String, Double> columnMap = new HashMap<>();

                rank.setDepartId(info.getDepart());
                rank.setDepartName(sysBaseAPI.translateDictFromTable("sys_depart","depart_name","id",info.getDepart()));

                rank.setTotalScore(info.getTotalPoints());
                columnMap.put("totalScore",info.getTotalPoints());


                // ?????????????????? id:score
                Map<String, Double> scoreMap = new HashMap<>();

                missionList.forEach(mission -> {

                    // ????????????????????????????????????
                    QueryWrapper<SmartAnswerAssContent> answerAssContentQueryWrapper = new QueryWrapper<>();

                    answerAssContentQueryWrapper.eq("main_id",info.getId())
                            .eq("ass_content_id",mission.getMissionId())
                            .eq("pid",0);

                    SmartAnswerAssContent smartAnswerAssContent =
                            smartAnswerAssContentService.getOne(answerAssContentQueryWrapper);
                    if(smartAnswerAssContent == null) {
                        scoreMap.put(mission.getId(), 0.0);
                    }
                    else {
                        scoreMap.put(mission.getId(), smartAnswerAssContent.getFinalScore());
                    }
                });

                rank.setScoreMap(scoreMap);
                rank.setRank(info.getRanking());
                columnMap.put("rank",info.getRanking() != null ? info.getRanking() : 0.0);
                columnMap.put("lastRank",0.0);

                rank.setColumnMap(columnMap);

                rankList.add(rank);
            });

        } else {
            answerInfoList.forEach(info -> {

                if(!StrUtil.equals(userDepartId, info.getDepart())) {
                    return;
                }

                Rank rank = new Rank();

                Map<String, Double> columnMap = new HashMap<>();

                rank.setDepartId(info.getDepart());
                rank.setDepartName(sysBaseAPI.translateDictFromTable("sys_depart","depart_name","id",info.getDepart()));
                log.info(sysBaseAPI.translateDictFromTable("sys_depart","depart_name","id",info.getDepart()));
                rank.setTotalScore(info.getTotalPoints());
                columnMap.put("totalScore",info.getTotalPoints());

                // ?????????????????? id:score
                Map<String, Double> scoreMap = new HashMap<>();

                missionList.forEach(mission -> {

                    // ????????????????????????????????????
                    QueryWrapper<SmartAnswerAssContent> answerAssContentQueryWrapper = new QueryWrapper<>();

                    answerAssContentQueryWrapper.eq("main_id",info.getId())
                            .eq("ass_content_id",mission.getMissionId())
                            .eq("pid",0);

                    SmartAnswerAssContent smartAnswerAssContent =
                            smartAnswerAssContentService.getOne(answerAssContentQueryWrapper);
                    if(smartAnswerAssContent == null) {
                        scoreMap.put(mission.getId(), 0.0);
                    }
                    else {
                        scoreMap.put(mission.getId(), smartAnswerAssContent.getFinalScore());
                    }
                });

                rank.setScoreMap(scoreMap);
                rank.setRank(info.getRanking());
                columnMap.put("rank",info.getRanking() != null ? info.getRanking() : 0.0);
                columnMap.put("lastRank",0.0);

                rank.setColumnMap(columnMap);

                rankList.add(rank);
            });
        }

        QueryWrapper<SmartRankVisible> rankVisibleQueryWrapper1 = new QueryWrapper<>();

        rankVisibleQueryWrapper1.eq("mission_id",id)
                .eq("tag","stable").eq("visible","1").orderByAsc("sort");

        List<SmartRankVisible> stableColumnList = smartRankVisibleService.list(rankVisibleQueryWrapper1);


        QueryWrapper<SmartRankVisible> rankVisibleQueryWrapper2 = new QueryWrapper<>();

        rankVisibleQueryWrapper2.eq("mission_id",id)
                .eq("tag","content").eq("visible","1").orderByAsc("sort");

        List<SmartRankVisible> contentColumnList = smartRankVisibleService.list(rankVisibleQueryWrapper2);

        Map<String, Object> dataList = new HashMap<>();

        Calendar calendar = Calendar.getInstance();
        int yearNow = calendar.get(Calendar.YEAR);

        dataList.put("stableColumnList",stableColumnList);
        dataList.put("contentColumnList", contentColumnList);
        dataList.put("rankList",rankList);
        dataList.put("date",yearNow);
        dataList.put("contentCount", contentColumnList.size());

        log.info(String.valueOf(dataList));

        String ftlTemplateName = "/templates/rank.ftl";
        WordUtils.exportWord(dataList, "??????", ftlTemplateName, response);
    }

    /**
     * ??????excel????????????
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, SmartAnswerInfo.class);
    }

}