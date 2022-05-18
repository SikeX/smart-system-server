package org.jeecg.modules.SmartFirstFormPeople.controller;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.system.vo.SysDepartModel;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.SmartFirstFormPeople.entity.SmartFirstFormPeople;
import org.jeecg.modules.SmartFirstFormPeople.service.ISmartFirstFormPeopleService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.modules.common.service.CommonService;
import org.jeecg.modules.common.util.ParamsUtil;
import org.jeecg.modules.smartEvaluateList.entity.MonthCount;
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
 * @Description: 执行第一种形态人员表
 * @Author: jeecg-boot
 * @Date:   2021-11-08
 * @Version: V1.0
 */
@Api(tags="执行第一种形态人员表")
@RestController
@RequestMapping("/SmartFirstFormPeople/smartFirstFormPeople")
@Slf4j
public class SmartFirstFormPeopleController extends JeecgController<SmartFirstFormPeople, ISmartFirstFormPeopleService> {
	@Autowired
	private ISmartFirstFormPeopleService smartFirstFormPeopleService;

	 @Autowired
	 private CommonService commonService;
	 @Autowired
	 private ISmartVerifyTypeService smartVerifyTypeService;
	 @Autowired
	 private ISysBaseAPI sysBaseAPI;
	 @Autowired
	 private BaseCommonService baseCommonService;
	 @Autowired
	 private SmartVerify smartVerify;
	 public String verifyType = "执行第一形态人";
	
	/**
	 * 分页列表查询
	 *
	 * @param smartFirstFormPeople
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "执行第一种形态人员表-分页列表查询")
	@ApiOperation(value="执行第一种形态人员表-分页列表查询", notes="执行第一种形态人员表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartFirstFormPeople smartFirstFormPeople,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		// 1. 规则，下面是 以**开始
		String rule = "in";
		// 2. 查询字段
		String field = "departId";
		// 获取登录用户信息，可以用来查询单位部门信息
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
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
		QueryWrapper<SmartFirstFormPeople> queryWrapper = QueryGenerator.initQueryWrapper(smartFirstFormPeople, map);
		Page<SmartFirstFormPeople> page = new Page<SmartFirstFormPeople>(pageNo, pageSize);
		IPage<SmartFirstFormPeople> pageList = smartFirstFormPeopleService.page(page, queryWrapper);
		// 请同步修改edit函数中，将departId变为null，不然会更新成名称
		List<String> departIds = pageList.getRecords().stream().map(SmartFirstFormPeople::getDepartId).collect(Collectors.toList());
		if (departIds != null && departIds.size() > 0) {
			Map<String, String> useDepNames = commonService.getDepNamesByIds(departIds);
			pageList.getRecords().forEach(item -> {
				item.setDepartId(useDepNames.get(item.getDepartId()));
			});
		}
		return Result.OK(pageList);
	}

	 /**
	  * 获取单位执行第一形态人员数目
	  * @param departId 单位ID
	  *
	  * @return
	  */
	 @GetMapping(value = "/countByDepartId")
	 public Result<JSONObject> countByDepartId(@RequestParam(name = "departId", required = true) String departId) {
		 Result<JSONObject> result = new Result<JSONObject>();
		 JSONObject obj = new JSONObject();

		 QueryWrapper<SmartFirstFormPeople> queryWrapper = new QueryWrapper<>();
		 queryWrapper.eq("interviewee_dept", departId).eq("del_flag", 0);
		 long count = smartFirstFormPeopleService.count(queryWrapper);

		 obj.put("count", count);
		 result.setResult(obj);

		 return result;
	 }
	
