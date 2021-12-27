package org.jeecg.modules.smartSupervision.controller;

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
import org.jeecg.modules.tasks.smartVerifyTask.service.SmartVerify;
import org.jeecg.modules.tasks.taskType.entity.SmartVerifyType;
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
import org.jeecg.modules.smartSupervision.entity.SmartSupervisionAnnex;
import org.jeecg.modules.smartSupervision.entity.SmartSupervision;
import org.jeecg.modules.smartSupervision.vo.SmartSupervisionPage;
import org.jeecg.modules.smartSupervision.service.ISmartSupervisionService;
import org.jeecg.modules.smartSupervision.service.ISmartSupervisionAnnexService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
 * @Description: 八项规定监督检查表
 * @Author: jeecg-boot
 * @Date:   2021-11-10
 * @Version: V1.0
 */
@Api(tags="八项规定监督检查表")
@RestController
@RequestMapping("/smartSupervision/smartSupervision")
@Slf4j
public class SmartSupervisionController {
	@Autowired
	private ISmartSupervisionService smartSupervisionService;
	@Autowired
	private ISmartSupervisionAnnexService smartSupervisionAnnexService;
	@Autowired
	private ISysBaseAPI sysBaseAPI;
	@Autowired
	private CommonService commonService;
	@Autowired
	private SmartVerify smartVerify;
	@Autowired
	private ISmartVerifyTypeService smartVerifyTypeService;
	@Autowired
	private BaseCommonService baseCommonService;

	public String verifyType = "监督检查";
	
