package org.jeecg.modules.smartFuneralReport.controller;

import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.ObjectUtil;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.modules.common.service.CommonService;
import org.jeecg.modules.common.util.ParamsUtil;
import org.jeecg.modules.constant.VerifyConstant;
import org.jeecg.modules.smartCreateAdvice.entity.SmartCreateAdvice;
import org.jeecg.modules.smartEvaluateMeeting.entity.SmartEvaluateMeeting;
import org.jeecg.modules.smartEvaluateMeeting.entity.SmartEvaluateMeetingAnnex;
import org.jeecg.modules.smartEvaluateMeeting.entity.SmartEvaluateMeetingPacpa;
import org.jeecg.modules.smartEvaluateMeeting.vo.SmartEvaluateMeetingPage;
import org.jeecg.modules.smartFuneralReport.entity.SmartFuneralReport;
import org.jeecg.modules.smartFuneralReport.service.ISmartFuneralReportService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.smartPostFuneralReport.entity.SmartPostFuneralReport;
import org.jeecg.modules.tasks.smartVerifyTask.service.SmartVerify;
import org.jeecg.modules.tasks.taskType.service.ISmartVerifyTypeService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: ?????????????????????
 * @Author: jeecg-boot
 * @Date:   2022-02-26
 * @Version: V1.0
 */
@Api(tags="?????????????????????")
@RestController
@RequestMapping("/smartFuneralReport/smartFuneralReport")
@Slf4j
public class SmartFuneralReportController extends JeecgController<SmartFuneralReport, ISmartFuneralReportService> {
	@Autowired
	private ISmartFuneralReportService smartFuneralReportService;
	 @Autowired
	 private ISmartVerifyTypeService smartVerifyTypeService;
	 @Autowired
	 private SmartVerify smartVerify;
	 @Autowired
	 private BaseCommonService baseCommonService;

	 @Autowired
	 CommonService commonService;

	 public String verifyType = "????????????";

	 @Autowired
	 private ISysBaseAPI sysBaseAPI;
	
	/**
	 * ??????????????????
	 *
	 * @param smartFuneralReport
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "?????????????????????-??????????????????")
	@ApiOperation(value="?????????????????????-??????????????????", notes="?????????????????????-??????????????????")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartFuneralReport smartFuneralReport,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
// ???????????????????????????????????????????????????????????????
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String username = sysUser.getUsername();
		// ??????????????????
		List<String> role = sysBaseAPI.getRolesByUsername(username);

		Page<SmartFuneralReport> page = new Page<SmartFuneralReport>(pageNo, pageSize);

		// ????????????????????????????????????????????????????????????
		if(role.contains("CommonUser")) {
			QueryWrapper<SmartFuneralReport> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("create_by",username).or().eq("people_id",sysUser.getId());
			IPage<SmartFuneralReport> pageList = smartFuneralReportService.page(page, queryWrapper);
			return Result.OK(pageList);
		} else {
			// 1. ?????????????????? ???**??????
			String rule = "in";
			// 2. ????????????
			String field = "departId";

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

			QueryWrapper<SmartFuneralReport> queryWrapper = QueryGenerator.initQueryWrapper(smartFuneralReport, map);

			IPage<SmartFuneralReport> pageList = smartFuneralReportService.page(page, queryWrapper);
//			// ???????????????edit???????????????departId??????null???????????????????????????
//			List<String> departIds = pageList.getRecords().stream().map(SmartFuneralReport::getDepartId).collect(Collectors.toList());
//			if (departIds != null && departIds.size() > 0) {
//				Map<String, String> useDepNames = commonService.getDepNamesByIds(departIds);
//				pageList.getRecords().forEach(item -> {
//					item.setDepartId(useDepNames.get(item.getDepartId()));
//				});
//			}
			return Result.OK(pageList);
		}
	}
	
	/**
	 *   ??????
	 *
	 * @param smartFuneralReport
	 * @return
	 */
	@AutoLog(value = "?????????????????????-????????????")
	@ApiOperation(value="?????????????????????-????????????", notes="?????????????????????-????????????")
	@PostMapping(value = "/submitVerify")
	public Result<?> submitVerify(@RequestBody SmartFuneralReport smartFuneralReport) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String orgCode = sysUser.getOrgCode();
		// ??????????????????
		if ("".equals(orgCode)) {
			return Result.error("???????????????????????????");
		}
		if(!smartVerifyTypeService.getIsVerifyStatusByType(verifyType)){
			return Result.error("????????????????????????????????????");
		}
		SmartFuneralReport smartFuneralReporteEntity = smartFuneralReportService.getById(smartFuneralReport.getId());

