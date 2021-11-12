package org.jeecg.modules.smart_window_unit.controller;

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
import org.jeecg.modules.smart_window_unit.entity.SmartWindowUnit;
import org.jeecg.modules.smart_window_unit.service.ISmartWindowUnitService;

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


import org.jeecg.modules.smart_window_unit.service.CodeService;
import org.jeecg.modules.smart_window_unit.api.response.BaseResponse;
import org.jeecg.modules.smart_window_unit.api.response.StatusCode;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.smart_window_unit.entity.SmartWindowUnit;
import org.jeecg.modules.smart_window_unit.service.ISmartWindowUnitService;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class BaseController {

	protected static Logger log= LoggerFactory.getLogger(BaseController.class);

}


 /**
 * @Description: 窗口单位
 * @Author: jeecg-boot
 * @Date:   2021-11-11
 * @Version: V1.0
 */
@Api(tags="窗口单位")
@RestController
@RequestMapping("/smart_window_unit/smartWindowUnit")
@Slf4j
public class SmartWindowUnitController extends JeecgController<SmartWindowUnit, ISmartWindowUnitService>{
	@Autowired
	private ISmartWindowUnitService smartWindowUnitService;


	 private CodeService codeService;

	 private static final String RootPath="E:\\Text\\QRCode";

	 private static final String FileFormat=".png";

	 private static final ThreadLocal<SimpleDateFormat> LOCALDATEFORMAT=ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMMddHHmmss"));


	/**
	 * 分页列表查询
	 *
	 * @param smartWindowUnit
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "窗口单位-分页列表查询")
	@ApiOperation(value="窗口单位-分页列表查询", notes="窗口单位-分页列表查询")
	@GetMapping(value = "/rootList")
	public Result<?> queryPageList(SmartWindowUnit smartWindowUnit,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		String hasQuery = req.getParameter("hasQuery");
        if(hasQuery != null && "true".equals(hasQuery)){
            QueryWrapper<SmartWindowUnit> queryWrapper =  QueryGenerator.initQueryWrapper(smartWindowUnit, req.getParameterMap());
            List<SmartWindowUnit> list = smartWindowUnitService.queryTreeListNoPage(queryWrapper);
            IPage<SmartWindowUnit> pageList = new Page<>(1, 10, list.size());
            pageList.setRecords(list);
            return Result.OK(pageList);
        }else{
            String parentId = smartWindowUnit.getPid();
            if (oConvertUtils.isEmpty(parentId)) {
                parentId = "0";
            }
            smartWindowUnit.setPid(null);
            QueryWrapper<SmartWindowUnit> queryWrapper = QueryGenerator.initQueryWrapper(smartWindowUnit, req.getParameterMap());
            // 使用 eq 防止模糊查询
            queryWrapper.eq("pid", parentId);
            Page<SmartWindowUnit> page = new Page<SmartWindowUnit>(pageNo, pageSize);
            IPage<SmartWindowUnit> pageList = smartWindowUnitService.page(page, queryWrapper);
            return Result.OK(pageList);
        }
	}

	 /**
      * 获取子数据
      * @param smartWindowUnit
      * @param req
      * @return
      */
	@AutoLog(value = "窗口单位-获取子数据")
	@ApiOperation(value="窗口单位-获取子数据", notes="窗口单位-获取子数据")
	@GetMapping(value = "/childList")
	public Result<?> queryPageList(SmartWindowUnit smartWindowUnit,HttpServletRequest req) {
		QueryWrapper<SmartWindowUnit> queryWrapper = QueryGenerator.initQueryWrapper(smartWindowUnit, req.getParameterMap());
		List<SmartWindowUnit> list = smartWindowUnitService.list(queryWrapper);
		IPage<SmartWindowUnit> pageList = new Page<>(1, 10, list.size());
        pageList.setRecords(list);
		return Result.OK(pageList);
	}

    /**
      * 批量查询子节点
      * @param parentIds 父ID（多个采用半角逗号分割）
      * @return 返回 IPage
      * @param parentIds
      * @return
      */
	@AutoLog(value = "窗口单位-批量获取子数据")
    @ApiOperation(value="窗口单位-批量获取子数据", notes="窗口单位-批量获取子数据")
    @GetMapping("/getChildListBatch")
    public Result getChildListBatch(@RequestParam("parentIds") String parentIds) {
        try {
            QueryWrapper<SmartWindowUnit> queryWrapper = new QueryWrapper<>();
            List<String> parentIdList = Arrays.asList(parentIds.split(","));
            queryWrapper.in("pid", parentIdList);
            List<SmartWindowUnit> list = smartWindowUnitService.list(queryWrapper);
            IPage<SmartWindowUnit> pageList = new Page<>(1, 10, list.size());
            pageList.setRecords(list);
            return Result.OK(pageList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("批量查询子节点失败：" + e.getMessage());
        }
    }

	/**
	 *   添加
	 *
	 * @param smartWindowUnit
	 * @return
	 */
	@AutoLog(value = "窗口单位-添加")
	@ApiOperation(value="窗口单位-添加", notes="窗口单位-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SmartWindowUnit smartWindowUnit) {
		smartWindowUnitService.addSmartWindowUnit(smartWindowUnit);
		// 1. 根据ID生成二维码，并存储到本地

		String content = smartWindowUnit.getId();

		BaseResponse response=new BaseResponse(StatusCode.Success);
		try {
			//将生成的二维码文件存放于文件目录中
			final String fileName=LOCALDATEFORMAT.get().format(new Date());
			codeService.createCodeToFile(content,RootPath+ File.separator+fileName+".png");


		}catch (Exception e){
			response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
		}
		// 2. 将存储路径保存到 smartWindowsUnit
		smartWindowUnit.setQrcode("D://doc//study//Code//Java//smart-system-server//upload_file");
		// 3. 调用  edit() 更新数据
		edit(smartWindowUnit);
		return Result.OK("添加成功！");

	}

	/**
	 *  编辑
	 *
	 * @param smartWindowUnit
	 * @return
	 */
	@AutoLog(value = "窗口单位-编辑")
	@ApiOperation(value="窗口单位-编辑", notes="窗口单位-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SmartWindowUnit smartWindowUnit) {
		smartWindowUnitService.updateSmartWindowUnit(smartWindowUnit);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "窗口单位-通过id删除")
	@ApiOperation(value="窗口单位-通过id删除", notes="窗口单位-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		smartWindowUnitService.deleteSmartWindowUnit(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "窗口单位-批量删除")
	@ApiOperation(value="窗口单位-批量删除", notes="窗口单位-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartWindowUnitService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "窗口单位-通过id查询")
	@ApiOperation(value="窗口单位-通过id查询", notes="窗口单位-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartWindowUnit smartWindowUnit = smartWindowUnitService.getById(id);
		if(smartWindowUnit==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(smartWindowUnit);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param smartWindowUnit
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartWindowUnit smartWindowUnit) {
		return super.exportXls(request, smartWindowUnit, SmartWindowUnit.class, "窗口单位");
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
		return super.importExcel(request, response, SmartWindowUnit.class);
    }

}
