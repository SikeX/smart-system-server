package org.jeecg.modules.smartTripleImportanceOneGreatness.controller;

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
import org.jeecg.modules.smartTripleImportanceOneGreatness.entity.SmartTripleImportanceOneGreatnessDecription;
import org.jeecg.modules.smartTripleImportanceOneGreatness.entity.SmartTripleImportanceOneGreatness;
import org.jeecg.modules.smartTripleImportanceOneGreatness.service.ISmartTripleImportanceOneGreatnessService;
import org.jeecg.modules.smartTripleImportanceOneGreatness.service.ISmartTripleImportanceOneGreatnessDecriptionService;
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
 * @Description: 三重一大表
 * @Author: jeecg-boot
 * @Date:   2021-11-04
 * @Version: V1.0
 */
@Api(tags="三重一大表")
@RestController
@RequestMapping("/smartTripleImportanceOneGreatness/smartTripleImportanceOneGreatness")
@Slf4j
public class SmartTripleImportanceOneGreatnessController extends JeecgController<SmartTripleImportanceOneGreatness, ISmartTripleImportanceOneGreatnessService> {

	@Autowired
	private ISmartTripleImportanceOneGreatnessService smartTripleImportanceOneGreatnessService;

	@Autowired
	private ISmartTripleImportanceOneGreatnessDecriptionService smartTripleImportanceOneGreatnessDecriptionService;


	/*---------------------------------主表处理-begin-------------------------------------*/

