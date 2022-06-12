package org.jeecg.modules.smart_data_sheet.controller;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.modules.common.service.CommonService;
import org.jeecg.modules.common.util.ParamsUtil;
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
import org.jeecg.modules.smart_data_sheet.entity.SmartDataSheetFile;
import org.jeecg.modules.smart_data_sheet.entity.SmartDataSheet;
import org.jeecg.modules.smart_data_sheet.vo.SmartDataSheetPage;
import org.jeecg.modules.smart_data_sheet.service.ISmartDataSheetService;
import org.jeecg.modules.smart_data_sheet.service.ISmartDataSheetFileService;
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
 * @Description: 资料库
 * @Author: jeecg-boot
 * @Date:   2021-11-11
 * @Version: V1.0
 */
@Api(tags="资料库")
@RestController
@RequestMapping("/smart_data_sheet/smartDataSheet")
@Slf4j
public class SmartDataSheetController {
	@Autowired
	private ISmartDataSheetService smartDataSheetService;
	@Autowired
	private ISmartDataSheetFileService smartDataSheetFileService;

	@Autowired
	CommonService commonService;

	public Result<?> edit(@RequestBody SmartDataSheetFile smartDataSheetFile) {
		SmartDataSheetFile newSmartDataSheetFile =
				smartDataSheetFileService.getById(smartDataSheetFile.getId());
		int currentCount = newSmartDataSheetFile.getTimes();
		newSmartDataSheetFile.setTimes(currentCount+1);
		smartDataSheetFileService.updateById(newSmartDataSheetFile);
		return Result.OK("更新成功!");
	}


	/**
	 * 分页列表查询
	 *
	 * @param smartDataSheet
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "资料库-分页列表查询")
	@ApiOperation(value="资料库-分页列表查询", notes="资料库-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartDataSheet smartDataSheet,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {

//		String rule = "eq";
//		// 2. 查询字段
//		String field = "type";
////		// 获取登录用户信息，可以用来查询单位部门信息
////		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
//
////		// 获取子单位ID
////		String childrenIdString = commonService.getChildrenIdStringByOrgCode(sysUser.getOrgCode());
//
//		HashMap<String, String[]> map = new HashMap<>(req.getParameterMap());
//		// 获取请求参数中的superQueryParams
//		List<String> paramsList = ParamsUtil.getSuperQueryParams(req.getParameterMap());
//
//		// 添加额外查询条件，用于权限控制
//		paramsList.add("%5B%7B%22rule%22:%22" + rule + "%22,%22type%22:%22int%22,%22dictCode%22:%22type_data%22,%22val%22:%22" + 0 + "%22,%22field%22:%22"+field+"%22%7D%5D");
////		paramsList.add("%5B%7B%22rule%22:%22" + rule + "%22,%22type%22:%22string%22,%22dictCode%22:%22%22,%22val%22:%22" + childrenIdString + "%22,%22field%22:%22" + field + "%22%7D%5D");
////		"%5B%7B%22rule%22:%22"+rule+  "%22,%22type%22:%22int%22,%22dictCode%22:%22type_data%22,%22val%22:%220%22,%22field%22:%22"+field+"%22%7D%5D"
//		String[] params = new String[paramsList.size()];
//		paramsList.toArray(params);
//		map.put("superQueryParams", params);
//		params = new String[]{"and"};
//		map.put("superQueryMatchType", params);

		QueryWrapper<SmartDataSheet> queryWrapper = QueryGenerator.initQueryWrapper(smartDataSheet, req.getParameterMap());
		Page<SmartDataSheet> page = new Page<SmartDataSheet>(pageNo, pageSize);
		IPage<SmartDataSheet> pageList = smartDataSheetService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param smartDataSheetPage
	 * @return
	 */
	@AutoLog(value = "资料库-添加")
	@ApiOperation(value="资料库-添加", notes="资料库-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SmartDataSheetPage smartDataSheetPage) {

		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String orgCode = sysUser.getOrgCode();
		if ("".equals(orgCode)) {
			return Result.error("本用户没有操作权限！");
		}
		String id = smartDataSheetService.getDepartIdByOrgCode(orgCode);
		smartDataSheetPage.setDepartmentid(id);

		SmartDataSheet smartDataSheet = new SmartDataSheet();
		BeanUtils.copyProperties(smartDataSheetPage, smartDataSheet);
		smartDataSheetService.saveMain(smartDataSheet, smartDataSheetPage.getSmartDataSheetFileList());
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param smartDataSheetPage
	 * @return
	 */
	@AutoLog(value = "资料库-编辑")
	@ApiOperation(value="资料库-编辑", notes="资料库-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SmartDataSheetPage smartDataSheetPage) {
		SmartDataSheet smartDataSheet = new SmartDataSheet();
		BeanUtils.copyProperties(smartDataSheetPage, smartDataSheet);
		SmartDataSheet smartDataSheetEntity = smartDataSheetService.getById(smartDataSheet.getId());
		if(smartDataSheetEntity==null) {
			return Result.error("未找到对应数据");
		}
		smartDataSheetService.updateMain(smartDataSheet, smartDataSheetPage.getSmartDataSheetFileList());
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "资料库-通过id删除")
	@ApiOperation(value="资料库-通过id删除", notes="资料库-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		smartDataSheetService.delMain(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "资料库-批量删除")
	@ApiOperation(value="资料库-批量删除", notes="资料库-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartDataSheetService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "资料库-通过id查询")
	@ApiOperation(value="资料库-通过id查询", notes="资料库-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartDataSheet smartDataSheet = smartDataSheetService.getById(id);
		if(smartDataSheet==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(smartDataSheet);

	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "资料库文件通过主表ID查询")
	@ApiOperation(value="资料库文件主表ID查询", notes="资料库文件-通主表ID查询")
	@GetMapping(value = "/querySmartDataSheetFileByMainId")
	public Result<?> querySmartDataSheetFileListByMainId(@RequestParam(name="id",required=true) String id) {
		List<SmartDataSheetFile> smartDataSheetFileList = smartDataSheetFileService.selectByMainId(id);
		return Result.OK(smartDataSheetFileList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param smartDataSheet
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartDataSheet smartDataSheet) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<SmartDataSheet> queryWrapper = QueryGenerator.initQueryWrapper(smartDataSheet, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<SmartDataSheet> queryList = smartDataSheetService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<SmartDataSheet> smartDataSheetList = new ArrayList<SmartDataSheet>();
      if(oConvertUtils.isEmpty(selections)) {
          smartDataSheetList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          smartDataSheetList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<SmartDataSheetPage> pageList = new ArrayList<SmartDataSheetPage>();
      for (SmartDataSheet main : smartDataSheetList) {
          SmartDataSheetPage vo = new SmartDataSheetPage();
          BeanUtils.copyProperties(main, vo);
          List<SmartDataSheetFile> smartDataSheetFileList = smartDataSheetFileService.selectByMainId(main.getId());
          vo.setSmartDataSheetFileList(smartDataSheetFileList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "资料库列表");
      mv.addObject(NormalExcelConstants.CLASS, SmartDataSheetPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("资料库数据", "导出人:"+sysUser.getRealname(), "资料库"));
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
              List<SmartDataSheetPage> list = ExcelImportUtil.importExcel(file.getInputStream(), SmartDataSheetPage.class, params);
              for (SmartDataSheetPage page : list) {
                  SmartDataSheet po = new SmartDataSheet();
                  BeanUtils.copyProperties(page, po);
                  smartDataSheetService.saveMain(po, page.getSmartDataSheetFileList());
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
