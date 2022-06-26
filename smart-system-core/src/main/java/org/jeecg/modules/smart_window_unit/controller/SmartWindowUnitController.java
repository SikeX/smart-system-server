package org.jeecg.modules.smart_window_unit.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.ObjectUtil;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.modules.common.service.CommonService;
import org.jeecg.modules.common.util.ParamsUtil;
import org.jeecg.modules.smart_window_people.entity.SmartWindowPeople;
import org.jeecg.modules.smart_window_people.service.ISmartWindowPeopleService;
import org.jeecg.modules.smart_window_unit.api.response.BaseResponse;
import org.jeecg.modules.smart_window_unit.api.response.StatusCode;
import org.jeecg.modules.smart_window_unit.entity.QRCodeUtil;
import org.jeecg.modules.smart_window_unit.entity.SmartWindowUnit;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.smart_window_unit.service.ISmartWindowUnitService;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.beans.BeanUtils;

class BaseController {

	protected static Logger log= LoggerFactory.getLogger(BaseController.class);

}



 /**
 * @Description: 窗口单位
 * @Author: jeecg-boot
 * @Date:   2021-12-02
 * @Version: V1.0
 */
@Api(tags="窗口单位")
@RestController
@RequestMapping("/smart_window_unit/smartWindowUnit")
@Slf4j
public class SmartWindowUnitController<ISysDepartService> extends JeecgController<SmartWindowUnit, ISmartWindowUnitService> {
	@Autowired
	private ISmartWindowUnitService smartWindowUnitService;

	@Autowired
	private ISmartWindowPeopleService smartWindowPersonService;
	@Autowired
	private ISysBaseAPI sysBaseAPI;
	@Autowired
	private BaseCommonService baseCommonService;
	@Autowired
	CommonService commonService;


	/**
	 * 分页列表查询
	 *
	 * @param smartWindowUnit
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "窗口单位-分页列表查询")
	@ApiOperation(value="窗口单位-分页列表查询", notes="窗口单位-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartWindowUnit smartWindowUnit,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SmartWindowUnit> queryWrapper = QueryGenerator.initQueryWrapper(smartWindowUnit, req.getParameterMap());
		Page<SmartWindowUnit> page = new Page<SmartWindowUnit>(pageNo, pageSize);
		IPage<SmartWindowUnit> pageList = smartWindowUnitService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加,并直接生成二维码
	 *
	 * @param smartWindowUnit
	 * @return
	 */
	@Value(value = "${jeecg.path.upload}/windows")
	private String RootPath;
	private static final String FileFormat=".png";
	private static final ThreadLocal<SimpleDateFormat> LOCALDATEFORMAT=ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMMddHHmmss"));

	@AutoLog(value = "窗口单位-添加")
	@ApiOperation(value="窗口单位-添加", notes="窗口单位-添加")
	@PostMapping(value = "/add")
	public Object add(@RequestBody SmartWindowUnit smartWindowUnit) {
		String pid = smartWindowUnit.getPid();
		String departName = smartWindowUnitService.getDepartNameById(pid);

		try {
			final String fileName=LOCALDATEFORMAT.get().format(new Date());
			//QRCodeUtil.createCodeToFile(content,new File(RootPath),fileName+FileFormat);


			log.info(fileName);
			// 2. 将存储路径保存到 smartWindowsUnit
			smartWindowUnitService.save(smartWindowUnit);
			// 3. 调用  edit() 更新数据
			edit(smartWindowUnit);

			String windowsId = smartWindowUnit.getId();
			System.out.println("##################################");
			String content = "https://www.dlqjjw.com/SmartEvaluate/modules/SmartEvaluateForm?" +
												"exeDeptId="+pid+"&exeDept="+departName+
												"&windowsId="+windowsId+"&windowsName="+smartWindowUnit.getName()+
												"&personId="+""+"&personName=大厅";//exeDept主管部门名称，windowsName窗口名称，personName具体被举报人名，可删除留空判断
			QRCodeUtil.createCodeToFile(content,new File(RootPath),fileName+FileFormat);
			smartWindowUnit.setQrcode("windows/"+fileName+FileFormat);
			smartWindowUnit.setPName(departName);
			smartWindowUnitService.updateById(smartWindowUnit);

			return Result.OK("添加成功！");
		}catch (Exception e){
			return Result.error(e.getMessage());
		}
	}
	
