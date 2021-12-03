package org.jeecg.modules.SmartPaper.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.SmartPaper.entity.SmartGradeNumber;
import org.jeecg.modules.SmartPaper.service.ISmartGradeNumberService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
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
@RequestMapping("/SmartPeople/smartGradeNumber")
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

	/* /**
	  *   添加人数
	  *
	  * @param
	  * @return
	  *//*
	 @AutoLog(value = "添加人数")
	 @ApiOperation(value="给不同的成绩等级添加人数", notes="给不同的成绩等级添加人数")
	 @PostMapping(value = "/addPersonNumber")
	 public Result<?> addPersonNumber(double exam_grade,double total_score,double pass_mark) {
		 //int exam_grade;//考试成绩
		 // int total_score;//试卷满分
		 //int pass_mark;//试卷及格线

	 	 int excellent_number=0;//优秀人数
		 int good_number=0;//良好人数
		 int pass_number=0;//及格人数
		 int fail_number=0;//不及格人数

		 double excellent_line =0.9*total_score;//优秀线

		 *//*if(exam_grade<=total_score && exam_grade>=0.9*total_score){
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
		 }*//*
		 return Result.OK("添加成功！");

	 }*/

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

   //优秀总数量
	 @RequestMapping(value = "excellentCount",method = RequestMethod.GET)
	 public Result<?> excellentCount(int total_score){
    	 int excellent_line= 90%total_score;//优秀线
    	 Integer count = smartGradeNumberService.excellentCount(excellent_line);
		 return Result.OK(count);
	 }
	 //良好总数量
	 @RequestMapping(value = "goodCount",method = RequestMethod.GET)
	 public Result<?> goodCount(int total_score){
		 int good_line=80%total_score;//良好线
		 Integer count = smartGradeNumberService.goodCount(good_line);
		 return Result.OK(count);
	 }
	 //及格总数量
	 @RequestMapping(value = "passCount",method = RequestMethod.GET)
	 public Result<?> passCount(){
		 Integer count = smartGradeNumberService.passCount();
		 return Result.OK(count);
	 }
	 //不及格总数量
	 @RequestMapping(value = "failCount",method = RequestMethod.GET)
	 public Result<?> failCount(){

		 Integer count = smartGradeNumberService.failCount();
		 return Result.OK(count);
	 }



 }
