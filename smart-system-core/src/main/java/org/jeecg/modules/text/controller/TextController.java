package org.jeecg.modules.text.controller;

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
import org.jeecg.modules.text.entity.Text;
import org.jeecg.modules.text.service.ITextService;

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
 * @Description: tet
 * @Author: jeecg-boot
 * @Date:   2021-11-06
 * @Version: V1.0
 */
@Api(tags="tet")
@RestController
@RequestMapping("/text/text")
@Slf4j
public class TextController extends JeecgController<Text, ITextService> {
	@Autowired
	private ITextService textService;
	
	/**
	 * 分页列表查询
	 *
	 * @param text
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "tet-分页列表查询")
	@ApiOperation(value="tet-分页列表查询", notes="tet-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Text text,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Text> queryWrapper = QueryGenerator.initQueryWrapper(text, req.getParameterMap());
		Page<Text> page = new Page<Text>(pageNo, pageSize);
		IPage<Text> pageList = textService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param text
	 * @return
	 */
	@AutoLog(value = "tet-添加")
	@ApiOperation(value="tet-添加", notes="tet-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Text text) {
		textService.save(text);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param text
	 * @return
	 */
	@AutoLog(value = "tet-编辑")
	@ApiOperation(value="tet-编辑", notes="tet-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Text text) {
		textService.updateById(text);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "tet-通过id删除")
	@ApiOperation(value="tet-通过id删除", notes="tet-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		textService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "tet-批量删除")
	@ApiOperation(value="tet-批量删除", notes="tet-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.textService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "tet-通过id查询")
	@ApiOperation(value="tet-通过id查询", notes="tet-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Text text = textService.getById(id);
		if(text==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(text);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param text
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Text text) {
        return super.exportXls(request, text, Text.class, "tet");
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
        return super.importExcel(request, response, Text.class);
    }

}
