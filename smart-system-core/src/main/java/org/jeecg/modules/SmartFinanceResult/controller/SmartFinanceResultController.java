package org.jeecg.modules.SmartFinanceResult.controller;

import org.jeecg.common.system.query.QueryGenerator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import java.util.Arrays;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.SmartFinanceResult.entity.SmartFinanceAnnex;
import org.jeecg.modules.SmartFinanceResult.entity.SmartFinanceResult;
import org.jeecg.modules.SmartFinanceResult.service.ISmartFinanceResultService;
import org.jeecg.modules.SmartFinanceResult.service.ISmartFinanceAnnexService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

 /**
 * @Description: 8项规定财物收支表
 * @Author: jeecg-boot
 * @Date:   2021-11-03
 * @Version: V1.0
 */
@Api(tags="8项规定财物收支表")
@RestController
@RequestMapping("/SmartFinanceResult/smartFinanceResult")
@Slf4j
public class SmartFinanceResultController extends JeecgController<SmartFinanceResult, ISmartFinanceResultService> {

	@Autowired
	private ISmartFinanceResultService smartFinanceResultService;

	@Autowired
	private ISmartFinanceAnnexService smartFinanceAnnexService;


	/*---------------------------------主表处理-begin-------------------------------------*/

	/**
	 * 分页列表查询
	 * @param smartFinanceResult
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "8项规定财物收支表-分页列表查询")
	@ApiOperation(value="8项规定财物收支表-分页列表查询", notes="8项规定财物收支表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartFinanceResult smartFinanceResult,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SmartFinanceResult> queryWrapper = QueryGenerator.initQueryWrapper(smartFinanceResult, req.getParameterMap());
		Page<SmartFinanceResult> page = new Page<SmartFinanceResult>(pageNo, pageSize);
		IPage<SmartFinanceResult> pageList = smartFinanceResultService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
     *   添加
     * @param smartFinanceResult
     * @return
     */
    @AutoLog(value = "8项规定财物收支表-添加")
    @ApiOperation(value="8项规定财物收支表-添加", notes="8项规定财物收支表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SmartFinanceResult smartFinanceResult) {
        smartFinanceResultService.save(smartFinanceResult);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     * @param smartFinanceResult
     * @return
     */
    @AutoLog(value = "8项规定财物收支表-编辑")
    @ApiOperation(value="8项规定财物收支表-编辑", notes="8项规定财物收支表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SmartFinanceResult smartFinanceResult) {
        smartFinanceResultService.updateById(smartFinanceResult);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     * @param id
     * @return
     */
    @AutoLog(value = "8项规定财物收支表-通过id删除")
    @ApiOperation(value="8项规定财物收支表-通过id删除", notes="8项规定财物收支表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name="id",required=true) String id) {
        smartFinanceResultService.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @AutoLog(value = "8项规定财物收支表-批量删除")
    @ApiOperation(value="8项规定财物收支表-批量删除", notes="8项规定财物收支表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        this.smartFinanceResultService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 导出
     * @return
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartFinanceResult smartFinanceResult) {
        return super.exportXls(request, smartFinanceResult, SmartFinanceResult.class, "8项规定财物收支表");
    }

    /**
     * 导入
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, SmartFinanceResult.class);
    }
	/*---------------------------------主表处理-end-------------------------------------*/
	

    /*--------------------------------子表处理-8项规定财物收支附件-begin----------------------------------------------*/
	/**
	 * 通过主表ID查询
	 * @return
	 */
	@AutoLog(value = "8项规定财物收支附件-通过主表ID查询")
	@ApiOperation(value="8项规定财物收支附件-通过主表ID查询", notes="8项规定财物收支附件-通过主表ID查询")
	@GetMapping(value = "/listSmartFinanceAnnexByMainId")
    public Result<?> listSmartFinanceAnnexByMainId(SmartFinanceAnnex smartFinanceAnnex,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest req) {
        QueryWrapper<SmartFinanceAnnex> queryWrapper = QueryGenerator.initQueryWrapper(smartFinanceAnnex, req.getParameterMap());
        Page<SmartFinanceAnnex> page = new Page<SmartFinanceAnnex>(pageNo, pageSize);
        IPage<SmartFinanceAnnex> pageList = smartFinanceAnnexService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

	/**
	 * 添加
	 * @param smartFinanceAnnex
	 * @return
	 */
	@AutoLog(value = "8项规定财物收支附件-添加")
	@ApiOperation(value="8项规定财物收支附件-添加", notes="8项规定财物收支附件-添加")
	@PostMapping(value = "/addSmartFinanceAnnex")
	public Result<?> addSmartFinanceAnnex(@RequestBody SmartFinanceAnnex smartFinanceAnnex) {
		smartFinanceAnnexService.save(smartFinanceAnnex);
		return Result.OK("添加成功！");
	}

    /**
	 * 编辑
	 * @param smartFinanceAnnex
	 * @return
	 */
	@AutoLog(value = "8项规定财物收支附件-编辑")
	@ApiOperation(value="8项规定财物收支附件-编辑", notes="8项规定财物收支附件-编辑")
	@PutMapping(value = "/editSmartFinanceAnnex")
	public Result<?> editSmartFinanceAnnex(@RequestBody SmartFinanceAnnex smartFinanceAnnex) {
		smartFinanceAnnexService.updateById(smartFinanceAnnex);
		return Result.OK("编辑成功!");
	}

	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "8项规定财物收支附件-通过id删除")
	@ApiOperation(value="8项规定财物收支附件-通过id删除", notes="8项规定财物收支附件-通过id删除")
	@DeleteMapping(value = "/deleteSmartFinanceAnnex")
	public Result<?> deleteSmartFinanceAnnex(@RequestParam(name="id",required=true) String id) {
		smartFinanceAnnexService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "8项规定财物收支附件-批量删除")
	@ApiOperation(value="8项规定财物收支附件-批量删除", notes="8项规定财物收支附件-批量删除")
	@DeleteMapping(value = "/deleteBatchSmartFinanceAnnex")
	public Result<?> deleteBatchSmartFinanceAnnex(@RequestParam(name="ids",required=true) String ids) {
	    this.smartFinanceAnnexService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

    /**
     * 导出
     * @return
     */
    @RequestMapping(value = "/exportSmartFinanceAnnex")
    public ModelAndView exportSmartFinanceAnnex(HttpServletRequest request, SmartFinanceAnnex smartFinanceAnnex) {
		 // Step.1 组装查询条件
		 QueryWrapper<SmartFinanceAnnex> queryWrapper = QueryGenerator.initQueryWrapper(smartFinanceAnnex, request.getParameterMap());
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		 // Step.2 获取导出数据
		 List<SmartFinanceAnnex> pageList = smartFinanceAnnexService.list(queryWrapper);
		 List<SmartFinanceAnnex> exportList = null;

		 // 过滤选中数据
		 String selections = request.getParameter("selections");
		 if (oConvertUtils.isNotEmpty(selections)) {
			 List<String> selectionList = Arrays.asList(selections.split(","));
			 exportList = pageList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
		 } else {
			 exportList = pageList;
		 }

		 // Step.3 AutoPoi 导出Excel
		 ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		 mv.addObject(NormalExcelConstants.FILE_NAME, "8项规定财物收支附件"); //此处设置的filename无效 ,前端会重更新设置一下
		 mv.addObject(NormalExcelConstants.CLASS, SmartFinanceAnnex.class);
		 mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("8项规定财物收支附件报表", "导出人:" + sysUser.getRealname(), "8项规定财物收支附件"));
		 mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
		 return mv;
    }

    /**
     * 导入
     * @return
     */
    @RequestMapping(value = "/importSmartFinanceAnnex/{mainId}")
    public Result<?> importSmartFinanceAnnex(HttpServletRequest request, HttpServletResponse response, @PathVariable("mainId") String mainId) {
		 MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		 Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		 for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			 MultipartFile file = entity.getValue();// 获取上传文件对象
			 ImportParams params = new ImportParams();
			 params.setTitleRows(2);
			 params.setHeadRows(1);
			 params.setNeedSave(true);
			 try {
				 List<SmartFinanceAnnex> list = ExcelImportUtil.importExcel(file.getInputStream(), SmartFinanceAnnex.class, params);
				 for (SmartFinanceAnnex temp : list) {
                    temp.setParentId(mainId);
				 }
				 long start = System.currentTimeMillis();
				 smartFinanceAnnexService.saveBatch(list);
				 log.info("消耗时间" + (System.currentTimeMillis() - start) + "毫秒");
				 return Result.OK("文件导入成功！数据行数：" + list.size());
			 } catch (Exception e) {
				 log.error(e.getMessage(), e);
				 return Result.error("文件导入失败:" + e.getMessage());
			 } finally {
				 try {
					 file.getInputStream().close();
				 } catch (IOException e) {
					 e.printStackTrace();
				 }
			 }
		 }
		 return Result.error("文件导入失败！");
    }

    /*--------------------------------子表处理-8项规定财物收支附件-end----------------------------------------------*/




}
