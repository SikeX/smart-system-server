package org.jeecg.modules.smartPostFuneralReport.controller;

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
import org.jeecg.modules.smartEvaluateMeeting.entity.SmartEvaluateMeeting;
import org.jeecg.modules.smartFuneralReport.entity.SmartFuneralReport;
import org.jeecg.modules.smartFuneralReport.service.ISmartFuneralReportService;
import org.jeecg.modules.smartFuneralReport.service.impl.SmartFuneralReportServiceImpl;
import org.jeecg.modules.smartPostFuneralReport.entity.SmartPostFuneralReport;
import org.jeecg.modules.smartPostFuneralReport.service.ISmartPostFuneralReportService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.smartPostFuneralReport.service.impl.SmartPostFuneralReportServiceImpl;
import org.jeecg.modules.smartPostFuneralReport.vo.FuneralReport;
import org.jeecg.modules.smartPostMarriage.entity.SmartPostMarriageReport;
import org.jeecg.modules.smartPostMarriage.service.ISmartPostMarriageReportFileService;
import org.jeecg.modules.tasks.smartVerifyTask.service.SmartVerify;
import org.jeecg.modules.tasks.taskType.service.ISmartVerifyTypeService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.smartExportWord.util.*;



 /**
 * @Description: 丧事事后报备表
 * @Author: jeecg-boot
 * @Date:   2022-02-26
 * @Version: V1.0
 */
@Api(tags="丧事事后报备表")
@RestController
@RequestMapping("/smartPostFuneralReport/smartPostFuneralReport")
@Slf4j
public class SmartPostFuneralReportController extends JeecgController<SmartPostFuneralReport, ISmartPostFuneralReportService> {
	@Autowired
	private ISmartPostFuneralReportService smartPostFuneralReportService;

	 @Autowired
	 private ISmartFuneralReportService smartFuneralReportService;

	 @Autowired
	 private SmartPostFuneralReportServiceImpl smartPostFuneralReportServiceImpl;

	 @Autowired
	 private ISmartVerifyTypeService smartVerifyTypeService;

//	 @Autowired
//	 private ISmartPostFuneralReportFileService smartPostFuneralReportFileService;

	 @Autowired
	 private BaseCommonService baseCommonService;

	 @Autowired
	 CommonService commonService;

	 @Autowired
	 private SmartVerify smartVerify;

	 public String verifyType = "丧事事后报备";

	 @Autowired
	 private ISysBaseAPI sysBaseAPI;
	
