package org.jeecg.modules.smartTripleImportanceOneGreatness.controller;

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
//import org.jeecg.modules.smartTripleImportanceOneGreatness.service.ISmartTripleImportanceOneGreatnessDescriptionService;
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
import org.jeecg.modules.smartTripleImportanceOneGreatness.entity.SmartTripleImportanceOneGreatnessPacca;
import org.jeecg.modules.smartTripleImportanceOneGreatness.entity.SmartTripleImportanceOneGreatness;
import org.jeecg.modules.smartTripleImportanceOneGreatness.vo.SmartTripleImportanceOneGreatnessPage;
import org.jeecg.modules.smartTripleImportanceOneGreatness.service.ISmartTripleImportanceOneGreatnessService;
import org.jeecg.modules.smartTripleImportanceOneGreatness.service.ISmartTripleImportanceOneGreatnessPaccaService;

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
 * @Description: 三重一大表
 * @Author: jeecg-boot
 * @Date:   2021-11-13
 * @Version: V1.0
 */
@Api(tags="三重一大表")
@RestController
@RequestMapping("/smartTripleImportanceOneGreatness/smartTripleImportanceOneGreatness")
@Slf4j
public class SmartTripleImportanceOneGreatnessController {
	@Autowired
	private ISmartTripleImportanceOneGreatnessService smartTripleImportanceOneGreatnessService;
	@Autowired
	private ISmartTripleImportanceOneGreatnessPaccaService smartTripleImportanceOneGreatnessPaccaService;
	/*@Autowired
	private ISmartTripleImportanceOneGreatnessDescriptionService smartTripleImportanceOneGreatnessDescriptionService;*/
	@Autowired
	private CommonService commonService;
	@Autowired
	private SmartVerify smartVerify;
	public String verifyType="三重一大";
	@Autowired
	private ISmartVerifyTypeService smartVerifyTypeService;
	@Autowired
	private ISysBaseAPI sysBaseAPI;
	@Autowired
	private BaseCommonService baseCommonService;

