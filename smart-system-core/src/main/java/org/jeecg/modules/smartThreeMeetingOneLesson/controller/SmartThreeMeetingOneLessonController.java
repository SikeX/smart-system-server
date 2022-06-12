package org.jeecg.modules.smartThreeMeetingOneLesson.controller;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.ObjectUtil;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.modules.common.service.CommonService;
import org.jeecg.modules.common.util.ParamsUtil;
import org.jeecg.modules.smartTripleImportanceOneGreatness.entity.SmartTripleImportanceOneGreatness;
import org.jeecg.modules.smartTripleImportanceOneGreatness.entity.SmartTripleImportanceOneGreatnessDescription;
import org.jeecg.modules.smartTripleImportanceOneGreatness.entity.SmartTripleImportanceOneGreatnessPacca;
import org.jeecg.modules.smartTripleImportanceOneGreatness.vo.SmartTripleImportanceOneGreatnessPage;
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
import org.jeecg.modules.smartThreeMeetingOneLesson.entity.SmartThreeMeetingOneLessonParticipants;
import org.jeecg.modules.smartThreeMeetingOneLesson.entity.SmartThreeMeetingOneLessonAnnex;
import org.jeecg.modules.smartThreeMeetingOneLesson.entity.SmartThreeMeetingOneLesson;
import org.jeecg.modules.smartThreeMeetingOneLesson.vo.SmartThreeMeetingOneLessonPage;
import org.jeecg.modules.smartThreeMeetingOneLesson.service.ISmartThreeMeetingOneLessonService;
import org.jeecg.modules.smartThreeMeetingOneLesson.service.ISmartThreeMeetingOneLessonParticipantsService;
import org.jeecg.modules.smartThreeMeetingOneLesson.service.ISmartThreeMeetingOneLessonAnnexService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 三会一课
 * @Author: jeecg-boot
 * @Date:   2021-11-14
 * @Version: V1.0
 */
@Api(tags="三会一课")
@RestController
@RequestMapping("/smartThreeMeetingOneLesson/smartThreeMeetingOneLesson")
@Slf4j
public class SmartThreeMeetingOneLessonController {
	@Autowired
	private ISmartThreeMeetingOneLessonService smartThreeMeetingOneLessonService;
	@Autowired
	private ISmartThreeMeetingOneLessonParticipantsService smartThreeMeetingOneLessonParticipantsService;
	@Autowired
	private ISmartThreeMeetingOneLessonAnnexService smartThreeMeetingOneLessonAnnexService;

	@Autowired
	CommonService commonService;
	@Autowired
	private SmartVerify smartVerify;
	public String verifyType="三会一课";
	@Autowired
	private ISmartVerifyTypeService smartVerifyTypeService;

	@Autowired
	private ISysBaseAPI sysBaseAPI;
	@Autowired
	private BaseCommonService baseCommonService;

