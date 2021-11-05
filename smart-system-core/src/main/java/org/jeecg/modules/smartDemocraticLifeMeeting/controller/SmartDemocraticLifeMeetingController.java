package org.jeecg.modules.smartDemocraticLifeMeeting.controller;

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
import org.jeecg.modules.smartDemocraticLifeMeeting.entity.SmartDemocraticLifePeople;
import org.jeecg.modules.smartDemocraticLifeMeeting.entity.SmartDemocraticLifeEnclosure;
import org.jeecg.modules.smartDemocraticLifeMeeting.entity.SmartDemocraticLifeMeeting;
import org.jeecg.modules.smartDemocraticLifeMeeting.service.ISmartDemocraticLifeMeetingService;
import org.jeecg.modules.smartDemocraticLifeMeeting.service.ISmartDemocraticLifePeopleService;
import org.jeecg.modules.smartDemocraticLifeMeeting.service.ISmartDemocraticLifeEnclosureService;
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
 * @Description: 民主生活会表
 * @Author: jeecg-boot
 * @Date:   2021-11-03
 * @Version: V1.0
 */
@Api(tags="民主生活会表")
@RestController
@RequestMapping("/smartDemocraticLifeMeeting/smartDemocraticLifeMeeting")
@Slf4j
public class SmartDemocraticLifeMeetingController extends JeecgController<SmartDemocraticLifeMeeting, ISmartDemocraticLifeMeetingService> {

	@Autowired
	private ISmartDemocraticLifeMeetingService smartDemocraticLifeMeetingService;

	@Autowired
	private ISmartDemocraticLifePeopleService smartDemocraticLifePeopleService;

	@Autowired
	private ISmartDemocraticLifeEnclosureService smartDemocraticLifeEnclosureService;


	/*---------------------------------主表处理-begin-------------------------------------*/

