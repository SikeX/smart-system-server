package org.jeecg.modules.smartAssessmentContent.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.smartAssessmentContent.entity.SmartAssessmentContent;
import org.jeecg.modules.smartAssessmentContent.service.ISmartAssessmentContentService;
import org.jeecg.modules.smartAssessmentMission.entity.SmartAssessmentMission;
import org.jeecg.modules.smartAssessmentMission.service.ISmartAssessmentMissionService;
import org.jeecg.modules.smartRankVisible.entity.SmartRankVisible;
import org.jeecg.modules.smartRankVisible.service.ISmartRankVisibleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Description: 考核节点表
 * @Author: jeecg-boot
 * @Date: 2022-02-12
 * @Version: V1.0
 */
@Api(tags = "考核节点表")
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

    /**
     * 分页列表查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "答题信息表-分页列表查询")
    @ApiOperation(value = "答题信息表-分页列表查询", notes = "答题信息表-分页列表查询")
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
            return Result.error("查询节点失败：" + e.getMessage());
        }
    }

    /**
     * 分页列表查询
     *
     * @param smartAssessmentContent
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "考核节点表-分页列表查询")
    @ApiOperation(value = "考核节点表-分页列表查询", notes = "考核节点表-分页列表查询")
    @GetMapping(value = "/rootList")
    public Result<?> queryRootList(SmartAssessmentContent smartAssessmentContent,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
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
            // 使用 eq 防止模糊查询
            queryWrapper.eq("pid", parentId);
            Page<SmartAssessmentContent> page = new Page<SmartAssessmentContent>(pageNo, pageSize);
            IPage<SmartAssessmentContent> pageList = smartAssessmentContentService.page(page, queryWrapper);
            return Result.OK(pageList);
        }
    }

    /**
     * 获取子数据
     *
     * @param smartAssessmentContent
     * @param req
     * @return
     */
    @AutoLog(value = "考核节点表-获取子数据")
    @ApiOperation(value = "考核节点表-获取子数据", notes = "考核节点表-获取子数据")
    @GetMapping(value = "/childList")
    public Result<?> queryPageList(SmartAssessmentContent smartAssessmentContent, HttpServletRequest req) {
        QueryWrapper<SmartAssessmentContent> queryWrapper = QueryGenerator.initQueryWrapper(smartAssessmentContent, req.getParameterMap());
        List<SmartAssessmentContent> list = smartAssessmentContentService.list(queryWrapper);
        IPage<SmartAssessmentContent> pageList = new Page<>(1, 10, list.size());
        pageList.setRecords(list);
        return Result.OK(pageList);
    }

    /**
     * 批量查询子节点
     *
     * @param parentIds 父ID（多个采用半角逗号分割）
     * @param parentIds
     * @return 返回 IPage
     * @return
     */
    @AutoLog(value = "考核节点表-批量获取子数据")
    @ApiOperation(value = "考核节点表-批量获取子数据", notes = "考核节点表-批量获取子数据")
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
            return Result.error("批量查询子节点失败：" + e.getMessage());
        }
    }

    /**
     * 添加
     *
     * @param smartAssessmentContent
     * @return
     */
    @AutoLog(value = "考核节点表-添加")
    @ApiOperation(value = "考核节点表-添加", notes = "考核节点表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SmartAssessmentContent smartAssessmentContent) {
        smartAssessmentContentService.addSmartAssessmentContent(smartAssessmentContent);
        // 配置考核排名字段是否可见
        if(smartAssessmentContent.getPid() == "0") {
            SmartRankVisible smartRankVisible = new SmartRankVisible();
            smartRankVisible.setMissionId(smartAssessmentContent.getMissionId());
            smartRankVisible.setContentId(smartAssessmentContent.getId());
            smartRankVisible.setVisible("1");
            smartRankVisibleService.save(smartRankVisible);

        }
        // 如果是考核要点,则自动向上级增加分数
        Integer point = smartAssessmentContent.getPoint();
        QueryWrapper<SmartAssessmentContent> queryWrapper = new QueryWrapper<>();

        if (smartAssessmentContent.getIsKey() == 1) {
            updateSuperiorPoint(smartAssessmentContent, point);
        }
        return Result.OK("添加成功！");
    }

    /**
     * 校对分数
     *
     * @param missionId
     * @return
     */
    @AutoLog(value = "考核节点表-校对分数")
    @ApiOperation(value = "考核节点表-校对分数", notes = "考核节点表-校对分数")
    @GetMapping(value = "/checkPoint")
    public Result<?> checkPoint(@RequestParam(name = "missionId", required = true) String missionId) {
        // 查询数据库中的数据
        QueryWrapper<SmartAssessmentContent> queryWrapper = new QueryWrapper<>();
        SmartAssessmentContent updateContent = new SmartAssessmentContent();
        updateContent.setPoint(0);
        queryWrapper.eq("mission_id", missionId).eq("is_key", 0);
        smartAssessmentContentService.update(updateContent, queryWrapper);

        queryWrapper.clear();
        queryWrapper.eq("mission_id", missionId).eq("is_key", 1);
        List<SmartAssessmentContent> assessmentContentList = smartAssessmentContentService.list(queryWrapper);
        assessmentContentList.forEach(smartAssessmentContent -> {
            updateSuperiorPoint(smartAssessmentContent, smartAssessmentContent.getPoint());
        });

        return Result.OK("校正成功!");
    }

    /**
     * 编辑
     *
     * @param smartAssessmentContent
     * @return
     */
    @AutoLog(value = "考核节点表-编辑")
    @ApiOperation(value = "考核节点表-编辑", notes = "考核节点表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SmartAssessmentContent smartAssessmentContent) {
        // 查询数据库中的数据
        QueryWrapper<SmartAssessmentContent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", smartAssessmentContent.getId());
        SmartAssessmentContent lastOne = smartAssessmentContentService.getOne(queryWrapper);

        if (lastOne.getIsKey() - smartAssessmentContent.getIsKey() == 1) {
            // 如果是由考核要点变成不是考核要点
            smartAssessmentContent.setInstructions(null);
            smartAssessmentContent.setPoint(0);
            smartAssessmentContent.setAssDepart(null);
            smartAssessmentContent.setAssTeam(null);
        }

        // 与更新的数据相减,得出增量
        int increment = smartAssessmentContent.getPoint() - lastOne.getPoint();

        // 先更新信息到数据库
        smartAssessmentContentService.updateSmartAssessmentContent(smartAssessmentContent);

        // 如果分数有变化则更新上级分数和总分
        if (increment != 0) {
            updateSuperiorPoint(smartAssessmentContent, increment);
        }
        return Result.OK("编辑成功!");
    }

    /**
     * 更新上级分数
     *
     * @param smartAssessmentContent
     * @param increment              分数增量
     */
    private void updateSuperiorPoint(SmartAssessmentContent smartAssessmentContent, int increment) {
        // 更新考核任务总分
        SmartAssessmentMission mission = smartAssessmentMissionService.getById(smartAssessmentContent.getMissionId());
        mission.setTotalPoint(mission.getTotalPoint() + increment);
        smartAssessmentMissionService.updateById(mission);

        QueryWrapper<SmartAssessmentContent> queryWrapper = new QueryWrapper<>();
        while (oConvertUtils.isNotEmpty(smartAssessmentContent.getPid())) {
            // 查找该考核要点的上级
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
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "考核节点表-通过id删除")
    @ApiOperation(value = "考核节点表-通过id删除", notes = "考核节点表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        // 查询数据库中的数据
        QueryWrapper<SmartAssessmentContent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        SmartAssessmentContent smartAssessmentContent = smartAssessmentContentService.getOne(queryWrapper);
        if (oConvertUtils.isNotEmpty(smartAssessmentContent)) {
            updateSuperiorPoint(smartAssessmentContent, smartAssessmentContent.getPoint() * -1);
        }
        smartAssessmentContentService.deleteSmartAssessmentContent(id);
        // 同步删除排名字段可见表中的内容
        QueryWrapper<SmartRankVisible> rankVisibleQueryWrapper = new QueryWrapper<>();
        rankVisibleQueryWrapper.eq("content_id",id);
        smartRankVisibleService.remove(rankVisibleQueryWrapper);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "考核节点表-批量删除")
    @ApiOperation(value = "考核节点表-批量删除", notes = "考核节点表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.smartAssessmentContentService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "考核节点表-通过id查询")
    @ApiOperation(value = "考核节点表-通过id查询", notes = "考核节点表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SmartAssessmentContent smartAssessmentContent = smartAssessmentContentService.getById(id);
        if (smartAssessmentContent == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(smartAssessmentContent);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param smartAssessmentContent
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartAssessmentContent smartAssessmentContent) {
        return super.exportXls(request, smartAssessmentContent, SmartAssessmentContent.class, "考核节点表");
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
        return super.importExcel(request, response, SmartAssessmentContent.class);
    }

}
