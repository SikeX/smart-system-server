package org.jeecg.modules.SmartTripeoMeeting.controller;

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
import org.jeecg.modules.SmartTripeoMeeting.entity.SmartTripeoMeeting;
import org.jeecg.modules.SmartTripeoMeeting.service.ISmartTripeoMeetingService;

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
 * @Description: 三员+会议开展
 * @Author: jeecg-boot
 * @Date:   2022-03-05
 * @Version: V1.0
 */
@Api(tags="三员+会议开展")
@RestController
@RequestMapping("/SmartTripeoMeeting/smartTripeoMeeting")
@Slf4j
public class SmartTripeoMeetingController extends JeecgController<SmartTripeoMeeting, ISmartTripeoMeetingService> {
	@Autowired
	private ISmartTripeoMeetingService smartTripeoMeetingService;
	
	/**
	 * 分页列表查询
	 *
	 * @param smartTripeoMeeting
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "三员+会议开展-分页列表查询")
	@ApiOperation(value="三员+会议开展-分页列表查询", notes="三员+会议开展-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartTripeoMeeting smartTripeoMeeting,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SmartTripeoMeeting> queryWrapper = QueryGenerator.initQueryWrapper(smartTripeoMeeting, req.getParameterMap());
		Page<SmartTripeoMeeting> page = new Page<SmartTripeoMeeting>(pageNo, pageSize);
		IPage<SmartTripeoMeeting> pageList = smartTripeoMeetingService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param smartTripeoMeeting
	 * @return
	 */
	@AutoLog(value = "三员+会议开展-添加")
	@ApiOperation(value="三员+会议开展-添加", notes="三员+会议开展-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SmartTripeoMeeting smartTripeoMeeting) {
		smartTripeoMeetingService.save(smartTripeoMeeting);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param smartTripeoMeeting
	 * @return
	 */
	@AutoLog(value = "三员+会议开展-编辑")
	@ApiOperation(value="三员+会议开展-编辑", notes="三员+会议开展-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SmartTripeoMeeting smartTripeoMeeting) {
		smartTripeoMeetingService.updateById(smartTripeoMeeting);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "三员+会议开展-通过id删除")
	@ApiOperation(value="三员+会议开展-通过id删除", notes="三员+会议开展-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		smartTripeoMeetingService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "三员+会议开展-批量删除")
	@ApiOperation(value="三员+会议开展-批量删除", notes="三员+会议开展-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartTripeoMeetingService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "三员+会议开展-通过id查询")
	@ApiOperation(value="三员+会议开展-通过id查询", notes="三员+会议开展-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartTripeoMeeting smartTripeoMeeting = smartTripeoMeetingService.getById(id);
		if(smartTripeoMeeting==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(smartTripeoMeeting);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param smartTripeoMeeting
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartTripeoMeeting smartTripeoMeeting) {
        return super.exportXls(request, smartTripeoMeeting, SmartTripeoMeeting.class, "三员+会议开展");
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
        return super.importExcel(request, response, SmartTripeoMeeting.class);
    }

}
