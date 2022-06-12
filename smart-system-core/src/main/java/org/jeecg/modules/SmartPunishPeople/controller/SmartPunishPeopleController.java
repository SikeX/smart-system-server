package org.jeecg.modules.SmartPunishPeople.controller;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.system.vo.SysDepartModel;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.SmartFirstFormPeople.entity.SmartFirstFormPeople;
import org.jeecg.modules.SmartInnerPartyTalk.entity.SmartInnerPartyTalk;
import org.jeecg.modules.SmartPunishPeople.entity.SmartPunishPeople;
import org.jeecg.modules.SmartPunishPeople.entity.TypeCount;
import org.jeecg.modules.SmartPunishPeople.service.ISmartPunishPeopleService;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.modules.common.service.CommonService;
import org.jeecg.modules.common.util.ParamsUtil;
import org.jeecg.modules.smartCreateAdvice.entity.SmartCreateAdvice;
import org.jeecg.modules.smartCreateAdvice.entity.SmartCreateAdviceAnnex;
import org.jeecg.modules.smartCreateAdvice.vo.SmartCreateAdvicePage;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 处分人员表
 * @Author: jeecg-boot
 * @Date:   2021-11-07
 * @Version: V1.0
 */
@Api(tags="处分人员表")
@RestController
@RequestMapping("/SmartPunishPeople/smartPunishPeople")
@Slf4j
public class SmartPunishPeopleController extends JeecgController<SmartPunishPeople, ISmartPunishPeopleService> {
	@Autowired
	private ISmartPunishPeopleService smartPunishPeopleService;
	 @Autowired
	 private CommonService commonService;
	@Autowired
	private ISysBaseAPI sysBaseAPI;
	@Autowired
	private BaseCommonService baseCommonService;
	//判断处分状态
	 public Integer punishStatu(Date begin,Date end) throws ParseException {
	 		Integer statu;
		 Date current = new Date();
		 System.out.println(current);
		 System.out.println(end);
		 if (begin.after(current)){
			 //未开始处分
			 statu = 2;
		 }else if(begin.before(current) && end.after(current)){
			 //处分中
			 statu = 1;
		 }else{
			 //处分结束
			 statu = 0;
		 }
		 return statu;
	 }
	/**
	 * 分页列表查询
	 *
	 * @param smartPunishPeople
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "处分人员表-分页列表查询")
	@ApiOperation(value="处分人员表-分页列表查询", notes="处分人员表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartPunishPeople smartPunishPeople,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) throws ParseException {
		/*// 1. 规则，下面是 以**开始
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
		QueryWrapper<SmartPunishPeople> queryWrapper = QueryGenerator.initQueryWrapper(smartPunishPeople, map);*/
		QueryWrapper<SmartPunishPeople> queryWrapper = QueryGenerator.initQueryWrapper(smartPunishPeople, req.getParameterMap());

