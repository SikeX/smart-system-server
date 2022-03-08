package org.jeecg.modules.smartAnswerRank.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Joiner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Description: 政治生态排名
 * @Author: sike
 * @Date: 2022-03-04
 * @Version: V1.0
 */
@Api(tags = "政治生态排名")
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

    /**
     * 通过主表ID查询
     * @return
     */
    @AutoLog(value = "政治生态排名-通过主表ID查询")
    @ApiOperation(value="政治生态排名-通过主表ID查询", notes="政治生态排名-通过主表ID查询")
    @GetMapping(value = "/getRankById")
    public Result<?> getRankByMainId(@RequestParam(name = "id") String id,
                                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                 HttpServletRequest req) {

        List<Rank> rankList = new ArrayList<>();

        // 查询考核内容
        QueryWrapper<SmartAssessmentContent> missionQueryWrapper = new QueryWrapper<>();

        missionQueryWrapper.eq("mission_id",id).eq("pid", 0);

        List<SmartAssessmentContent> missionList =  smartAssessmentContentService.list(missionQueryWrapper);

        // 查询答题信息表（需要答题的单位）
        QueryWrapper<SmartAnswerInfo> answerInfoQueryWrapper = new QueryWrapper<>();

        answerInfoQueryWrapper.eq("mission_id", id);

        List<SmartAnswerInfo> answerInfoList = smartAnswerInfoService.list(answerInfoQueryWrapper);

        answerInfoList.forEach(info -> {

            Rank rank = new Rank();

            rank.setDepartId(info.getDepart());
            rank.setDepartName(sysBaseAPI.translateDictFromTable("sys_depart","depart_name","id",info.getDepart()));
            rank.setTotalScore(info.getTotalPoints());

            List scoreList = new ArrayList();

            // 考核内容成绩 id:score
            Map<String, Double> scoreMap = new HashMap<>();

            missionList.forEach(mission -> {

                // 查询各单位的各内容的分数
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

            rank.setSourceMap(scoreMap);
            rank.setRank(info.getRanking());

            rankList.add(rank);
        });

//        Page<Rank> page = new Page<Rank>(pageNo, pageSize);
//        IPage<SmartAnswerFile> pageList = smartAssessmentContentService.page(page, queryWrapper);
        return Result.OK(rankList);
    }

    @AutoLog(value = "政治生态排名-通过主表ID查询任务类型")
    @ApiOperation(value="政治生态排名-通过主表ID查询任务类型", notes="政治生态排名-通过主表ID查询任务类型")
    @GetMapping(value = "/getScoreTypeById")
    public Result<?> getScoreTypeById(@RequestParam(name = "id") String id,
                                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                 HttpServletRequest req) {
        // 查询考核内容
        QueryWrapper<SmartAssessmentContent> missionQueryWrapper = new QueryWrapper<>();

        missionQueryWrapper.eq("mission_id",id).eq("pid", 0);

        return Result.OK(smartAssessmentContentService.list(missionQueryWrapper));
    }

    @AutoLog(value = "考核任务表-分页列表查询")
    @ApiOperation(value="考核任务表-分页列表查询", notes="考核任务表-分页列表查询")
    @GetMapping(value = "/getMissionList")
    public Result<?> getMissionList(SmartAssessmentMission smartAssessmentMission,
                                   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<SmartAssessmentMission> queryWrapper = QueryGenerator.initQueryWrapper(smartAssessmentMission, req.getParameterMap());
        queryWrapper.eq("mission_status","已发布");
        Page<SmartAssessmentMission> page = new Page<SmartAssessmentMission>(pageNo, pageSize);
        IPage<SmartAssessmentMission> pageList = smartAssessmentMissionService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 考核组查看
     *
     * @param smartAnswerInfo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "答题信息表-分页列表查询")
    @ApiOperation(value = "答题信息表-分页列表查询", notes = "答题信息表-分页列表查询")
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
     * 分页列表查询
     *
     * @param smartAnswerInfo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "答题信息表-分页列表查询")
    @ApiOperation(value = "答题信息表-分页列表查询", notes = "答题信息表-分页列表查询")
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
     * 添加
     *
     * @param smartAnswerInfo
     * @return
     */
    @AutoLog(value = "答题信息表-添加")
    @ApiOperation(value = "答题信息表-添加", notes = "答题信息表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SmartAnswerInfo smartAnswerInfo) {
        smartAnswerInfoService.save(smartAnswerInfo);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param smartAnswerInfo
     * @return
     */
    @AutoLog(value = "答题信息表-编辑")
    @ApiOperation(value = "答题信息表-编辑", notes = "答题信息表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SmartAnswerInfo smartAnswerInfo) {
        smartAnswerInfoService.updateById(smartAnswerInfo);
        return Result.OK("编辑成功!");
    }

    /**
     * 签收
     *
     * @param smartAnswerInfo
     * @return
     */
    @AutoLog(value = "答题信息表-签收")
    @ApiOperation(value = "答题信息表-签收", notes = "答题信息表-签收")
    @PutMapping(value = "/sign")
    public Result<?> sign(@RequestBody SmartAnswerInfo smartAnswerInfo) {
        // 检查是否已截止
        int dateDiff = DateUtils.dateDiff('s', DateUtils.getCalendar(DateUtils.getMillis(smartAnswerInfo.getEndTime())), DateUtils.getCalendar());
        if (dateDiff <= 0) {
            return Result.error("已过截止时间!");
        }

        smartAnswerInfo.setMissionStatus("已签收");
        smartAnswerInfoService.updateById(smartAnswerInfo);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();


        // 更新考核任务被考核单位状态
        QueryWrapper<SmartAssessmentDepart> departQueryWrapper = new QueryWrapper<>();
        departQueryWrapper.eq("mission_id", smartAnswerInfo.getMissionId()).eq("assessment_depart", smartAnswerInfo.getDepart());
        SmartAssessmentDepart assessmentDepart = smartAssessmentDepartService.getOne(departQueryWrapper);
        assessmentDepart.setSignStatus("已签收");
        assessmentDepart.setSignUser(sysUser.getId());
        assessmentDepart.setSignTime(DateUtils.getDate());
        smartAssessmentDepartService.updateById(assessmentDepart);


        // 生成答题记录
        QueryWrapper<SmartAssessmentContent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mission_id", smartAnswerInfo.getMissionId());
        List<SmartAssessmentContent> smartAssessmentContentList = smartAssessmentContentService.list(queryWrapper);
        for (SmartAssessmentContent smartAssessmentContent : smartAssessmentContentList) {
            SmartAnswerAssContent smartAnswerAssContent = new SmartAnswerAssContent();
            smartAnswerAssContent.setMainId(smartAnswerInfo.getId());
            smartAnswerAssContent.setPid(smartAssessmentContent.getPid());
            smartAnswerAssContent.setHasChild(smartAssessmentContent.getHasChild());
            smartAnswerAssContent.setAssContentId(smartAssessmentContent.getId());
            smartAnswerAssContentService.save(smartAnswerAssContent);
        }
        return Result.OK("签收成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "答题信息表-通过id删除")
    @ApiOperation(value = "答题信息表-通过id删除", notes = "答题信息表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        smartAnswerInfoService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "答题信息表-批量删除")
    @ApiOperation(value = "答题信息表-批量删除", notes = "答题信息表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.smartAnswerInfoService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "答题信息表-通过id查询")
    @ApiOperation(value = "答题信息表-通过id查询", notes = "答题信息表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SmartAnswerInfo smartAnswerInfo = smartAnswerInfoService.getById(id);
        if (smartAnswerInfo == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(smartAnswerInfo);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param smartAnswerInfo
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartAnswerInfo smartAnswerInfo) {
        return super.exportXls(request, smartAnswerInfo, SmartAnswerInfo.class, "答题信息表");
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
        return super.importExcel(request, response, SmartAnswerInfo.class);
    }

}