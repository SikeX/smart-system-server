package org.jeecg.modules.SmartOrgMeeting.controller;

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
import org.jeecg.modules.SmartOrgMeeting.entity.SmartOrgMeetingPacpa;
import org.jeecg.modules.SmartOrgMeeting.entity.SmartOrgMeetingAnnex;
import org.jeecg.modules.SmartOrgMeeting.entity.SmartOrgMeeting;
import org.jeecg.modules.SmartOrgMeeting.service.ISmartOrgMeetingService;
import org.jeecg.modules.SmartOrgMeeting.service.ISmartOrgMeetingPacpaService;
import org.jeecg.modules.SmartOrgMeeting.service.ISmartOrgMeetingAnnexService;
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
 * @Description: 组织生活会
 * @Author: jeecg-boot
 * @Date:   2021-11-03
 * @Version: V1.0
 */
@Api(tags="组织生活会")
@RestController
@RequestMapping("/SmartOrgMeeting/smartOrgMeeting")
@Slf4j
public class SmartOrgMeetingController extends JeecgController<SmartOrgMeeting, ISmartOrgMeetingService> {

	@Autowired
	private ISmartOrgMeetingService smartOrgMeetingService;

	@Autowired
	private ISmartOrgMeetingPacpaService smartOrgMeetingPacpaService;

	@Autowired
	private ISmartOrgMeetingAnnexService smartOrgMeetingAnnexService;


	/*---------------------------------主表处理-begin-------------------------------------*/