	/**
	 *  编辑
	 *
	 * @param smartWindowUnit
	 * @return
	 */
	@AutoLog(value = "窗口单位-编辑")
	@ApiOperation(value="窗口单位-编辑", notes="窗口单位-编辑")
	@PutMapping(value = "/edit")
	public Object edit(@RequestBody SmartWindowUnit smartWindowUnit) {

		smartWindowUnitService.updateById(smartWindowUnit);

		QueryWrapper<SmartWindowPeople> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("department_id",smartWindowUnit.getId());

		List<SmartWindowPeople> people = smartWindowPersonService.list(queryWrapper);
		SmartWindowPeople smartWindowPeople = new SmartWindowPeople();
		for(SmartWindowPeople I : people){
			I.setPrincipal(smartWindowUnit.getPrincipal());
			I.setPrincipalName(smartWindowUnit.getPrincipalName());

			smartWindowPersonService.updateById(I);
		}


		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "窗口单位-通过id删除")
	@ApiOperation(value="窗口单位-通过id删除", notes="窗口单位-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		smartWindowUnitService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "窗口单位-批量删除")
	@ApiOperation(value="窗口单位-批量删除", notes="窗口单位-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartWindowUnitService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "窗口单位-通过id查询")
	@ApiOperation(value="窗口单位-通过id查询", notes="窗口单位-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartWindowUnit smartWindowUnit = smartWindowUnitService.getById(id);
		if(smartWindowUnit==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(smartWindowUnit);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param smartWindowUnit
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request,HttpServletResponse response, SmartWindowUnit smartWindowUnit) throws Exception {

		// 获取登录用户信息，可以用来查询单位部门信息
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String username = sysUser.getUsername();

// 获取用户角色
		List<String> role = sysBaseAPI.getRolesByUsername(username);
		List<SmartWindowUnit> queryList = new ArrayList<SmartWindowUnit>();

// 如果是普通用户，则只能看到自己创建的数据
		if(role.contains("CommonUser")) {
			QueryWrapper<SmartWindowUnit> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("create_by",username);
			queryList = smartWindowUnitService.list(queryWrapper);
		} else {
			// 1. 规则，下面是 以**开始
			String rule = "in";
			// 2. 查询字段
			String field = "departId";

			// 获取子单位ID
			String childrenIdString = commonService.getChildrenIdStringByOrgCode(sysUser.getOrgCode());

			HashMap<String, String[]> map = new HashMap<>(request.getParameterMap());
			// 获取请求参数中的superQueryParams
			List<String> paramsList = ParamsUtil.getSuperQueryParams(request.getParameterMap());

			// 添加额外查询条件，用于权限控制
			paramsList.add("%5B%7B%22rule%22:%22" + rule + "%22,%22type%22:%22string%22,%22dictCode%22:%22%22,%22val%22:%22"
					+ childrenIdString
					+ "%22,%22field%22:%22" + field + "%22%7D%5D");
			String[] params = new String[paramsList.size()];
			paramsList.toArray(params);
			map.put("superQueryParams", params);
			params = new String[]{"and"};
			map.put("superQueryMatchType", params);
			QueryWrapper<SmartWindowUnit> queryWrapper = QueryGenerator.initQueryWrapper(smartWindowUnit, map);

			queryList = smartWindowUnitService.list(queryWrapper);
		}


		// Step.1 组装查询条件查询数据

		//Step.2 获取导出数据
		// 过滤选中数据
		String selections = request.getParameter("selections");
		List<SmartWindowUnit> smartWindowUnitList = new ArrayList<SmartWindowUnit>();
		if(oConvertUtils.isEmpty(selections)) {
			smartWindowUnitList = queryList;
		}else {
			List<String> selectionList = Arrays.asList(selections.split(","));
			smartWindowUnitList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
		}

		// Step.3 组装pageList
		List<SmartWindowUnit> pageList = new ArrayList<SmartWindowUnit>();
		for (SmartWindowUnit main : smartWindowUnitList) {
			SmartWindowUnit vo = new SmartWindowUnit();
			BeanUtils.copyProperties(main, vo);
//			List<SmartCreateAdviceAnnex> smartCreateAdviceAnnexList = smartCreateAdviceAnnexService.selectByMainId(main.getId());
//			vo.setSmartCreateAdviceAnnexList(smartCreateAdviceAnnexList);
			pageList.add(vo);
		}

		// Step.4 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		mv.addObject(NormalExcelConstants.FILE_NAME, "制发建议表列表");
		mv.addObject(NormalExcelConstants.CLASS, SmartWindowUnit.class);
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("制发建议表数据", "导出人:"+sysUser.getRealname(), "制发建议表"));
		mv.addObject(NormalExcelConstants.DATA_LIST, pageList);

		// List深拷贝，否则返回前端会没数据
		List<SmartWindowUnit> newPageList = ObjectUtil.cloneByStream(pageList);

		baseCommonService.addExportLog(mv.getModel(), "制发建议", request , response);

		mv.addObject(NormalExcelConstants.DATA_LIST, newPageList);

		return mv;


//        return super.exportXls(request, smartWindowUnit, SmartWindowUnit.class, "窗口单位");
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
        return super.importExcel(request, response, SmartWindowUnit.class);
    }

}