	/**
	 * 分页列表查询
	 *
	 * @param smartSupervision
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "八项规定监督检查表-分页列表查询")
	@ApiOperation(value="八项规定监督检查表-分页列表查询", notes="八项规定监督检查表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartSupervision smartSupervision,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		// 获取登录用户信息，可以用来查询单位部门信息
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		String username = sysUser.getUsername();

		// 获取用户角色
		List<String> role = sysBaseAPI.getRolesByUsername(username);

		Page<SmartSupervision> page = new Page<SmartSupervision>(pageNo, pageSize);

		// 如果是普通用户，则只能看到自己创建的数据
		if(role.contains("CommonUser")) {
			QueryWrapper<SmartSupervision> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("create_by",username);
			IPage<SmartSupervision> pageList = smartSupervisionService.page(page, queryWrapper);
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
			QueryWrapper<SmartSupervision> queryWrapper = QueryGenerator.initQueryWrapper(smartSupervision, map);

			IPage<SmartSupervision> pageList = smartSupervisionService.page(page, queryWrapper);
			List<String> departIds = pageList.getRecords().stream().map(SmartSupervision::getDepartId).collect(Collectors.toList());
			if (departIds != null && departIds.size() > 0) {
				Map<String, String> useDepNames = commonService.getDepNamesByIds(departIds);
				pageList.getRecords().forEach(item -> {
					item.setDepartId(useDepNames.get(item.getDepartId()));
				});
			}
			return Result.OK(pageList);
		}
	}
	
	/**
	 *   添加
	 *
	 * @param smartSupervisionPage
	 * @return
	 */
	@AutoLog(value = "八项规定监督检查表-添加")
	@ApiOperation(value="八项规定监督检查表-添加", notes="八项规定监督检查表-添加")
	@Transactional
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SmartSupervisionPage smartSupervisionPage) {

		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String orgCode = sysUser.getOrgCode();
		log.info(orgCode);
		if("".equals(orgCode)) {
			return Result.error("本用户没有操作权限");
		}

		String id = sysBaseAPI.getDepartIdsByOrgCode(orgCode);
//		log.info(id);
		smartSupervisionPage.setDepartId(id);
		SmartSupervision smartSupervision = new SmartSupervision();
		BeanUtils.copyProperties(smartSupervisionPage, smartSupervision);
//		log.info(smartSupervision.getId());

		Boolean isVerify = smartVerifyTypeService.getIsVerifyStatusByType(verifyType);
		if(isVerify){
			smartSupervisionService.saveMain(smartSupervision, smartSupervisionPage.getSmartSupervisionAnnexList());
			String recordId = smartSupervision.getId();
			log.info("recordId is"+recordId);
			smartVerify.addVerifyRecord(recordId,verifyType);
			smartSupervision.setVerifyStatus(smartVerify.getFlowStatusById(recordId).toString());
			smartSupervisionService.updateById(smartSupervision);
		} else {
			// 设置审核状态为免审
			smartSupervision.setVerifyStatus("3");
			// 直接添加，不走审核流程
			smartSupervisionService.saveMain(smartSupervision, smartSupervisionPage.getSmartSupervisionAnnexList());
		}

		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param smartSupervisionPage
	 * @return
	 */
	@AutoLog(value = "八项规定监督检查表-编辑")
	@ApiOperation(value="八项规定监督检查表-编辑", notes="八项规定监督检查表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SmartSupervisionPage smartSupervisionPage) {
		SmartSupervision smartSupervision = new SmartSupervision();
		BeanUtils.copyProperties(smartSupervisionPage, smartSupervision);
		SmartSupervision smartSupervisionEntity = smartSupervisionService.getById(smartSupervision.getId());
		if(smartSupervisionEntity==null) {
			return Result.error("未找到对应数据");
		}
		smartSupervision.setDepartId(null);
		smartSupervision.setCreateTime(null);
		smartSupervisionService.updateMain(smartSupervision, smartSupervisionPage.getSmartSupervisionAnnexList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "八项规定监督检查表-通过id删除")
	@ApiOperation(value="八项规定监督检查表-通过id删除", notes="八项规定监督检查表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		smartSupervisionService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "八项规定监督检查表-批量删除")
	@ApiOperation(value="八项规定监督检查表-批量删除", notes="八项规定监督检查表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartSupervisionService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "八项规定监督检查表-通过id查询")
	@ApiOperation(value="八项规定监督检查表-通过id查询", notes="八项规定监督检查表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartSupervision smartSupervision = smartSupervisionService.getById(id);
		if(smartSupervision==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(smartSupervision);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "8项规定监督检查附件表通过主表ID查询")
	@ApiOperation(value="8项规定监督检查附件表主表ID查询", notes="8项规定监督检查附件表-通主表ID查询")
	@GetMapping(value = "/querySmartSupervisionAnnexByMainId")
	public Result<?> querySmartSupervisionAnnexListByMainId(@RequestParam(name="id",required=true) String id) {
		List<SmartSupervisionAnnex> smartSupervisionAnnexList = smartSupervisionAnnexService.selectByMainId(id);
		return Result.OK(smartSupervisionAnnexList);
	}

	 /**
	  * 导出excel
	  *
	  * @param req
	  * @param smartCreateAdvice
	  */
	 @RequestMapping(value = "/exportXls")
	 public ModelAndView exportXls(HttpServletRequest req,
								   HttpServletResponse response, SmartSupervision smartSupervision) throws Exception {

// 获取登录用户信息，可以用来查询单位部门信息
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		 String username = sysUser.getUsername();

// 获取用户角色
		 List<String> role = sysBaseAPI.getRolesByUsername(username);

		 List<SmartSupervision> queryList = new ArrayList<SmartSupervision>();


// 如果是普通用户，则只能看到自己创建的数据
		 if(role.contains("CommonUser")) {
			 QueryWrapper<SmartSupervision> queryWrapper = new QueryWrapper<>();
			 queryWrapper.eq("create_by",username);
			 queryList = smartSupervisionService.list(queryWrapper);
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
			 QueryWrapper<SmartSupervision> queryWrapper = QueryGenerator.initQueryWrapper(smartSupervision, map);

			 queryList = smartSupervisionService.list(queryWrapper);
		 }


		 // Step.1 组装查询条件查询数据

		 //Step.2 获取导出数据
		 // 过滤选中数据
		 String selections = req.getParameter("selections");
		 List<SmartSupervision> smartSupervisionList = new ArrayList<SmartSupervision>();
		 if(oConvertUtils.isEmpty(selections)) {
			 smartSupervisionList = queryList;
		 }else {
			 List<String> selectionList = Arrays.asList(selections.split(","));
			 smartSupervisionList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
		 }

		 // Step.3 组装pageList
		 List<SmartSupervisionPage> pageList = new ArrayList<SmartSupervisionPage>();
		 for (SmartSupervision main : smartSupervisionList) {
			 SmartSupervisionPage vo = new SmartSupervisionPage();
			 BeanUtils.copyProperties(main, vo);
			 List<SmartSupervisionAnnex> smartSupervisionAnnexList = smartSupervisionAnnexService.selectByMainId(main.getId());
			 vo.setSmartSupervisionAnnexList(smartSupervisionAnnexList);
			 pageList.add(vo);
		 }

		 // Step.4 AutoPoi 导出Excel
		 ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		 mv.addObject(NormalExcelConstants.FILE_NAME, "监督检查列表");
		 mv.addObject(NormalExcelConstants.CLASS, SmartSupervisionPage.class);
		 mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("监督检查表数据", "导出人:"+sysUser.getRealname(), "监督检查表"));
		 mv.addObject(NormalExcelConstants.DATA_LIST, pageList);

		 // List深拷贝，否则返回前端会没数据
		 List<SmartSupervisionPage> newPageList = ObjectUtil.cloneByStream(pageList);

		 baseCommonService.addExportLog(mv.getModel(), "监督检查", req, response);

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
              List<SmartSupervisionPage> list = ExcelImportUtil.importExcel(file.getInputStream(), SmartSupervisionPage.class, params);
              for (SmartSupervisionPage page : list) {
                  SmartSupervision po = new SmartSupervision();
                  BeanUtils.copyProperties(page, po);
                  smartSupervisionService.saveMain(po, page.getSmartSupervisionAnnexList());
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

	 @AutoLog(value = "更新文件下载次数")
	 @ApiOperation(value="更新文件下载次数", notes="更新文件下载次数")
	 @PutMapping(value = "/downloadCount")
	 public Result<?> edit(@RequestBody SmartSupervisionAnnex smartSupervisionAnnex) {
		 SmartSupervisionAnnex newSmartSupervisionAnnex = smartSupervisionAnnexService.getById(smartSupervisionAnnex.getId());
		 int currentCount = newSmartSupervisionAnnex.getDownloadCount();
		 newSmartSupervisionAnnex.setDownloadCount(currentCount+1);
		 smartSupervisionAnnexService.updateById(newSmartSupervisionAnnex);
		 return Result.OK("更新成功!");
	 }

}
