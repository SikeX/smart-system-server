package org.jeecg.modules.wePower.smartPublicityProject.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
 * @Description: 项目管理
 * @Author: jeecg-boot
 * @Date:   2022-03-09
 * @Version: V1.0
 */
@Api(tags="项目管理")
@RestController
@RequestMapping("/smartPublicityProject/smartPublicityProject")
@Slf4j
public class SmartPublicityProjectController {
	@Autowired
	private ISmartPublicityProjectService smartPublicityProjectService;
	@Autowired
	private ISmartPublicityProjectVerifyService smartPublicityProjectVerifyService;
	
	/**
	 * 分页列表查询
	 *
	 * @param smartPublicityProject
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "项目管理-分页列表查询")
	@ApiOperation(value="项目管理-分页列表查询", notes="项目管理-分页列表查询")
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
	
	/**
	 *   添加
	 *
	 * @param smartPublicityProjectPage
	 * @return
	 */
	@AutoLog(value = "项目管理-添加")
	@ApiOperation(value="项目管理-添加", notes="项目管理-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SmartPublicityProjectPage smartPublicityProjectPage) {
		SmartPublicityProject smartPublicityProject = new SmartPublicityProject();
		BeanUtils.copyProperties(smartPublicityProjectPage, smartPublicityProject);
		smartPublicityProjectService.saveMain(smartPublicityProject, smartPublicityProjectPage.getSmartPublicityProjectVerifyList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param smartPublicityProjectPage
	 * @return
	 */
	@AutoLog(value = "项目管理-编辑")
	@ApiOperation(value="项目管理-编辑", notes="项目管理-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SmartPublicityProjectPage smartPublicityProjectPage) {
		SmartPublicityProject smartPublicityProject = new SmartPublicityProject();
		BeanUtils.copyProperties(smartPublicityProjectPage, smartPublicityProject);
		SmartPublicityProject smartPublicityProjectEntity = smartPublicityProjectService.getById(smartPublicityProject.getId());
		if(smartPublicityProjectEntity==null) {
			return Result.error("未找到对应数据");
		}
		smartPublicityProjectService.updateMain(smartPublicityProject, smartPublicityProjectPage.getSmartPublicityProjectVerifyList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "项目管理-通过id删除")
	@ApiOperation(value="项目管理-通过id删除", notes="项目管理-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		smartPublicityProjectService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "项目管理-批量删除")
	@ApiOperation(value="项目管理-批量删除", notes="项目管理-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartPublicityProjectService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "项目管理-通过id查询")
	@ApiOperation(value="项目管理-通过id查询", notes="项目管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartPublicityProject smartPublicityProject = smartPublicityProjectService.getById(id);
		if(smartPublicityProject==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(smartPublicityProject);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "项目审核通过主表ID查询")
	@ApiOperation(value="项目审核主表ID查询", notes="项目审核-通主表ID查询")
	@GetMapping(value = "/querySmartPublicityProjectVerifyByMainId")
	public Result<?> querySmartPublicityProjectVerifyListByMainId(@RequestParam(name="id",required=true) String id) {
		List<SmartPublicityProjectVerify> smartPublicityProjectVerifyList = smartPublicityProjectVerifyService.selectByMainId(id);
		return Result.OK(smartPublicityProjectVerifyList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param smartPublicityProject
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartPublicityProject smartPublicityProject) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<SmartPublicityProject> queryWrapper = QueryGenerator.initQueryWrapper(smartPublicityProject, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<SmartPublicityProject> queryList = smartPublicityProjectService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<SmartPublicityProject> smartPublicityProjectList = new ArrayList<SmartPublicityProject>();
      if(oConvertUtils.isEmpty(selections)) {
          smartPublicityProjectList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          smartPublicityProjectList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<SmartPublicityProjectPage> pageList = new ArrayList<SmartPublicityProjectPage>();
      for (SmartPublicityProject main : smartPublicityProjectList) {
          SmartPublicityProjectPage vo = new SmartPublicityProjectPage();
          BeanUtils.copyProperties(main, vo);
          List<SmartPublicityProjectVerify> smartPublicityProjectVerifyList = smartPublicityProjectVerifyService.selectByMainId(main.getId());
          vo.setSmartPublicityProjectVerifyList(smartPublicityProjectVerifyList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "项目管理列表");
      mv.addObject(NormalExcelConstants.CLASS, SmartPublicityProjectPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("项目管理数据", "导出人:"+sysUser.getRealname(), "项目管理"));
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
              List<SmartPublicityProjectPage> list = ExcelImportUtil.importExcel(file.getInputStream(), SmartPublicityProjectPage.class, params);
              for (SmartPublicityProjectPage page : list) {
                  SmartPublicityProject po = new SmartPublicityProject();
                  BeanUtils.copyProperties(page, po);
                  smartPublicityProjectService.saveMain(po, page.getSmartPublicityProjectVerifyList());
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