	/**
	 * 分页列表查询
	 *
	 * @param smartTripleImportanceOneGreatness
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "三重一大表-分页列表查询")
	@ApiOperation(value="三重一大表-分页列表查询", notes="三重一大表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartTripleImportanceOneGreatness smartTripleImportanceOneGreatness,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		// 获取登录用户信息，可以用来查询单位部门信息
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		String username = sysUser.getUsername();

		//获取用户角色
		List<String> role = sysBaseAPI.getRolesByUsername(username);
		if (role.contains("CommonUser")) {
			QueryWrapper<SmartTripleImportanceOneGreatness> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("create_by", username);
			Page<SmartTripleImportanceOneGreatness> page
					= new Page<SmartTripleImportanceOneGreatness>(pageNo, pageSize);
			IPage<SmartTripleImportanceOneGreatness> pageList
					= smartTripleImportanceOneGreatnessService.page(page, queryWrapper);
			return Result.OK(pageList);
		} else {
			// 1. 规则，下面是 以**开始
			String rule = "in";
			// 2. 查询字段
			String field = "documentid";


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

			QueryWrapper<SmartTripleImportanceOneGreatness> queryWrapper = QueryGenerator.initQueryWrapper(smartTripleImportanceOneGreatness, map);
			Page<SmartTripleImportanceOneGreatness> page = new Page<SmartTripleImportanceOneGreatness>(pageNo, pageSize);
			IPage<SmartTripleImportanceOneGreatness> pageList = smartTripleImportanceOneGreatnessService.page(page, queryWrapper);
//			// 请同步修改edit函数中，将departId变为null，不然会更新成名称
//			List<String> departIds = pageList.getRecords().stream().map(SmartTripleImportanceOneGreatness::getDocumentid).collect(Collectors.toList());
//			if (departIds != null && departIds.size() > 0) {
//				Map<String, String> useDepNames = commonService.getDepNamesByIds(departIds);
//				pageList.getRecords().forEach(item -> {
//					item.setDocumentid(useDepNames.get(item.getDocumentid()));
//				});
//			}
			return Result.OK(pageList);
		}
	}
	/**
	 *   添加
	 *
	 * @param smartTripleImportanceOneGreatnessPage
	 * @return
	 */
	@AutoLog(value = "三重一大表-添加")
	@ApiOperation(value="三重一大表-添加", notes="三重一大表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SmartTripleImportanceOneGreatnessPage smartTripleImportanceOneGreatnessPage) {

		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String orgCode = sysUser.getOrgCode();
		if ("".equals(orgCode)) {
			return Result.error("本用户没有操作权限！");
		}
		String id = commonService.getDepartIdByOrgCode(orgCode);
		if (id == null) {
			return Result.error("没有找到部门！");
		}
		SmartTripleImportanceOneGreatness smartTripleImportanceOneGreatness = new SmartTripleImportanceOneGreatness();

		BeanUtils.copyProperties(smartTripleImportanceOneGreatnessPage, smartTripleImportanceOneGreatness);
		smartTripleImportanceOneGreatness.setDocumentid(sysUser.getDepartId());


		//smartVerify.addVerifyRecord(smartTripleImportanceOneGreatness.getId(),verifyType);

		Boolean isVerify = smartVerifyTypeService.getIsVerifyStatusByType(verifyType);
		if(isVerify){
			smartTripleImportanceOneGreatnessService.saveMain(smartTripleImportanceOneGreatness,
					smartTripleImportanceOneGreatnessPage.getSmartTripleImportanceOneGreatnessPaccaList());
			String recordId = smartTripleImportanceOneGreatness.getId();
		    smartVerify.addVerifyRecord(recordId,verifyType);
		    smartTripleImportanceOneGreatness.setVerifyStatus(smartVerify.getFlowStatusById(recordId).toString());
		    smartTripleImportanceOneGreatnessService.updateById(smartTripleImportanceOneGreatness); }
		    else {
		    	// 设置审核状态为免审
			 smartTripleImportanceOneGreatness.setVerifyStatus("3");
			// 直接添加，不走审核流程
			smartTripleImportanceOneGreatnessService.saveMain(smartTripleImportanceOneGreatness,
					smartTripleImportanceOneGreatnessPage.getSmartTripleImportanceOneGreatnessPaccaList());
		    }

		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param smartTripleImportanceOneGreatnessPage
	 * @return
	 */
	@AutoLog(value = "三重一大表-编辑")
	@ApiOperation(value="三重一大表-编辑", notes="三重一大表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SmartTripleImportanceOneGreatnessPage smartTripleImportanceOneGreatnessPage) {
		SmartTripleImportanceOneGreatness smartTripleImportanceOneGreatness = new SmartTripleImportanceOneGreatness();
		BeanUtils.copyProperties(smartTripleImportanceOneGreatnessPage, smartTripleImportanceOneGreatness);
		SmartTripleImportanceOneGreatness smartTripleImportanceOneGreatnessEntity = smartTripleImportanceOneGreatnessService.getById(smartTripleImportanceOneGreatness.getId());
		if(smartTripleImportanceOneGreatnessEntity==null) {
			return Result.error("未找到对应数据");
		}
		smartTripleImportanceOneGreatness.setDocumentid(null);
		smartTripleImportanceOneGreatness.setCreateTime(null);

		smartTripleImportanceOneGreatnessService.updateMain(smartTripleImportanceOneGreatness,
				smartTripleImportanceOneGreatnessPage.getSmartTripleImportanceOneGreatnessPaccaList());
		return Result.OK("编辑成功!");
	}

//	 @AutoLog(value = "更新文件下载次数")
//	 @ApiOperation(value="更新文件下载次数", notes="更新文件下载次数")
//	 @PutMapping(value = "/downloadTimes")
//	 public Result<?> edit(@RequestBody SmartTripleImportanceOneGreatnessDescription
//								   smartTripleImportanceOneGreatnessDescription) {
//		 SmartTripleImportanceOneGreatnessDescription
//				 newSmartTripleImportanceOneGreatnessDescription
//				 =smartTripleImportanceOneGreatnessDescriptionService.getById(smartTripleImportanceOneGreatnessDescription.getId());
//		 int currentCount = newSmartTripleImportanceOneGreatnessDescription.getDownloadTimes();
//		 newSmartTripleImportanceOneGreatnessDescription.setDownloadTimes(currentCount+1);
//		 smartTripleImportanceOneGreatnessDescriptionService.updateById(newSmartTripleImportanceOneGreatnessDescription);
//
//		 return Result.OK("更新成功!");
//	 }

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "三重一大表-通过id删除")
	@ApiOperation(value="三重一大表-通过id删除", notes="三重一大表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		smartTripleImportanceOneGreatnessService.delMain(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "三重一大表-批量删除")
	@ApiOperation(value="三重一大表-批量删除", notes="三重一大表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartTripleImportanceOneGreatnessService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "三重一大表-通过id查询")
	@ApiOperation(value="三重一大表-通过id查询", notes="三重一大表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartTripleImportanceOneGreatness smartTripleImportanceOneGreatness = smartTripleImportanceOneGreatnessService.getById(id);
		if(smartTripleImportanceOneGreatness==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(smartTripleImportanceOneGreatness);

	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "三重一大参会人员表通过主表ID查询")
	@ApiOperation(value="三重一大参会人员表主表ID查询", notes="三重一大参会人员表-通主表ID查询")
	@GetMapping(value = "/querySmartTripleImportanceOneGreatnessPaccaByMainId")
	public Result<?> querySmartTripleImportanceOneGreatnessPaccaListByMainId(@RequestParam(name="id",required=true) String id) {
		List<SmartTripleImportanceOneGreatnessPacca> smartTripleImportanceOneGreatnessPaccaList = smartTripleImportanceOneGreatnessPaccaService.selectByMainId(id);
		return Result.OK(smartTripleImportanceOneGreatnessPaccaList);
	}
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 *//*
	@AutoLog(value = "三重一大附件表通过主表ID查询")
	@ApiOperation(value="三重一大附件表主表ID查询", notes="三重一大附件表-通主表ID查询")
	@GetMapping(value = "/querySmartTripleImportanceOneGreatnessDescriptionByMainId")
	public Result<?> querySmartTripleImportanceOneGreatnessDescriptionListByMainId(@RequestParam(name="id",required=true) String id) {
		List<SmartTripleImportanceOneGreatnessDescription> smartTripleImportanceOneGreatnessDescriptionList = smartTripleImportanceOneGreatnessDescriptionService.selectByMainId(id);
		return Result.OK(smartTripleImportanceOneGreatnessDescriptionList);
	}*/

    /**
    * 导出excel
    *
    * @param req
    * @param smartTripleImportanceOneGreatness
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest req,HttpServletResponse response,
								  SmartTripleImportanceOneGreatness smartTripleImportanceOneGreatness) throws IOException {

       // 获取登录用户信息，可以用来查询单位部门信息
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		String username = sysUser.getUsername();

		//获取用户角色
		List<String> role = sysBaseAPI.getRolesByUsername(username);
		List<SmartTripleImportanceOneGreatness> queryList=new ArrayList<SmartTripleImportanceOneGreatness>();

		if (role.contains("CommonUser")) {
			QueryWrapper<SmartTripleImportanceOneGreatness> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("create_by", username);
			queryList=smartTripleImportanceOneGreatnessService.list(queryWrapper);

		} else {
			// 1. 规则，下面是 以**开始
			String rule = "in";
			// 2. 查询字段
			String field = "documentid";


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

			QueryWrapper<SmartTripleImportanceOneGreatness> queryWrapper = QueryGenerator.initQueryWrapper(smartTripleImportanceOneGreatness, map);
			queryList=smartTripleImportanceOneGreatnessService.list(queryWrapper);
		}
      // Step.1 组装查询条件查询数据

      //Step.2 获取导出数据

      // 过滤选中数据
      String selections = req.getParameter("selections");
      List<SmartTripleImportanceOneGreatness> smartTripleImportanceOneGreatnessList = new ArrayList<SmartTripleImportanceOneGreatness>();
      if(oConvertUtils.isEmpty(selections)) {
          smartTripleImportanceOneGreatnessList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          smartTripleImportanceOneGreatnessList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<SmartTripleImportanceOneGreatnessPage> pageList = new ArrayList<SmartTripleImportanceOneGreatnessPage>();
      for (SmartTripleImportanceOneGreatness main : smartTripleImportanceOneGreatnessList) {
          SmartTripleImportanceOneGreatnessPage vo = new SmartTripleImportanceOneGreatnessPage();
          BeanUtils.copyProperties(main, vo);
          List<SmartTripleImportanceOneGreatnessPacca> smartTripleImportanceOneGreatnessPaccaList = smartTripleImportanceOneGreatnessPaccaService.selectByMainId(main.getId());
          vo.setSmartTripleImportanceOneGreatnessPaccaList(smartTripleImportanceOneGreatnessPaccaList);
          /*List<SmartTripleImportanceOneGreatnessDescription> smartTripleImportanceOneGreatnessDescriptionList = smartTripleImportanceOneGreatnessDescriptionService.selectByMainId(main.getId());
          vo.setSmartTripleImportanceOneGreatnessDescriptionList(smartTripleImportanceOneGreatnessDescriptionList);*/
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "三重一大表列表");
      mv.addObject(NormalExcelConstants.CLASS, SmartTripleImportanceOneGreatnessPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("三重一大表数据", "导出人:"+sysUser.getRealname(), "三重一大表"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      // List深拷贝，否则返回前端会没数据
		List<SmartTripleImportanceOneGreatnessPage> newPageList = ObjectUtil.cloneByStream(pageList);
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
              List<SmartTripleImportanceOneGreatnessPage> list = ExcelImportUtil.importExcel(file.getInputStream(), SmartTripleImportanceOneGreatnessPage.class, params);
              for (SmartTripleImportanceOneGreatnessPage page : list) {
                  SmartTripleImportanceOneGreatness po = new SmartTripleImportanceOneGreatness();
                  BeanUtils.copyProperties(page, po);
                  smartTripleImportanceOneGreatnessService.saveMain(po, page.getSmartTripleImportanceOneGreatnessPaccaList());
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
