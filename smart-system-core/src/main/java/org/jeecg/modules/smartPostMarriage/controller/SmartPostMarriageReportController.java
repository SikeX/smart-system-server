package org.jeecg.modules.smartPostMarriage.controller;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.modules.common.service.CommonService;
import org.jeecg.modules.common.util.ParamsUtil;
import org.jeecg.modules.tasks.smartVerifyTask.service.SmartVerify;
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
import org.jeecg.modules.smartPostMarriage.entity.SmartPostMarriageReportFile;
import org.jeecg.modules.smartPostMarriage.entity.SmartPostMarriageReport;
import org.jeecg.modules.smartPostMarriage.vo.SmartPostMarriageReportPage;
import org.jeecg.modules.smartPostMarriage.service.ISmartPostMarriageReportService;
import org.jeecg.modules.smartPostMarriage.service.ISmartPostMarriageReportFileService;
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
 * @Description: 8项规定婚后报备表
 * @Author: jeecg-boot
 * @Date:   2021-11-10
 * @Version: V1.0
 */
@Api(tags="8项规定婚后报备表")
@RestController
@RequestMapping("/smartPostMarriage/smartPostMarriageReport")
@Slf4j
public class SmartPostMarriageReportController {
	 @Autowired
	 private ISmartPostMarriageReportService smartPostMarriageReportService;
	 @Autowired
	 private ISmartPostMarriageReportFileService smartPostMarriageReportFileService;

	 @Autowired
	 CommonService commonService;

	 @Autowired
	 private SmartVerify smartVerify;
	 public String verifyType = "婚后报备";


	 /**
	  * 分页列表查询
	  *
	  * @param smartPostMarriageReport
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "8项规定婚后报备表-分页列表查询")
	 @ApiOperation(value = "8项规定婚后报备表-分页列表查询", notes = "8项规定婚后报备表-分页列表查询")
	 @GetMapping(value = "/list")
	 public Result<?> queryPageList(SmartPostMarriageReport smartPostMarriageReport,
									@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
									@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
									HttpServletRequest req) {

		 // TODO：规则，下面是 以＊*开始
		 String rule = "in";
		 // TODO：查询字段
		 String field = "workDepartment";
		 // 获取登录用户信息，可以用来查询单位部门信息
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 System.out.println(sysUser.getOrgCode() + "此用户的部门");

		 // 获取子单位ID
		 String childrenIdString = commonService.getChildrenIdStringByOrgCode(sysUser.getOrgCode());

		 // 添加查询参数，下面的参数是查询以用户所在部门编码开头的的所有单位数据，即用户所在单位和子单位的信息
		 // superQueryParams=[{"rule":"right_like","type":"string","dictCode":"","val":"用户所在的部门","field":"departId"}]
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

		 QueryWrapper<SmartPostMarriageReport> queryWrapper = QueryGenerator.initQueryWrapper(smartPostMarriageReport, map);
		 Page<SmartPostMarriageReport> page = new Page<SmartPostMarriageReport>(pageNo, pageSize);
		 IPage<SmartPostMarriageReport> pageList = smartPostMarriageReportService.page(page, queryWrapper);

		 List<String> departIds = pageList.getRecords().stream().map(SmartPostMarriageReport::getWorkDepartment).collect(Collectors.toList());
		 if (departIds != null && departIds.size() > 0) {
			 Map<String, String> useDepNames = commonService.getDepNamesByIds(departIds);
			 pageList.getRecords().forEach(item -> {
				 item.setWorkDepartment(useDepNames.get(item.getWorkDepartment()));
			 });
		 }

		 return Result.OK(pageList);
	 }

	 /**
	  * 添加
	  *
	  * @param smartPostMarriageReportPage
	  * @return
	  */
	 @AutoLog(value = "8项规定婚后报备表-添加")
	 @ApiOperation(value = "8项规定婚后报备表-添加", notes = "8项规定婚后报备表-添加")
	 @PostMapping(value = "/add")
	 public Result<?> add(@RequestBody SmartPostMarriageReportPage smartPostMarriageReportPage) {

	 	//审核功能
	 	smartVerify.addVerifyRecord(smartPostMarriageReportPage.getId(),verifyType);

		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 String orgCode = sysUser.getOrgCode();
		 if ("".equals(orgCode)) {
			 return Result.error("本用户没有操作权限！");
		 }
		 System.out.println(orgCode + "此用户的orgcode");
		 String id = smartPostMarriageReportService.getDepartIdByOrgCode(orgCode);
		 smartPostMarriageReportPage.setWorkDepartment(id);
		 System.out.println(id + "id等于");

		 SmartPostMarriageReport smartPostMarriageReport = new SmartPostMarriageReport();
		 BeanUtils.copyProperties(smartPostMarriageReportPage, smartPostMarriageReport);
		 smartPostMarriageReportService.saveMain(smartPostMarriageReport, smartPostMarriageReportPage.getSmartPostMarriageReportFileList());
		 return Result.OK("添加成功！");
	 }

