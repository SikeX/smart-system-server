package org.jeecg.modules.smartDemocraticLifeMeeting.controller;

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
import org.jeecg.modules.constant.VerifyConstant;
import org.jeecg.modules.smartDemocraticLifeMeeting.entity.SmartDemocraticLifeMeeting;
import org.jeecg.modules.smartDemocraticLifeMeeting.entity.SmartDemocraticLifePeople;
import org.jeecg.modules.smartDemocraticLifeMeeting.service.ISmartDemocraticLifeMeetingService;
import org.jeecg.modules.smartDemocraticLifeMeeting.service.ISmartDemocraticLifePeopleService;
import org.jeecg.modules.smartDemocraticLifeMeeting.vo.SmartDemocraticLifeMeetingPage;
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
 * @Description: 民主生活会表
 * @Author: jeecg-boot
 * @Date: 2021-11-12
 * @Version: V1.0
 */
@Api(tags = "民主生活会表")
@RestController
@RequestMapping("/smartDemocraticLifeMeeting/smartDemocraticLifeMeeting")
@Slf4j
public class SmartDemocraticLifeMeetingController {
	@Autowired
	private ISmartDemocraticLifeMeetingService smartDemocraticLifeMeetingService;
	@Autowired
	private ISmartDemocraticLifePeopleService smartDemocraticLifePeopleService;
    /**
     * 审核
     */
    @Autowired
    private SmartVerify smartVerify;
    @Autowired
    private ISmartVerifyTypeService smartVerifyTypeService;
    public String verifyType = "民主生活会";
	@Autowired
	CommonService commonService;
	@Autowired
	private ISysBaseAPI sysBaseAPI;
	@Autowired
	private BaseCommonService baseCommonService;

	/**
	 * 分页列表查询
	 *
	 * @param smartDemocraticLifeMeeting
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "民主生活会表-分页列表查询")
	@ApiOperation(value = "民主生活会表-分页列表查询", notes = "民主生活会表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartDemocraticLifeMeeting smartDemocraticLifeMeeting,
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
		QueryWrapper<SmartDemocraticLifeMeeting> queryWrapper = QueryGenerator.initQueryWrapper(smartDemocraticLifeMeeting, map);
		Page<SmartDemocraticLifeMeeting> page = new Page<SmartDemocraticLifeMeeting>(pageNo, pageSize);
		IPage<SmartDemocraticLifeMeeting> pageList = smartDemocraticLifeMeetingService.page(page, queryWrapper);
		List<String> departIds = pageList.getRecords().stream().map(SmartDemocraticLifeMeeting::getDepartId).collect(Collectors.toList());
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
	 * @param smartDemocraticLifeMeetingPage
	 * @return
	 */
	@AutoLog(value = "民主生活会表-添加")
	@ApiOperation(value = "民主生活会表-添加", notes = "民主生活会表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SmartDemocraticLifeMeetingPage smartDemocraticLifeMeetingPage) {
		SmartDemocraticLifeMeeting smartDemocraticLifeMeeting = new SmartDemocraticLifeMeeting();
		BeanUtils.copyProperties(smartDemocraticLifeMeetingPage, smartDemocraticLifeMeeting);
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String orgCode = sysUser.getOrgCode();
		if ("".equals(orgCode)) {
			return Result.error("本用户没有操作权限！");
		}
		String id = commonService.getDepartIdByOrgCode(orgCode);
		if (id == null) {
			return Result.error("没有找到部门！");
		}
		smartDemocraticLifeMeeting.setDepartId(id);
		smartDemocraticLifeMeeting.setCreatorId(sysUser.getId());

		Boolean isVerify = smartVerifyTypeService.getIsVerifyStatusByType(verifyType);
		if (isVerify) {
			// 如果任务需要审核，则设置任务为待提交状态
			smartDemocraticLifeMeeting.setVerifyStatus(VerifyConstant.VERIFY_STATUS_TOSUBMIT);
		} else {
			// 设置审核状态为免审
			smartDemocraticLifeMeeting.setVerifyStatus(VerifyConstant.VERIFY_STATUS_FREE);
		}
		smartDemocraticLifeMeetingService.saveMain(smartDemocraticLifeMeeting, smartDemocraticLifeMeetingPage.getSmartDemocraticLifePeopleList());
		return Result.OK("添加成功！");
	}


