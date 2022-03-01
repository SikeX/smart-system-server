package org.jeecg.modules.smart_reception.controller;

import org.jeecg.common.system.query.QueryGenerator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.smart_8_escorted_meal.controller.Smart_8EscortedMealController;
import org.jeecg.modules.smart_8_escorted_meal.entity.Smart_8EscortedMeal;
import org.jeecg.modules.smart_8_escorted_meal.service.ISmart_8EscortedMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import java.util.Arrays;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.smart_reception.entity.Smart_8Visitor;
import org.jeecg.modules.smart_reception.entity.Smart_8Stay;
import org.jeecg.modules.smart_reception.entity.Smart_8Dining;
import org.jeecg.modules.smart_reception.entity.Smart_8List;
import org.jeecg.modules.smart_reception.entity.SmartReception;
import org.jeecg.modules.smart_reception.service.ISmartReceptionService;
import org.jeecg.modules.smart_reception.service.ISmart_8VisitorService;
import org.jeecg.modules.smart_reception.service.ISmart_8StayService;
import org.jeecg.modules.smart_reception.service.ISmart_8DiningService;
import org.jeecg.modules.smart_reception.service.ISmart_8ListService;
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
 * @Description: 公务接待2.0
 * @Author: jeecg-boot
 * @Date:   2022-02-28
 * @Version: V1.0
 */
@Api(tags="公务接待2.0")
@RestController
@RequestMapping("/smart_reception/smartReception")
@Slf4j
public class SmartReceptionController extends JeecgController<SmartReception, ISmartReceptionService> {

	@Autowired
	private ISmartReceptionService smartReceptionService;

	@Autowired
	private ISmart_8VisitorService smart_8VisitorService;

	@Autowired
	private ISmart_8StayService smart_8StayService;

	@Autowired
	private ISmart_8DiningService smart_8DiningService;

	@Autowired
	private ISmart_8ListService smart_8ListService;

	@Autowired
	private ISmart_8EscortedMealService smart_8EscortedMealService;


	/*---------------------------------主表处理-begin-------------------------------------*/

