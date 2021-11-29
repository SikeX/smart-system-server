package org.jeecg.modules.smartGradeNumber.controller;

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
import org.jeecg.modules.smartGradeNumber.entity.SmartGradeNumber;
import org.jeecg.modules.smartGradeNumber.service.ISmartGradeNumberService;

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
 * @Description: 成绩分布人数表
 * @Author: jeecg-boot
 * @Date:   2021-11-24
 * @Version: V1.0
 */
@Api(tags="成绩分布人数表")
@RestController
@RequestMapping("/smartGradeNumber/smartGradeNumber")
@Slf4j
public class SmartGradeNumberController extends JeecgController<SmartGradeNumber, ISmartGradeNumberService> {
	@Autowired
	private ISmartGradeNumberService smartGradeNumberService;

	/**
	 * 分页列表查询
	 *
	 * @param smartGradeNumber
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "成绩分布人数表-分页列表查询")
	@ApiOperation(value="成绩分布人数表-分页列表查询", notes="成绩分布人数表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartGradeNumber smartGradeNumber,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SmartGradeNumber> queryWrapper = QueryGenerator.initQueryWrapper(smartGradeNumber, req.getParameterMap());
		Page<SmartGradeNumber> page = new Page<SmartGradeNumber>(pageNo, pageSize);
		IPage<SmartGradeNumber> pageList = smartGradeNumberService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param smartGradeNumber
	 * @return
	 */
	@AutoLog(value = "成绩分布人数表-添加")
	@ApiOperation(value="成绩分布人数表-添加", notes="成绩分布人数表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SmartGradeNumber smartGradeNumber) {
		smartGradeNumberService.save(smartGradeNumber);
		return Result.OK("添加成功！");
	}

	 /**
	  *   添加人数
	  *
	  * @param
	  * @return
	  */
	 @AutoLog(value = "添加人数")
	 @ApiOperation(value="给不同的成绩等级添加人数", notes="给不同的成绩等级添加人数")
	 @PostMapping(value = "/addPersonNumber")
	 public Result<?> addPersonNumber(int exam_grade,int total_score,int pass_mark) {
		 //int exam_grade;//考试成绩
		 // int total_score;//试卷满分
		 //int pass_mark;//试卷及格线

	 	 int excellent_number=0;//优秀人数
		 int good_number=0;//良好人数
		 int pass_number=0;//及格人数
		 int fail_number=0;//不及格人数

		 if(exam_grade<=total_score && exam_grade>=0.9*total_score){
		 	//优秀

		 	 excellent_number += 1;

		 } else if(exam_grade<0.9*total_score && exam_grade>=0.8*total_score){
		 	//良好

			 good_number += 1;

		 } else if(exam_grade<0.8*total_score && exam_grade>=pass_mark){
		 	//及格

			 pass_number += 1;

		 }else{
		 	//不及格
			 fail_number += 1;
		 }
		 return Result.OK("添加成功！");

	 }

	/**
	 *  编辑
	 *
	 * @param smartGradeNumber
	 * @return
	 */
	@AutoLog(value = "成绩分布人数表-编辑")
	@ApiOperation(value="成绩分布人数表-编辑", notes="成绩分布人数表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SmartGradeNumber smartGradeNumber) {
		smartGradeNumberService.updateById(smartGradeNumber);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "成绩分布人数表-通过id删除")
	@ApiOperation(value="成绩分布人数表-通过id删除", notes="成绩分布人数表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		smartGradeNumberService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "成绩分布人数表-批量删除")
	@ApiOperation(value="成绩分布人数表-批量删除", notes="成绩分布人数表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartGradeNumberService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "成绩分布人数表-通过id查询")
	@ApiOperation(value="成绩分布人数表-通过id查询", notes="成绩分布人数表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartGradeNumber smartGradeNumber = smartGradeNumberService.getById(id);
		if(smartGradeNumber==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(smartGradeNumber);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param smartGradeNumber
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartGradeNumber smartGradeNumber) {
        return super.exportXls(request, smartGradeNumber, SmartGradeNumber.class, "成绩分布人数表");
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
        return super.importExcel(request, response, SmartGradeNumber.class);
    }

}
