package org.jeecg.modules.wePower.smartPublicityProject.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.modules.wePower.smartPublicityPower.entity.SmartPublicityPower;
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
import org.jeecg.modules.wePower.smartPublicityProject.entity.SmartPublicityProjectVerify;
import org.jeecg.modules.wePower.smartPublicityProject.entity.SmartPublicityProject;
import org.jeecg.modules.wePower.smartPublicityProject.vo.SmartPublicityProjectPage;
import org.jeecg.modules.wePower.smartPublicityProject.service.ISmartPublicityProjectService;
import org.jeecg.modules.wePower.smartPublicityProject.service.ISmartPublicityProjectVerifyService;
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
 * @Description: ????????????
 * @Author: jeecg-boot
 * @Date:   2022-03-09
 * @Version: V1.0
 */
@Api(tags="????????????")
@RestController
@RequestMapping("/smartPublicityProject/smartPublicityProject")
@Slf4j
public class SmartPublicityProjectController {
	@Autowired
	private ISmartPublicityProjectService smartPublicityProjectService;
	@Autowired
	private ISmartPublicityProjectVerifyService smartPublicityProjectVerifyService;
	
	/**
	 * ??????????????????
	 *
	 * @param smartPublicityProject
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "????????????-??????????????????")
	@ApiOperation(value="????????????-??????????????????", notes="????????????-??????????????????")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartPublicityProject smartPublicityProject,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SmartPublicityProject> queryWrapper = QueryGenerator.initQueryWrapper(smartPublicityProject, req.getParameterMap());
		Page<SmartPublicityProject> page = new Page<SmartPublicityProject>(pageNo, pageSize);
		IPage<SmartPublicityProject> pageList = smartPublicityProjectService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 @AutoLog(value = "????????????????????????-??????????????????")
	 @ApiOperation(value="????????????????????????-??????????????????", notes="????????????????????????-??????????????????")
	 @GetMapping(value = "/listAdmin")
	 public Result<?> queryPageListAdmin(SmartPublicityProject smartPublicityProject,
										 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
										 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
										 HttpServletRequest req) {

		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 String orgCode = sysUser.getOrgCode();
		 if ("".equals(orgCode)) {
			 return Result.error("??????????????????????????????");
		 }
		 QueryWrapper<SmartPublicityProject> queryWrapper = QueryGenerator.initQueryWrapper(smartPublicityProject, req.getParameterMap());
		 queryWrapper.eq("sys_org_code", orgCode);
		 Page<SmartPublicityProject> page = new Page<SmartPublicityProject>(pageNo, pageSize);
		 IPage<SmartPublicityProject> pageList = smartPublicityProjectService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }
	
	/**
	 *   ??????
	 *
	 * @param smartPublicityProjectPage
	 * @return
	 */
	@AutoLog(value = "????????????-??????")
	@ApiOperation(value="????????????-??????", notes="????????????-??????")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SmartPublicityProjectPage smartPublicityProjectPage) {
		SmartPublicityProject smartPublicityProject = new SmartPublicityProject();
		BeanUtils.copyProperties(smartPublicityProjectPage, smartPublicityProject);
		smartPublicityProjectService.saveMain(smartPublicityProject, smartPublicityProjectPage.getSmartPublicityProjectVerifyList());
		return Result.OK("???????????????");
	}
	
	/**
	 *  ??????
	 *
	 * @param smartPublicityProjectPage
	 * @return
	 */
	@AutoLog(value = "????????????-??????")
	@ApiOperation(value="????????????-??????", notes="????????????-??????")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SmartPublicityProjectPage smartPublicityProjectPage) {
		SmartPublicityProject smartPublicityProject = new SmartPublicityProject();
		BeanUtils.copyProperties(smartPublicityProjectPage, smartPublicityProject);
		SmartPublicityProject smartPublicityProjectEntity = smartPublicityProjectService.getById(smartPublicityProject.getId());
		if(smartPublicityProjectEntity==null) {
			return Result.error("?????????????????????");
		}
		smartPublicityProjectService.updateMain(smartPublicityProject, smartPublicityProjectPage.getSmartPublicityProjectVerifyList());
		return Result.OK("????????????!");
	}
	
	/**
	 *   ??????id??????
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "????????????-??????id??????")
	@ApiOperation(value="????????????-??????id??????", notes="????????????-??????id??????")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		smartPublicityProjectService.delMain(id);
		return Result.OK("????????????!");
	}
	
	/**
	 *  ????????????
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "????????????-????????????")
	@ApiOperation(value="????????????-????????????", notes="????????????-????????????")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartPublicityProjectService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("?????????????????????");
	}
	
	/**
	 * ??????id??????
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "????????????-??????id??????")
	@ApiOperation(value="????????????-??????id??????", notes="????????????-??????id??????")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartPublicityProject smartPublicityProject = smartPublicityProjectService.getById(id);
		if(smartPublicityProject==null) {
			return Result.error("?????????????????????");
		}
		return Result.OK(smartPublicityProject);

	}
	
	/**
	 * ??????id??????
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "????????????????????????ID??????")
	@ApiOperation(value="??????????????????ID??????", notes="????????????-?????????ID??????")
	@GetMapping(value = "/querySmartPublicityProjectVerifyByMainId")
	public Result<?> querySmartPublicityProjectVerifyListByMainId(@RequestParam(name="id",required=true) String id) {
		List<SmartPublicityProjectVerify> smartPublicityProjectVerifyList = smartPublicityProjectVerifyService.selectByMainId(id);
		return Result.OK(smartPublicityProjectVerifyList);
	}

    /**
    * ??????excel
    *
    * @param request
    * @param smartPublicityProject
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartPublicityProject smartPublicityProject) {
      // Step.1 ??????????????????????????????
      QueryWrapper<SmartPublicityProject> queryWrapper = QueryGenerator.initQueryWrapper(smartPublicityProject, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 ??????????????????
      List<SmartPublicityProject> queryList = smartPublicityProjectService.list(queryWrapper);
      // ??????????????????
      String selections = request.getParameter("selections");
      List<SmartPublicityProject> smartPublicityProjectList = new ArrayList<SmartPublicityProject>();
      if(oConvertUtils.isEmpty(selections)) {
          smartPublicityProjectList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          smartPublicityProjectList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 ??????pageList
      List<SmartPublicityProjectPage> pageList = new ArrayList<SmartPublicityProjectPage>();
      for (SmartPublicityProject main : smartPublicityProjectList) {
          SmartPublicityProjectPage vo = new SmartPublicityProjectPage();
          BeanUtils.copyProperties(main, vo);
          List<SmartPublicityProjectVerify> smartPublicityProjectVerifyList = smartPublicityProjectVerifyService.selectByMainId(main.getId());
          vo.setSmartPublicityProjectVerifyList(smartPublicityProjectVerifyList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi ??????Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "??????????????????");
      mv.addObject(NormalExcelConstants.CLASS, SmartPublicityProjectPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("??????????????????", "?????????:"+sysUser.getRealname(), "????????????"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
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
              List<SmartPublicityProjectPage> list = ExcelImportUtil.importExcel(file.getInputStream(), SmartPublicityProjectPage.class, params);
              for (SmartPublicityProjectPage page : list) {
                  SmartPublicityProject po = new SmartPublicityProject();
                  BeanUtils.copyProperties(page, po);
                  smartPublicityProjectService.saveMain(po, page.getSmartPublicityProjectVerifyList());
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

}