		String recordId = smartFuneralReporteEntity.getId();
		smartVerify.addVerifyRecord(recordId, verifyType);
		smartFuneralReporteEntity.setVerifyStatus(smartVerify.getFlowStatusById(recordId).toString());
		smartFuneralReportService.updateById(smartFuneralReporteEntity);
		return Result.OK("???????????????");
	}

	 /**
	  *   ??????
	  *
	  * @param smartFuneralReport
	  * @return
	  */
	 @AutoLog(value = "?????????????????????-??????")
	 @ApiOperation(value="?????????????????????-??????", notes="?????????????????????-??????")
	 @PostMapping(value = "/add")
	 public Result<?> add(@RequestBody SmartFuneralReport smartFuneralReport) {
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 String orgCode = sysUser.getOrgCode();
		 // ??????????????????
		 if ("".equals(orgCode)) {
			 return Result.error("???????????????????????????");
		 }
		 String id = commonService.getDepartIdByOrgCode(orgCode);
		 if (id == null) {
			 return Result.error("?????????????????????");
		 }
		 smartFuneralReport.setDepartId(id);
		 smartFuneralReport.setReportTime(new Date());
		 smartFuneralReportService.save(smartFuneralReport);

		 Boolean isVerify = smartVerifyTypeService.getIsVerifyStatusByType(verifyType);
		 if(isVerify){
			 // ????????????????????????????????????????????????????????????
			 smartFuneralReport.setVerifyStatus(VerifyConstant.VERIFY_STATUS_TOSUBMIT);
		 } else {
			 // ???????????????????????????
			 smartFuneralReport.setVerifyStatus(VerifyConstant.VERIFY_STATUS_FREE);
			 // ?????????????????????????????????
		 }
		 smartFuneralReportService.updateById(smartFuneralReport);
		 return Result.OK("???????????????");
	 }
	
	/**
	 *  ??????
	 *
	 * @param smartFuneralReport
	 * @return
	 */
	@AutoLog(value = "?????????????????????-??????")
	@ApiOperation(value="?????????????????????-??????", notes="?????????????????????-??????")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SmartFuneralReport smartFuneralReport) {
		SmartFuneralReport smartFuneralReportEntity = smartFuneralReportService.getById(smartFuneralReport.getId());
		if (smartFuneralReportEntity == null) {
			return Result.error("?????????????????????");
		}
		log.info("???????????????" + smartFuneralReportEntity.getVerifyStatus());
		if(!(smartFuneralReportEntity.getVerifyStatus().equals(VerifyConstant.VERIFY_STATUS_TOSUBMIT) || smartFuneralReportEntity.getVerifyStatus().equals(VerifyConstant.VERIFY_STATUS_FREE))){
			return Result.error("??????????????????????????????????????????");
		}

		smartFuneralReportService.updateById(smartFuneralReport);
		return Result.OK("????????????!");
	}
	
	/**
	 *   ??????id??????
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "?????????????????????-??????id??????")
	@ApiOperation(value="?????????????????????-??????id??????", notes="?????????????????????-??????id??????")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		smartFuneralReportService.removeById(id);
		return Result.OK("????????????!");
	}
	
	/**
	 *  ????????????
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "?????????????????????-????????????")
	@ApiOperation(value="?????????????????????-????????????", notes="?????????????????????-????????????")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartFuneralReportService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("??????????????????!");
	}
	
	/**
	 * ??????id??????
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "?????????????????????-??????id??????")
	@ApiOperation(value="?????????????????????-??????id??????", notes="?????????????????????-??????id??????")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartFuneralReport smartFuneralReport = smartFuneralReportService.getById(id);
		if(smartFuneralReport==null) {
			return Result.error("?????????????????????");
		}
		return Result.OK(smartFuneralReport);
	}

    /**
    * ??????excel
    *
    * @param req
    * @param smartFuneralReport
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest req,  HttpServletResponse response, SmartFuneralReport smartFuneralReport)throws Exception {
		// ???????????????????????????????????????????????????????????????
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		String username = sysUser.getUsername();

		// ??????????????????
		List<String> role = sysBaseAPI.getRolesByUsername(username);

		List<SmartFuneralReport> queryList = new ArrayList<SmartFuneralReport>();


		// ????????????????????????????????????????????????????????????
		if(role.contains("CommonUser")) {
			QueryWrapper<SmartFuneralReport> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("create_by",username);
			queryList = smartFuneralReportService.list(queryWrapper);
		} else {
			// 1. ?????????????????? ???**??????
			String rule = "in";
			// 2. ????????????
			String field = "departId";

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
			QueryWrapper<SmartFuneralReport> queryWrapper = QueryGenerator.initQueryWrapper(smartFuneralReport, map);

			queryList = smartFuneralReportService.list(queryWrapper);
		}



		// Step.1 ??????????????????????????????
//      QueryWrapper<SmartEvaluateMeeting> queryWrapper = QueryGenerator.initQueryWrapper(smartEvaluateMeeting, request.getParameterMap());
//      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		//Step.2 ??????????????????
//      List<SmartEvaluateMeeting> queryList = smartEvaluateMeetingService.list(queryWrapper);
		// ??????????????????
		String selections = req.getParameter("selections");
		List<SmartFuneralReport> smartFuneralReportList = new ArrayList<SmartFuneralReport>();
		if(oConvertUtils.isEmpty(selections)) {
			smartFuneralReportList = queryList;
		}else {
			List<String> selectionList = Arrays.asList(selections.split(","));
			smartFuneralReportList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
		}

		// Step.3 ??????pageList
//		List<SmartFuneralReport> pageList = new ArrayList<SmartFuneralReport>();
//		for (SmartFuneralReport main : smartFuneralReportList) {
//			SmartFuneralReportPage vo = new SmartFuneralReportPage();
//			BeanUtils.copyProperties(main, vo);
//			List<SmartEvaluateMeetingPacpa> smartEvaluateMeetingPacpaList = smartEvaluateMeetingPacpaService.selectByMainId(main.getId());
//			vo.setSmartEvaluateMeetingPacpaList(smartEvaluateMeetingPacpaList);
//			List<SmartEvaluateMeetingAnnex> smartEvaluateMeetingAnnexList = smartEvaluateMeetingAnnexService.selectByMainId(main.getId());
//			vo.setSmartEvaluateMeetingAnnexList(smartEvaluateMeetingAnnexList);
//			pageList.add(vo);
//		}

		// Step.4 AutoPoi ??????Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		mv.addObject(NormalExcelConstants.FILE_NAME, "?????????????????????");
		mv.addObject(NormalExcelConstants.CLASS, SmartFuneralReport.class);
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("???????????????????????????", "?????????:"+sysUser.getRealname(), "???????????????"));
		mv.addObject(NormalExcelConstants.DATA_LIST, smartFuneralReportList);

		// List??????????????????????????????????????????
		List<SmartFuneralReport> newPageList = ObjectUtil.cloneByStream(smartFuneralReportList);

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
        return super.importExcel(request, response, SmartFuneralReport.class);
    }

	 /**
	  * id??????????????????
	  *
	  * @param id
	  * @return
	  */
	 @RequestMapping(value = "/getById")
	 public Result<?> getById(@RequestParam(name="id",required=true) String id) {
	 	SmartFuneralReport funeralReport = smartFuneralReportService.getById(id);
	 	return Result.OK(funeralReport);
	 }

}
