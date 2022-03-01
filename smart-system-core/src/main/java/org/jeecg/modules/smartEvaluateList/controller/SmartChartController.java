package org.jeecg.modules.smartEvaluateList.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.common.util.ParamsUtil;
import org.jeecg.modules.smartEvaluateList.entity.MonthCount;
import org.jeecg.modules.smartEvaluateList.entity.SmartEvaluateWindow;
import org.jeecg.modules.smartEvaluateList.entity.TypeCount;
import org.jeecg.modules.smartEvaluateList.entity.MonthCountEight;
import org.jeecg.modules.smartEvaluateList.entity.peopleAvg;
import org.jeecg.modules.smartEvaluateList.service.ISmartChartService;
import org.jeecg.modules.smartEvaluateList.service.ISmartEvaluateWindowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Description: 阳光评廉评价
 * @Author: zxh
 * @Date:   2021-12-06
 * @Version: V1.0
 */
@RestController
@RequestMapping("/smartEvaluateList/chart")
@Slf4j
public class SmartChartController extends JeecgController<peopleAvg, ISmartChartService> {
	@Autowired
	private ISmartChartService smartChartService;
	/**
	 *   按月统计
	 *
	 * @param
	 * @return
	 */
	@AutoLog(value = "阳光评廉-按月统计")
	@ApiOperation(value="阳光评廉-按月统计", notes="阳光评廉-按月统计")
	@ResponseBody
	@GetMapping(value = "/countByMonth")
	public Result<?> countByMonth(@RequestParam (value="year",required = false) String year) {
		try{

			if(year == null || year.isEmpty()){
				//获取当前年份
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
				Date date = new Date();
				year =sdf.format(date);
			}else{
				year = year.substring(1,year.length()-1);
			}
			//System.out.println("yyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
			System.out.println(year);
			List<MonthCount> list = smartChartService.countByMonth(year);
			return Result.OK(list);
		}catch (Exception e){
			return Result.error("error");
		}

	}

	/**
	 *   按月统计
	 *
	 * @param
	 * @return
	 */
	@AutoLog(value = "八项规定-按月统计")
	@ApiOperation(value="八项规定-按月统计", notes="八项规定-按月统计")
	@ResponseBody
	@GetMapping(value = "/countEight")
	public Result<?> countEight(@RequestParam (value="year",required = false) String year) {
		try{

			if(year == null || year.isEmpty()){
				//获取当前年份
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
				Date date = new Date();
				year =sdf.format(date);
			}else{
				year = year.substring(1,year.length()-1);
			}
			//System.out.println("yyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
			System.out.println(year);
			List<MonthCountEight> list = smartChartService.countEight(year);
			return Result.OK(list);
		}catch (Exception e){
			return Result.error("error");
		}

	}

	@AutoLog(value = "阳光评廉-按类型统计")
	@ApiOperation(value="阳光评廉-按类型统计", notes="阳光评廉-按类型统计")
	@GetMapping(value = "/countByGrade")
	public Result<?> countByGrade(@RequestParam (value="year",required = false) String year) {
		try{
			if(year == null || year.isEmpty()){
				//获取当前年份
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
				Date date = new Date();
				year =sdf.format(date);
			}else{
				year = year.substring(1,year.length()-1);
			}
			//System.out.println("ggggggggggggggggggggggg");
			System.out.println(year);
			List<TypeCount> list = smartChartService.countByGrade(year);
			return Result.OK(list);
		}catch (Exception e){
			return Result.error("error");
		}

	}

	@AutoLog(value = "八项规定-按类型统计")
	@ApiOperation(value="八项规定-按类型统计", notes="八项规定-按类型统计")
	@GetMapping(value = "/countByType")
	public Result<?> countByType(@RequestParam (value="year",required = false) String year) {
		try{
			if(year == null || year.isEmpty()){
				//获取当前年份
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
				Date date = new Date();
				year =sdf.format(date);
			}else{
				year = year.substring(1,year.length()-1);
			}
			//System.out.println("ggggggggggggggggggggggg");
			System.out.println(year);
			List<TypeCount> list = smartChartService.countByType(year);
			return Result.OK(list);
		}catch (Exception e){
			return Result.error("error");
		}

	}