	/**
	 * 分页列表查询
	 * @param smartDemocraticLifeMeeting
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "民主生活会表-分页列表查询")
	@ApiOperation(value="民主生活会表-分页列表查询", notes="民主生活会表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartDemocraticLifeMeeting smartDemocraticLifeMeeting,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SmartDemocraticLifeMeeting> queryWrapper = QueryGenerator.initQueryWrapper(smartDemocraticLifeMeeting, req.getParameterMap());
		Page<SmartDemocraticLifeMeeting> page = new Page<SmartDemocraticLifeMeeting>(pageNo, pageSize);
		IPage<SmartDemocraticLifeMeeting> pageList = smartDemocraticLifeMeetingService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
     *   添加
     * @param smartDemocraticLifeMeeting
     * @return
     */
    @AutoLog(value = "民主生活会表-添加")
    @ApiOperation(value="民主生活会表-添加", notes="民主生活会表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SmartDemocraticLifeMeeting smartDemocraticLifeMeeting) {
        smartDemocraticLifeMeetingService.save(smartDemocraticLifeMeeting);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     * @param smartDemocraticLifeMeeting
     * @return
     */
    @AutoLog(value = "民主生活会表-编辑")
    @ApiOperation(value="民主生活会表-编辑", notes="民主生活会表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SmartDemocraticLifeMeeting smartDemocraticLifeMeeting) {
        smartDemocraticLifeMeetingService.updateById(smartDemocraticLifeMeeting);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     * @param id
     * @return
     */
    @AutoLog(value = "民主生活会表-通过id删除")
    @ApiOperation(value="民主生活会表-通过id删除", notes="民主生活会表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name="id",required=true) String id) {
        smartDemocraticLifeMeetingService.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @AutoLog(value = "民主生活会表-批量删除")
    @ApiOperation(value="民主生活会表-批量删除", notes="民主生活会表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        this.smartDemocraticLifeMeetingService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 导出
     * @return
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartDemocraticLifeMeeting smartDemocraticLifeMeeting) {
        return super.exportXls(request, smartDemocraticLifeMeeting, SmartDemocraticLifeMeeting.class, "民主生活会表");
    }

    /**
     * 导入
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, SmartDemocraticLifeMeeting.class);
    }
	/*---------------------------------主表处理-end-------------------------------------*/
	

    /*--------------------------------子表处理-民主生活参会人员表-begin----------------------------------------------*/
	/**
	 * 通过主表ID查询
	 * @return
	 */
	@AutoLog(value = "民主生活参会人员表-通过主表ID查询")
	@ApiOperation(value="民主生活参会人员表-通过主表ID查询", notes="民主生活参会人员表-通过主表ID查询")
	@GetMapping(value = "/listSmartDemocraticLifePeopleByMainId")
    public Result<?> listSmartDemocraticLifePeopleByMainId(SmartDemocraticLifePeople smartDemocraticLifePeople,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest req) {
        QueryWrapper<SmartDemocraticLifePeople> queryWrapper = QueryGenerator.initQueryWrapper(smartDemocraticLifePeople, req.getParameterMap());
        Page<SmartDemocraticLifePeople> page = new Page<SmartDemocraticLifePeople>(pageNo, pageSize);
        IPage<SmartDemocraticLifePeople> pageList = smartDemocraticLifePeopleService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

	/**
	 * 添加
	 * @param smartDemocraticLifePeople
	 * @return
	 */
	@AutoLog(value = "民主生活参会人员表-添加")
	@ApiOperation(value="民主生活参会人员表-添加", notes="民主生活参会人员表-添加")
	@PostMapping(value = "/addSmartDemocraticLifePeople")
	public Result<?> addSmartDemocraticLifePeople(@RequestBody SmartDemocraticLifePeople smartDemocraticLifePeople) {
		smartDemocraticLifePeopleService.save(smartDemocraticLifePeople);
		return Result.OK("添加成功！");
	}

    /**
	 * 编辑
	 * @param smartDemocraticLifePeople
	 * @return
	 */
	@AutoLog(value = "民主生活参会人员表-编辑")
	@ApiOperation(value="民主生活参会人员表-编辑", notes="民主生活参会人员表-编辑")
	@PutMapping(value = "/editSmartDemocraticLifePeople")
	public Result<?> editSmartDemocraticLifePeople(@RequestBody SmartDemocraticLifePeople smartDemocraticLifePeople) {
		smartDemocraticLifePeopleService.updateById(smartDemocraticLifePeople);
		return Result.OK("编辑成功!");
	}

	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "民主生活参会人员表-通过id删除")
	@ApiOperation(value="民主生活参会人员表-通过id删除", notes="民主生活参会人员表-通过id删除")
	@DeleteMapping(value = "/deleteSmartDemocraticLifePeople")
	public Result<?> deleteSmartDemocraticLifePeople(@RequestParam(name="id",required=true) String id) {
		smartDemocraticLifePeopleService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "民主生活参会人员表-批量删除")
	@ApiOperation(value="民主生活参会人员表-批量删除", notes="民主生活参会人员表-批量删除")
	@DeleteMapping(value = "/deleteBatchSmartDemocraticLifePeople")
	public Result<?> deleteBatchSmartDemocraticLifePeople(@RequestParam(name="ids",required=true) String ids) {
	    this.smartDemocraticLifePeopleService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

    /**
     * 导出
     * @return
     */
    @RequestMapping(value = "/exportSmartDemocraticLifePeople")
    public ModelAndView exportSmartDemocraticLifePeople(HttpServletRequest request, SmartDemocraticLifePeople smartDemocraticLifePeople) {
		 // Step.1 组装查询条件
		 QueryWrapper<SmartDemocraticLifePeople> queryWrapper = QueryGenerator.initQueryWrapper(smartDemocraticLifePeople, request.getParameterMap());
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		 // Step.2 获取导出数据
		 List<SmartDemocraticLifePeople> pageList = smartDemocraticLifePeopleService.list(queryWrapper);
		 List<SmartDemocraticLifePeople> exportList = null;

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
		 mv.addObject(NormalExcelConstants.FILE_NAME, "民主生活参会人员表"); //此处设置的filename无效 ,前端会重更新设置一下
		 mv.addObject(NormalExcelConstants.CLASS, SmartDemocraticLifePeople.class);
		 mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("民主生活参会人员表报表", "导出人:" + sysUser.getRealname(), "民主生活参会人员表"));
		 mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
		 return mv;
    }

    /**
     * 导入
     * @return
     */
    @RequestMapping(value = "/importSmartDemocraticLifePeople/{mainId}")
    public Result<?> importSmartDemocraticLifePeople(HttpServletRequest request, HttpServletResponse response, @PathVariable("mainId") String mainId) {
		 MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		 Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		 for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			 MultipartFile file = entity.getValue();// 获取上传文件对象
			 ImportParams params = new ImportParams();
			 params.setTitleRows(2);
			 params.setHeadRows(1);
			 params.setNeedSave(true);
			 try {
				 List<SmartDemocraticLifePeople> list = ExcelImportUtil.importExcel(file.getInputStream(), SmartDemocraticLifePeople.class, params);
				 for (SmartDemocraticLifePeople temp : list) {
                    temp.setMeetingId(mainId);
				 }
				 long start = System.currentTimeMillis();
				 smartDemocraticLifePeopleService.saveBatch(list);
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

    /*--------------------------------子表处理-民主生活参会人员表-end----------------------------------------------*/

    /*--------------------------------子表处理-民主生活会附件表-begin----------------------------------------------*/
	/**
	 * 通过主表ID查询
	 * @return
	 */
	@AutoLog(value = "民主生活会附件表-通过主表ID查询")
	@ApiOperation(value="民主生活会附件表-通过主表ID查询", notes="民主生活会附件表-通过主表ID查询")
	@GetMapping(value = "/listSmartDemocraticLifeEnclosureByMainId")
    public Result<?> listSmartDemocraticLifeEnclosureByMainId(SmartDemocraticLifeEnclosure smartDemocraticLifeEnclosure,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest req) {
        QueryWrapper<SmartDemocraticLifeEnclosure> queryWrapper = QueryGenerator.initQueryWrapper(smartDemocraticLifeEnclosure, req.getParameterMap());
        Page<SmartDemocraticLifeEnclosure> page = new Page<SmartDemocraticLifeEnclosure>(pageNo, pageSize);
        IPage<SmartDemocraticLifeEnclosure> pageList = smartDemocraticLifeEnclosureService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

	/**
	 * 添加
	 * @param smartDemocraticLifeEnclosure
	 * @return
	 */
	@AutoLog(value = "民主生活会附件表-添加")
	@ApiOperation(value="民主生活会附件表-添加", notes="民主生活会附件表-添加")
	@PostMapping(value = "/addSmartDemocraticLifeEnclosure")
	public Result<?> addSmartDemocraticLifeEnclosure(@RequestBody SmartDemocraticLifeEnclosure smartDemocraticLifeEnclosure) {
		smartDemocraticLifeEnclosureService.save(smartDemocraticLifeEnclosure);
		return Result.OK("添加成功！");
	}

    /**
	 * 编辑
	 * @param smartDemocraticLifeEnclosure
	 * @return
	 */
	@AutoLog(value = "民主生活会附件表-编辑")
	@ApiOperation(value="民主生活会附件表-编辑", notes="民主生活会附件表-编辑")
	@PutMapping(value = "/editSmartDemocraticLifeEnclosure")
	public Result<?> editSmartDemocraticLifeEnclosure(@RequestBody SmartDemocraticLifeEnclosure smartDemocraticLifeEnclosure) {
		smartDemocraticLifeEnclosureService.updateById(smartDemocraticLifeEnclosure);
		return Result.OK("编辑成功!");
	}

	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "民主生活会附件表-通过id删除")
	@ApiOperation(value="民主生活会附件表-通过id删除", notes="民主生活会附件表-通过id删除")
	@DeleteMapping(value = "/deleteSmartDemocraticLifeEnclosure")
	public Result<?> deleteSmartDemocraticLifeEnclosure(@RequestParam(name="id",required=true) String id) {
		smartDemocraticLifeEnclosureService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "民主生活会附件表-批量删除")
	@ApiOperation(value="民主生活会附件表-批量删除", notes="民主生活会附件表-批量删除")
	@DeleteMapping(value = "/deleteBatchSmartDemocraticLifeEnclosure")
	public Result<?> deleteBatchSmartDemocraticLifeEnclosure(@RequestParam(name="ids",required=true) String ids) {
	    this.smartDemocraticLifeEnclosureService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

    /**
     * 导出
     * @return
     */
    @RequestMapping(value = "/exportSmartDemocraticLifeEnclosure")
    public ModelAndView exportSmartDemocraticLifeEnclosure(HttpServletRequest request, SmartDemocraticLifeEnclosure smartDemocraticLifeEnclosure) {
		 // Step.1 组装查询条件
		 QueryWrapper<SmartDemocraticLifeEnclosure> queryWrapper = QueryGenerator.initQueryWrapper(smartDemocraticLifeEnclosure, request.getParameterMap());
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		 // Step.2 获取导出数据
		 List<SmartDemocraticLifeEnclosure> pageList = smartDemocraticLifeEnclosureService.list(queryWrapper);
		 List<SmartDemocraticLifeEnclosure> exportList = null;

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
		 mv.addObject(NormalExcelConstants.FILE_NAME, "民主生活会附件表"); //此处设置的filename无效 ,前端会重更新设置一下
		 mv.addObject(NormalExcelConstants.CLASS, SmartDemocraticLifeEnclosure.class);
		 mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("民主生活会附件表报表", "导出人:" + sysUser.getRealname(), "民主生活会附件表"));
		 mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
		 return mv;
    }

    /**
     * 导入
     * @return
     */
    @RequestMapping(value = "/importSmartDemocraticLifeEnclosure/{mainId}")
    public Result<?> importSmartDemocraticLifeEnclosure(HttpServletRequest request, HttpServletResponse response, @PathVariable("mainId") String mainId) {
		 MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		 Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		 for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			 MultipartFile file = entity.getValue();// 获取上传文件对象
			 ImportParams params = new ImportParams();
			 params.setTitleRows(2);
			 params.setHeadRows(1);
			 params.setNeedSave(true);
			 try {
				 List<SmartDemocraticLifeEnclosure> list = ExcelImportUtil.importExcel(file.getInputStream(), SmartDemocraticLifeEnclosure.class, params);
				 for (SmartDemocraticLifeEnclosure temp : list) {
                    temp.setMeetingId(mainId);
				 }
				 long start = System.currentTimeMillis();
				 smartDemocraticLifeEnclosureService.saveBatch(list);
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

    /*--------------------------------子表处理-民主生活会附件表-end----------------------------------------------*/




}