	/**
	 * 分页列表查询
	 * @param smartOrgMeeting
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "组织生活会-分页列表查询")
	@ApiOperation(value="组织生活会-分页列表查询", notes="组织生活会-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartOrgMeeting smartOrgMeeting,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SmartOrgMeeting> queryWrapper = QueryGenerator.initQueryWrapper(smartOrgMeeting, req.getParameterMap());
		Page<SmartOrgMeeting> page = new Page<SmartOrgMeeting>(pageNo, pageSize);
		IPage<SmartOrgMeeting> pageList = smartOrgMeetingService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
     *   添加
     * @param smartOrgMeeting
     * @return
     */
    @AutoLog(value = "组织生活会-添加")
    @ApiOperation(value="组织生活会-添加", notes="组织生活会-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SmartOrgMeeting smartOrgMeeting) {
        smartOrgMeetingService.save(smartOrgMeeting);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     * @param smartOrgMeeting
     * @return
     */
    @AutoLog(value = "组织生活会-编辑")
    @ApiOperation(value="组织生活会-编辑", notes="组织生活会-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SmartOrgMeeting smartOrgMeeting) {
        smartOrgMeetingService.updateById(smartOrgMeeting);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     * @param id
     * @return
     */
    @AutoLog(value = "组织生活会-通过id删除")
    @ApiOperation(value="组织生活会-通过id删除", notes="组织生活会-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name="id",required=true) String id) {
        smartOrgMeetingService.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @AutoLog(value = "组织生活会-批量删除")
    @ApiOperation(value="组织生活会-批量删除", notes="组织生活会-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        this.smartOrgMeetingService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 导出
     * @return
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartOrgMeeting smartOrgMeeting) {
        return super.exportXls(request, smartOrgMeeting, SmartOrgMeeting.class, "组织生活会");
    }

    /**
     * 导入
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, SmartOrgMeeting.class);
    }
	/*---------------------------------主表处理-end-------------------------------------*/
	

    /*--------------------------------子表处理-组织生活会参会人员表-begin----------------------------------------------*/
	/**
	 * 通过主表ID查询
	 * @return
	 */
	@AutoLog(value = "组织生活会参会人员表-通过主表ID查询")
	@ApiOperation(value="组织生活会参会人员表-通过主表ID查询", notes="组织生活会参会人员表-通过主表ID查询")
	@GetMapping(value = "/listSmartOrgMeetingPacpaByMainId")
    public Result<?> listSmartOrgMeetingPacpaByMainId(SmartOrgMeetingPacpa smartOrgMeetingPacpa,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest req) {
        QueryWrapper<SmartOrgMeetingPacpa> queryWrapper = QueryGenerator.initQueryWrapper(smartOrgMeetingPacpa, req.getParameterMap());
        Page<SmartOrgMeetingPacpa> page = new Page<SmartOrgMeetingPacpa>(pageNo, pageSize);
        IPage<SmartOrgMeetingPacpa> pageList = smartOrgMeetingPacpaService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

	/**
	 * 添加
	 * @param smartOrgMeetingPacpa
	 * @return
	 */
	@AutoLog(value = "组织生活会参会人员表-添加")
	@ApiOperation(value="组织生活会参会人员表-添加", notes="组织生活会参会人员表-添加")
	@PostMapping(value = "/addSmartOrgMeetingPacpa")
	public Result<?> addSmartOrgMeetingPacpa(@RequestBody SmartOrgMeetingPacpa smartOrgMeetingPacpa) {
		smartOrgMeetingPacpaService.save(smartOrgMeetingPacpa);
		return Result.OK("添加成功！");
	}

    /**
	 * 编辑
	 * @param smartOrgMeetingPacpa
	 * @return
	 */
	@AutoLog(value = "组织生活会参会人员表-编辑")
	@ApiOperation(value="组织生活会参会人员表-编辑", notes="组织生活会参会人员表-编辑")
	@PutMapping(value = "/editSmartOrgMeetingPacpa")
	public Result<?> editSmartOrgMeetingPacpa(@RequestBody SmartOrgMeetingPacpa smartOrgMeetingPacpa) {
		smartOrgMeetingPacpaService.updateById(smartOrgMeetingPacpa);
		return Result.OK("编辑成功!");
	}

	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "组织生活会参会人员表-通过id删除")
	@ApiOperation(value="组织生活会参会人员表-通过id删除", notes="组织生活会参会人员表-通过id删除")
	@DeleteMapping(value = "/deleteSmartOrgMeetingPacpa")
	public Result<?> deleteSmartOrgMeetingPacpa(@RequestParam(name="id",required=true) String id) {
		smartOrgMeetingPacpaService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "组织生活会参会人员表-批量删除")
	@ApiOperation(value="组织生活会参会人员表-批量删除", notes="组织生活会参会人员表-批量删除")
	@DeleteMapping(value = "/deleteBatchSmartOrgMeetingPacpa")
	public Result<?> deleteBatchSmartOrgMeetingPacpa(@RequestParam(name="ids",required=true) String ids) {
	    this.smartOrgMeetingPacpaService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

    /**
     * 导出
     * @return
     */
    @RequestMapping(value = "/exportSmartOrgMeetingPacpa")
    public ModelAndView exportSmartOrgMeetingPacpa(HttpServletRequest request, SmartOrgMeetingPacpa smartOrgMeetingPacpa) {
		 // Step.1 组装查询条件
		 QueryWrapper<SmartOrgMeetingPacpa> queryWrapper = QueryGenerator.initQueryWrapper(smartOrgMeetingPacpa, request.getParameterMap());
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		 // Step.2 获取导出数据
		 List<SmartOrgMeetingPacpa> pageList = smartOrgMeetingPacpaService.list(queryWrapper);
		 List<SmartOrgMeetingPacpa> exportList = null;

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
		 mv.addObject(NormalExcelConstants.FILE_NAME, "组织生活会参会人员表"); //此处设置的filename无效 ,前端会重更新设置一下
		 mv.addObject(NormalExcelConstants.CLASS, SmartOrgMeetingPacpa.class);
		 mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("组织生活会参会人员表报表", "导出人:" + sysUser.getRealname(), "组织生活会参会人员表"));
		 mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
		 return mv;
    }

    /**
     * 导入
     * @return
     */
    @RequestMapping(value = "/importSmartOrgMeetingPacpa/{mainId}")
    public Result<?> importSmartOrgMeetingPacpa(HttpServletRequest request, HttpServletResponse response, @PathVariable("mainId") String mainId) {
		 MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		 Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		 for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			 MultipartFile file = entity.getValue();// 获取上传文件对象
			 ImportParams params = new ImportParams();
			 params.setTitleRows(2);
			 params.setHeadRows(1);
			 params.setNeedSave(true);
			 try {
				 List<SmartOrgMeetingPacpa> list = ExcelImportUtil.importExcel(file.getInputStream(), SmartOrgMeetingPacpa.class, params);
				 for (SmartOrgMeetingPacpa temp : list) {
                    temp.setParentId(mainId);
				 }
				 long start = System.currentTimeMillis();
				 smartOrgMeetingPacpaService.saveBatch(list);
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

    /*--------------------------------子表处理-组织生活会参会人员表-end----------------------------------------------*/

    /*--------------------------------子表处理-组织生活会附件表-begin----------------------------------------------*/
	/**
	 * 通过主表ID查询
	 * @return
	 */
	@AutoLog(value = "组织生活会附件表-通过主表ID查询")
	@ApiOperation(value="组织生活会附件表-通过主表ID查询", notes="组织生活会附件表-通过主表ID查询")
	@GetMapping(value = "/listSmartOrgMeetingAnnexByMainId")
    public Result<?> listSmartOrgMeetingAnnexByMainId(SmartOrgMeetingAnnex smartOrgMeetingAnnex,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest req) {
        QueryWrapper<SmartOrgMeetingAnnex> queryWrapper = QueryGenerator.initQueryWrapper(smartOrgMeetingAnnex, req.getParameterMap());
        Page<SmartOrgMeetingAnnex> page = new Page<SmartOrgMeetingAnnex>(pageNo, pageSize);
        IPage<SmartOrgMeetingAnnex> pageList = smartOrgMeetingAnnexService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

	/**
	 * 添加
	 * @param smartOrgMeetingAnnex
	 * @return
	 */
	@AutoLog(value = "组织生活会附件表-添加")
	@ApiOperation(value="组织生活会附件表-添加", notes="组织生活会附件表-添加")
	@PostMapping(value = "/addSmartOrgMeetingAnnex")
	public Result<?> addSmartOrgMeetingAnnex(@RequestBody SmartOrgMeetingAnnex smartOrgMeetingAnnex) {
		smartOrgMeetingAnnexService.save(smartOrgMeetingAnnex);
		return Result.OK("添加成功！");
	}

    /**
	 * 编辑
	 * @param smartOrgMeetingAnnex
	 * @return
	 */
	@AutoLog(value = "组织生活会附件表-编辑")
	@ApiOperation(value="组织生活会附件表-编辑", notes="组织生活会附件表-编辑")
	@PutMapping(value = "/editSmartOrgMeetingAnnex")
	public Result<?> editSmartOrgMeetingAnnex(@RequestBody SmartOrgMeetingAnnex smartOrgMeetingAnnex) {
		smartOrgMeetingAnnexService.updateById(smartOrgMeetingAnnex);
		return Result.OK("编辑成功!");
	}

	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "组织生活会附件表-通过id删除")
	@ApiOperation(value="组织生活会附件表-通过id删除", notes="组织生活会附件表-通过id删除")
	@DeleteMapping(value = "/deleteSmartOrgMeetingAnnex")
	public Result<?> deleteSmartOrgMeetingAnnex(@RequestParam(name="id",required=true) String id) {
		smartOrgMeetingAnnexService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "组织生活会附件表-批量删除")
	@ApiOperation(value="组织生活会附件表-批量删除", notes="组织生活会附件表-批量删除")
	@DeleteMapping(value = "/deleteBatchSmartOrgMeetingAnnex")
	public Result<?> deleteBatchSmartOrgMeetingAnnex(@RequestParam(name="ids",required=true) String ids) {
	    this.smartOrgMeetingAnnexService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

    /**
     * 导出
     * @return
     */
    @RequestMapping(value = "/exportSmartOrgMeetingAnnex")
    public ModelAndView exportSmartOrgMeetingAnnex(HttpServletRequest request, SmartOrgMeetingAnnex smartOrgMeetingAnnex) {
		 // Step.1 组装查询条件
		 QueryWrapper<SmartOrgMeetingAnnex> queryWrapper = QueryGenerator.initQueryWrapper(smartOrgMeetingAnnex, request.getParameterMap());
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		 // Step.2 获取导出数据
		 List<SmartOrgMeetingAnnex> pageList = smartOrgMeetingAnnexService.list(queryWrapper);
		 List<SmartOrgMeetingAnnex> exportList = null;

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
		 mv.addObject(NormalExcelConstants.FILE_NAME, "组织生活会附件表"); //此处设置的filename无效 ,前端会重更新设置一下
		 mv.addObject(NormalExcelConstants.CLASS, SmartOrgMeetingAnnex.class);
		 mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("组织生活会附件表报表", "导出人:" + sysUser.getRealname(), "组织生活会附件表"));
		 mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
		 return mv;
    }

    /**
     * 导入
     * @return
     */
    @RequestMapping(value = "/importSmartOrgMeetingAnnex/{mainId}")
    public Result<?> importSmartOrgMeetingAnnex(HttpServletRequest request, HttpServletResponse response, @PathVariable("mainId") String mainId) {
		 MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		 Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		 for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			 MultipartFile file = entity.getValue();// 获取上传文件对象
			 ImportParams params = new ImportParams();
			 params.setTitleRows(2);
			 params.setHeadRows(1);
			 params.setNeedSave(true);
			 try {
				 List<SmartOrgMeetingAnnex> list = ExcelImportUtil.importExcel(file.getInputStream(), SmartOrgMeetingAnnex.class, params);
				 for (SmartOrgMeetingAnnex temp : list) {
                    temp.setParentId(mainId);
				 }
				 long start = System.currentTimeMillis();
				 smartOrgMeetingAnnexService.saveBatch(list);
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

    /*--------------------------------子表处理-组织生活会附件表-end----------------------------------------------*/




}