	/**
	 * 分页列表查询
	 *
	 * @param smartPostFuneralReport
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "丧事事后报备表-分页列表查询")
	@ApiOperation(value="丧事事后报备表-分页列表查询", notes="丧事事后报备表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartPostFuneralReport smartPostFuneralReport,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
// 获取登录用户信息，可以用来查询单位部门信息
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String username = sysUser.getUsername();
		// 获取用户角色
		List<String> role = sysBaseAPI.getRolesByUsername(username);

		Page<SmartPostFuneralReport> page = new Page<SmartPostFuneralReport>(pageNo, pageSize);

		// 如果是普通用户，则只能看到自己创建的数据
		if(role.contains("CommonUser")) {
			QueryWrapper<SmartPostFuneralReport> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("create_by",username);
			IPage<SmartPostFuneralReport> pageList = smartPostFuneralReportService.page(page, queryWrapper);
			return Result.OK(pageList);
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

			QueryWrapper<SmartPostFuneralReport> queryWrapper = QueryGenerator.initQueryWrapper(smartPostFuneralReport, map);

			IPage<SmartPostFuneralReport> pageList = smartPostFuneralReportService.page(page, queryWrapper);
			// 请同步修改edit函数中，将departId变为null，不然会更新成名称
//			List<String> departIds = pageList.getRecords().stream().map(SmartPostFuneralReport::getDepartId).collect(Collectors.toList());
//			if (departIds != null && departIds.size() > 0) {
//				Map<String, String> useDepNames = commonService.getDepNamesByIds(departIds);
//				pageList.getRecords().forEach(item -> {
//					item.setDepartId(useDepNames.get(item.getDepartId()));
//				});
//			}
			System.out.println(pageList);
		return Result.OK(pageList);
	}
	}
	
	/**
	 *   添加
	 *
	 * @param smartPostFuneralReport
	 * @return
	 */
	@AutoLog(value = "丧事事后报备表-添加")
	@ApiOperation(value="丧事事后报备表-添加", notes="丧事事后报备表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SmartPostFuneralReport smartPostFuneralReport) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String orgCode = sysUser.getOrgCode();
		// 获取用户角色
		if ("".equals(orgCode)) {
			return Result.error("用户没有操作权限！");
		}
		String id = commonService.getDepartIdByOrgCode(orgCode);
		if (id == null) {
			return Result.error("未找到您所属部门，请检查部门是否存在！");
		}
		SmartFuneralReport preReport = smartFuneralReportService.getById(
				smartPostFuneralReport.getPreId()
		);
//		if (preReport.getIfReport() == 1) {
//			return Result.error("请勿重复报备！");
//		}
		smartPostFuneralReport.setDepartId(id);
		smartPostFuneralReport.setReportTime(new Date());
		Boolean isVerify = smartVerifyTypeService.getIsVerifyStatusByType(verifyType);
		if(isVerify){
			smartPostFuneralReportService.save(smartPostFuneralReport);
			String recordId = smartPostFuneralReport.getId();
			log.info("recordId is"+recordId);
			smartVerify.addVerifyRecord(recordId,verifyType);
			smartPostFuneralReport.setVerifyStatus(smartVerify.getFlowStatusById(recordId).toString());
			smartPostFuneralReportService.updateById(smartPostFuneralReport);
		} else {
			// 设置审核状态为免审
			smartPostFuneralReport.setVerifyStatus("3");
			// 直接添加，不走审核流程
			smartPostFuneralReportService.save(smartPostFuneralReport);
		}
		//更改前报备isReport为"1"
		preReport.setIfReport(1);
		smartFuneralReportService.updateById(preReport);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param smartPostFuneralReport
	 * @return
	 */
	@AutoLog(value = "丧事事后报备表-编辑")
	@ApiOperation(value="丧事事后报备表-编辑", notes="丧事事后报备表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SmartPostFuneralReport smartPostFuneralReport) {
		smartPostFuneralReportService.updateById(smartPostFuneralReport);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "丧事事后报备表-通过id删除")
	@ApiOperation(value="丧事事后报备表-通过id删除", notes="丧事事后报备表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		smartPostFuneralReportService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "丧事事后报备表-批量删除")
	@ApiOperation(value="丧事事后报备表-批量删除", notes="丧事事后报备表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartPostFuneralReportService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "丧事事后报备表-通过id查询")
	@ApiOperation(value="丧事事后报备表-通过id查询", notes="丧事事后报备表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartPostFuneralReport smartPostFuneralReport = smartPostFuneralReportService.getById(id);
		if(smartPostFuneralReport==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(smartPostFuneralReport);
	}

     //根据婚前id查找后报备记录
     @GetMapping(value = "/queryByPreId")
     public Result<?> queryByPreId(@RequestParam(name = "preId", required = true) String preId) {
         SmartPostFuneralReport smartPostFuneralReport = smartPostFuneralReportService.getByPreId(preId);
         if (smartPostFuneralReport == null) {
             return Result.error("未找到对应数据");
         }
         return Result.OK(smartPostFuneralReport);

     }

    /**
    * 导出excel
    *
    * @param req
    * @param smartPostFuneralReport
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest req, HttpServletResponse response,  SmartPostFuneralReport smartPostFuneralReport)throws Exception {
		// 获取登录用户信息，可以用来查询单位部门信息
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		String username = sysUser.getUsername();

		// 获取用户角色
		List<String> role = sysBaseAPI.getRolesByUsername(username);

		List<SmartPostFuneralReport> queryList = new ArrayList<SmartPostFuneralReport>();


		// 如果是普通用户，则只能看到自己创建的数据
		if(role.contains("CommonUser")) {
			QueryWrapper<SmartPostFuneralReport> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("create_by",username);
			queryList = smartPostFuneralReportService.list(queryWrapper);
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
			QueryWrapper<SmartPostFuneralReport> queryWrapper = QueryGenerator.initQueryWrapper(smartPostFuneralReport, map);

			queryList = smartPostFuneralReportService.list(queryWrapper);
		}



		// Step.1 组装查询条件查询数据
//      QueryWrapper<SmartEvaluateMeeting> queryWrapper = QueryGenerator.initQueryWrapper(smartEvaluateMeeting, request.getParameterMap());
//      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		//Step.2 获取导出数据
//      List<SmartEvaluateMeeting> queryList = smartEvaluateMeetingService.list(queryWrapper);
		// 过滤选中数据
		String selections = req.getParameter("selections");
		List<SmartPostFuneralReport> smartPostFuneralReportList = new ArrayList<SmartPostFuneralReport>();
		if(oConvertUtils.isEmpty(selections)) {
			smartPostFuneralReportList = queryList;
		}else {
			List<String> selectionList = Arrays.asList(selections.split(","));
			smartPostFuneralReportList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
		}

		// Step.3 组装pageList
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

		// Step.4 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		mv.addObject(NormalExcelConstants.FILE_NAME, "丧事事后报备表");
		mv.addObject(NormalExcelConstants.CLASS, SmartPostFuneralReport.class);
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("丧事事后报备表数据", "导出人:"+sysUser.getRealname(), "丧事事后报备表表"));
		mv.addObject(NormalExcelConstants.DATA_LIST, smartPostFuneralReportList);

		// List深拷贝，否则返回前端会没数据
		List<SmartPostFuneralReport> newPageList = ObjectUtil.cloneByStream(smartPostFuneralReportList);

		baseCommonService.addExportLog(mv.getModel(), "丧事事后报备", req, response);

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
        return super.importExcel(request, response, SmartPostFuneralReport.class);
    }



	 @GetMapping(value = "/exportWord")
	 public void exportWord(@RequestParam(name = "ids", required = true) String ids, HttpServletResponse response, HttpServletRequest request) {

		 List<String> idsList = Arrays.asList(ids.split(","));
		 QueryWrapper<SmartPostFuneralReport> queryWrapper = new QueryWrapper<>();
		 queryWrapper.select("id").in("pre_id", idsList);
		 List<SmartPostFuneralReport> list = smartPostFuneralReportService.list(queryWrapper);

		 List<String> idList = new ArrayList<>();

		 for(int i = 0; i < list.size(); i++){
			 idList.add(list.get(i).getId());
		 }

		 List<FuneralReport> funeralReports = smartPostFuneralReportService.listByIds(idList);


		 //存放数据map
		 List<Map<String, Object>> dataList = new ArrayList<>();
		 //存放文件名
		 List<String> fileNamesList = new ArrayList<>();
		 for (int i = 0; i < funeralReports.size(); i++) {
//			 //数据完善
			 if(funeralReports.get(i).getSex() == null)
			 {funeralReports.get(i).setSex("");}
			 else if ( funeralReports.get(i).getSex().equals("2")) {
				 funeralReports.get(i).setSex("女");
			 } else if (funeralReports.get(i).getSex().equals("1")) {
				 funeralReports.get(i).setSex("男");
			 }

//			 //部门名称
//			 List<String> departids = new ArrayList<>();
//			 departids.add(smartPostMarriageReports.get(i).getWorkDepartment());
//			 Map<String, String> departNames = commonService.getDepNamesByIds(departids);
//			 smartPostMarriageReports.get(i).setWorkDepartment(departNames.get(smartPostMarriageReports.get(i).getWorkDepartment()));

			 //设置数据
			 if(funeralReports.get(i).getBirthday()== null || funeralReports.get(i).getBirthday().equals("") )
			 {funeralReports.get(i).setAge("");}
			 else{
			 	funeralReports.get(i).setAge(smartPostFuneralReportServiceImpl.getAge(funeralReports.get(i).getBirthday()));}
			 if(funeralReports.get(i).getPositionRank()== null || funeralReports.get(i).getPositionRank().equals(""))
			 {funeralReports.get(i).setPositionRank("");}
			 else{
				 if(smartPostFuneralReportServiceImpl.getDicText("1174509082208395266",funeralReports.get(i).getPositionRank())!=null){
			 	funeralReports.get(i).setPositionRank(smartPostFuneralReportServiceImpl.getDicText("1174509082208395266",funeralReports.get(i).getPositionRank()));}
			 }
			 if(funeralReports.get(i).getOrgCode()== null || funeralReports.get(i).getOrgCode().equals(""))
			 {funeralReports.get(i).setOrgCode("");}
			 else{
				 if(smartPostFuneralReportServiceImpl.getDepByOrgCode(funeralReports.get(i).getOrgCode())!=null){
				 funeralReports.get(i).setOrgCode(smartPostFuneralReportServiceImpl.getDepByOrgCode(funeralReports.get(i).getOrgCode()));}
			 }
			 if(funeralReports.get(i).getPost()== null || funeralReports.get(i).getPost().equals(""))
			 {funeralReports.get(i).setPost("");}
			 else{
				 funeralReports.get(i).setPost(smartPostFuneralReportServiceImpl.getPostByCode(funeralReports.get(i).getPost()));
			 }
			 if(funeralReports.get(i).getPoliticalStatus()== null || funeralReports.get(i).getPoliticalStatus().equals(""))
			 {funeralReports.get(i).setPoliticalStatus("");}
			 else{
			 	if(smartPostFuneralReportServiceImpl.getDicText("1455557934932160513",funeralReports.get(i).getPoliticalStatus())!=null){
				 funeralReports.get(i).setPoliticalStatus(smartPostFuneralReportServiceImpl.getDicText("1455557934932160513",funeralReports.get(i).getPoliticalStatus()));}
			 }
			 if(funeralReports.get(i).getPhone()== null)
			 {funeralReports.get(i).setPhone("");}
			 if(funeralReports.get(i).getElseState()== null)
			 {funeralReports.get(i).setElseState("");}
			 Map<String, Object> dataMap = new HashMap<>();
			 dataMap.put("e", funeralReports.get(i));
			 dataList.add(dataMap);

			 //文件名，注意于数据对应
			 String fileName = funeralReports.get(i).getRealname();
			 fileNamesList.add(fileName);
		 }

		 //设置模板
		 String ftlTemplateName = "/templates/funeralReport.ftl";
		 WordUtils.exportWordBatch(dataList, fileNamesList, ftlTemplateName, response, request);
	 }

}
