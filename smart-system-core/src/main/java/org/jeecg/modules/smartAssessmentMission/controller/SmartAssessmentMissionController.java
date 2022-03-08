package org.jeecg.modules.smartAssessmentMission.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Joiner;
import io.github.classgraph.utils.Join;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtil;
import org.jeecg.common.util.DateUtils;
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
import org.jeecg.modules.smartRankVisible.service.ISmartRankVisibleService;
import org.jeecg.modules.smartAssessmentTeam.entity.SmartAssessmentTeam;
import org.jeecg.modules.smartAssessmentTeam.service.ISmartAssessmentTeamService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 考核任务表
 * @Author: jeecg-boot
 * @Date:   2022-02-12
 * @Version: V1.0
 */
@Api(tags="考核任务表")
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


	/*---------------------------------主表处理-begin-------------------------------------*/

	/**
	 * 分页列表查询
	 * @param smartAssessmentMission
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "考核任务表-分页列表查询")
	@ApiOperation(value="考核任务表-分页列表查询", notes="考核任务表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartAssessmentMission smartAssessmentMission,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SmartAssessmentMission> queryWrapper = QueryGenerator.initQueryWrapper(smartAssessmentMission, req.getParameterMap());
		Page<SmartAssessmentMission> page = new Page<SmartAssessmentMission>(pageNo, pageSize);
		IPage<SmartAssessmentMission> pageList = smartAssessmentMissionService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 * 分页列表查询用户考核组参与评分的考核任务
	 * @param smartAssessmentMission
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "考核任务表-分页列表查询考核组参与的考核任务")
	@ApiOperation(value="考核任务表-分页列表查询考核组参与的考核任务", notes="考核任务表-分页列表查询考核组参与的考核任务")
	@GetMapping(value = "/teamMissionList")
	public Result<?> queryTeamPageList(SmartAssessmentMission smartAssessmentMission,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		// 查询当前用户所在考核组
		QueryWrapper<SmartAssessmentTeam> teamQueryWrapper = new QueryWrapper<>();
		teamQueryWrapper.select("distinct id").or().in("team_leader", sysUser.getId())
				.or().in("deputy_team_Leader", sysUser.getId())
				.or().in("members", sysUser.getId());
		List<SmartAssessmentTeam> teamList = smartAssessmentTeamService.list(teamQueryWrapper);
		List<String> teamIdList = new ArrayList<>();
		teamList.forEach(smartAssessmentTeam -> teamIdList.add(smartAssessmentTeam.getId()));

		// 查询考核组参与的考核任务ID
		QueryWrapper<SmartAssessmentContent> contentQueryWrapper = new QueryWrapper<>();
		contentQueryWrapper.select("distinct mission_id").in("ass_team", teamIdList);
		List<SmartAssessmentContent> contentList = smartAssessmentContentService.list(contentQueryWrapper);
		List<String> missionIdList = new ArrayList<>();
		contentList.forEach(smartAssessmentContent -> missionIdList.add(smartAssessmentContent.getMissionId()));

		QueryWrapper<SmartAssessmentMission> queryWrapper = QueryGenerator.initQueryWrapper(smartAssessmentMission, req.getParameterMap());
		queryWrapper.ne("mission_status", "未发布").in("id", missionIdList);
		Page<SmartAssessmentMission> page = new Page<SmartAssessmentMission>(pageNo, pageSize);
		IPage<SmartAssessmentMission> pageList = smartAssessmentMissionService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 * 分页列表查询用户考核单位参与评分的考核任务
	 * @param smartAssessmentMission
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "考核任务表-分页列表查询考核组参与的考核任务")
	@ApiOperation(value="考核任务表-分页列表查询考核组参与的考核任务", notes="考核任务表-分页列表查询考核组参与的考核任务")
	@GetMapping(value = "/departmentMissionList")
	public Result<?> queryDepartmentPageList(SmartAssessmentMission smartAssessmentMission,
									   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									   HttpServletRequest req) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		// 查询当前用户所在考核单位
		QueryWrapper<SmartAssessmentDepartment> departmentQueryWrapper = new QueryWrapper<>();
		departmentQueryWrapper.eq("depart_id", sysUser.getDepartId()).eq("depart_user", sysUser.getId());
		SmartAssessmentDepartment assessmentDepartment = smartAssessmentDepartmentService.getOne(departmentQueryWrapper);
		if (oConvertUtils.isEmpty(assessmentDepartment)) {
			return Result.error("没有权限查看");
		}

		// 查询考核单位参与的考核任务ID
		QueryWrapper<SmartAssessmentContent> contentQueryWrapper = new QueryWrapper<>();
		contentQueryWrapper.select("distinct mission_id").eq("ass_depart", assessmentDepartment.getId());
		List<SmartAssessmentContent> contentList = smartAssessmentContentService.list(contentQueryWrapper);
		List<String> missionIdList = new ArrayList<>();
		contentList.forEach(smartAssessmentContent -> missionIdList.add(smartAssessmentContent.getMissionId()));

		QueryWrapper<SmartAssessmentMission> queryWrapper = QueryGenerator.initQueryWrapper(smartAssessmentMission, req.getParameterMap());
		queryWrapper.ne("mission_status", "未发布").in("id", missionIdList);
		Page<SmartAssessmentMission> page = new Page<SmartAssessmentMission>(pageNo, pageSize);
		IPage<SmartAssessmentMission> pageList = smartAssessmentMissionService.page(page, queryWrapper);
		return Result.OK(pageList);
	}


	/**
     *   添加
     * @param smartAssessmentMission
     * @return
     */
    @AutoLog(value = "考核任务表-添加")
    @ApiOperation(value="考核任务表-添加", notes="考核任务表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SmartAssessmentMission smartAssessmentMission) {
        smartAssessmentMissionService.save(smartAssessmentMission);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     * @param smartAssessmentMission
     * @return
     */
    @AutoLog(value = "考核任务表-编辑")
    @ApiOperation(value="考核任务表-编辑", notes="考核任务表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SmartAssessmentMission smartAssessmentMission) {
        smartAssessmentMissionService.updateById(smartAssessmentMission);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     * @param id
     * @return
     */
    @AutoLog(value = "考核任务表-通过id删除")
    @ApiOperation(value="考核任务表-通过id删除", notes="考核任务表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		// 删除考核任务下的考核内容
		QueryWrapper<SmartAnswerAssContent> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("mission_id", id);
		smartAnswerAssContentService.remove(queryWrapper);
		smartAssessmentMissionService.delMain(id);
		return Result.OK("删除成功!");
    }

	/**
	 * 撤销任务发布
	 * @param smartAssessmentMission
	 * @return
	 */
	@AutoLog(value = "考核任务表-撤销任务发布")
	@ApiOperation(value="考核任务表-撤销任务发布", notes="考核任务表-撤销任务发布")
	@PutMapping(value = "/reset")
	public Result<?> reset(@RequestBody SmartAssessmentMission smartAssessmentMission) {
		// 查询答题信息表相关数据的ID
		QueryWrapper<SmartAnswerInfo> answerInfoQueryWrapper = new QueryWrapper<>();
		answerInfoQueryWrapper.select("id").eq("mission_id", smartAssessmentMission.getId());
		List<SmartAnswerInfo> answerInfoList = smartAnswerInfoService.list(answerInfoQueryWrapper);
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
	 * 发布
	 * @param smartAssessmentMission
	 * @return
	 */
	@AutoLog(value = "考核任务表-发布")
	@ApiOperation(value="考核任务表-发布", notes="考核任务表-发布")
	@PutMapping(value = "/publish")
	public Result<?> publish(@RequestBody SmartAssessmentMission smartAssessmentMission) {
		smartAssessmentMission.setMissionStatus("已发布");
		smartAssessmentMissionService.updateById(smartAssessmentMission);
		List<SmartAssessmentDepart> smartAssessmentDeparts = smartAssessmentDepartService.selectByMainId(smartAssessmentMission.getId());
		smartAssessmentDeparts.forEach(smartAssessmentDepart -> {
			SmartAnswerInfo smartAnswerInfo = new SmartAnswerInfo();
			smartAnswerInfo.setMissionId(smartAssessmentMission.getId());
			smartAnswerInfo.setMissionStatus("未签收");
			smartAnswerInfo.setEndTime(smartAssessmentDepart.getDeadline());
			smartAnswerInfo.setDepart(smartAssessmentDepart.getAssessmentDepart());
			smartAnswerInfoService.save(smartAnswerInfo);
		});
		return Result.OK("发布成功");
	}

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @AutoLog(value = "考核任务表-批量删除")
    @ApiOperation(value="考核任务表-批量删除", notes="考核任务表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        this.smartAssessmentMissionService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 导出
     * @return
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartAssessmentMission smartAssessmentMission) {
        return super.exportXls(request, smartAssessmentMission, SmartAssessmentMission.class, "考核任务表");
    }

    /**
     * 导入
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
	 * @return
	 */
	@AutoLog(value = "考核任务被考核单位-通过主表ID查询")
	@ApiOperation(value="考核任务被考核单位-通过主表ID查询", notes="考核任务被考核单位-通过主表ID查询")
	@GetMapping(value = "/listSmartAssessmentDepartByMainId")
    public Result<?> listSmartAssessmentDepartByMainId(SmartAssessmentDepart smartAssessmentDepart,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest req) {
        QueryWrapper<SmartAssessmentDepart> queryWrapper = QueryGenerator.initQueryWrapper(smartAssessmentDepart, req.getParameterMap());
        Page<SmartAssessmentDepart> page = new Page<SmartAssessmentDepart>(pageNo, pageSize);
        IPage<SmartAssessmentDepart> pageList = smartAssessmentDepartService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

	/**
	 * 添加
	 * @param smartAssessmentDepart
	 * @return
	 */
	@AutoLog(value = "考核任务被考核单位-添加")
	@ApiOperation(value="考核任务被考核单位-添加", notes="考核任务被考核单位-添加")
	@PostMapping(value = "/addSmartAssessmentDepart")
	public Result<?> addSmartAssessmentDepart(@RequestBody SmartAssessmentDepart smartAssessmentDepart) {
		smartAssessmentDepartService.save(smartAssessmentDepart);
		return Result.OK("添加成功！");
	}

    /**
	 * 编辑
	 * @param smartAssessmentDepart
	 * @return
	 */
	@AutoLog(value = "考核任务被考核单位-编辑")
	@ApiOperation(value="考核任务被考核单位-编辑", notes="考核任务被考核单位-编辑")
	@PutMapping(value = "/editSmartAssessmentDepart")
	public Result<?> editSmartAssessmentDepart(@RequestBody SmartAssessmentDepart smartAssessmentDepart) {
		// 更新答题信息表中的截止时间
		QueryWrapper<SmartAnswerInfo> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("mission_id", smartAssessmentDepart.getMissionId()).eq("depart", smartAssessmentDepart.getAssessmentDepart());
		SmartAnswerInfo answerInfo = smartAnswerInfoService.getOne(queryWrapper);

		// 如果还没有发布任务则不更新
		if (oConvertUtils.isNotEmpty(answerInfo)) {
			// 如果时间不相等更新
			if (answerInfo.getEndTime().getTime() != smartAssessmentDepart.getDeadline().getTime()) {
				answerInfo.setEndTime(smartAssessmentDepart.getDeadline());
				smartAnswerInfoService.updateById(answerInfo);
			}
		}

		smartAssessmentDepartService.updateById(smartAssessmentDepart);
		return Result.OK("编辑成功!");
	}

	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "考核任务被考核单位-通过id删除")
	@ApiOperation(value="考核任务被考核单位-通过id删除", notes="考核任务被考核单位-通过id删除")
	@DeleteMapping(value = "/deleteSmartAssessmentDepart")
	public Result<?> deleteSmartAssessmentDepart(@RequestParam(name="id",required=true) String id) {
		smartAssessmentDepartService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "考核任务被考核单位-批量删除")
	@ApiOperation(value="考核任务被考核单位-批量删除", notes="考核任务被考核单位-批量删除")
	@DeleteMapping(value = "/deleteBatchSmartAssessmentDepart")
	public Result<?> deleteBatchSmartAssessmentDepart(@RequestParam(name="ids",required=true) String ids) {
	    this.smartAssessmentDepartService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

    /**
     * 导出
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
     * @return
     */
    @RequestMapping(value = "/importSmartAssessmentDepart/{mainId}")
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