	@AutoLog(value = "阳光评廉-人员评分均值")
	@ApiOperation(value="阳光评廉-人员评分均值", notes="阳光评廉-人员评分均值")
	@GetMapping(value = "/avgByPeople")
	public Result<?> avgByPeople(peopleAvg peopleAvg,
								 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								 HttpServletRequest req) {
		try{
			String oldWindowsName = peopleAvg.getWindowsName();
			System.out.println(oldWindowsName);
			String windowsName = "";
			if(oldWindowsName == null || oldWindowsName.equals("")){
				windowsName = oldWindowsName;
			}else {
				windowsName = oldWindowsName.replace('*','%');
			}
			QueryWrapper<peopleAvg> queryWrapper = QueryGenerator.initQueryWrapper(peopleAvg, req.getParameterMap());
			System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOO");
			System.out.println(ParamsUtil.getSuperQueryParams(req.getParameterMap()));
			Page<peopleAvg> page = new Page<peopleAvg>(pageNo, pageSize);
			IPage<peopleAvg> pageList = smartChartService.avgByPeople(page,windowsName);
			return Result.OK(pageList);
		}catch (Exception e){
			return Result.error("error");
		}

	}
	@AutoLog(value = "阳光评廉-窗口评分均值")
	@ApiOperation(value="阳光评廉-窗口评分均值", notes="阳光评廉-窗口评分均值")
	@GetMapping(value = "/windowsByGrade")
	public Result<?> windowsByGrade(peopleAvg peopleAvg,
								 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								 HttpServletRequest req) {
		try{
			String oldWindowsName = peopleAvg.getWindowsName();
			System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOO");
			System.out.println(oldWindowsName);
			String windowsName = "";
			if(oldWindowsName == null || oldWindowsName.equals("")){
				windowsName = oldWindowsName;
			}else {
				windowsName = oldWindowsName.replace('*','%');
			}
			QueryWrapper<peopleAvg> queryWrapper = QueryGenerator.initQueryWrapper(peopleAvg, req.getParameterMap());
			Page<peopleAvg> page = new Page<peopleAvg>(pageNo, pageSize);
			IPage<peopleAvg> pageList = smartChartService.windowsByGrade(page,windowsName);
			return Result.OK(pageList);
		}catch (Exception e){
			return Result.error("error");
		}

	}
	@AutoLog(value = "阳光评廉-统计窗口评价次数")
	@ApiOperation(value="阳光评廉-统计窗口评价次数", notes="阳光评廉-统计窗口评价次数")
	@GetMapping(value = "/windowsRankByCount")
	public Result<?> windowsRankByCount(@RequestParam (value="year",required = false) String year) {
		try{
			if(year == null || year.isEmpty()){
				//获取当前年份
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
				Date date = new Date();
				year =sdf.format(date);
			}else{
				year = year.substring(1,year.length()-1);
			}
			System.out.println("ggggggggggggggggggggggg");
			System.out.println(year);
			List<TypeCount> rankCountlist = smartChartService.windowsRankByCount(year);
			return Result.OK(rankCountlist);
		}catch (Exception e){
			return Result.error("error");
		}

	}
	@AutoLog(value = "阳光评廉-统计窗口评价次数")
	@ApiOperation(value="阳光评廉-统计窗口评价次数", notes="阳光评廉-统计窗口评价次数")
	@ResponseBody
	@GetMapping(value = "/windowsRankByGrade")
	public Result<?> windowsRankByGrade(@RequestParam (value="year",required = false) String year) {
		try{
			if(year == null || year.isEmpty()){
				//获取当前年份
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
				Date date = new Date();
				year =sdf.format(date);
			}else{
				year = year.substring(1,year.length()-1);
			}
			System.out.println("pppppppppppppppppppp");
			System.out.println(year);
			List<TypeCount> list = smartChartService.windowsRankByGrade(year);
			return Result.OK(list);
		}catch (Exception e){
			return Result.error("error");
		}

	}

}
