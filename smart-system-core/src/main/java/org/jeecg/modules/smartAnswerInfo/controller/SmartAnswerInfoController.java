package org.jeecg.modules.smartAnswerInfo.controller;

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
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
//import org.jeecg.common.util.DateUtil;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.smartAnswerAssContent.entity.SmartAnswerAssContent;
import org.jeecg.modules.smartAnswerAssContent.service.ISmartAnswerAssContentService;
import org.jeecg.modules.smartAnswerInfo.entity.SmartAnswerInfo;
import org.jeecg.modules.smartAnswerInfo.entity.SmartDepartContentScore;
import org.jeecg.modules.smartAnswerInfo.service.ISmartAnswerInfoService;
import org.jeecg.modules.smartAssessmentContent.entity.SmartAssessmentContent;
import org.jeecg.modules.smartAssessmentContent.service.ISmartAssessmentContentService;
import org.jeecg.modules.smartAssessmentMission.entity.SmartAssessmentDepart;
import org.jeecg.modules.smartAssessmentMission.service.ISmartAssessmentDepartService;
import org.jeecg.modules.smartAssessmentTeam.service.ISmartAssessmentTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 答题信息表
 * @Author: jeecg-boot
 * @Date: 2022-02-21
 * @Version: V1.0
 */
@Api(tags = "答题信息表")
@RestController
@RequestMapping("/smartAnswerInfo/smartAnswerInfo")
@Slf4j
public class SmartAnswerInfoController extends JeecgController<SmartAnswerInfo, ISmartAnswerInfoService> {
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
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SmartAnswerInfo smartAnswerInfo,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        // 被考核单位查询包含自己的所有考核任务ID
        QueryWrapper<SmartAssessmentDepart> smartAssessmentDepartQueryWrapper = new QueryWrapper<>();
        smartAssessmentDepartQueryWrapper.select("distinct mission_id").eq("depart_user", sysUser.getId()).eq("assessment_depart", sysUser.getDepartId());
        List<SmartAssessmentDepart> missionList = smartAssessmentDepartService.list(smartAssessmentDepartQueryWrapper);
        List<String> missionIdList = new ArrayList<>();
        missionList.forEach(mission -> {
            missionIdList.add(mission.getMissionId());
        });

        if (missionList.size() == 0) {
            return Result.error("无考核任务！");
        }

        // 查询上面所有考核任务信息
        QueryWrapper<SmartAnswerInfo> queryWrapper = QueryGenerator.initQueryWrapper(smartAnswerInfo, req.getParameterMap());
        // 包含本单位的任务
        queryWrapper.eq("depart", sysUser.getDepartId()).in("mission_id", missionIdList);
        Page<SmartAnswerInfo> page = new Page<SmartAnswerInfo>(pageNo, pageSize);
        IPage<SmartAnswerInfo> pageList = smartAnswerInfoService.page(page, queryWrapper);
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
        if (oConvertUtils.isEmpty(smartAnswerInfo.getDepart())) {
            return Result.error("请联系管理员！");
        }
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        QueryWrapper<SmartAnswerInfo> queryWrapper = QueryGenerator.initQueryWrapper(smartAnswerInfo, req.getParameterMap());
        Page<SmartAnswerInfo> page = new Page<SmartAnswerInfo>(pageNo, pageSize);
        IPage<SmartAnswerInfo> pageList = smartAnswerInfoService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 考核组查看
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @AutoLog(value = "答题信息表-分页列表查询")
    @ApiOperation(value = "答题信息表-分页列表查询", notes = "答题信息表-分页列表查询")
    @GetMapping(value = "/listDepartContentScore")
    public Result<?> queryChargePageList(@RequestParam(name = "missionId") String missionId,
                                         @RequestParam(name = "assContentId") String assContentId,
                                         @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<SmartDepartContentScore> page = new Page<>(pageNo, pageSize);
        List<SmartDepartContentScore> departContentScores = smartAnswerInfoService.selectByMissionIdAndContentId(page, missionId, assContentId);
        return Result.OK(departContentScores);
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
//        int dateDiff = DateUtils.dateDiff('s', DateUtils.getCalendar(DateUtils.getMillis(smartAnswerInfo.getEndTime())), DateUtils.getCalendar());
//        if (dateDiff <= 0) {
//            return Result.error("已过截止时间!");
//        }

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
        // 首先查询所有考核内容、摘要、要点
        QueryWrapper<SmartAssessmentContent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mission_id", smartAnswerInfo.getMissionId());
        List<SmartAssessmentContent> smartAssessmentContentList = smartAssessmentContentService.list(queryWrapper);
        // 然后遍历生成记录
        for (SmartAssessmentContent smartAssessmentContent : smartAssessmentContentList) {
            SmartAnswerAssContent smartAnswerAssContent = new SmartAnswerAssContent();
            smartAnswerAssContent.setMainId(smartAnswerInfo.getId());
            smartAnswerAssContent.setPid(smartAssessmentContent.getPid());
            smartAnswerAssContent.setHasChild(smartAssessmentContent.getHasChild());
            smartAnswerAssContent.setAssContentId(smartAssessmentContent.getId());
            smartAnswerAssContent.setIsKey(smartAssessmentContent.getIsKey());
            smartAnswerAssContentService.save(smartAnswerAssContent);
        }

        smartAnswerInfo.setMissionStatus("已签收");
        smartAnswerInfoService.updateById(smartAnswerInfo);

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