		Page<SmartPunishPeople> page = new Page<SmartPunishPeople>(pageNo, pageSize);
		IPage<SmartPunishPeople> pageList = smartPunishPeopleService.page(page, queryWrapper);
		// 请同步修改edit函数中，将departId变为null，不然会更新成名称
		List<String> departIds = pageList.getRecords().stream().map(SmartPunishPeople::getDepartId).collect(Collectors.toList());
		if (departIds != null && departIds.size() > 0) {
			Map<String, String> useDepNames = commonService.getDepNamesByIds(departIds);
			pageList.getRecords().forEach(item -> {
				item.setDepartId(useDepNames.get(item.getDepartId()));
			});
		}
		return Result.OK(pageList);
	}

	/**
	 * 获取单位处分人员数目
	 * @param departId 单位ID
	 *
	 * @return
	 */
	@GetMapping(value = "/countByDepartId")
	public Result<JSONObject> countByDepartId(@RequestParam(name = "departId", required = true) String departId) {
		Result<JSONObject> result = new Result<JSONObject>();
		JSONObject obj = new JSONObject();

		// 查询单位处分人员数目
		QueryWrapper<SmartPunishPeople> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("depart_code", departId).eq("del_flag", 0);
		long count = smartPunishPeopleService.count(queryWrapper);

		// 查询单位负责人是否被处分
		Integer mainPeopleCount = smartPunishPeopleService.countMainPeopleByDepart(departId);

		obj.put("count", count);
		obj.put("mainPeopleCount", mainPeopleCount);
		result.setResult(obj);

		return result;
	}
	
	/**
	 *   添加
	 *
	 * @param smartPunishPeople
	 * @return
	 */
	@AutoLog(value = "处分人员表-添加")
	@ApiOperation(value="处分人员表-添加", notes="处分人员表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SmartPunishPeople smartPunishPeople) throws ParseException {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String orgCode = sysUser.getOrgCode();
		if ("".equals(orgCode)) {
			return Result.error("本用户没有操作权限！");
		}
		String id = commonService.getDepartIdByOrgCode(orgCode);
		if (id == null) {
			return Result.error("没有找到部门！");
		}
		smartPunishPeople.setDepartId(id);
		//更改处分状态
		Date begin =  smartPunishPeople.getBeginTime();
		Date end = smartPunishPeople.getRemoveTime();
		smartPunishPeople.setStatu(punishStatu(begin,end));
		smartPunishPeopleService.save(smartPunishPeople);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param smartPunishPeople
	 * @return
	 */
	@AutoLog(value = "处分人员表-编辑")
	@ApiOperation(value="处分人员表-编辑", notes="处分人员表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SmartPunishPeople smartPunishPeople) throws ParseException {
		SmartPunishPeople smartPunishPeopleEntity = smartPunishPeopleService.getById(smartPunishPeople.getId());
		if(smartPunishPeopleEntity==null) {
			return Result.error("未找到对应数据");
		}
		smartPunishPeople.setDepartId(null);
		smartPunishPeople.setCreateTime(null);
		//更改处分状态
		Date begin =  smartPunishPeople.getBeginTime();
		Date end = smartPunishPeople.getRemoveTime();
		smartPunishPeople.setStatu(punishStatu(begin,end));
		smartPunishPeopleService.updateById(smartPunishPeople);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "处分人员表-通过id删除")
	@ApiOperation(value="处分人员表-通过id删除", notes="处分人员表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		smartPunishPeopleService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "处分人员表-批量删除")
	@ApiOperation(value="处分人员表-批量删除", notes="处分人员表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartPunishPeopleService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "处分人员表-通过id查询")
	@ApiOperation(value="处分人员表-通过id查询", notes="处分人员表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartPunishPeople smartPunishPeople = smartPunishPeopleService.getById(id);
		if(smartPunishPeople==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(smartPunishPeople);
	}

    /**
    * 导出excel
    *
    * @param req
    * @param smartPunishPeople
    */
    @RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(HttpServletRequest req,
								  HttpServletResponse response, SmartPunishPeople smartPunishPeople) throws Exception {

		// 获取登录用户信息，可以用来查询单位部门信息
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		String username = sysUser.getUsername();

		// 获取用户角色
		List<String> role = sysBaseAPI.getRolesByUsername(username);

		List<SmartPunishPeople> queryList = new ArrayList<SmartPunishPeople>();


		// 如果是普通用户，则只能看到自己创建的数据
		if(role.contains("CommonUser")) {
			QueryWrapper<SmartPunishPeople> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("create_by",username);
			queryList = smartPunishPeopleService.list(queryWrapper);
		} else {
			/*// 1. 规则，下面是 以**开始
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
			map.put("superQueryMatchType", params);*/
			QueryWrapper<SmartPunishPeople> queryWrapper = QueryGenerator.initQueryWrapper(smartPunishPeople, req.getParameterMap());

			queryList = smartPunishPeopleService.list(queryWrapper);
		}


		// Step.1 组装查询条件查询数据

		//Step.2 获取导出数据
		// 过滤选中数据
		String selections = req.getParameter("selections");
		List<SmartPunishPeople> smartPunishPeopleList = new ArrayList<SmartPunishPeople>();
		if(oConvertUtils.isEmpty(selections)) {
			smartPunishPeopleList = queryList;
		}else {
			List<String> selectionList = Arrays.asList(selections.split(","));
			smartPunishPeopleList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
		}

		// Step.3 组装pageList
		List<SmartPunishPeople> pageList = new ArrayList<SmartPunishPeople>();
		for (SmartPunishPeople main : smartPunishPeopleList) {
			SmartPunishPeople vo = new SmartPunishPeople();
			BeanUtils.copyProperties(main, vo);
			pageList.add(vo);
		}

		// Step.4 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		mv.addObject(NormalExcelConstants.FILE_NAME, "处分人员列表");
		mv.addObject(NormalExcelConstants.CLASS, SmartPunishPeople.class);
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("处分人员表数据", "导出人:"+sysUser.getRealname(), "处分人员表"));
		mv.addObject(NormalExcelConstants.DATA_LIST, pageList);

		// List深拷贝，否则返回前端会没数据
		List<SmartPunishPeople> newPageList = ObjectUtil.cloneByStream(pageList);

		baseCommonService.addExportLog(mv.getModel(), "处分人员", req, response);

		mv.addObject(NormalExcelConstants.DATA_LIST, newPageList);

		return mv;

	}

   /* public ModelAndView exportXls(HttpServletRequest request, SmartPunishPeople smartPunishPeople) {
        return super.exportXls(request, smartPunishPeople, SmartPunishPeople.class, "处分人员表");
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
        return super.importExcel(request, response, SmartPunishPeople.class);
    }
	//处分人员总数量
    @RequestMapping(value = "/punishPeopleCount",method = RequestMethod.GET)
	public Result<?> punishPeopleCount(){
    	QueryWrapper<SmartPunishPeople> queryWrapper = new QueryWrapper<>();
    	queryWrapper.eq("del_flag",0);
    	Integer count = (smartPunishPeopleService.list(queryWrapper)).size();
		return Result.OK(count);
	}
	//按处分类型统计
	@RequestMapping(value = "/punishPeopleCountByType",method = RequestMethod.GET)
	public Result<?> punishPeopleCountByType() {
		List<TypeCount>  list = smartPunishPeopleService.punishPeopleCountByType();
//		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//		System.out.println(list);
		return Result.OK(list);
	}
	//本月即将解除处分人员数量
	@RequestMapping(value = "/punishPeopleCountByMonth",method = RequestMethod.GET)
	public Result<?> punishPeopleCountByMonth() {
    	Date date = new Date();
		QueryWrapper<SmartPunishPeople> queryWrapper = new QueryWrapper<>();
		queryWrapper.ge("remove_time",date);
		Integer count = (smartPunishPeopleService.list(queryWrapper)).size();
		return Result.OK(count);
	}

}
