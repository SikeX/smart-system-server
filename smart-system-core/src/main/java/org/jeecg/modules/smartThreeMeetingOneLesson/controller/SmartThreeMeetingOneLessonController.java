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
import org.jeecg.modules.constant.VerifyConstant;
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
 * @Description: ????????????
 * @Author: jeecg-boot
 * @Date:   2021-11-14
 * @Version: V1.0
 */
@Api(tags="????????????")
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
	public String verifyType="????????????";
	@Autowired
	private ISmartVerifyTypeService smartVerifyTypeService;

	@Autowired
	private ISysBaseAPI sysBaseAPI;
	@Autowired
	private BaseCommonService baseCommonService;

	/**
	 * ??????????????????
	 *
	 * @param smartThreeMeetingOneLesson
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "????????????-??????????????????")
	@ApiOperation(value="????????????-??????????????????", notes="????????????-??????????????????")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartThreeMeetingOneLesson smartThreeMeetingOneLesson,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		// 1. ?????????????????? ???**??????
		String rule = "in";
		// 2. ????????????
		String field = "departmentId";
		// ???????????????????????????????????????????????????????????????
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		System.out.println("++++++++++++++++++");
		System.out.println(sysUser.getOrgCode());
		System.out.println("++++++++++++++++++");

		if ("".equals(sysUser.getOrgCode())) {
			return Result.error("????????????");
		}

		// ???????????????ID
		String childrenIdString = commonService.getChildrenIdStringByOrgCode(sysUser.getOrgCode());

		HashMap<String, String[]> map = new HashMap<>(req.getParameterMap());
		// ????????????????????????superQueryParams
		List<String> paramsList = ParamsUtil.getSuperQueryParams(req.getParameterMap());

		// ?????????????????????????????????????????????
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
	 *   ??????
	 *
	 * @param smartThreeMeetingOneLessonPage
	 * @return
	 */
	@AutoLog(value = "????????????-??????")
	@ApiOperation(value="????????????-??????", notes="????????????-??????")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SmartThreeMeetingOneLessonPage smartThreeMeetingOneLessonPage) {
		SmartThreeMeetingOneLesson smartThreeMeetingOneLesson = new SmartThreeMeetingOneLesson();
		BeanUtils.copyProperties(smartThreeMeetingOneLessonPage, smartThreeMeetingOneLesson);

		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String orgCode = sysUser.getOrgCode();
		if ("".equals(orgCode)) {
			return Result.error("??????????????????????????????");
		}
		String id = commonService.getDepartIdByOrgCode(orgCode);
		if (id == null) {
			return Result.error("?????????????????????");
		}

//		SmartThreeMeetingOneLesson smartThreeMeetingOneLesson = new SmartThreeMeetingOneLesson();
//		BeanUtils.copyProperties(smartThreeMeetingOneLessonPage, smartThreeMeetingOneLesson);

		smartThreeMeetingOneLesson.setDepartmentId(id);
		smartThreeMeetingOneLessonService.saveMain(smartThreeMeetingOneLesson, smartThreeMeetingOneLessonPage.getSmartThreeMeetingOneLessonParticipantsList(),smartThreeMeetingOneLessonPage.getSmartThreeMeetingOneLessonAnnexList());

		Boolean isVerify = smartVerifyTypeService.getIsVerifyStatusByType(verifyType);
		if(isVerify){
			smartThreeMeetingOneLesson.setVerifyStatus(VerifyConstant.VERIFY_STATUS_TOSUBMIT);
		}
		else {
			// ???????????????????????????
			smartThreeMeetingOneLesson.setVerifyStatus(VerifyConstant.VERIFY_STATUS_FREE);
		}
		smartThreeMeetingOneLessonService.updateById(smartThreeMeetingOneLesson);

		//		if(isVerify){
////			smartThreeMeetingOneLessonService.saveMain(
////					smartThreeMeetingOneLesson,
////					smartThreeMeetingOneLessonPage.getSmartThreeMeetingOneLessonParticipantsList(),
////					smartThreeMeetingOneLessonPage.getSmartThreeMeetingOneLessonAnnexList());
////			String recordId = smartThreeMeetingOneLesson.getId();
////			smartVerify.addVerifyRecord(recordId,verifyType);
////			smartThreeMeetingOneLesson.setVerifyStatus(smartVerify.getFlowStatusById(recordId).toString());
////			smartThreeMeetingOneLessonService.updateById(smartThreeMeetingOneLesson); }
//		else {
//			// ???????????????????????????
////			smartThreeMeetingOneLesson.setVerifyStatus("3");
////			// ?????????????????????????????????
////			smartThreeMeetingOneLessonService.saveMain(
////					smartThreeMeetingOneLesson,
////					smartThreeMeetingOneLessonPage.getSmartThreeMeetingOneLessonParticipantsList(),
////					smartThreeMeetingOneLessonPage.getSmartThreeMeetingOneLessonAnnexList());
//		}

		return Result.OK("???????????????");
	}

	/**
	 *   ????????????????????????
	 *
	 * @param smartThreeMeetingOneLesson
	 * @return
	 */
	@AutoLog(value = "????????????-????????????")
	@ApiOperation(value="????????????-????????????", notes="????????????-????????????")
	@PostMapping(value = "/submitVerify")
	public Result<?> submitVerify(@RequestBody SmartThreeMeetingOneLesson smartThreeMeetingOneLesson) {

		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String orgCode = sysUser.getOrgCode();
		if ("".equals(orgCode)) {
			return Result.error("??????????????????????????????");
		}

		if(!smartVerifyTypeService.getIsVerifyStatusByType(verifyType)){
			return Result.error("????????????????????????????????????");
		}

		SmartThreeMeetingOneLesson smartThreeMeetingOneLessonEntity = smartThreeMeetingOneLessonService.getById(smartThreeMeetingOneLesson.getId());

		String recordId = smartThreeMeetingOneLessonEntity.getId();
		smartVerify.addVerifyRecord(recordId,verifyType);
		smartThreeMeetingOneLessonEntity.setVerifyStatus(smartVerify.getFlowStatusById(recordId).toString());
		smartThreeMeetingOneLessonService.updateById(smartThreeMeetingOneLessonEntity);

		return Result.OK("???????????????");
	}


	/**
	 *  ??????
	 *
	 * @param smartThreeMeetingOneLessonPage
	 * @return
	 */
	@AutoLog(value = "????????????-??????")
	@ApiOperation(value="????????????-??????", notes="????????????-??????")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SmartThreeMeetingOneLessonPage smartThreeMeetingOneLessonPage) {
		SmartThreeMeetingOneLesson smartThreeMeetingOneLesson = new SmartThreeMeetingOneLesson();
		BeanUtils.copyProperties(smartThreeMeetingOneLessonPage, smartThreeMeetingOneLesson);
		SmartThreeMeetingOneLesson smartThreeMeetingOneLessonEntity = smartThreeMeetingOneLessonService.getById(smartThreeMeetingOneLesson.getId());
		if(smartThreeMeetingOneLessonEntity==null) {
			return Result.error("?????????????????????");
		}
		if(!(smartThreeMeetingOneLessonEntity.getVerifyStatus().equals(VerifyConstant.VERIFY_STATUS_TOSUBMIT) || smartThreeMeetingOneLessonEntity.getVerifyStatus().equals(VerifyConstant.VERIFY_STATUS_FREE))){
			return Result.error("??????????????????????????????????????????");
		}
//		smartThreeMeetingOneLesson.setDepartmentId(null);
//		smartThreeMeetingOneLesson.setCreateTime(null);
		smartThreeMeetingOneLessonService.updateMain(smartThreeMeetingOneLessonEntity, smartThreeMeetingOneLessonPage.getSmartThreeMeetingOneLessonParticipantsList(),smartThreeMeetingOneLessonPage.getSmartThreeMeetingOneLessonAnnexList());
		return Result.OK("????????????!");
	}

	/**
	 *   ??????id??????
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "????????????-??????id??????")
	@ApiOperation(value="????????????-??????id??????", notes="????????????-??????id??????")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		smartThreeMeetingOneLessonService.delMain(id);
		return Result.OK("????????????!");
	}

	/**
	 *  ????????????
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "????????????-????????????")
	@ApiOperation(value="????????????-????????????", notes="????????????-????????????")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartThreeMeetingOneLessonService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("?????????????????????");
	}

	/**
	 * ??????id??????
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "????????????-??????id??????")
	@ApiOperation(value="????????????-??????id??????", notes="????????????-??????id??????")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartThreeMeetingOneLesson smartThreeMeetingOneLesson = smartThreeMeetingOneLessonService.getById(id);
		if(smartThreeMeetingOneLesson==null) {
			return Result.error("?????????????????????");
		}
		return Result.OK(smartThreeMeetingOneLesson);

	}

	/**
	 * ??????id??????
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "???????????????????????????????????????ID??????")
	@ApiOperation(value="?????????????????????????????????ID??????", notes="???????????????????????????-?????????ID??????")
	@GetMapping(value = "/querySmartThreeMeetingOneLessonParticipantsByMainId")
	public Result<?> querySmartThreeMeetingOneLessonParticipantsListByMainId(@RequestParam(name="id",required=true) String id) {
		List<SmartThreeMeetingOneLessonParticipants> smartThreeMeetingOneLessonParticipantsList = smartThreeMeetingOneLessonParticipantsService.selectByMainId(id);
		return Result.OK(smartThreeMeetingOneLessonParticipantsList);
	}
	/**
	 * ??????id??????
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "?????????????????????????????????ID??????")
	@ApiOperation(value="???????????????????????????ID??????", notes="?????????????????????-?????????ID??????")
	@GetMapping(value = "/querySmartThreeMeetingOneLessonAnnexByMainId")
	public Result<?> querySmartThreeMeetingOneLessonAnnexListByMainId(@RequestParam(name="id",required=true) String id) {
		List<SmartThreeMeetingOneLessonAnnex> smartThreeMeetingOneLessonAnnexList = smartThreeMeetingOneLessonAnnexService.selectByMainId(id);
		return Result.OK(smartThreeMeetingOneLessonAnnexList);
	}

	/**
	 * ??????excel
	 *
	 * @param req
	 * @param smartThreeMeetingOneLesson
	 */
	@RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(HttpServletRequest req,HttpServletResponse response,
								  SmartThreeMeetingOneLesson smartThreeMeetingOneLesson) throws IOException {

		// ???????????????????????????????????????????????????????????????
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		String username = sysUser.getUsername();

		//??????????????????
		List<String> role = sysBaseAPI.getRolesByUsername(username);
		List<SmartThreeMeetingOneLesson> queryList=new ArrayList<SmartThreeMeetingOneLesson>();

		if (role.contains("CommonUser")) {
			QueryWrapper<SmartThreeMeetingOneLesson> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("create_by", username);
			queryList=smartThreeMeetingOneLessonService.list(queryWrapper);

		} else {
			// 1. ?????????????????? ???**??????
			String rule = "in";
			// 2. ????????????
			String field = "departmentId";


			// ???????????????ID
			String childrenIdString = commonService.getChildrenIdStringByOrgCode(sysUser.getOrgCode());

			HashMap<String, String[]> map = new HashMap<>(req.getParameterMap());
			// ????????????????????????superQueryParams
			List<String> paramsList = ParamsUtil.getSuperQueryParams(req.getParameterMap());

			// ?????????????????????????????????????????????
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

		// Step.1 ??????????????????????????????

		//Step.2 ??????????????????

		// ??????????????????
		String selections = req.getParameter("selections");
		List<SmartThreeMeetingOneLesson> smartThreeMeetingOneLessonList = new ArrayList<SmartThreeMeetingOneLesson>();
		if(oConvertUtils.isEmpty(selections)) {
			smartThreeMeetingOneLessonList = queryList;
		}else {
			List<String> selectionList = Arrays.asList(selections.split(","));
			smartThreeMeetingOneLessonList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
		}

		// Step.3 ??????pageList
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

		// Step.4 AutoPoi ??????Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		mv.addObject(NormalExcelConstants.FILE_NAME, "??????????????????");
		mv.addObject(NormalExcelConstants.CLASS, SmartThreeMeetingOneLessonPage.class);
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("?????????????????????", "?????????:"+sysUser.getRealname(), "???????????????"));
		mv.addObject(NormalExcelConstants.DATA_LIST, pageList);

		// List??????????????????????????????????????????
		List<SmartThreeMeetingOneLessonPage> newPageList = ObjectUtil.cloneByStream(pageList);
		baseCommonService.addExportLog(mv.getModel(), "????????????", req, response);
		mv.addObject(NormalExcelConstants.DATA_LIST, newPageList);
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
	public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			MultipartFile file = entity.getValue();// ????????????????????????
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
				return Result.OK("?????????????????????????????????:" + list.size());
			} catch (Exception e) {
				log.error(e.getMessage(),e);
				return Result.error("??????????????????:"+e.getMessage());
			} finally {
				try {
					file.getInputStream().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return Result.OK("?????????????????????");
	}

	@AutoLog(value = "????????????????????????")
	@ApiOperation(value = "????????????????????????", notes = "????????????????????????")
	@PutMapping(value = "/downloadCount")
	public Result<?> downloadCount(@RequestBody SmartThreeMeetingOneLessonAnnex smartThreeMeetingOneLessonAnnex) {
		SmartThreeMeetingOneLessonAnnex newSmartThreeMeetingOneLessonAnnex = smartThreeMeetingOneLessonAnnexService.getById(smartThreeMeetingOneLessonAnnex.getId());
		if (newSmartThreeMeetingOneLessonAnnex == null) {
			return Result.error("?????????????????????");
		}
		Integer downloadCount = newSmartThreeMeetingOneLessonAnnex.getDownloadTimes();
		newSmartThreeMeetingOneLessonAnnex.setDownloadTimes(downloadCount + 1);
		smartThreeMeetingOneLessonAnnexService.updateById(newSmartThreeMeetingOneLessonAnnex);
		return Result.OK("????????????!");
	}

}