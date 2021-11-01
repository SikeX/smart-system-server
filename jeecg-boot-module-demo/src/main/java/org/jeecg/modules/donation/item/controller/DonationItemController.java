package org.jeecg.modules.donation.item.controller;

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
import org.jeecg.modules.donation.item.entity.DonationItem;
import org.jeecg.modules.donation.item.service.IDonationItemService;

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
 * @Description: 捐赠项目
 * @Author: jeecg-boot
 * @Date:   2021-10-22
 * @Version: V1.0
 */
@Api(tags="捐赠项目")
@RestController
@RequestMapping("/item/donationItem")
@Slf4j
public class DonationItemController extends JeecgController<DonationItem, IDonationItemService> {
	@Autowired
	private IDonationItemService donationItemService;
	
	/**
	 * 分页列表查询
	 *
	 * @param donationItem
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "捐赠项目-分页列表查询")
	@ApiOperation(value="捐赠项目-分页列表查询", notes="捐赠项目-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DonationItem donationItem,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<DonationItem> queryWrapper = QueryGenerator.initQueryWrapper(donationItem, req.getParameterMap());
		Page<DonationItem> page = new Page<DonationItem>(pageNo, pageSize);
		IPage<DonationItem> pageList = donationItemService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param donationItem
	 * @return
	 */
	@AutoLog(value = "捐赠项目-添加")
	@ApiOperation(value="捐赠项目-添加", notes="捐赠项目-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DonationItem donationItem) {
		donationItemService.save(donationItem);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param donationItem
	 * @return
	 */
	@AutoLog(value = "捐赠项目-编辑")
	@ApiOperation(value="捐赠项目-编辑", notes="捐赠项目-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DonationItem donationItem) {
		donationItemService.updateById(donationItem);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "捐赠项目-通过id删除")
	@ApiOperation(value="捐赠项目-通过id删除", notes="捐赠项目-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		donationItemService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "捐赠项目-批量删除")
	@ApiOperation(value="捐赠项目-批量删除", notes="捐赠项目-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.donationItemService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "捐赠项目-通过id查询")
	@ApiOperation(value="捐赠项目-通过id查询", notes="捐赠项目-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DonationItem donationItem = donationItemService.getById(id);
		if(donationItem==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(donationItem);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param donationItem
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DonationItem donationItem) {
        return super.exportXls(request, donationItem, DonationItem.class, "捐赠项目");
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
        return super.importExcel(request, response, DonationItem.class);
    }

}
