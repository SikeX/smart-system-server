package org.jeecg.modules.smart_data_sheet_new.controller;

import java.util.Arrays;
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
import org.jeecg.modules.smart_data_sheet_new.entity.SmartDataSheetNew;
import org.jeecg.modules.smart_data_sheet_new.service.ISmartDataSheetNewService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.smart_window_unit.service.ISmartWindowUnitService;
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
 * @Description: 资料库
 * @Author: jeecg-boot
 * @Date:   2021-12-07
 * @Version: V1.0
 */
@Api(tags="资料库")
@RestController
@RequestMapping("/smart_data_sheet_new/smartDataSheetNew")
@Slf4j
public class SmartDataSheetNewController extends JeecgController<SmartDataSheetNew, ISmartDataSheetNewService> {
	@Autowired
	private ISmartDataSheetNewService smartDataSheetNewService;

	/**
	 * 分页列表查询
	 *
	 * @param smartDataSheetNew
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "资料库-分页列表查询")
	@ApiOperation(value="资料库-分页列表查询", notes="资料库-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartDataSheetNew smartDataSheetNew,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SmartDataSheetNew> queryWrapper = QueryGenerator.initQueryWrapper(smartDataSheetNew, req.getParameterMap());
		Page<SmartDataSheetNew> page = new Page<SmartDataSheetNew>(pageNo, pageSize);
		IPage<SmartDataSheetNew> pageList = smartDataSheetNewService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param smartDataSheetNew
	 * @return
	 */
	@AutoLog(value = "资料库-添加")
	@ApiOperation(value="资料库-添加", notes="资料库-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SmartDataSheetNew smartDataSheetNew) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String orgCode = sysUser.getOrgCode();
		if ("".equals(orgCode)) {
			return Result.error("本用户没有操作权限！");
		}
		String id = smartDataSheetNewService.getDepartIdByOrgCode(orgCode);
		String name =smartDataSheetNewService.getDepartNameById(id) ;
		smartDataSheetNew.setDepartmentid(name);
		String publishId = smartDataSheetNew.getCreateBy();
		String pName = smartDataSheetNewService.getUserNameById(publishId);
//		smartDataSheetNew.setPublisher(pName);


		smartDataSheetNewService.save(smartDataSheetNew);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param smartDataSheetNew
	 * @return
	 */
	@AutoLog(value = "资料库-编辑")
	@ApiOperation(value="资料库-编辑", notes="资料库-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SmartDataSheetNew smartDataSheetNew) {
		smartDataSheetNewService.updateById(smartDataSheetNew);
		return Result.OK("编辑成功!");
	}
	 @AutoLog(value = "更新文件下载次数")
	 @ApiOperation(value="更新文件下载次数", notes="更新文件下载次数")
	 @PutMapping(value = "/downloadCount")
	 public Result<?> downloadCount(@RequestBody SmartDataSheetNew smartDataSheetNew) {
		 SmartDataSheetNew newSmartDataSheetNew =
				 smartDataSheetNewService.getById(smartDataSheetNew.getId());
		 int currentCount = newSmartDataSheetNew.getTimes();
		 newSmartDataSheetNew.setTimes(currentCount + 1);
		 smartDataSheetNewService.updateById(newSmartDataSheetNew);
		 return Result.OK("更新成功!");
	 }
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "资料库-通过id删除")
	@ApiOperation(value="资料库-通过id删除", notes="资料库-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		smartDataSheetNewService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "资料库-批量删除")
	@ApiOperation(value="资料库-批量删除", notes="资料库-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartDataSheetNewService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "资料库-通过id查询")
	@ApiOperation(value="资料库-通过id查询", notes="资料库-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartDataSheetNew smartDataSheetNew = smartDataSheetNewService.getById(id);
		if(smartDataSheetNew==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(smartDataSheetNew);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param smartDataSheetNew
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartDataSheetNew smartDataSheetNew) {
        return super.exportXls(request, smartDataSheetNew, SmartDataSheetNew.class, "资料库");
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
        return super.importExcel(request, response, SmartDataSheetNew.class);
    }

}