	/**
	 *   添加
	 *
	 * @param smartFirstFormPeople
	 * @return
	 */
	@AutoLog(value = "执行第一种形态人员表-添加")
	@ApiOperation(value="执行第一种形态人员表-添加", notes="执行第一种形态人员表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SmartFirstFormPeople smartFirstFormPeople) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String orgCode = sysUser.getOrgCode();
		if ("".equals(orgCode)) {
			return Result.error("本用户没有操作权限！");
		}
		String id = commonService.getDepartIdByOrgCode(orgCode);
		if (id == null) {
			return Result.error("没有找到部门！");
		}
		smartFirstFormPeople.setDepartId(id);
		//smartFirstFormPeopleService.save(smartFirstFormPeople);
		//审核
		Boolean isVerify = smartVerifyTypeService.getIsVerifyStatusByType(verifyType);
		if(isVerify){
			smartFirstFormPeopleService.save(smartFirstFormPeople);
			String recordId = smartFirstFormPeople.getId();
			smartVerify.addVerifyRecord(recordId,verifyType);
			smartFirstFormPeople.setVerifyStatus(smartVerify.getFlowStatusById(recordId).toString());
			smartFirstFormPeopleService.updateById(smartFirstFormPeople);
		} else {
			// 设置审核状态为免审
			smartFirstFormPeople.setVerifyStatus("3");
			// 直接添加，不走审核流程
			smartFirstFormPeopleService.save(smartFirstFormPeople);
		}
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param smartFirstFormPeople
	 * @return
	 */
	@AutoLog(value = "执行第一种形态人员表-编辑")
	@ApiOperation(value="执行第一种形态人员表-编辑", notes="执行第一种形态人员表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SmartFirstFormPeople smartFirstFormPeople) {
		SmartFirstFormPeople smartFirstFormPeopleEntity = smartFirstFormPeopleService.getById(smartFirstFormPeople.getId());
		if(smartFirstFormPeopleEntity==null) {
			return Result.error("未找到对应数据");
		}
		smartFirstFormPeople.setDepartId(null);
		smartFirstFormPeople.setCreateTime(null);
		smartFirstFormPeopleService.updateById(smartFirstFormPeople);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "执行第一种形态人员表-通过id删除")
	@ApiOperation(value="执行第一种形态人员表-通过id删除", notes="执行第一种形态人员表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		smartFirstFormPeopleService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "执行第一种形态人员表-批量删除")
	@ApiOperation(value="执行第一种形态人员表-批量删除", notes="执行第一种形态人员表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartFirstFormPeopleService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "执行第一种形态人员表-通过id查询")
	@ApiOperation(value="执行第一种形态人员表-通过id查询", notes="执行第一种形态人员表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartFirstFormPeople smartFirstFormPeople = smartFirstFormPeopleService.getById(id);
		if(smartFirstFormPeople==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(smartFirstFormPeople);
	}

    /**
    * 导出excel
    *
    * @param req
    * @param smartFirstFormPeople
    */
    @RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(HttpServletRequest req,
								  HttpServletResponse response, SmartFirstFormPeople smartFirstFormPeople) throws Exception {

		// 获取登录用户信息，可以用来查询单位部门信息
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		String username = sysUser.getUsername();

		// 获取用户角色
		List<String> role = sysBaseAPI.getRolesByUsername(username);

		List<SmartFirstFormPeople> queryList = new ArrayList<SmartFirstFormPeople>();


		// 如果是普通用户，则只能看到自己创建的数据
		if(role.contains("CommonUser")) {
			QueryWrapper<SmartFirstFormPeople> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("create_by",username);
			queryList = smartFirstFormPeopleService.list(queryWrapper);
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
			QueryWrapper<SmartFirstFormPeople> queryWrapper = QueryGenerator.initQueryWrapper(smartFirstFormPeople, map);

			queryList = smartFirstFormPeopleService.list(queryWrapper);
		}


		// Step.1 组装查询条件查询数据

		//Step.2 获取导出数据
		// 过滤选中数据
		String selections = req.getParameter("selections");
		List<SmartFirstFormPeople> smartFirstFormPeopleList = new ArrayList<SmartFirstFormPeople>();
		if(oConvertUtils.isEmpty(selections)) {
			smartFirstFormPeopleList = queryList;
		}else {
			List<String> selectionList = Arrays.asList(selections.split(","));
			smartFirstFormPeopleList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
		}

		// Step.3 组装pageList
		List<SmartFirstFormPeople> pageList = new ArrayList<SmartFirstFormPeople>();
		for (SmartFirstFormPeople main : smartFirstFormPeopleList) {
			SmartFirstFormPeople vo = new SmartFirstFormPeople();
			BeanUtils.copyProperties(main, vo);
			pageList.add(vo);
		}

		// Step.4 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		mv.addObject(NormalExcelConstants.FILE_NAME, "执行第一形态人列表");
		mv.addObject(NormalExcelConstants.CLASS, SmartFirstFormPeople.class);
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("执行第一形态人表数据", "导出人:"+sysUser.getRealname(), "执行第一形态人表"));
		mv.addObject(NormalExcelConstants.DATA_LIST, pageList);

		// List深拷贝，否则返回前端会没数据
		List<SmartFirstFormPeople> newPageList = ObjectUtil.cloneByStream(pageList);

		baseCommonService.addExportLog(mv.getModel(), "执行第一形态人", req, response);

		mv.addObject(NormalExcelConstants.DATA_LIST, newPageList);

		return mv;

	}

    /*public ModelAndView exportXls(HttpServletRequest request, SmartFirstFormPeople smartFirstFormPeople) {
        return super.exportXls(request, smartFirstFormPeople, SmartFirstFormPeople.class, "执行第一种形态人员表");
    }*/

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, SmartFirstFormPeople.class);
    }

	 @AutoLog(value = "按月统计")
	 @ApiOperation(value="按月统计", notes="按月统计")
	 @ResponseBody
	 @GetMapping(value = "/statistics")
	 public Result<List<MonthCount>> statistics(@RequestParam (value="year",required = false) String year,
								 @RequestParam (value="departCode",required = false) String departCode) {
		 try{
			 if(year == null || year.isEmpty()){
				 //获取当前年份
				 SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
				 Date date = new Date();
				 year =sdf.format(date);
			 }else{
				 year = year.substring(1,year.length()-1);
			 }
			 System.out.println(year+","+departCode);
			 List<MonthCount> list = smartFirstFormPeopleService.statistics(year,departCode);
			 System.out.println("list"+list);
			 return Result.OK(list);
		 }catch (Exception e){
			 return Result.error("error");
		 }

	 }

}
