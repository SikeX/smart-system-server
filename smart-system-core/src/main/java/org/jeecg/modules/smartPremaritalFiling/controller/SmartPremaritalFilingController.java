package org.jeecg.modules.smartPremaritalFiling.controller;

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
import org.jeecg.modules.smartPremaritalFiling.entity.SmartPremaritalFilingApp;
import org.jeecg.modules.smartPremaritalFiling.entity.SmartPremaritalFiling;
import org.jeecg.modules.smartPremaritalFiling.vo.SmartPremaritalFilingPage;
import org.jeecg.modules.smartPremaritalFiling.service.ISmartPremaritalFilingService;
import org.jeecg.modules.smartPremaritalFiling.service.ISmartPremaritalFilingAppService;
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
 * @Description: 8项规定婚前报备表
 * @Author: jeecg-boot
 * @Date:   2021-11-02
 * @Version: V1.0
 */
@Api(tags="8项规定婚前报备表")
@RestController
@RequestMapping("/smartPremaritalFiling/smartPremaritalFiling")
@Slf4j
public class SmartPremaritalFilingController {
	@Autowired
	private ISmartPremaritalFilingService smartPremaritalFilingService;
	@Autowired
	private ISmartPremaritalFilingAppService smartPremaritalFilingAppService;
	
	/**
	 * 分页列表查询
	 *
	 * @param smartPremaritalFiling
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "8项规定婚前报备表-分页列表查询")
	@ApiOperation(value="8项规定婚前报备表-分页列表查询", notes="8项规定婚前报备表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartPremaritalFiling smartPremaritalFiling,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SmartPremaritalFiling> queryWrapper = QueryGenerator.initQueryWrapper(smartPremaritalFiling, req.getParameterMap());
		Page<SmartPremaritalFiling> page = new Page<SmartPremaritalFiling>(pageNo, pageSize);
		IPage<SmartPremaritalFiling> pageList = smartPremaritalFilingService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param smartPremaritalFilingPage
	 * @return
	 */
	@AutoLog(value = "8项规定婚前报备表-添加")
	@ApiOperation(value="8项规定婚前报备表-添加", notes="8项规定婚前报备表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SmartPremaritalFilingPage smartPremaritalFilingPage) {
		SmartPremaritalFiling smartPremaritalFiling = new SmartPremaritalFiling();
		BeanUtils.copyProperties(smartPremaritalFilingPage, smartPremaritalFiling);
		smartPremaritalFilingService.saveMain(smartPremaritalFiling, smartPremaritalFilingPage.getSmartPremaritalFilingAppList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param smartPremaritalFilingPage
	 * @return
	 */
	@AutoLog(value = "8项规定婚前报备表-编辑")
	@ApiOperation(value="8项规定婚前报备表-编辑", notes="8项规定婚前报备表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SmartPremaritalFilingPage smartPremaritalFilingPage) {
		SmartPremaritalFiling smartPremaritalFiling = new SmartPremaritalFiling();
		BeanUtils.copyProperties(smartPremaritalFilingPage, smartPremaritalFiling);
		SmartPremaritalFiling smartPremaritalFilingEntity = smartPremaritalFilingService.getById(smartPremaritalFiling.getId());
		if(smartPremaritalFilingEntity==null) {
			return Result.error("未找到对应数据");
		}
		smartPremaritalFilingService.updateMain(smartPremaritalFiling, smartPremaritalFilingPage.getSmartPremaritalFilingAppList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "8项规定婚前报备表-通过id删除")
	@ApiOperation(value="8项规定婚前报备表-通过id删除", notes="8项规定婚前报备表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		smartPremaritalFilingService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "8项规定婚前报备表-批量删除")
	@ApiOperation(value="8项规定婚前报备表-批量删除", notes="8项规定婚前报备表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartPremaritalFilingService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "8项规定婚前报备表-通过id查询")
	@ApiOperation(value="8项规定婚前报备表-通过id查询", notes="8项规定婚前报备表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartPremaritalFiling smartPremaritalFiling = smartPremaritalFilingService.getById(id);
		if(smartPremaritalFiling==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(smartPremaritalFiling);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "8项规定婚前报备表附表通过主表ID查询")
	@ApiOperation(value="8项规定婚前报备表附表主表ID查询", notes="8项规定婚前报备表附表-通主表ID查询")
	@GetMapping(value = "/querySmartPremaritalFilingAppByMainId")
	public Result<?> querySmartPremaritalFilingAppListByMainId(@RequestParam(name="id",required=true) String id) {
		List<SmartPremaritalFilingApp> smartPremaritalFilingAppList = smartPremaritalFilingAppService.selectByMainId(id);
		return Result.OK(smartPremaritalFilingAppList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param smartPremaritalFiling
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartPremaritalFiling smartPremaritalFiling) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<SmartPremaritalFiling> queryWrapper = QueryGenerator.initQueryWrapper(smartPremaritalFiling, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<SmartPremaritalFiling> queryList = smartPremaritalFilingService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<SmartPremaritalFiling> smartPremaritalFilingList = new ArrayList<SmartPremaritalFiling>();
      if(oConvertUtils.isEmpty(selections)) {
          smartPremaritalFilingList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          smartPremaritalFilingList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<SmartPremaritalFilingPage> pageList = new ArrayList<SmartPremaritalFilingPage>();
      for (SmartPremaritalFiling main : smartPremaritalFilingList) {
          SmartPremaritalFilingPage vo = new SmartPremaritalFilingPage();
          BeanUtils.copyProperties(main, vo);
          List<SmartPremaritalFilingApp> smartPremaritalFilingAppList = smartPremaritalFilingAppService.selectByMainId(main.getId());
          vo.setSmartPremaritalFilingAppList(smartPremaritalFilingAppList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "8项规定婚前报备表列表");
      mv.addObject(NormalExcelConstants.CLASS, SmartPremaritalFilingPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("8项规定婚前报备表数据", "导出人:"+sysUser.getRealname(), "8项规定婚前报备表"));
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
              List<SmartPremaritalFilingPage> list = ExcelImportUtil.importExcel(file.getInputStream(), SmartPremaritalFilingPage.class, params);
              for (SmartPremaritalFilingPage page : list) {
                  SmartPremaritalFiling po = new SmartPremaritalFiling();
                  BeanUtils.copyProperties(page, po);
                  smartPremaritalFilingService.saveMain(po, page.getSmartPremaritalFilingAppList());
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
