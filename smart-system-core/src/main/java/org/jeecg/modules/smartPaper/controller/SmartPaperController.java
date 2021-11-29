package org.jeecg.modules.smartPaper.controller;

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
import org.jeecg.modules.smartPaper.entity.SmartPaper;
import org.jeecg.modules.smartPaper.service.ISmartPaperService;

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
 * @Description: 试卷表
 * @Author: jeecg-boot
 * @Date:   2021-11-22
 * @Version: V1.0
 */
@Api(tags="试卷表")
@RestController
@RequestMapping("/smartPaper/smartPaper")
@Slf4j
public class SmartPaperController extends JeecgController<SmartPaper, ISmartPaperService> {
	@Autowired
	private ISmartPaperService smartPaperService;
	@Autowired
	private ISmartExamInformationService smartExamInformationService;
	/**
	 * 分页列表查询
	 *
	 * @param smartPaper
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "试卷表-分页列表查询")
	@ApiOperation(value="试卷表-分页列表查询", notes="试卷表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartPaper smartPaper,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SmartPaper> queryWrapper = QueryGenerator.initQueryWrapper(smartPaper, req.getParameterMap());
		Page<SmartPaper> page = new Page<SmartPaper>(pageNo, pageSize);
		IPage<SmartPaper> pageList = smartPaperService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param smartPaper
	 * @return
	 */
	@AutoLog(value = "试卷表-添加")
	@ApiOperation(value="试卷表-添加", notes="试卷表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SmartPaper smartPaper) {
		SmartExamInformation smartExamInformation=new SmartExamInformation();
		//考试信息表的考试姓名对应试卷表的试卷姓名
		smartExamInformation.setExamName(smartPaper.getPaperName());
		smartExamInformationService.save(smartExamInformation);
		smartPaperService.save(smartPaper);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param smartPaper
	 * @return
	 */
	@AutoLog(value = "试卷表-编辑")
	@ApiOperation(value="试卷表-编辑", notes="试卷表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SmartPaper smartPaper) {
		smartPaperService.updateById(smartPaper);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "试卷表-通过id删除")
	@ApiOperation(value="试卷表-通过id删除", notes="试卷表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		smartPaperService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "试卷表-批量删除")
	@ApiOperation(value="试卷表-批量删除", notes="试卷表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartPaperService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "试卷表-通过id查询")
	@ApiOperation(value="试卷表-通过id查询", notes="试卷表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartPaper smartPaper = smartPaperService.getById(id);
		if(smartPaper==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(smartPaper);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param smartPaper
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartPaper smartPaper) {
        return super.exportXls(request, smartPaper, SmartPaper.class, "试卷表");
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
        return super.importExcel(request, response, SmartPaper.class);
    }

}