	/**
	 * 提交审核
	 *
	 * @param smartDemocraticLifeMeeting
	 * @return
	 */
	@AutoLog(value = "民主生活会表-提交审核")
	@ApiOperation(value = "民主生活会表-提交审核", notes = "民主生活会表-提交审核")
	@PostMapping(value = "/submitVerify")
	public Result<?> submitVerify(@RequestBody SmartDemocraticLifeMeeting smartDemocraticLifeMeeting) {

		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String orgCode = sysUser.getOrgCode();
		if ("".equals(orgCode)) {
			return Result.error("本用户没有操作权限！");
		}
		String id = commonService.getDepartIdByOrgCode(orgCode);
		if (id == null) {
			return Result.error("没有找到部门！");
		}

		if(!smartVerifyTypeService.getIsVerifyStatusByType(verifyType)){
			return Result.error("免审任务，无需提交审核！");
		}

		SmartDemocraticLifeMeeting democraticLifeMeeting = smartDemocraticLifeMeetingService.getById(smartDemocraticLifeMeeting.getId());

		democraticLifeMeeting.setDepartId(id);
		democraticLifeMeeting.setCreatorId(sysUser.getId());

		String recordId = democraticLifeMeeting.getId();
		smartVerify.addVerifyRecord(recordId, verifyType);
		democraticLifeMeeting.setVerifyStatus(smartVerify.getFlowStatusById(recordId).toString());
		smartDemocraticLifeMeetingService.updateById(democraticLifeMeeting);

		return Result.OK("提交成功！");
	}


