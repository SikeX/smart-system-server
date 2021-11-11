package org.jeecg.modules.testAttached.controller;

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
import org.jeecg.modules.testAttached.entity.TestAttachedFile;
import org.jeecg.modules.testAttached.entity.TestAttached;
import org.jeecg.modules.testAttached.vo.TestAttachedPage;
import org.jeecg.modules.testAttached.service.ITestAttachedService;
import org.jeecg.modules.testAttached.service.ITestAttachedFileService;
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
 * @Description: 附件测试主表
 * @Author: jeecg-boot
 * @Date:   2021-11-07
 * @Version: V1.0
 */
@Api(tags="附件测试主表")
@RestController
@RequestMapping("/testAttached/testAttached")
@Slf4j
public class TestAttachedController {
	@Autowired
	private ITestAttachedService testAttachedService;
	@Autowired
	private ITestAttachedFileService testAttachedFileService;
	
	/**
	 * 分页列表查询
	 *
	 * @param testAttached
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "附件测试主表-分页列表查询")
	@ApiOperation(value="附件测试主表-分页列表查询", notes="附件测试主表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(TestAttached testAttached,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TestAttached> queryWrapper = QueryGenerator.initQueryWrapper(testAttached, req.getParameterMap());
		Page<TestAttached> page = new Page<TestAttached>(pageNo, pageSize);
		IPage<TestAttached> pageList = testAttachedService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param testAttachedPage
	 * @return
	 */
	@AutoLog(value = "附件测试主表-添加")
	@ApiOperation(value="附件测试主表-添加", notes="附件测试主表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody TestAttachedPage testAttachedPage) {
		TestAttached testAttached = new TestAttached();
//		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
//		log.info(String.valueOf(testAttachedPage));
//		log.info(sysUser.getUsername());
		BeanUtils.copyProperties(testAttachedPage, testAttached);
		testAttachedService.saveMain(testAttached, testAttachedPage.getTestAttachedFileList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param testAttachedPage
	 * @return
	 */
	@AutoLog(value = "附件测试主表-编辑")
	@ApiOperation(value="附件测试主表-编辑", notes="附件测试主表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody TestAttachedPage testAttachedPage) {
		TestAttached testAttached = new TestAttached();
		BeanUtils.copyProperties(testAttachedPage, testAttached);
		TestAttached testAttachedEntity = testAttachedService.getById(testAttached.getId());
		if(testAttachedEntity==null) {
			return Result.error("未找到对应数据");
		}
		testAttachedService.updateMain(testAttached, testAttachedPage.getTestAttachedFileList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "附件测试主表-通过id删除")
	@ApiOperation(value="附件测试主表-通过id删除", notes="附件测试主表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		testAttachedService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "附件测试主表-批量删除")
	@ApiOperation(value="附件测试主表-批量删除", notes="附件测试主表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.testAttachedService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "附件测试主表-通过id查询")
	@ApiOperation(value="附件测试主表-通过id查询", notes="附件测试主表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		TestAttached testAttached = testAttachedService.getById(id);
		if(testAttached==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(testAttached);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "附件测试附表通过主表ID查询")
	@ApiOperation(value="附件测试附表主表ID查询", notes="附件测试附表-通主表ID查询")
	@GetMapping(value = "/queryTestAttachedFileByMainId")
	public Result<?> queryTestAttachedFileListByMainId(@RequestParam(name="id",required=true) String id) {
		List<TestAttachedFile> testAttachedFileList = testAttachedFileService.selectByMainId(id);
		return Result.OK(testAttachedFileList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param testAttached
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TestAttached testAttached) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<TestAttached> queryWrapper = QueryGenerator.initQueryWrapper(testAttached, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<TestAttached> queryList = testAttachedService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<TestAttached> testAttachedList = new ArrayList<TestAttached>();
      if(oConvertUtils.isEmpty(selections)) {
          testAttachedList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          testAttachedList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<TestAttachedPage> pageList = new ArrayList<TestAttachedPage>();
      for (TestAttached main : testAttachedList) {
          TestAttachedPage vo = new TestAttachedPage();
          BeanUtils.copyProperties(main, vo);
          List<TestAttachedFile> testAttachedFileList = testAttachedFileService.selectByMainId(main.getId());
          vo.setTestAttachedFileList(testAttachedFileList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "附件测试主表列表");
      mv.addObject(NormalExcelConstants.CLASS, TestAttachedPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("附件测试主表数据", "导出人:"+sysUser.getRealname(), "附件测试主表"));
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
              List<TestAttachedPage> list = ExcelImportUtil.importExcel(file.getInputStream(), TestAttachedPage.class, params);
              for (TestAttachedPage page : list) {
                  TestAttached po = new TestAttached();
                  BeanUtils.copyProperties(page, po);
                  testAttachedService.saveMain(po, page.getTestAttachedFileList());
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
	 public Result<?> edit(@RequestBody TestAttachedFile testAttachedFile) {
		 TestAttachedFile newTestAttachedFile = testAttachedFileService.getById(testAttachedFile.getId());
		 int currentCount = newTestAttachedFile.getDownloadTimes();
		 newTestAttachedFile.setDownloadTimes(currentCount+1);
		 testAttachedFileService.updateById(newTestAttachedFile);
		 return Result.OK("更新成功!");
	 }

}
