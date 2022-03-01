package org.jeecg.modules.smartFuneralReport.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

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

 /**
 * @Description: 丧事口头报备表
 * @Author: jeecg-boot
 * @Date:   2022-02-26
 * @Version: V1.0
 */
@Api(tags="丧事口头报备表")
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

	 public String verifyType = "丧事报备";

	 @Autowired
	 private ISysBaseAPI sysBaseAPI;
	
	/**
	 * 分页列表查询
	 *
	 * @param smartFuneralReport
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "丧事口头报备表-分页列表查询")
	@ApiOperation(value="丧事口头报备表-分页列表查询", notes="丧事口头报备表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartFuneralReport smartFuneralReport,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
// 获取登录用户信息，可以用来查询单位部门信息
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String username = sysUser.getUsername();
		// 获取用户角色
		List<String> role = sysBaseAPI.getRolesByUsername(username);

		Page<SmartFuneralReport> page = new Page<SmartFuneralReport>(pageNo, pageSize);

		// 如果是普通用户，则只能看到自己创建的数据
		if(role.contains("CommonUser")) {
			QueryWrapper<SmartFuneralReport> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("create_by",username);
			IPage<SmartFuneralReport> pageList = smartFuneralReportService.page(page, queryWrapper);
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

			QueryWrapper<SmartFuneralReport> queryWrapper = QueryGenerator.initQueryWrapper(smartFuneralReport, map);

			IPage<SmartFuneralReport> pageList = smartFuneralReportService.page(page, queryWrapper);
//			// 请同步修改edit函数中，将departId变为null，不然会更新成名称
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
	 *   添加
	 *
	 * @param smartFuneralReport
	 * @return
	 */
	@AutoLog(value = "丧事口头报备表-添加")
	@ApiOperation(value="丧事口头报备表-添加", notes="丧事口头报备表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SmartFuneralReport smartFuneralReport) {
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
		smartFuneralReport.setDepartId(id);
		Boolean isVerify = smartVerifyTypeService.getIsVerifyStatusByType(verifyType);
		if(isVerify){
			smartFuneralReportService.save(smartFuneralReport);
			String recordId = smartFuneralReport.getId();
			log.info("recordId is"+recordId);
			smartVerify.addVerifyRecord(recordId,verifyType);
			smartFuneralReport.setVerifyStatus(smartVerify.getFlowStatusById(recordId).toString());
			smartFuneralReportService.updateById(smartFuneralReport);
		} else {
			// 设置审核状态为免审
			smartFuneralReport.setVerifyStatus("3");
			// 直接添加，不走审核流程
			smartFuneralReportService.save(smartFuneralReport);
		}
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param smartFuneralReport
	 * @return
	 */
	@AutoLog(value = "丧事口头报备表-编辑")
	@ApiOperation(value="丧事口头报备表-编辑", notes="丧事口头报备表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SmartFuneralReport smartFuneralReport) {
		smartFuneralReportService.updateById(smartFuneralReport);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "丧事口头报备表-通过id删除")
	@ApiOperation(value="丧事口头报备表-通过id删除", notes="丧事口头报备表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		smartFuneralReportService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "丧事口头报备表-批量删除")
	@ApiOperation(value="丧事口头报备表-批量删除", notes="丧事口头报备表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartFuneralReportService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "丧事口头报备表-通过id查询")
	@ApiOperation(value="丧事口头报备表-通过id查询", notes="丧事口头报备表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartFuneralReport smartFuneralReport = smartFuneralReportService.getById(id);
		if(smartFuneralReport==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(smartFuneralReport);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param smartFuneralReport
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartFuneralReport smartFuneralReport) {
        return super.exportXls(request, smartFuneralReport, SmartFuneralReport.class, "丧事口头报备表");
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
        return super.importExcel(request, response, SmartFuneralReport.class);
    }

	 /**
	  * id获取其他信息
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