	/**
	 * 分页列表查询
	 * @param smartReception
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "公务接待2.0-分页列表查询")
	@ApiOperation(value="公务接待2.0-分页列表查询", notes="公务接待2.0-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartReception smartReception,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SmartReception> queryWrapper = QueryGenerator.initQueryWrapper(smartReception, req.getParameterMap());
		Page<SmartReception> page = new Page<SmartReception>(pageNo, pageSize);
		IPage<SmartReception> pageList = smartReceptionService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
     *   添加
     * @param smartReception
     * @return
     */
    @AutoLog(value = "公务接待2.0-添加")
    @ApiOperation(value="公务接待2.0-添加", notes="公务接待2.0-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SmartReception smartReception) {
        smartReceptionService.save(smartReception);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     * @param smartReception
     * @return
     */
    @AutoLog(value = "公务接待2.0-编辑")
    @ApiOperation(value="公务接待2.0-编辑", notes="公务接待2.0-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SmartReception smartReception) {
        smartReceptionService.updateById(smartReception);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     * @param id
     * @return
     */
    @AutoLog(value = "公务接待2.0-通过id删除")
    @ApiOperation(value="公务接待2.0-通过id删除", notes="公务接待2.0-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name="id",required=true) String id) {
        smartReceptionService.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @AutoLog(value = "公务接待2.0-批量删除")
    @ApiOperation(value="公务接待2.0-批量删除", notes="公务接待2.0-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        this.smartReceptionService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 导出
     * @return
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartReception smartReception) {
        return super.exportXls(request, smartReception, SmartReception.class, "公务接待2.0");
    }

    /**
     * 导入
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, SmartReception.class);
    }
	/*---------------------------------主表处理-end-------------------------------------*/
	

    /*--------------------------------子表处理-来访人员信息表-begin----------------------------------------------*/
	/**
	 * 通过主表ID查询
	 * @return
	 */
	@AutoLog(value = "来访人员信息表-通过主表ID查询")
	@ApiOperation(value="来访人员信息表-通过主表ID查询", notes="来访人员信息表-通过主表ID查询")
	@GetMapping(value = "/listSmart_8VisitorByMainId")
    public Result<?> listSmart_8VisitorByMainId(Smart_8Visitor smart_8Visitor,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest req) {
        QueryWrapper<Smart_8Visitor> queryWrapper = QueryGenerator.initQueryWrapper(smart_8Visitor, req.getParameterMap());
        Page<Smart_8Visitor> page = new Page<Smart_8Visitor>(pageNo, pageSize);
        IPage<Smart_8Visitor> pageList = smart_8VisitorService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

	/**
	 * 添加
	 * @param smart_8Visitor
	 * @return
	 */
	@AutoLog(value = "来访人员信息表-添加")
	@ApiOperation(value="来访人员信息表-添加", notes="来访人员信息表-添加")
	@PostMapping(value = "/addSmart_8Visitor")
	public Result<?> addSmart_8Visitor(@RequestBody Smart_8Visitor smart_8Visitor) {
		smart_8VisitorService.save(smart_8Visitor);
		return Result.OK("添加成功！");
	}

    /**
	 * 编辑
	 * @param smart_8Visitor
	 * @return
	 */
	@AutoLog(value = "来访人员信息表-编辑")
	@ApiOperation(value="来访人员信息表-编辑", notes="来访人员信息表-编辑")
	@PutMapping(value = "/editSmart_8Visitor")
	public Result<?> editSmart_8Visitor(@RequestBody Smart_8Visitor smart_8Visitor) {
		smart_8VisitorService.updateById(smart_8Visitor);
		return Result.OK("编辑成功!");
	}

	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "来访人员信息表-通过id删除")
	@ApiOperation(value="来访人员信息表-通过id删除", notes="来访人员信息表-通过id删除")
	@DeleteMapping(value = "/deleteSmart_8Visitor")
	public Result<?> deleteSmart_8Visitor(@RequestParam(name="id",required=true) String id) {
		smart_8VisitorService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "来访人员信息表-批量删除")
	@ApiOperation(value="来访人员信息表-批量删除", notes="来访人员信息表-批量删除")
	@DeleteMapping(value = "/deleteBatchSmart_8Visitor")
	public Result<?> deleteBatchSmart_8Visitor(@RequestParam(name="ids",required=true) String ids) {
	    this.smart_8VisitorService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

    /**
     * 导出
     * @return
     */
    @RequestMapping(value = "/exportSmart_8Visitor")
    public ModelAndView exportSmart_8Visitor(HttpServletRequest request, Smart_8Visitor smart_8Visitor) {
		 // Step.1 组装查询条件
		 QueryWrapper<Smart_8Visitor> queryWrapper = QueryGenerator.initQueryWrapper(smart_8Visitor, request.getParameterMap());
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		 // Step.2 获取导出数据
		 List<Smart_8Visitor> pageList = smart_8VisitorService.list(queryWrapper);
		 List<Smart_8Visitor> exportList = null;

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
		 mv.addObject(NormalExcelConstants.FILE_NAME, "来访人员信息表"); //此处设置的filename无效 ,前端会重更新设置一下
		 mv.addObject(NormalExcelConstants.CLASS, Smart_8Visitor.class);
		 mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("来访人员信息表报表", "导出人:" + sysUser.getRealname(), "来访人员信息表"));
		 mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
		 return mv;
    }

    /**
     * 导入
     * @return
     */
    @RequestMapping(value = "/importSmart_8Visitor/{mainId}")
    public Result<?> importSmart_8Visitor(HttpServletRequest request, HttpServletResponse response, @PathVariable("mainId") String mainId) {
		 MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		 Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		 for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			 MultipartFile file = entity.getValue();// 获取上传文件对象
			 ImportParams params = new ImportParams();
			 params.setTitleRows(2);
			 params.setHeadRows(1);
			 params.setNeedSave(true);
			 try {
				 List<Smart_8Visitor> list = ExcelImportUtil.importExcel(file.getInputStream(), Smart_8Visitor.class, params);
				 for (Smart_8Visitor temp : list) {
                    temp.setMainId(mainId);
				 }
				 long start = System.currentTimeMillis();
				 smart_8VisitorService.saveBatch(list);
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

    /*--------------------------------子表处理-来访人员信息表-end----------------------------------------------*/

    /*--------------------------------子表处理-住宿信息-begin----------------------------------------------*/
	/**
	 * 通过主表ID查询
	 * @return
	 */
	@AutoLog(value = "住宿信息-通过主表ID查询")
	@ApiOperation(value="住宿信息-通过主表ID查询", notes="住宿信息-通过主表ID查询")
	@GetMapping(value = "/listSmart_8StayByMainId")
    public Result<?> listSmart_8StayByMainId(Smart_8Stay smart_8Stay,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest req) {
        QueryWrapper<Smart_8Stay> queryWrapper = QueryGenerator.initQueryWrapper(smart_8Stay, req.getParameterMap());
        Page<Smart_8Stay> page = new Page<Smart_8Stay>(pageNo, pageSize);
        IPage<Smart_8Stay> pageList = smart_8StayService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

	/**
	 * 添加
	 * @param smart_8Stay
	 * @return
	 */
	@AutoLog(value = "住宿信息-添加")
	@ApiOperation(value="住宿信息-添加", notes="住宿信息-添加")
	@PostMapping(value = "/addSmart_8Stay")
	public Result<?> addSmart_8Stay(@RequestBody Smart_8Stay smart_8Stay) {
		smart_8StayService.save(smart_8Stay);
		return Result.OK("添加成功！");
	}

    /**
	 * 编辑
	 * @param smart_8Stay
	 * @return
	 */
	@AutoLog(value = "住宿信息-编辑")
	@ApiOperation(value="住宿信息-编辑", notes="住宿信息-编辑")
	@PutMapping(value = "/editSmart_8Stay")
	public Result<?> editSmart_8Stay(@RequestBody Smart_8Stay smart_8Stay) {
		smart_8StayService.updateById(smart_8Stay);
		return Result.OK("编辑成功!");
	}

	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "住宿信息-通过id删除")
	@ApiOperation(value="住宿信息-通过id删除", notes="住宿信息-通过id删除")
	@DeleteMapping(value = "/deleteSmart_8Stay")
	public Result<?> deleteSmart_8Stay(@RequestParam(name="id",required=true) String id) {
		smart_8StayService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "住宿信息-批量删除")
	@ApiOperation(value="住宿信息-批量删除", notes="住宿信息-批量删除")
	@DeleteMapping(value = "/deleteBatchSmart_8Stay")
	public Result<?> deleteBatchSmart_8Stay(@RequestParam(name="ids",required=true) String ids) {
	    this.smart_8StayService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

    /**
     * 导出
     * @return
     */
    @RequestMapping(value = "/exportSmart_8Stay")
    public ModelAndView exportSmart_8Stay(HttpServletRequest request, Smart_8Stay smart_8Stay) {
		 // Step.1 组装查询条件
		 QueryWrapper<Smart_8Stay> queryWrapper = QueryGenerator.initQueryWrapper(smart_8Stay, request.getParameterMap());
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		 // Step.2 获取导出数据
		 List<Smart_8Stay> pageList = smart_8StayService.list(queryWrapper);
		 List<Smart_8Stay> exportList = null;

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
		 mv.addObject(NormalExcelConstants.FILE_NAME, "住宿信息"); //此处设置的filename无效 ,前端会重更新设置一下
		 mv.addObject(NormalExcelConstants.CLASS, Smart_8Stay.class);
		 mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("住宿信息报表", "导出人:" + sysUser.getRealname(), "住宿信息"));
		 mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
		 return mv;
    }

    /**
     * 导入
     * @return
     */
    @RequestMapping(value = "/importSmart_8Stay/{mainId}")
    public Result<?> importSmart_8Stay(HttpServletRequest request, HttpServletResponse response, @PathVariable("mainId") String mainId) {
		 MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		 Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		 for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			 MultipartFile file = entity.getValue();// 获取上传文件对象
			 ImportParams params = new ImportParams();
			 params.setTitleRows(2);
			 params.setHeadRows(1);
			 params.setNeedSave(true);
			 try {
				 List<Smart_8Stay> list = ExcelImportUtil.importExcel(file.getInputStream(), Smart_8Stay.class, params);
				 for (Smart_8Stay temp : list) {
                    temp.setMainId(mainId);
				 }
				 long start = System.currentTimeMillis();
				 smart_8StayService.saveBatch(list);
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

    /*--------------------------------子表处理-住宿信息-end----------------------------------------------*/

    /*--------------------------------子表处理-用餐情况-begin----------------------------------------------*/
	/**
	 * 通过主表ID查询
	 * @return
	 */
	@AutoLog(value = "用餐情况-通过主表ID查询")
	@ApiOperation(value="用餐情况-通过主表ID查询", notes="用餐情况-通过主表ID查询")
	@GetMapping(value = "/listSmart_8DiningByMainId")
    public Result<?> listSmart_8DiningByMainId(Smart_8Dining smart_8Dining,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest req) {
        QueryWrapper<Smart_8Dining> queryWrapper = QueryGenerator.initQueryWrapper(smart_8Dining, req.getParameterMap());
        Page<Smart_8Dining> page = new Page<Smart_8Dining>(pageNo, pageSize);
        IPage<Smart_8Dining> pageList = smart_8DiningService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

	/**
	 * 添加
	 * @param smart_8Dining
	 * @return
	 */
	@AutoLog(value = "用餐情况-添加")
	@ApiOperation(value="用餐情况-添加", notes="用餐情况-添加")
	@PostMapping(value = "/addSmart_8Dining")
	public Result<?> addSmart_8Dining(@RequestBody Smart_8Dining smart_8Dining) {
		smart_8DiningService.save(smart_8Dining);
		Smart_8EscortedMeal smart_8EscortedMeal = new Smart_8EscortedMeal();
		smart_8EscortedMeal.setMainId(smart_8Dining.getId());
		smart_8EscortedMealService.save(smart_8EscortedMeal);

		return Result.OK("添加成功！");
	}

    /**
	 * 编辑
	 * @param smart_8Dining
	 * @return
	 */
	@AutoLog(value = "用餐情况-编辑")
	@ApiOperation(value="用餐情况-编辑", notes="用餐情况-编辑")
	@PutMapping(value = "/editSmart_8Dining")
	public Result<?> editSmart_8Dining(@RequestBody Smart_8Dining smart_8Dining) {
		smart_8DiningService.updateById(smart_8Dining);
		return Result.OK("编辑成功!");
	}

	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "用餐情况-通过id删除")
	@ApiOperation(value="用餐情况-通过id删除", notes="用餐情况-通过id删除")
	@DeleteMapping(value = "/deleteSmart_8Dining")
	public Result<?> deleteSmart_8Dining(@RequestParam(name="id",required=true) String id) {
		smart_8DiningService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "用餐情况-批量删除")
	@ApiOperation(value="用餐情况-批量删除", notes="用餐情况-批量删除")
	@DeleteMapping(value = "/deleteBatchSmart_8Dining")
	public Result<?> deleteBatchSmart_8Dining(@RequestParam(name="ids",required=true) String ids) {
	    this.smart_8DiningService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

    /**
     * 导出
     * @return
     */
    @RequestMapping(value = "/exportSmart_8Dining")
    public ModelAndView exportSmart_8Dining(HttpServletRequest request, Smart_8Dining smart_8Dining) {
		 // Step.1 组装查询条件
		 QueryWrapper<Smart_8Dining> queryWrapper = QueryGenerator.initQueryWrapper(smart_8Dining, request.getParameterMap());
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		 // Step.2 获取导出数据
		 List<Smart_8Dining> pageList = smart_8DiningService.list(queryWrapper);
		 List<Smart_8Dining> exportList = null;

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
		 mv.addObject(NormalExcelConstants.FILE_NAME, "用餐情况"); //此处设置的filename无效 ,前端会重更新设置一下
		 mv.addObject(NormalExcelConstants.CLASS, Smart_8Dining.class);
		 mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("用餐情况报表", "导出人:" + sysUser.getRealname(), "用餐情况"));
		 mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
		 return mv;
    }

    /**
     * 导入
     * @return
     */
    @RequestMapping(value = "/importSmart_8Dining/{mainId}")
    public Result<?> importSmart_8Dining(HttpServletRequest request, HttpServletResponse response, @PathVariable("mainId") String mainId) {
		 MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		 Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		 for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			 MultipartFile file = entity.getValue();// 获取上传文件对象
			 ImportParams params = new ImportParams();
			 params.setTitleRows(2);
			 params.setHeadRows(1);
			 params.setNeedSave(true);
			 try {
				 List<Smart_8Dining> list = ExcelImportUtil.importExcel(file.getInputStream(), Smart_8Dining.class, params);
				 for (Smart_8Dining temp : list) {
                    temp.setMainId(mainId);
				 }
				 long start = System.currentTimeMillis();
				 smart_8DiningService.saveBatch(list);
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

    /*--------------------------------子表处理-用餐情况-end----------------------------------------------*/

    /*--------------------------------子表处理-接待清单-begin----------------------------------------------*/
	/**
	 * 通过主表ID查询
	 * @return
	 */
	@AutoLog(value = "接待清单-通过主表ID查询")
	@ApiOperation(value="接待清单-通过主表ID查询", notes="接待清单-通过主表ID查询")
	@GetMapping(value = "/listSmart_8ListByMainId")
    public Result<?> listSmart_8ListByMainId(Smart_8List smart_8List,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest req) {
        QueryWrapper<Smart_8List> queryWrapper = QueryGenerator.initQueryWrapper(smart_8List, req.getParameterMap());
        Page<Smart_8List> page = new Page<Smart_8List>(pageNo, pageSize);
        IPage<Smart_8List> pageList = smart_8ListService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

	/**
	 * 添加
	 * @param smart_8List
	 * @return
	 */
	@AutoLog(value = "接待清单-添加")
	@ApiOperation(value="接待清单-添加", notes="接待清单-添加")
	@PostMapping(value = "/addSmart_8List")
	public Result<?> addSmart_8List(@RequestBody Smart_8List smart_8List) {
		smart_8ListService.save(smart_8List);
		return Result.OK("添加成功！");
	}

    /**
	 * 编辑
	 * @param smart_8List
	 * @return
	 */
	@AutoLog(value = "接待清单-编辑")
	@ApiOperation(value="接待清单-编辑", notes="接待清单-编辑")
	@PutMapping(value = "/editSmart_8List")
	public Result<?> editSmart_8List(@RequestBody Smart_8List smart_8List) {
		smart_8ListService.updateById(smart_8List);
		return Result.OK("编辑成功!");
	}

	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "接待清单-通过id删除")
	@ApiOperation(value="接待清单-通过id删除", notes="接待清单-通过id删除")
	@DeleteMapping(value = "/deleteSmart_8List")
	public Result<?> deleteSmart_8List(@RequestParam(name="id",required=true) String id) {
		smart_8ListService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "接待清单-批量删除")
	@ApiOperation(value="接待清单-批量删除", notes="接待清单-批量删除")
	@DeleteMapping(value = "/deleteBatchSmart_8List")
	public Result<?> deleteBatchSmart_8List(@RequestParam(name="ids",required=true) String ids) {
	    this.smart_8ListService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

    /**
     * 导出
     * @return
     */
    @RequestMapping(value = "/exportSmart_8List")
    public ModelAndView exportSmart_8List(HttpServletRequest request, Smart_8List smart_8List) {
		 // Step.1 组装查询条件
		 QueryWrapper<Smart_8List> queryWrapper = QueryGenerator.initQueryWrapper(smart_8List, request.getParameterMap());
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		 // Step.2 获取导出数据
		 List<Smart_8List> pageList = smart_8ListService.list(queryWrapper);
		 List<Smart_8List> exportList = null;

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
		 mv.addObject(NormalExcelConstants.FILE_NAME, "接待清单"); //此处设置的filename无效 ,前端会重更新设置一下
		 mv.addObject(NormalExcelConstants.CLASS, Smart_8List.class);
		 mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("接待清单报表", "导出人:" + sysUser.getRealname(), "接待清单"));
		 mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
		 return mv;
    }

    /**
     * 导入
     * @return
     */
    @RequestMapping(value = "/importSmart_8List/{mainId}")
    public Result<?> importSmart_8List(HttpServletRequest request, HttpServletResponse response, @PathVariable("mainId") String mainId) {
		 MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		 Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		 for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			 MultipartFile file = entity.getValue();// 获取上传文件对象
			 ImportParams params = new ImportParams();
			 params.setTitleRows(2);
			 params.setHeadRows(1);
			 params.setNeedSave(true);
			 try {
				 List<Smart_8List> list = ExcelImportUtil.importExcel(file.getInputStream(), Smart_8List.class, params);
				 for (Smart_8List temp : list) {
                    temp.setMainId(mainId);
				 }
				 long start = System.currentTimeMillis();
				 smart_8ListService.saveBatch(list);
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

    /*--------------------------------子表处理-接待清单-end----------------------------------------------*/




}
