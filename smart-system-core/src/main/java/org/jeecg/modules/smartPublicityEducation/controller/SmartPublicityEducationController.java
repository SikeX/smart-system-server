package org.jeecg.modules.smartPublicityEducation.controller;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URLDecoder;
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
import org.jeecg.modules.smartPublicityEducation.entity.SmartPublicityEducationPeople;
import org.jeecg.modules.smartPublicityEducation.entity.SmartPublicityEducation;
import org.jeecg.modules.smartPublicityEducation.vo.SmartPublicityEducationPage;
import org.jeecg.modules.smartPublicityEducation.service.ISmartPublicityEducationService;
import org.jeecg.modules.smartPublicityEducation.service.ISmartPublicityEducationPeopleService;
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
 * @Description: 宣传教育
 * @Author: jeecg-boot
 * @Date:   2021-12-29
 * @Version: V1.0
 */
@Api(tags="宣传教育")
@RestController
@RequestMapping("/smartPublicityEducation/smartPublicityEducation")
@Slf4j
public class SmartPublicityEducationController {
	@Autowired
	private ISmartPublicityEducationService smartPublicityEducationService;
	@Autowired
	private ISmartPublicityEducationPeopleService smartPublicityEducationPeopleService;
	
	/**
	 * 分页列表查询
	 *
	 * @param smartPublicityEducation
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "宣传教育-分页列表查询")
	@ApiOperation(value="宣传教育-分页列表查询", notes="宣传教育-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartPublicityEducation smartPublicityEducation,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SmartPublicityEducation> queryWrapper = QueryGenerator.initQueryWrapper(smartPublicityEducation, req.getParameterMap());
		Page<SmartPublicityEducation> page = new Page<SmartPublicityEducation>(pageNo, pageSize);
		IPage<SmartPublicityEducation> pageList = smartPublicityEducationService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param smartPublicityEducationPage
	 * @return
	 */
	@AutoLog(value = "宣传教育-添加")
	@ApiOperation(value="宣传教育-添加", notes="宣传教育-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SmartPublicityEducationPage smartPublicityEducationPage) {
		SmartPublicityEducation smartPublicityEducation = new SmartPublicityEducation();
		BeanUtils.copyProperties(smartPublicityEducationPage, smartPublicityEducation);
		smartPublicityEducationService.saveMain(smartPublicityEducation, smartPublicityEducationPage.getSmartPublicityEducationPeopleList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param smartPublicityEducationPage
	 * @return
	 */
	@AutoLog(value = "宣传教育-编辑")
	@ApiOperation(value="宣传教育-编辑", notes="宣传教育-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SmartPublicityEducationPage smartPublicityEducationPage) {
		SmartPublicityEducation smartPublicityEducation = new SmartPublicityEducation();
		BeanUtils.copyProperties(smartPublicityEducationPage, smartPublicityEducation);
		SmartPublicityEducation smartPublicityEducationEntity = smartPublicityEducationService.getById(smartPublicityEducation.getId());
		if(smartPublicityEducationEntity==null) {
			return Result.error("未找到对应数据");
		}
		smartPublicityEducationService.updateMain(smartPublicityEducation, smartPublicityEducationPage.getSmartPublicityEducationPeopleList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "宣传教育-通过id删除")
	@ApiOperation(value="宣传教育-通过id删除", notes="宣传教育-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		smartPublicityEducationService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "宣传教育-批量删除")
	@ApiOperation(value="宣传教育-批量删除", notes="宣传教育-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartPublicityEducationService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "宣传教育-通过id查询")
	@ApiOperation(value="宣传教育-通过id查询", notes="宣传教育-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartPublicityEducation smartPublicityEducation = smartPublicityEducationService.getById(id);
		if(smartPublicityEducation==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(smartPublicityEducation);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "宣传教育参会人员通过主表ID查询")
	@ApiOperation(value="宣传教育参会人员主表ID查询", notes="宣传教育参会人员-通主表ID查询")
	@GetMapping(value = "/querySmartPublicityEducationPeopleByMainId")
	public Result<?> querySmartPublicityEducationPeopleListByMainId(@RequestParam(name="id",required=true) String id) {
		List<SmartPublicityEducationPeople> smartPublicityEducationPeopleList = smartPublicityEducationPeopleService.selectByMainId(id);
		return Result.OK(smartPublicityEducationPeopleList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param smartPublicityEducation
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartPublicityEducation smartPublicityEducation) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<SmartPublicityEducation> queryWrapper = QueryGenerator.initQueryWrapper(smartPublicityEducation, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<SmartPublicityEducation> queryList = smartPublicityEducationService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<SmartPublicityEducation> smartPublicityEducationList = new ArrayList<SmartPublicityEducation>();
      if(oConvertUtils.isEmpty(selections)) {
          smartPublicityEducationList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          smartPublicityEducationList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<SmartPublicityEducationPage> pageList = new ArrayList<SmartPublicityEducationPage>();
      for (SmartPublicityEducation main : smartPublicityEducationList) {
          SmartPublicityEducationPage vo = new SmartPublicityEducationPage();
          BeanUtils.copyProperties(main, vo);
          List<SmartPublicityEducationPeople> smartPublicityEducationPeopleList = smartPublicityEducationPeopleService.selectByMainId(main.getId());
          vo.setSmartPublicityEducationPeopleList(smartPublicityEducationPeopleList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "宣传教育列表");
      mv.addObject(NormalExcelConstants.CLASS, SmartPublicityEducationPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("宣传教育数据", "导出人:"+sysUser.getRealname(), "宣传教育"));
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
              List<SmartPublicityEducationPage> list = ExcelImportUtil.importExcel(file.getInputStream(), SmartPublicityEducationPage.class, params);
              for (SmartPublicityEducationPage page : list) {
                  SmartPublicityEducation po = new SmartPublicityEducation();
                  BeanUtils.copyProperties(page, po);
                  smartPublicityEducationService.saveMain(po, page.getSmartPublicityEducationPeopleList());
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
