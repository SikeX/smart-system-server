package org.jeecg.modules.smart_8regulations_for_reception.controller;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.ObjectUtil;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.modules.common.service.CommonService;
import org.jeecg.modules.common.util.ParamsUtil;
import org.jeecg.modules.smart_8regulations_for_reception.entity.*;
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
import org.jeecg.modules.smart_8regulations_for_reception.vo.Smart_8regulationsForReceptionPage;
import org.jeecg.modules.smart_8regulations_for_reception.service.ISmart_8regulationsForReceptionService;
import org.jeecg.modules.smart_8regulations_for_reception.service.ISmart_8regulationsForReceptionStaffService;
import org.jeecg.modules.smart_8regulations_for_reception.service.ISmart_8regulationsForReceptiondStaffService;
import org.jeecg.modules.smart_8regulations_for_reception.service.ISmart_8regulationsForReceptionActivityService;
import org.jeecg.modules.smart_8regulations_for_reception.service.ISmart_8regulationsForReceptionAppendixService;
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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;



 /**
 * @Description: 八项规定公务接待
 * @Author: jeecg-boot
 * @Date:   2021-11-11
 * @Version: V1.0
 */
@Api(tags="八项规定公务接待")
@RestController
@RequestMapping("/smart_8regulations_for_reception/smart_8regulationsForReception")
@Slf4j
public class Smart_8regulationsForReceptionController {
	@Autowired
	private ISmart_8regulationsForReceptionService smart_8regulationsForReceptionService;
	@Autowired
	private ISmart_8regulationsForReceptionStaffService smart_8regulationsForReceptionStaffService;
	@Autowired
	private ISmart_8regulationsForReceptiondStaffService smart_8regulationsForReceptiondStaffService;
	@Autowired
	private ISmart_8regulationsForReceptionActivityService smart_8regulationsForReceptionActivityService;
	@Autowired
	private ISmart_8regulationsForReceptionAppendixService smart_8regulationsForReceptionAppendixService;

	@Autowired
	CommonService commonService;
	private BaseCommonService baseCommonService;
	private ISysBaseAPI sysBaseAPI;

	@AutoLog(value = "更新文件下载次数")
	@ApiOperation(value="更新文件下载次数", notes="更新文件下载次数")
	@PutMapping(value = "/downloadCount")
	public Result<?> edit(@RequestBody Smart_8regulationsForReceptionAppendix smart_8regulationsForReceptionAppendix) {
		Smart_8regulationsForReceptionAppendix newSmart_8regulationsForReceptionAppendix =
			smart_8regulationsForReceptionAppendixService.getById(smart_8regulationsForReceptionAppendix.getId());
	int currentCount = newSmart_8regulationsForReceptionAppendix.getDownloadTimes();
	newSmart_8regulationsForReceptionAppendix.setDownloadTimes(currentCount+1);
		smart_8regulationsForReceptionAppendixService.updateById(newSmart_8regulationsForReceptionAppendix);
	return Result.OK("更新成功!");
	}



