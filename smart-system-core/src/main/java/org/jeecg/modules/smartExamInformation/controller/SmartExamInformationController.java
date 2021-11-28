package org.jeecg.modules.smartExamInformation.controller;

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
import org.jeecg.modules.smartExamInformation.entity.SmartExamInformation;
import org.jeecg.modules.smartExamInformation.service.ISmartExamInformationService;

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
 * @Description: 考试信息表
 * @Author: jeecg-boot
 * @Date:   2021-11-23
 * @Version: V1.0
 */
@Api(tags="考试信息表")
@RestController
@RequestMapping("/smartExamInformation/smartExamInformation")
@Slf4j
public class SmartExamInformationController extends JeecgController<SmartExamInformation, ISmartExamInformationService> {
	@Autowired
	private ISmartExamInformationService smartExamInformationService;
	
	/**
	 * 分页列表查询
	 *
	 * @param smartExamInformation
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "考试信息表-分页列表查询")
	@ApiOperation(value="考试信息表-分页列表查询", notes="考试信息表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartExamInformation smartExamInformation,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SmartExamInformation> queryWrapper = QueryGenerator.initQueryWrapper(smartExamInformation, req.getParameterMap());
		Page<SmartExamInformation> page = new Page<SmartExamInformation>(pageNo, pageSize);
		IPage<SmartExamInformation> pageList = smartExamInformationService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param smartExamInformation
	 * @return
	 */
	@AutoLog(value = "考试信息表-添加")
	@ApiOperation(value="考试信息表-添加", notes="考试信息表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SmartExamInformation smartExamInformation) {
		smartExamInformationService.save(smartExamInformation);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param smartExamInformation
	 * @return
	 */
	@AutoLog(value = "考试信息表-编辑")
	@ApiOperation(value="考试信息表-编辑", notes="考试信息表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SmartExamInformation smartExamInformation) {
		smartExamInformationService.updateById(smartExamInformation);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "考试信息表-通过id删除")
	@ApiOperation(value="考试信息表-通过id删除", notes="考试信息表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		smartExamInformationService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "考试信息表-批量删除")
	@ApiOperation(value="考试信息表-批量删除", notes="考试信息表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartExamInformationService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "考试信息表-通过id查询")
	@ApiOperation(value="考试信息表-通过id查询", notes="考试信息表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartExamInformation smartExamInformation = smartExamInformationService.getById(id);
		if(smartExamInformation==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(smartExamInformation);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param smartExamInformation
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartExamInformation smartExamInformation) {
        return super.exportXls(request, smartExamInformation, SmartExamInformation.class, "考试信息表");
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
        return super.importExcel(request, response, SmartExamInformation.class);
    }

}
