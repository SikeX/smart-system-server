package org.jeecg.modules.calendarEvent.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.calendarEvent.entity.CalendarEvent;
import org.jeecg.modules.calendarEvent.service.ICalendarEventService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.common.util.ParamsUtil;
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
 * @Description: 日历事件
 * @Author: jeecg-boot
 * @Date:   2021-12-03
 * @Version: V1.0
 */
@Api(tags="日历事件")
@RestController
@RequestMapping("/calendarEvent/calendarEvent")
@Slf4j
public class CalendarEventController extends JeecgController<CalendarEvent, ICalendarEventService> {
	@Autowired
	private ICalendarEventService calendarEventService;

	/**
	 * 分页列表查询
	 *
	 * @param calendarEvent
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "日历事件-分页列表查询")
	@ApiOperation(value="日历事件-分页列表查询", notes="日历事件-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(CalendarEvent calendarEvent,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		// 1. 规则，下面是 以**开始
		String rule = "eq";
		// 2. 查询字段
		String field = "creatorId";
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		HashMap<String, String[]> map = new HashMap<>(req.getParameterMap());
		// 获取请求参数中的superQueryParams
		List<String> paramsList = ParamsUtil.getSuperQueryParams(req.getParameterMap());

		// 添加额外查询条件，用于权限控制
		paramsList.add("%5B%7B%22rule%22:%22" + rule + "%22,%22type%22:%22string%22,%22dictCode%22:%22%22,%22val%22:%22"
				+ sysUser.getId()
				+ "%22,%22field%22:%22" + field + "%22%7D%5D");
		String[] params = new String[paramsList.size()];
		paramsList.toArray(params);
		map.put("superQueryParams", params);
		params = new String[]{"and"};
		map.put("superQueryMatchType", params);

		QueryWrapper<CalendarEvent> queryWrapper = QueryGenerator.initQueryWrapper(calendarEvent, map);
		Page<CalendarEvent> page = new Page<CalendarEvent>(pageNo, pageSize);
		IPage<CalendarEvent> pageList = calendarEventService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param calendarEvent
	 * @return
	 */
	@AutoLog(value = "日历事件-添加")
	@ApiOperation(value="日历事件-添加", notes="日历事件-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody CalendarEvent calendarEvent) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		calendarEvent.setCreatorId(sysUser.getId());
		calendarEventService.save(calendarEvent);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param calendarEvent
	 * @return
	 */
	@AutoLog(value = "日历事件-编辑")
	@ApiOperation(value="日历事件-编辑", notes="日历事件-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody CalendarEvent calendarEvent) {
		calendarEventService.updateById(calendarEvent);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "日历事件-通过id删除")
	@ApiOperation(value="日历事件-通过id删除", notes="日历事件-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		calendarEventService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "日历事件-批量删除")
	@ApiOperation(value="日历事件-批量删除", notes="日历事件-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.calendarEventService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "日历事件-通过id查询")
	@ApiOperation(value="日历事件-通过id查询", notes="日历事件-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		CalendarEvent calendarEvent = calendarEventService.getById(id);
		if(calendarEvent==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(calendarEvent);
	}

	/**
	 * 导出excel
	 *
	 * @param request
	 * @param calendarEvent
	 */
	@RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(HttpServletRequest request, CalendarEvent calendarEvent) {
		return super.exportXls(request, calendarEvent, CalendarEvent.class, "日历事件");
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
		return super.importExcel(request, response, CalendarEvent.class);
	}

}