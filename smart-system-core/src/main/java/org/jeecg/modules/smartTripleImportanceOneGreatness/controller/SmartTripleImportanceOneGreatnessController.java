package org.jeecg.modules.smartTripleImportanceOneGreatness.controller;

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
import org.jeecg.modules.smartTripleImportanceOneGreatness.entity.SmartTripleImportanceOneGreatnessDecription;
import org.jeecg.modules.smartTripleImportanceOneGreatness.entity.SmartTripleImportanceOneGreatness;
import org.jeecg.modules.smartTripleImportanceOneGreatness.vo.SmartTripleImportanceOneGreatnessPage;
import org.jeecg.modules.smartTripleImportanceOneGreatness.service.ISmartTripleImportanceOneGreatnessService;
import org.jeecg.modules.smartTripleImportanceOneGreatness.service.ISmartTripleImportanceOneGreatnessDecriptionService;
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
 * @Date:   2021-11-01
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
	private ISmartTripleImportanceOneGreatnessDecriptionService smartTripleImportanceOneGreatnessDecriptionService;
	
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
		QueryWrapper<SmartTripleImportanceOneGreatness> queryWrapper = QueryGenerator.initQueryWrapper(smartTripleImportanceOneGreatness, req.getParameterMap());
		Page<SmartTripleImportanceOneGreatness> page = new Page<SmartTripleImportanceOneGreatness>(pageNo, pageSize);
		IPage<SmartTripleImportanceOneGreatness> pageList = smartTripleImportanceOneGreatnessService.page(page, queryWrapper);
		return Result.OK(pageList);
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
		SmartTripleImportanceOneGreatness smartTripleImportanceOneGreatness = new SmartTripleImportanceOneGreatness();
		BeanUtils.copyProperties(smartTripleImportanceOneGreatnessPage, smartTripleImportanceOneGreatness);
		smartTripleImportanceOneGreatnessService.saveMain(smartTripleImportanceOneGreatness, smartTripleImportanceOneGreatnessPage.getSmartTripleImportanceOneGreatnessDecriptionList());
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
		smartTripleImportanceOneGreatnessService.updateMain(smartTripleImportanceOneGreatness, smartTripleImportanceOneGreatnessPage.getSmartTripleImportanceOneGreatnessDecriptionList());
		return Result.OK("编辑成功!");
	}
	
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
	@AutoLog(value = "三重一大附件表通过主表ID查询")
	@ApiOperation(value="三重一大附件表主表ID查询", notes="三重一大附件表-通主表ID查询")
	@GetMapping(value = "/querySmartTripleImportanceOneGreatnessDecriptionByMainId")
	public Result<?> querySmartTripleImportanceOneGreatnessDecriptionListByMainId(@RequestParam(name="id",required=true) String id) {
		List<SmartTripleImportanceOneGreatnessDecription> smartTripleImportanceOneGreatnessDecriptionList = smartTripleImportanceOneGreatnessDecriptionService.selectByMainId(id);
		return Result.OK(smartTripleImportanceOneGreatnessDecriptionList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param smartTripleImportanceOneGreatness
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartTripleImportanceOneGreatness smartTripleImportanceOneGreatness) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<SmartTripleImportanceOneGreatness> queryWrapper = QueryGenerator.initQueryWrapper(smartTripleImportanceOneGreatness, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<SmartTripleImportanceOneGreatness> queryList = smartTripleImportanceOneGreatnessService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
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
          List<SmartTripleImportanceOneGreatnessDecription> smartTripleImportanceOneGreatnessDecriptionList = smartTripleImportanceOneGreatnessDecriptionService.selectByMainId(main.getId());
          vo.setSmartTripleImportanceOneGreatnessDecriptionList(smartTripleImportanceOneGreatnessDecriptionList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "三重一大表列表");
      mv.addObject(NormalExcelConstants.CLASS, SmartTripleImportanceOneGreatnessPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("三重一大表数据", "导出人:"+sysUser.getRealname(), "三重一大表"));
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
              List<SmartTripleImportanceOneGreatnessPage> list = ExcelImportUtil.importExcel(file.getInputStream(), SmartTripleImportanceOneGreatnessPage.class, params);
              for (SmartTripleImportanceOneGreatnessPage page : list) {
                  SmartTripleImportanceOneGreatness po = new SmartTripleImportanceOneGreatness();
                  BeanUtils.copyProperties(page, po);
                  smartTripleImportanceOneGreatnessService.saveMain(po, page.getSmartTripleImportanceOneGreatnessDecriptionList());
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