	/**
	 * 分页列表查询
	 *
	 * @param smart_8regulationsForReception
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "八项规定公务接待-分页列表查询")
	@ApiOperation(value="八项规定公务接待-分页列表查询", notes="八项规定公务接待-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Smart_8regulationsForReception smart_8regulationsForReception,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {

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


		QueryWrapper<Smart_8regulationsForReception> queryWrapper = QueryGenerator.initQueryWrapper(smart_8regulationsForReception, req.getParameterMap());
		Page<Smart_8regulationsForReception> page = new Page<Smart_8regulationsForReception>(pageNo, pageSize);
		IPage<Smart_8regulationsForReception> pageList = smart_8regulationsForReceptionService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param smart_8regulationsForReceptionPage
	 * @return
	 */
	@AutoLog(value = "八项规定公务接待-添加")
	@ApiOperation(value="八项规定公务接待-添加", notes="八项规定公务接待-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Smart_8regulationsForReceptionPage smart_8regulationsForReceptionPage) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String orgCode = sysUser.getOrgCode();
		if ("".equals(orgCode)) {
			return Result.error("本用户没有操作权限！");
		}
		String id = smart_8regulationsForReceptionService.getDepartIdByOrgCode(orgCode);
		smart_8regulationsForReceptionPage.setDepartmentid(id);


		Smart_8regulationsForReception smart_8regulationsForReception = new Smart_8regulationsForReception();
		BeanUtils.copyProperties(smart_8regulationsForReceptionPage, smart_8regulationsForReception);
		smart_8regulationsForReceptionService.saveMain(smart_8regulationsForReception, smart_8regulationsForReceptionPage.getSmart_8regulationsForReceptionStaffList(),smart_8regulationsForReceptionPage.getSmart_8regulationsForReceptiondStaffList(),smart_8regulationsForReceptionPage.getSmart_8regulationsForReceptionActivityList(),smart_8regulationsForReceptionPage.getSmart_8regulationsForReceptionAppendixList());
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param smart_8regulationsForReceptionPage
	 * @return
	 */
	@AutoLog(value = "八项规定公务接待-编辑")
	@ApiOperation(value="八项规定公务接待-编辑", notes="八项规定公务接待-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Smart_8regulationsForReceptionPage smart_8regulationsForReceptionPage) {
		Smart_8regulationsForReception smart_8regulationsForReception = new Smart_8regulationsForReception();
		BeanUtils.copyProperties(smart_8regulationsForReceptionPage, smart_8regulationsForReception);
		Smart_8regulationsForReception smart_8regulationsForReceptionEntity = smart_8regulationsForReceptionService.getById(smart_8regulationsForReception.getId());
		if(smart_8regulationsForReceptionEntity==null) {
			return Result.error("未找到对应数据");
		}
		smart_8regulationsForReceptionService.updateMain(smart_8regulationsForReception, smart_8regulationsForReceptionPage.getSmart_8regulationsForReceptionStaffList(),smart_8regulationsForReceptionPage.getSmart_8regulationsForReceptiondStaffList(),smart_8regulationsForReceptionPage.getSmart_8regulationsForReceptionActivityList(),smart_8regulationsForReceptionPage.getSmart_8regulationsForReceptionAppendixList());
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "八项规定公务接待-通过id删除")
	@ApiOperation(value="八项规定公务接待-通过id删除", notes="八项规定公务接待-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		smart_8regulationsForReceptionService.delMain(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "八项规定公务接待-批量删除")
	@ApiOperation(value="八项规定公务接待-批量删除", notes="八项规定公务接待-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smart_8regulationsForReceptionService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "八项规定公务接待-通过id查询")
	@ApiOperation(value="八项规定公务接待-通过id查询", notes="八项规定公务接待-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Smart_8regulationsForReception smart_8regulationsForReception = smart_8regulationsForReceptionService.getById(id);
		if(smart_8regulationsForReception==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(smart_8regulationsForReception);

	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "八项规定公务接待人员信息通过主表ID查询")
	@ApiOperation(value="八项规定公务接待人员信息主表ID查询", notes="八项规定公务接待人员信息-通主表ID查询")
	@GetMapping(value = "/querySmart_8regulationsForReceptionStaffByMainId")
	public Result<?> querySmart_8regulationsForReceptionStaffListByMainId(@RequestParam(name="id",required=true) String id) {
		List<Smart_8regulationsForReceptionStaff> smart_8regulationsForReceptionStaffList = smart_8regulationsForReceptionStaffService.selectByMainId(id);
		return Result.OK(smart_8regulationsForReceptionStaffList);
	}
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "八项规定公务接待陪同人员信息通过主表ID查询")
	@ApiOperation(value="八项规定公务接待陪同人员信息主表ID查询", notes="八项规定公务接待陪同人员信息-通主表ID查询")
	@GetMapping(value = "/querySmart_8regulationsForReceptiondStaffByMainId")
	public Result<?> querySmart_8regulationsForReceptiondStaffListByMainId(@RequestParam(name="id",required=true) String id) {
		List<Smart_8regulationsForReceptiondStaff> smart_8regulationsForReceptiondStaffList = smart_8regulationsForReceptiondStaffService.selectByMainId(id);
		return Result.OK(smart_8regulationsForReceptiondStaffList);
	}
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "八项规定公务接待公务活动项目通过主表ID查询")
	@ApiOperation(value="八项规定公务接待公务活动项目主表ID查询", notes="八项规定公务接待公务活动项目-通主表ID查询")
	@GetMapping(value = "/querySmart_8regulationsForReceptionActivityByMainId")
	public Result<?> querySmart_8regulationsForReceptionActivityListByMainId(@RequestParam(name="id",required=true) String id) {
		List<Smart_8regulationsForReceptionActivity> smart_8regulationsForReceptionActivityList = smart_8regulationsForReceptionActivityService.selectByMainId(id);
		return Result.OK(smart_8regulationsForReceptionActivityList);
	}
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "八项规定公务接待信息附件表通过主表ID查询")
	@ApiOperation(value="八项规定公务接待信息附件表主表ID查询", notes="八项规定公务接待信息附件表-通主表ID查询")
	@GetMapping(value = "/querySmart_8regulationsForReceptionAppendixByMainId")
	public Result<?> querySmart_8regulationsForReceptionAppendixListByMainId(@RequestParam(name="id",required=true) String id) {
		List<Smart_8regulationsForReceptionAppendix> smart_8regulationsForReceptionAppendixList = smart_8regulationsForReceptionAppendixService.selectByMainId(id);
		return Result.OK(smart_8regulationsForReceptionAppendixList);
	}

    /**
    * 导出excel
    *
    * @param req
    * @param smart_8regulationsForReception
    */
    @RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(HttpServletRequest req,HttpServletResponse response, Smart_8regulationsForReception smart_8regulationsForReception) throws Exception {

		// 获取登录用户信息，可以用来查询单位部门信息
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		String username = sysUser.getUsername();

// 获取用户角色
		List<String> role = sysBaseAPI.getRolesByUsername(username);

		List<Smart_8regulationsForReception> queryList = new ArrayList<Smart_8regulationsForReception>();


// 如果是普通用户，则只能看到自己创建的数据
		if(role.contains("CommonUser")) {
			QueryWrapper<Smart_8regulationsForReception> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("create_by",username);
			queryList = smart_8regulationsForReceptionService.list(queryWrapper);
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
			QueryWrapper<Smart_8regulationsForReception> queryWrapper = QueryGenerator.initQueryWrapper(smart_8regulationsForReception, map);

			queryList = smart_8regulationsForReceptionService.list(queryWrapper);
		}
		// Step.1 组装查询条件查询数据

		//Step.2 获取导出数据
		// 过滤选中数据
		String selections = req.getParameter("selections");
		List<Smart_8regulationsForReception> smart_8regulationsForReceptionList = new ArrayList<Smart_8regulationsForReception>();
		if(oConvertUtils.isEmpty(selections)) {
			smart_8regulationsForReceptionList = queryList;
		}else {
			List<String> selectionList = Arrays.asList(selections.split(","));
			smart_8regulationsForReceptionList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
		}

// Step.3 组装pageList
		List<Smart_8regulationsForReceptionPage> pageList = new ArrayList<Smart_8regulationsForReceptionPage>();
		for (Smart_8regulationsForReception main : smart_8regulationsForReceptionList) {
			Smart_8regulationsForReceptionPage vo = new Smart_8regulationsForReceptionPage();
			BeanUtils.copyProperties(main, vo);
			List<Smart_8regulationsForReceptionStaff> smart_8regulationsForReceptionStaffList = smart_8regulationsForReceptionStaffService.selectByMainId(main.getId());
			vo.setSmart_8regulationsForReceptionStaffList(smart_8regulationsForReceptionStaffList);
			List<Smart_8regulationsForReceptiondStaff> smart_8regulationsForReceptiondStaffList = smart_8regulationsForReceptiondStaffService.selectByMainId(main.getId());
			vo.setSmart_8regulationsForReceptiondStaffList(smart_8regulationsForReceptiondStaffList);
			List<Smart_8regulationsForReceptionActivity> smart_8regulationsForReceptionActivityList = smart_8regulationsForReceptionActivityService.selectByMainId(main.getId());
			vo.setSmart_8regulationsForReceptionActivityList(smart_8regulationsForReceptionActivityList);
			List<Smart_8regulationsForReceptionAppendix> smart_8regulationsForReceptionAppendixList = smart_8regulationsForReceptionAppendixService.selectByMainId(main.getId());
			vo.setSmart_8regulationsForReceptionAppendixList(smart_8regulationsForReceptionAppendixList);
			pageList.add(vo);
		}

		//

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "八项规定公务接待列表");
      mv.addObject(NormalExcelConstants.CLASS, Smart_8regulationsForReceptionPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("八项规定公务接待数据", "导出人:"+sysUser.getRealname(), "八项规定公务接待"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);

		// List深拷贝，否则返回前端会没数据
		List<Smart_8regulationsForReceptionPage> newPageList = ObjectUtil.cloneByStream(pageList);

		baseCommonService.addExportLog(mv.getModel(), "八项规定公务接待", req, response);

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
              List<Smart_8regulationsForReceptionPage> list = ExcelImportUtil.importExcel(file.getInputStream(), Smart_8regulationsForReceptionPage.class, params);
              for (Smart_8regulationsForReceptionPage page : list) {
                  Smart_8regulationsForReception po = new Smart_8regulationsForReception();
                  BeanUtils.copyProperties(page, po);
                  smart_8regulationsForReceptionService.saveMain(po, page.getSmart_8regulationsForReceptionStaffList(),page.getSmart_8regulationsForReceptiondStaffList(),page.getSmart_8regulationsForReceptionActivityList(),page.getSmart_8regulationsForReceptionAppendixList());
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

}