	 /**
	  * 编辑
	  *
	  * @param smartPostMarriageReportPage
	  * @return
	  */
	 @AutoLog(value = "8项规定婚后报备表-编辑")
	 @ApiOperation(value = "8项规定婚后报备表-编辑", notes = "8项规定婚后报备表-编辑")
	 @PutMapping(value = "/edit")
	 public Result<?> edit(@RequestBody SmartPostMarriageReportPage smartPostMarriageReportPage) {
		 SmartPostMarriageReport smartPostMarriageReport = new SmartPostMarriageReport();
		 BeanUtils.copyProperties(smartPostMarriageReportPage, smartPostMarriageReport);
		 SmartPostMarriageReport smartPostMarriageReportEntity = smartPostMarriageReportService.getById(smartPostMarriageReport.getId());
		 if (smartPostMarriageReportEntity == null) {
			 return Result.error("未找到对应数据");
		 }
		 smartPostMarriageReport.setWorkDepartment(null);
		 smartPostMarriageReport.setCreateTime(null);
		 smartPostMarriageReportService.updateMain(smartPostMarriageReport, smartPostMarriageReportPage.getSmartPostMarriageReportFileList());
		 return Result.OK("编辑成功!");
	 }

	 /**
	  * 通过id删除
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "8项规定婚后报备表-通过id删除")
	 @ApiOperation(value = "8项规定婚后报备表-通过id删除", notes = "8项规定婚后报备表-通过id删除")
	 @DeleteMapping(value = "/delete")
	 public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
		 smartPostMarriageReportService.delMain(id);
		 return Result.OK("删除成功!");
	 }

	 /**
	  * 批量删除
	  *
	  * @param ids
	  * @return
	  */
	 @AutoLog(value = "8项规定婚后报备表-批量删除")
	 @ApiOperation(value = "8项规定婚后报备表-批量删除", notes = "8项规定婚后报备表-批量删除")
	 @DeleteMapping(value = "/deleteBatch")
	 public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
		 this.smartPostMarriageReportService.delBatchMain(Arrays.asList(ids.split(",")));
		 return Result.OK("批量删除成功！");
	 }

	 /**
	  * 通过id查询
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "8项规定婚后报备表-通过id查询")
	 @ApiOperation(value = "8项规定婚后报备表-通过id查询", notes = "8项规定婚后报备表-通过id查询")
	 @GetMapping(value = "/queryById")
	 public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
		 SmartPostMarriageReport smartPostMarriageReport = smartPostMarriageReportService.getById(id);
		 if (smartPostMarriageReport == null) {
			 return Result.error("未找到对应数据");
		 }
		 return Result.OK(smartPostMarriageReport);

	 }

	 /**
	  * 通过id查询
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "8项规定婚后报备宴请发票与附件表通过主表ID查询")
	 @ApiOperation(value = "8项规定婚后报备宴请发票与附件表主表ID查询", notes = "8项规定婚后报备宴请发票与附件表-通主表ID查询")
	 @GetMapping(value = "/querySmartPostMarriageReportFileByMainId")
	 public Result<?> querySmartPostMarriageReportFileListByMainId(@RequestParam(name = "id", required = true) String id) {
		 List<SmartPostMarriageReportFile> smartPostMarriageReportFileList = smartPostMarriageReportFileService.selectByMainId(id);
		 return Result.OK(smartPostMarriageReportFileList);
	 }

	 /**
	  * 导出excel
	  *
	  * @param request
	  * @param smartPostMarriageReport
	  */
	 @RequestMapping(value = "/exportXls")
	 public ModelAndView exportXls(HttpServletRequest request, SmartPostMarriageReport smartPostMarriageReport) {
		 // Step.1 组装查询条件查询数据
		 QueryWrapper<SmartPostMarriageReport> queryWrapper = QueryGenerator.initQueryWrapper(smartPostMarriageReport, request.getParameterMap());
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		 //Step.2 获取导出数据
		 List<SmartPostMarriageReport> queryList = smartPostMarriageReportService.list(queryWrapper);
		 // 过滤选中数据
		 String selections = request.getParameter("selections");
		 List<SmartPostMarriageReport> smartPostMarriageReportList = new ArrayList<SmartPostMarriageReport>();
		 if (oConvertUtils.isEmpty(selections)) {
			 smartPostMarriageReportList = queryList;
		 } else {
			 List<String> selectionList = Arrays.asList(selections.split(","));
			 smartPostMarriageReportList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
		 }

		 // Step.3 组装pageList
		 List<SmartPostMarriageReportPage> pageList = new ArrayList<SmartPostMarriageReportPage>();
		 for (SmartPostMarriageReport main : smartPostMarriageReportList) {
			 SmartPostMarriageReportPage vo = new SmartPostMarriageReportPage();
			 BeanUtils.copyProperties(main, vo);
			 List<SmartPostMarriageReportFile> smartPostMarriageReportFileList = smartPostMarriageReportFileService.selectByMainId(main.getId());
			 vo.setSmartPostMarriageReportFileList(smartPostMarriageReportFileList);
			 pageList.add(vo);
		 }

		 // Step.4 AutoPoi 导出Excel
		 ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		 mv.addObject(NormalExcelConstants.FILE_NAME, "8项规定婚后报备表列表");
		 mv.addObject(NormalExcelConstants.CLASS, SmartPostMarriageReportPage.class);
		 mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("8项规定婚后报备表数据", "导出人:" + sysUser.getRealname(), "8项规定婚后报备表"));
		 mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
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
				 List<SmartPostMarriageReportPage> list = ExcelImportUtil.importExcel(file.getInputStream(), SmartPostMarriageReportPage.class, params);
				 for (SmartPostMarriageReportPage page : list) {
					 SmartPostMarriageReport po = new SmartPostMarriageReport();
					 BeanUtils.copyProperties(page, po);
					 smartPostMarriageReportService.saveMain(po, page.getSmartPostMarriageReportFileList());
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

	 @AutoLog(value = "更新文件下载次数")
	 @ApiOperation(value = "更新文件下载次数", notes = "更新文件下载次数")
	 @PutMapping(value = "/downloadCount")
	 public Result<?> edit(@RequestBody SmartPostMarriageReportFile smartPostMarriageReportFile) {
		 SmartPostMarriageReportFile newSmartPostMarriageReportFile =
				 smartPostMarriageReportFileService.getById(smartPostMarriageReportFile.getId());
		 int currentCount = newSmartPostMarriageReportFile.getDownloadCount();
		 newSmartPostMarriageReportFile.setDownloadCount(currentCount + 1);
		 smartPostMarriageReportFileService.updateById(newSmartPostMarriageReportFile);
		 return Result.OK("更新成功!");
	 }
 }