	/**
	 * 分页列表查询
	 * @param smartTripleImportanceOneGreatness
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "三重一大表-分页列表查询")
	@ApiOperation(value="三重一大表-分页列表查询", notes="三重一大表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartTripleImportanceOneGreatness smartTripleImportanceOneGreatness,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SmartTripleImportanceOneGreatness> queryWrapper = QueryGenerator.initQueryWrapper(smartTripleImportanceOneGreatness, req.getParameterMap());
		Page<SmartTripleImportanceOneGreatness> page = new Page<SmartTripleImportanceOneGreatness>(pageNo, pageSize);
		IPage<SmartTripleImportanceOneGreatness> pageList = smartTripleImportanceOneGreatnessService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
     *   添加
     * @param smartTripleImportanceOneGreatness
     * @return
     */
    @AutoLog(value = "三重一大表-添加")
    @ApiOperation(value="三重一大表-添加", notes="三重一大表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SmartTripleImportanceOneGreatness smartTripleImportanceOneGreatness) {
        smartTripleImportanceOneGreatnessService.save(smartTripleImportanceOneGreatness);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     * @param smartTripleImportanceOneGreatness
     * @return
     */
    @AutoLog(value = "三重一大表-编辑")
    @ApiOperation(value="三重一大表-编辑", notes="三重一大表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SmartTripleImportanceOneGreatness smartTripleImportanceOneGreatness) {
        smartTripleImportanceOneGreatnessService.updateById(smartTripleImportanceOneGreatness);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     * @param id
     * @return
     */
    @AutoLog(value = "三重一大表-通过id删除")
    @ApiOperation(value="三重一大表-通过id删除", notes="三重一大表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name="id",required=true) String id) {
        smartTripleImportanceOneGreatnessService.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @AutoLog(value = "三重一大表-批量删除")
    @ApiOperation(value="三重一大表-批量删除", notes="三重一大表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        this.smartTripleImportanceOneGreatnessService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 导出
     * @return
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartTripleImportanceOneGreatness smartTripleImportanceOneGreatness) {
        return super.exportXls(request, smartTripleImportanceOneGreatness, SmartTripleImportanceOneGreatness.class, "三重一大表");
    }

    /**
     * 导入
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, SmartTripleImportanceOneGreatness.class);
    }
	/*---------------------------------主表处理-end-------------------------------------*/
	

    /*--------------------------------子表处理-三重一大附件表-begin----------------------------------------------*/
	/**
	 * 通过主表ID查询
	 * @return
	 */
	@AutoLog(value = "三重一大附件表-通过主表ID查询")
	@ApiOperation(value="三重一大附件表-通过主表ID查询", notes="三重一大附件表-通过主表ID查询")
	@GetMapping(value = "/listSmartTripleImportanceOneGreatnessDecriptionByMainId")
    public Result<?> listSmartTripleImportanceOneGreatnessDecriptionByMainId(SmartTripleImportanceOneGreatnessDecription smartTripleImportanceOneGreatnessDecription,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest req) {
        QueryWrapper<SmartTripleImportanceOneGreatnessDecription> queryWrapper = QueryGenerator.initQueryWrapper(smartTripleImportanceOneGreatnessDecription, req.getParameterMap());
        Page<SmartTripleImportanceOneGreatnessDecription> page = new Page<SmartTripleImportanceOneGreatnessDecription>(pageNo, pageSize);
        IPage<SmartTripleImportanceOneGreatnessDecription> pageList = smartTripleImportanceOneGreatnessDecriptionService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

	/**
	 * 添加
	 * @param smartTripleImportanceOneGreatnessDecription
	 * @return
	 */
	@AutoLog(value = "三重一大附件表-添加")
	@ApiOperation(value="三重一大附件表-添加", notes="三重一大附件表-添加")
	@PostMapping(value = "/addSmartTripleImportanceOneGreatnessDecription")
	public Result<?> addSmartTripleImportanceOneGreatnessDecription(@RequestBody SmartTripleImportanceOneGreatnessDecription smartTripleImportanceOneGreatnessDecription) {
		smartTripleImportanceOneGreatnessDecriptionService.save(smartTripleImportanceOneGreatnessDecription);
		return Result.OK("添加成功！");
	}

    /**
	 * 编辑
	 * @param smartTripleImportanceOneGreatnessDecription
	 * @return
	 */
	@AutoLog(value = "三重一大附件表-编辑")
	@ApiOperation(value="三重一大附件表-编辑", notes="三重一大附件表-编辑")
	@PutMapping(value = "/editSmartTripleImportanceOneGreatnessDecription")
	public Result<?> editSmartTripleImportanceOneGreatnessDecription(@RequestBody SmartTripleImportanceOneGreatnessDecription smartTripleImportanceOneGreatnessDecription) {
		smartTripleImportanceOneGreatnessDecriptionService.updateById(smartTripleImportanceOneGreatnessDecription);
		return Result.OK("编辑成功!");
	}

	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "三重一大附件表-通过id删除")
	@ApiOperation(value="三重一大附件表-通过id删除", notes="三重一大附件表-通过id删除")
	@DeleteMapping(value = "/deleteSmartTripleImportanceOneGreatnessDecription")
	public Result<?> deleteSmartTripleImportanceOneGreatnessDecription(@RequestParam(name="id",required=true) String id) {
		smartTripleImportanceOneGreatnessDecriptionService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "三重一大附件表-批量删除")
	@ApiOperation(value="三重一大附件表-批量删除", notes="三重一大附件表-批量删除")
	@DeleteMapping(value = "/deleteBatchSmartTripleImportanceOneGreatnessDecription")
	public Result<?> deleteBatchSmartTripleImportanceOneGreatnessDecription(@RequestParam(name="ids",required=true) String ids) {
	    this.smartTripleImportanceOneGreatnessDecriptionService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

    /**
     * 导出
     * @return
     */
    @RequestMapping(value = "/exportSmartTripleImportanceOneGreatnessDecription")
    public ModelAndView exportSmartTripleImportanceOneGreatnessDecription(HttpServletRequest request, SmartTripleImportanceOneGreatnessDecription smartTripleImportanceOneGreatnessDecription) {
		 // Step.1 组装查询条件
		 QueryWrapper<SmartTripleImportanceOneGreatnessDecription> queryWrapper = QueryGenerator.initQueryWrapper(smartTripleImportanceOneGreatnessDecription, request.getParameterMap());
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		 // Step.2 获取导出数据
		 List<SmartTripleImportanceOneGreatnessDecription> pageList = smartTripleImportanceOneGreatnessDecriptionService.list(queryWrapper);
		 List<SmartTripleImportanceOneGreatnessDecription> exportList = null;

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
		 mv.addObject(NormalExcelConstants.FILE_NAME, "三重一大附件表"); //此处设置的filename无效 ,前端会重更新设置一下
		 mv.addObject(NormalExcelConstants.CLASS, SmartTripleImportanceOneGreatnessDecription.class);
		 mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("三重一大附件表报表", "导出人:" + sysUser.getRealname(), "三重一大附件表"));
		 mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
		 return mv;
    }

    /**
     * 导入
     * @return
     */
    @RequestMapping(value = "/importSmartTripleImportanceOneGreatnessDecription/{mainId}")
    public Result<?> importSmartTripleImportanceOneGreatnessDecription(HttpServletRequest request, HttpServletResponse response, @PathVariable("mainId") String mainId) {
		 MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		 Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		 for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			 MultipartFile file = entity.getValue();// 获取上传文件对象
			 ImportParams params = new ImportParams();
			 params.setTitleRows(2);
			 params.setHeadRows(1);
			 params.setNeedSave(true);
			 try {
				 List<SmartTripleImportanceOneGreatnessDecription> list = ExcelImportUtil.importExcel(file.getInputStream(), SmartTripleImportanceOneGreatnessDecription.class, params);
				 for (SmartTripleImportanceOneGreatnessDecription temp : list) {
                    temp.setMeetingId(mainId);
				 }
				 long start = System.currentTimeMillis();
				 smartTripleImportanceOneGreatnessDecriptionService.saveBatch(list);
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

    /*--------------------------------子表处理-三重一大附件表-end----------------------------------------------*/




}