	/**
	 * 分页列表查询
	 *
	 * @param smartThreeMeetingOneLesson
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "三会一课-分页列表查询")
	@ApiOperation(value="三会一课-分页列表查询", notes="三会一课-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartThreeMeetingOneLesson smartThreeMeetingOneLesson,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		// 1. 规则，下面是 以**开始
		String rule = "in";
		// 2. 查询字段
		String field = "departmentId";
		// 获取登录用户信息，可以用来查询单位部门信息
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		System.out.println("++++++++++++++++++");
		System.out.println(sysUser.getOrgCode());
		System.out.println("++++++++++++++++++");

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
//		System.out.println("++++++++++++++++++");
//		System.out.println(paramsList);
//		System.out.println("++++++++++++++++++");
		String[] params = new String[paramsList.size()];
		paramsList.toArray(params);
		map.put("superQueryParams", params);
		params = new String[]{"and"};
		map.put("superQueryMatchType", params);

//		QueryWrapper<SmartThreeMeetingOneLesson> queryWrapper = QueryGenerator.initQueryWrapper(smartThreeMeetingOneLesson, req.getParameterMap());
//		Page<SmartThreeMeetingOneLesson> page = new Page<SmartThreeMeetingOneLesson>(pageNo, pageSize);
//		IPage<SmartThreeMeetingOneLesson> pageList = smartThreeMeetingOneLessonService.page(page, queryWrapper);

		QueryWrapper<SmartThreeMeetingOneLesson> queryWrapper = QueryGenerator.initQueryWrapper(smartThreeMeetingOneLesson, map);
		Page<SmartThreeMeetingOneLesson> page = new Page<SmartThreeMeetingOneLesson>(pageNo, pageSize);
		IPage<SmartThreeMeetingOneLesson> pageList = smartThreeMeetingOneLessonService.page(page, queryWrapper);


		List<String> departIds = pageList.getRecords().stream().map(SmartThreeMeetingOneLesson::getDepartmentId).collect(Collectors.toList());
		if (departIds != null && departIds.size() > 0) {
			Map<String, String> useDepNames = commonService.getDepNamesByIds(departIds);
			pageList.getRecords().forEach(item -> {
				item.setDepartmentId(useDepNames.get(item.getDepartmentId()));
			});
		}

		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param smartThreeMeetingOneLessonPage
	 * @return
	 */
	@AutoLog(value = "三会一课-添加")
	@ApiOperation(value="三会一课-添加", notes="三会一课-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SmartThreeMeetingOneLessonPage smartThreeMeetingOneLessonPage) {
		SmartThreeMeetingOneLesson smartThreeMeetingOneLesson = new SmartThreeMeetingOneLesson();
		BeanUtils.copyProperties(smartThreeMeetingOneLessonPage, smartThreeMeetingOneLesson);

		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String orgCode = sysUser.getOrgCode();
		if ("".equals(orgCode)) {
			return Result.error("本用户没有操作权限！");
		}
		String id = commonService.getDepartIdByOrgCode(orgCode);
		if (id == null) {
			return Result.error("没有找到部门！");
		}
		smartThreeMeetingOneLesson.setDepartmentId(id);
//		smartThreeMeetingOneLessonService.saveMain(smartThreeMeetingOneLesson, smartThreeMeetingOneLessonPage.getSmartThreeMeetingOneLessonParticipantsList(),smartThreeMeetingOneLessonPage.getSmartThreeMeetingOneLessonAnnexList());

		Boolean isVerify = smartVerifyTypeService.getIsVerifyStatusByType(verifyType);
		if(isVerify){
			smartThreeMeetingOneLessonService.saveMain(
					smartThreeMeetingOneLesson,
					smartThreeMeetingOneLessonPage.getSmartThreeMeetingOneLessonParticipantsList(),
					smartThreeMeetingOneLessonPage.getSmartThreeMeetingOneLessonAnnexList());
			String recordId = smartThreeMeetingOneLesson.getId();
			smartVerify.addVerifyRecord(recordId,verifyType);
			smartThreeMeetingOneLesson.setVerifyStatus(smartVerify.getFlowStatusById(recordId).toString());
			smartThreeMeetingOneLessonService.updateById(smartThreeMeetingOneLesson); }
		else {
			// 设置审核状态为免审
			smartThreeMeetingOneLesson.setVerifyStatus("3");
			// 直接添加，不走审核流程
			smartThreeMeetingOneLessonService.saveMain(
					smartThreeMeetingOneLesson,
					smartThreeMeetingOneLessonPage.getSmartThreeMeetingOneLessonParticipantsList(),
					smartThreeMeetingOneLessonPage.getSmartThreeMeetingOneLessonAnnexList());
		}

		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param smartThreeMeetingOneLessonPage
	 * @return
	 */
	@AutoLog(value = "三会一课-编辑")
	@ApiOperation(value="三会一课-编辑", notes="三会一课-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SmartThreeMeetingOneLessonPage smartThreeMeetingOneLessonPage) {
		SmartThreeMeetingOneLesson smartThreeMeetingOneLesson = new SmartThreeMeetingOneLesson();
		BeanUtils.copyProperties(smartThreeMeetingOneLessonPage, smartThreeMeetingOneLesson);
		SmartThreeMeetingOneLesson smartThreeMeetingOneLessonEntity = smartThreeMeetingOneLessonService.getById(smartThreeMeetingOneLesson.getId());
		if(smartThreeMeetingOneLessonEntity==null) {
			return Result.error("未找到对应数据");
		}
		smartThreeMeetingOneLesson.setDepartmentId(null);
		smartThreeMeetingOneLesson.setCreateTime(null);
		smartThreeMeetingOneLessonService.updateMain(smartThreeMeetingOneLesson, smartThreeMeetingOneLessonPage.getSmartThreeMeetingOneLessonParticipantsList(),smartThreeMeetingOneLessonPage.getSmartThreeMeetingOneLessonAnnexList());
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "三会一课-通过id删除")
	@ApiOperation(value="三会一课-通过id删除", notes="三会一课-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		smartThreeMeetingOneLessonService.delMain(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "三会一课-批量删除")
	@ApiOperation(value="三会一课-批量删除", notes="三会一课-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartThreeMeetingOneLessonService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "三会一课-通过id查询")
	@ApiOperation(value="三会一课-通过id查询", notes="三会一课-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartThreeMeetingOneLesson smartThreeMeetingOneLesson = smartThreeMeetingOneLessonService.getById(id);
		if(smartThreeMeetingOneLesson==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(smartThreeMeetingOneLesson);

	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "三会一课参会人员表通过主表ID查询")
	@ApiOperation(value="三会一课参会人员表主表ID查询", notes="三会一课参会人员表-通主表ID查询")
	@GetMapping(value = "/querySmartThreeMeetingOneLessonParticipantsByMainId")
	public Result<?> querySmartThreeMeetingOneLessonParticipantsListByMainId(@RequestParam(name="id",required=true) String id) {
		List<SmartThreeMeetingOneLessonParticipants> smartThreeMeetingOneLessonParticipantsList = smartThreeMeetingOneLessonParticipantsService.selectByMainId(id);
		return Result.OK(smartThreeMeetingOneLessonParticipantsList);
	}
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "三会一课附件表通过主表ID查询")
	@ApiOperation(value="三会一课附件表主表ID查询", notes="三会一课附件表-通主表ID查询")
	@GetMapping(value = "/querySmartThreeMeetingOneLessonAnnexByMainId")
	public Result<?> querySmartThreeMeetingOneLessonAnnexListByMainId(@RequestParam(name="id",required=true) String id) {
		List<SmartThreeMeetingOneLessonAnnex> smartThreeMeetingOneLessonAnnexList = smartThreeMeetingOneLessonAnnexService.selectByMainId(id);
		return Result.OK(smartThreeMeetingOneLessonAnnexList);
	}

	/**
	 * 导出excel
	 *
	 * @param req
	 * @param smartThreeMeetingOneLesson
	 */
	@RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(HttpServletRequest req,HttpServletResponse response,
								  SmartThreeMeetingOneLesson smartThreeMeetingOneLesson) throws IOException {

		// 获取登录用户信息，可以用来查询单位部门信息
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		String username = sysUser.getUsername();

		//获取用户角色
		List<String> role = sysBaseAPI.getRolesByUsername(username);
		List<SmartThreeMeetingOneLesson> queryList=new ArrayList<SmartThreeMeetingOneLesson>();

		if (role.contains("CommonUser")) {
			QueryWrapper<SmartThreeMeetingOneLesson> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("create_by", username);
			queryList=smartThreeMeetingOneLessonService.list(queryWrapper);

		} else {
			// 1. 规则，下面是 以**开始
			String rule = "in";
			// 2. 查询字段
			String field = "departmentId";


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

			QueryWrapper<SmartThreeMeetingOneLesson> queryWrapper = QueryGenerator.initQueryWrapper(smartThreeMeetingOneLesson, map);
			queryList=smartThreeMeetingOneLessonService.list(queryWrapper);
		}

		// Step.1 组装查询条件查询数据

		//Step.2 获取导出数据

		// 过滤选中数据
		String selections = req.getParameter("selections");
		List<SmartThreeMeetingOneLesson> smartThreeMeetingOneLessonList = new ArrayList<SmartThreeMeetingOneLesson>();
		if(oConvertUtils.isEmpty(selections)) {
			smartThreeMeetingOneLessonList = queryList;
		}else {
			List<String> selectionList = Arrays.asList(selections.split(","));
			smartThreeMeetingOneLessonList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
		}

		// Step.3 组装pageList
		List<SmartThreeMeetingOneLessonPage> pageList = new ArrayList<SmartThreeMeetingOneLessonPage>();
		for (SmartThreeMeetingOneLesson main : smartThreeMeetingOneLessonList) {
			SmartThreeMeetingOneLessonPage vo = new SmartThreeMeetingOneLessonPage();
			BeanUtils.copyProperties(main, vo);
			List<SmartThreeMeetingOneLessonParticipants> smartThreeMeetingOneLessonParticipantsList = smartThreeMeetingOneLessonParticipantsService.selectByMainId(main.getId());
			vo.setSmartThreeMeetingOneLessonParticipantsList(smartThreeMeetingOneLessonParticipantsList);
			List<SmartThreeMeetingOneLessonAnnex> smartThreeMeetingOneLessonAnnexList = smartThreeMeetingOneLessonAnnexService.selectByMainId(main.getId());
			vo.setSmartThreeMeetingOneLessonAnnexList(smartThreeMeetingOneLessonAnnexList);
			pageList.add(vo);
		}

		// Step.4 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		mv.addObject(NormalExcelConstants.FILE_NAME, "三会一课列表");
		mv.addObject(NormalExcelConstants.CLASS, SmartThreeMeetingOneLessonPage.class);
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("三会一课表数据", "导出人:"+sysUser.getRealname(), "三会一课表"));
		mv.addObject(NormalExcelConstants.DATA_LIST, pageList);

		// List深拷贝，否则返回前端会没数据
		List<SmartThreeMeetingOneLessonPage> newPageList = ObjectUtil.cloneByStream(pageList);
		baseCommonService.addExportLog(mv.getModel(), "三重一大", req, response);
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
				List<SmartThreeMeetingOneLessonPage> list = ExcelImportUtil.importExcel(file.getInputStream(), SmartThreeMeetingOneLessonPage.class, params);
				for (SmartThreeMeetingOneLessonPage page : list) {
					SmartThreeMeetingOneLesson po = new SmartThreeMeetingOneLesson();
					BeanUtils.copyProperties(page, po);
					smartThreeMeetingOneLessonService.saveMain(po, page.getSmartThreeMeetingOneLessonParticipantsList(),page.getSmartThreeMeetingOneLessonAnnexList());
				}
				return Result.OK("文件导入成功！数据行数:" + list.size());
			} catch (Exception e) {
				log.error(e.getMessage(),e);
				return Result.error("文件导入失败:"+e.getMessage());
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
	public Result<?> downloadCount(@RequestBody SmartThreeMeetingOneLessonAnnex smartThreeMeetingOneLessonAnnex) {
		SmartThreeMeetingOneLessonAnnex newSmartThreeMeetingOneLessonAnnex = smartThreeMeetingOneLessonAnnexService.getById(smartThreeMeetingOneLessonAnnex.getId());
		if (newSmartThreeMeetingOneLessonAnnex == null) {
			return Result.error("未找到对应数据");
		}
		Integer downloadCount = newSmartThreeMeetingOneLessonAnnex.getDownloadTimes();
		newSmartThreeMeetingOneLessonAnnex.setDownloadTimes(downloadCount + 1);
		smartThreeMeetingOneLessonAnnexService.updateById(newSmartThreeMeetingOneLessonAnnex);
		return Result.OK("更新成功!");
	}

}