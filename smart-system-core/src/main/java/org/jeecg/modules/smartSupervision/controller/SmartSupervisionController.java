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
import org.jeecg.modules.constant.VerifyConstant;
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
 * @Description: ???????????????????????????
 * @Author: jeecg-boot
 * @Date:   2021-11-10
 * @Version: V1.0
 */
@Api(tags="???????????????????????????")
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

	public String verifyType = "????????????";
	
	/**
	 * ??????????????????
	 *
	 * @param smartSupervision
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "???????????????????????????-??????????????????")
	@ApiOperation(value="???????????????????????????-??????????????????", notes="???????????????????????????-??????????????????")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartSupervision smartSupervision,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		// ???????????????????????????????????????????????????????????????
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		String username = sysUser.getUsername();

		// ??????????????????
		List<String> role = sysBaseAPI.getRolesByUsername(username);

		Page<SmartSupervision> page = new Page<SmartSupervision>(pageNo, pageSize);

		// ????????????????????????????????????????????????????????????
		if(role.contains("CommonUser")) {
			QueryWrapper<SmartSupervision> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("create_by",username);
			IPage<SmartSupervision> pageList = smartSupervisionService.page(page, queryWrapper);
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
	 *   ??????
	 *
	 * @param smartSupervisionPage
	 * @return
	 */
	@AutoLog(value = "???????????????????????????-??????")
	@ApiOperation(value="???????????????????????????-??????", notes="???????????????????????????-??????")
	@Transactional
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SmartSupervision smartSupervision) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		String orgCode = sysUser.getOrgCode();
		if ("".equals(orgCode)) {
			return Result.error("??????????????????????????????");
		}
		String id = commonService.getDepartIdByOrgCode(orgCode);
		if (id == null) {
			return Result.error("?????????????????????");
		}
		smartSupervision.setDepartId(id);
		smartSupervisionService.save(smartSupervision);

		Boolean isVerify = smartVerifyTypeService.getIsVerifyStatusByType(verifyType);
		if (isVerify) {
			// ????????????????????????????????????????????????????????????
			smartSupervision.setVerifyStatus(VerifyConstant.VERIFY_STATUS_TOSUBMIT);
		} else {
			// ???????????????????????????
			smartSupervision.setVerifyStatus(VerifyConstant.VERIFY_STATUS_FREE);
		}

		smartSupervisionService.updateById(smartSupervision);


		return Result.OK("???????????????");
	}
	
	/**
	 *  ??????
	 *
	 * @param smartSupervisionPage
	 * @return
	 */
	@AutoLog(value = "???????????????????????????-??????")
	@ApiOperation(value="???????????????????????????-??????", notes="???????????????????????????-??????")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SmartSupervisionPage smartSupervisionPage) {
		SmartSupervision smartSupervision = new SmartSupervision();
		BeanUtils.copyProperties(smartSupervisionPage, smartSupervision);
		SmartSupervision smartSupervisionEntity = smartSupervisionService.getById(smartSupervision.getId());
		if(smartSupervisionEntity==null) {
			return Result.error("?????????????????????");
		}
//		smartSupervision.setDepartId(null);
//		smartSupervision.setCreateTime(null);
		if(!(smartSupervisionEntity.getVerifyStatus().equals(VerifyConstant.VERIFY_STATUS_TOSUBMIT) || smartSupervisionEntity.getVerifyStatus().equals(VerifyConstant.VERIFY_STATUS_FREE))){
			return Result.error("??????????????????????????????????????????");
		}
		smartSupervisionService.updateById(smartSupervision);
		return Result.OK("????????????!");
	}

	 @AutoLog(value = "????????????-????????????")
	 @ApiOperation(value = "????????????-????????????", notes = "????????????-????????????")
	 @PostMapping(value = "/submitVerify")
	 public Result<?> submitVerify(@RequestBody SmartSupervision smartSupervision) {
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		 String orgCode = sysUser.getOrgCode();
		 if ("".equals(orgCode)) {
			 return Result.error("??????????????????????????????");
		 }

		 if(!smartVerifyTypeService.getIsVerifyStatusByType(verifyType)){
			 return Result.error("????????????????????????????????????");
		 }

		 SmartSupervision smartSupervisionEntity = smartSupervisionService.getById(smartSupervision.getId());

		 String recordId = smartSupervisionEntity.getId();
		 smartVerify.addVerifyRecord(recordId, verifyType);
		 smartSupervisionEntity.setVerifyStatus(smartVerify.getFlowStatusById(recordId).toString());
		 smartSupervisionService.updateById(smartSupervisionEntity);

		 return Result.OK("???????????????");
	 }
	
	/**
	 *   ??????id??????
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "???????????????????????????-??????id??????")
	@ApiOperation(value="???????????????????????????-??????id??????", notes="???????????????????????????-??????id??????")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		smartSupervisionService.removeById(id);
		return Result.OK("????????????!");
	}
	
	/**
	 *  ????????????
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "???????????????????????????-????????????")
	@ApiOperation(value="???????????????????????????-????????????", notes="???????????????????????????-????????????")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartSupervisionService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("?????????????????????");
	}
	
	/**
	 * ??????id??????
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "???????????????????????????-??????id??????")
	@ApiOperation(value="???????????????????????????-??????id??????", notes="???????????????????????????-??????id??????")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartSupervision smartSupervision = smartSupervisionService.getById(id);
		if(smartSupervision==null) {
			return Result.error("?????????????????????");
		}
		return Result.OK(smartSupervision);

	}

	 /**
	  * ??????excel
	  *
	  * @param req
	  * @param smartCreateAdvice
	  */
	 @RequestMapping(value = "/exportXls")
	 public ModelAndView exportXls(HttpServletRequest req,
								   HttpServletResponse response, SmartSupervision smartSupervision) throws Exception {

// ???????????????????????????????????????????????????????????????
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		 String username = sysUser.getUsername();

// ??????????????????
		 List<String> role = sysBaseAPI.getRolesByUsername(username);

		 List<SmartSupervision> queryList = new ArrayList<SmartSupervision>();


// ????????????????????????????????????????????????????????????
		 if(role.contains("CommonUser")) {
			 QueryWrapper<SmartSupervision> queryWrapper = new QueryWrapper<>();
			 queryWrapper.eq("create_by",username);
			 queryList = smartSupervisionService.list(queryWrapper);
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
			 QueryWrapper<SmartSupervision> queryWrapper = QueryGenerator.initQueryWrapper(smartSupervision, map);

			 queryList = smartSupervisionService.list(queryWrapper);
		 }


		 // Step.1 ??????????????????????????????

		 //Step.2 ??????????????????
		 // ??????????????????
		 String selections = req.getParameter("selections");
		 List<SmartSupervision> smartSupervisionList = new ArrayList<SmartSupervision>();
		 if(oConvertUtils.isEmpty(selections)) {
			 smartSupervisionList = queryList;
		 }else {
			 List<String> selectionList = Arrays.asList(selections.split(","));
			 smartSupervisionList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
		 }

		 // Step.3 ??????pageList
		 List<SmartSupervisionPage> pageList = new ArrayList<SmartSupervisionPage>();
		 for (SmartSupervision main : smartSupervisionList) {
			 SmartSupervisionPage vo = new SmartSupervisionPage();
			 BeanUtils.copyProperties(main, vo);
			 List<SmartSupervisionAnnex> smartSupervisionAnnexList = smartSupervisionAnnexService.selectByMainId(main.getId());
			 vo.setSmartSupervisionAnnexList(smartSupervisionAnnexList);
			 pageList.add(vo);
		 }

		 // Step.4 AutoPoi ??????Excel
		 ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		 mv.addObject(NormalExcelConstants.FILE_NAME, "??????????????????");
		 mv.addObject(NormalExcelConstants.CLASS, SmartSupervisionPage.class);
		 mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("?????????????????????", "?????????:"+sysUser.getRealname(), "???????????????"));
		 mv.addObject(NormalExcelConstants.DATA_LIST, pageList);

		 // List??????????????????????????????????????????
		 List<SmartSupervisionPage> newPageList = ObjectUtil.cloneByStream(pageList);

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
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          MultipartFile file = entity.getValue();// ????????????????????????
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
              return Result.OK("?????????????????????????????????:" + list.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("??????????????????:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.OK("?????????????????????");
    }

	 @AutoLog(value = "????????????????????????")
	 @ApiOperation(value="????????????????????????", notes="????????????????????????")
	 @PutMapping(value = "/downloadCount")
	 public Result<?> edit(@RequestBody SmartSupervisionAnnex smartSupervisionAnnex) {
		 SmartSupervisionAnnex newSmartSupervisionAnnex = smartSupervisionAnnexService.getById(smartSupervisionAnnex.getId());
		 int currentCount = newSmartSupervisionAnnex.getDownloadCount();
		 newSmartSupervisionAnnex.setDownloadCount(currentCount+1);
		 smartSupervisionAnnexService.updateById(newSmartSupervisionAnnex);
		 return Result.OK("????????????!");
	 }

}
