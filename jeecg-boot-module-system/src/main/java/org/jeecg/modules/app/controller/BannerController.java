package org.jeecg.modules.app.controller;

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
import org.jeecg.modules.app.entity.Banner;
import org.jeecg.modules.app.service.IBannerService;

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
 * @Description: app功能模块
 * @Author: jeecg-boot
 * @Date:   2021-11-08
 * @Version: V1.0
 */
@Api(tags="app功能模块")
@RestController
@RequestMapping("/app/banner")
@Slf4j
public class BannerController extends JeecgController<Banner, IBannerService> {
	@Autowired
	private IBannerService bannerService;
	
	/**
	 * 分页列表查询
	 *
	 * @param banner
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "app功能模块-分页列表查询")
	@ApiOperation(value="app功能模块-分页列表查询", notes="app功能模块-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Banner banner,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Banner> queryWrapper = QueryGenerator.initQueryWrapper(banner, req.getParameterMap());
		Page<Banner> page = new Page<Banner>(pageNo, pageSize);
		IPage<Banner> pageList = bannerService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param banner
	 * @return
	 */
	@AutoLog(value = "app功能模块-添加")
	@ApiOperation(value="app功能模块-添加", notes="app功能模块-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Banner banner) {
		bannerService.save(banner);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param banner
	 * @return
	 */
	@AutoLog(value = "app功能模块-编辑")
	@ApiOperation(value="app功能模块-编辑", notes="app功能模块-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Banner banner) {
		bannerService.updateById(banner);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "app功能模块-通过id删除")
	@ApiOperation(value="app功能模块-通过id删除", notes="app功能模块-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		bannerService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "app功能模块-批量删除")
	@ApiOperation(value="app功能模块-批量删除", notes="app功能模块-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.bannerService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "app功能模块-通过id查询")
	@ApiOperation(value="app功能模块-通过id查询", notes="app功能模块-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Banner banner = bannerService.getById(id);
		if(banner==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(banner);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param banner
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Banner banner) {
        return super.exportXls(request, banner, Banner.class, "app功能模块");
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
        return super.importExcel(request, response, Banner.class);
    }

}