	/**
	 * 编辑
	 *
	 * @param smartDemocraticLifeMeetingPage
	 * @return
	 */
	@AutoLog(value = "民主生活会表-编辑")
	@ApiOperation(value = "民主生活会表-编辑", notes = "民主生活会表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SmartDemocraticLifeMeetingPage smartDemocraticLifeMeetingPage) {
		SmartDemocraticLifeMeeting smartDemocraticLifeMeeting = new SmartDemocraticLifeMeeting();
		BeanUtils.copyProperties(smartDemocraticLifeMeetingPage, smartDemocraticLifeMeeting);
		SmartDemocraticLifeMeeting smartDemocraticLifeMeetingEntity = smartDemocraticLifeMeetingService.getById(smartDemocraticLifeMeeting.getId());
		if (smartDemocraticLifeMeetingEntity == null) {
			return Result.error("未找到对应数据");
		}

		if(!(smartDemocraticLifeMeetingEntity.getVerifyStatus().equals(VerifyConstant.VERIFY_STATUS_TOSUBMIT) || smartDemocraticLifeMeetingEntity.getVerifyStatus().equals(VerifyConstant.VERIFY_STATUS_FREE))){
			return Result.error("该任务已提交审核，不能修改！");
		}

		smartDemocraticLifeMeeting.setDepartId(null);
		smartDemocraticLifeMeeting.setCreateTime(null);
		smartDemocraticLifeMeetingService.updateMain(smartDemocraticLifeMeeting, smartDemocraticLifeMeetingPage.getSmartDemocraticLifePeopleList());
		return Result.OK("编辑成功!");
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "民主生活会表-通过id删除")
	@ApiOperation(value = "民主生活会表-通过id删除", notes = "民主生活会表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
		smartDemocraticLifeMeetingService.delMain(id);
		return Result.OK("删除成功!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "民主生活会表-批量删除")
	@ApiOperation(value = "民主生活会表-批量删除", notes = "民主生活会表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
		this.smartDemocraticLifeMeetingService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "民主生活会表-通过id查询")
	@ApiOperation(value = "民主生活会表-通过id查询", notes = "民主生活会表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
		SmartDemocraticLifeMeeting smartDemocraticLifeMeeting = smartDemocraticLifeMeetingService.getById(id);
		if (smartDemocraticLifeMeeting == null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(smartDemocraticLifeMeeting);

	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "民主生活参会人员表通过主表ID查询")
	@ApiOperation(value = "民主生活参会人员表主表ID查询", notes = "民主生活参会人员表-通主表ID查询")
	@GetMapping(value = "/querySmartDemocraticLifePeopleByMainId")
	public Result<?> querySmartDemocraticLifePeopleListByMainId(@RequestParam(name = "id", required = true) String id) {
		List<SmartDemocraticLifePeople> smartDemocraticLifePeopleList = smartDemocraticLifePeopleService.selectByMainId(id);
		return Result.OK(smartDemocraticLifePeopleList);
	}

	/**
	 * 导出excel
	 *
	 * @param req
	 * @param smartDemocraticLifeMeeting
	 */
	@RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(HttpServletRequest req, SmartDemocraticLifeMeeting smartDemocraticLifeMeeting, HttpServletResponse response) throws Exception {
		// Step.1 组装查询条件查询数据
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String username = sysUser.getUsername();
		// 获取用户角色
		List<String> role = sysBaseAPI.getRolesByUsername(username);

		List<SmartDemocraticLifeMeeting> queryList = new ArrayList<SmartDemocraticLifeMeeting>();

		if(role.contains("CommonUser")) {
			QueryWrapper<SmartDemocraticLifeMeeting> queryWrapper = QueryGenerator.initQueryWrapper(smartDemocraticLifeMeeting, req.getParameterMap());

			queryWrapper.eq("create_by",username);
			queryList = smartDemocraticLifeMeetingService.list(queryWrapper);
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
			QueryWrapper<SmartDemocraticLifeMeeting> queryWrapper = QueryGenerator.initQueryWrapper(smartDemocraticLifeMeeting, map);

			queryList = smartDemocraticLifeMeetingService.list(queryWrapper);
		}

		// Step.1 组装查询条件查询数据

		//Step.2 获取导出数据
		// 过滤选中数据
		String selections = req.getParameter("selections");
		List<SmartDemocraticLifeMeeting> smartDemocraticLifeMeetingList = new ArrayList<SmartDemocraticLifeMeeting>();
		if(oConvertUtils.isEmpty(selections)) {
			smartDemocraticLifeMeetingList = queryList;
		}else {
			List<String> selectionList = Arrays.asList(selections.split(","));
			smartDemocraticLifeMeetingList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
		}

		// Step.3 组装pageList
		List<SmartDemocraticLifeMeetingPage> pageList = new ArrayList<SmartDemocraticLifeMeetingPage>();
		for (SmartDemocraticLifeMeeting main : smartDemocraticLifeMeetingList) {
			SmartDemocraticLifeMeetingPage vo = new SmartDemocraticLifeMeetingPage();
			BeanUtils.copyProperties(main, vo);
			List<SmartDemocraticLifePeople> smartDemocraticLifePeopleList = smartDemocraticLifePeopleService.selectByMainId(main.getId());
			vo.setSmartDemocraticLifePeopleList(smartDemocraticLifePeopleList);
			pageList.add(vo);
		}

		// Step.4 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		mv.addObject(NormalExcelConstants.FILE_NAME, "民主生活会表列表");
		mv.addObject(NormalExcelConstants.CLASS, SmartDemocraticLifeMeetingPage.class);
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("民主生活会表数据", "导出人:" + sysUser.getRealname(), "民主生活会表"));
		mv.addObject(NormalExcelConstants.DATA_LIST, pageList);

		// List深拷贝，否则返回前端会没数据
		List<SmartDemocraticLifeMeetingPage> newPageList = ObjectUtil.cloneByStream(pageList);

		baseCommonService.addExportLog(mv.getModel(), "民主生活会", req, response);

		mv.addObject(NormalExcelConstants.DATA_LIST, newPageList);

		return mv;


		/*
		//Step.2 获取导出数据
		List<SmartDemocraticLifeMeeting> queryList = smartDemocraticLifeMeetingService.list(queryWrapper);
		// 过滤选中数据
		String selections = request.getParameter("selections");
		List<SmartDemocraticLifeMeeting> smartDemocraticLifeMeetingList = new ArrayList<SmartDemocraticLifeMeeting>();
		if (oConvertUtils.isEmpty(selections)) {
			smartDemocraticLifeMeetingList = queryList;
		} else {
			List<String> selectionList = Arrays.asList(selections.split(","));
			smartDemocraticLifeMeetingList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
		}

		// Step.3 组装pageList
		List<SmartDemocraticLifeMeetingPage> pageList = new ArrayList<SmartDemocraticLifeMeetingPage>();
		for (SmartDemocraticLifeMeeting main : smartDemocraticLifeMeetingList) {
			SmartDemocraticLifeMeetingPage vo = new SmartDemocraticLifeMeetingPage();
			BeanUtils.copyProperties(main, vo);
			List<SmartDemocraticLifePeople> smartDemocraticLifePeopleList = smartDemocraticLifePeopleService.selectByMainId(main.getId());
			vo.setSmartDemocraticLifePeopleList(smartDemocraticLifePeopleList);
			List<SmartDemocraticLifeEnclosure> smartDemocraticLifeEnclosureList = smartDemocraticLifeEnclosureService.selectByMainId(main.getId());
			vo.setSmartDemocraticLifeEnclosureList(smartDemocraticLifeEnclosureList);
			pageList.add(vo);
		}

		// Step.4 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		mv.addObject(NormalExcelConstants.FILE_NAME, "民主生活会表列表");
		mv.addObject(NormalExcelConstants.CLASS, SmartDemocraticLifeMeetingPage.class);
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("民主生活会表数据", "导出人:" + sysUser.getRealname(), "民主生活会表"));
		mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
		return mv;

		 */
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
				List<SmartDemocraticLifeMeetingPage> list = ExcelImportUtil.importExcel(file.getInputStream(), SmartDemocraticLifeMeetingPage.class, params);
				for (SmartDemocraticLifeMeetingPage page : list) {
					SmartDemocraticLifeMeeting po = new SmartDemocraticLifeMeeting();
					BeanUtils.copyProperties(page, po);
					smartDemocraticLifeMeetingService.saveMain(po, page.getSmartDemocraticLifePeopleList());
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