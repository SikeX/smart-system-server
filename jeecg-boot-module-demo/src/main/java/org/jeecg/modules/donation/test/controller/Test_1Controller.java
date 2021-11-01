package org.jeecg.modules.donation.test.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.donation.test.entity.Test_1;
import org.jeecg.modules.donation.test.service.ITest_1Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 描述
 * @Author: jeecg-boot
 * @Date:   2021-10-30
 * @Version: V1.0
 */
@Api(tags="描述")
@RestController
@RequestMapping("/test/test_1")
@Slf4j
public class Test_1Controller extends JeecgController<Test_1, ITest_1Service> {
	@Autowired
	private ITest_1Service test_1Service;
	
	/**
	 * 分页列表查询
	 *
	 * @param test_1
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "描述-分页列表查询")
	@ApiOperation(value="描述-分页列表查询", notes="描述-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Test_1 test_1,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Test_1> queryWrapper = QueryGenerator.initQueryWrapper(test_1, req.getParameterMap());
		Page<Test_1> page = new Page<Test_1>(pageNo, pageSize);
		IPage<Test_1> pageList = test_1Service.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param test_1
	 * @return
	 */
	@AutoLog(value = "描述-添加")
	@ApiOperation(value="描述-添加", notes="描述-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Test_1 test_1) {
		test_1Service.save(test_1);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param test_1
	 * @return
	 */
	@AutoLog(value = "描述-编辑")
	@ApiOperation(value="描述-编辑", notes="描述-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Test_1 test_1) {
		test_1Service.updateById(test_1);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "描述-通过id删除")
	@ApiOperation(value="描述-通过id删除", notes="描述-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		test_1Service.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "描述-批量删除")
	@ApiOperation(value="描述-批量删除", notes="描述-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.test_1Service.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "描述-通过id查询")
	@ApiOperation(value="描述-通过id查询", notes="描述-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Test_1 test_1 = test_1Service.getById(id);
		if(test_1==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(test_1);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param test_1
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Test_1 test_1) {
        return super.exportXls(request, test_1, Test_1.class, "描述");
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
        return super.importExcel(request, response, Test_1.class);
    }

}
