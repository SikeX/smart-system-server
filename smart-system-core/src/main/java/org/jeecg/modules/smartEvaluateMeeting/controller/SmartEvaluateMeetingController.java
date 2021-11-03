package org.jeecg.modules.smartEvaluateMeeting.controller;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.smartEvaluateMeeting.entity.SmartEvaluateMeetingPacpa;
import org.jeecg.modules.smartEvaluateMeeting.entity.SmartEvaluateMeetingAnnex;
import org.jeecg.modules.smartEvaluateMeeting.entity.SmartEvaluateMeeting;
import org.jeecg.modules.smartEvaluateMeeting.vo.SmartEvaluateMeetingPage;
import org.jeecg.modules.smartEvaluateMeeting.service.ISmartEvaluateMeetingService;
import org.jeecg.modules.smartEvaluateMeeting.service.ISmartEvaluateMeetingPacpaService;
import org.jeecg.modules.smartEvaluateMeeting.service.ISmartEvaluateMeetingAnnexService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 述责述廉表
 * @Author: jeecg-boot
 * @Date:   2021-11-03
 * @Version: V1.0
 */
@Api(tags="述责述廉表")
@RestController
@RequestMapping("/smartEvaluateMeeting/smartEvaluateMeeting")
@Slf4j
public class SmartEvaluateMeetingController {
	@Autowired
	private ISmartEvaluateMeetingService smartEvaluateMeetingService;
	@Autowired
	private ISmartEvaluateMeetingPacpaService smartEvaluateMeetingPacpaService;
	@Autowired
	private ISmartEvaluateMeetingAnnexService smartEvaluateMeetingAnnexService;
	
	/**
	 * 分页列表查询
	 *
	 * @param smartEvaluateMeeting
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "述责述廉表-分页列表查询")
	@ApiOperation(value="述责述廉表-分页列表查询", notes="述责述廉表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartEvaluateMeeting smartEvaluateMeeting,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SmartEvaluateMeeting> queryWrapper = QueryGenerator.initQueryWrapper(smartEvaluateMeeting, req.getParameterMap());
		Page<SmartEvaluateMeeting> page = new Page<SmartEvaluateMeeting>(pageNo, pageSize);
		IPage<SmartEvaluateMeeting> pageList = smartEvaluateMeetingService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param smartEvaluateMeetingPage
	 * @return
	 */
	@AutoLog(value = "述责述廉表-添加")
	@ApiOperation(value="述责述廉表-添加", notes="述责述廉表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SmartEvaluateMeetingPage smartEvaluateMeetingPage) {
		SmartEvaluateMeeting smartEvaluateMeeting = new SmartEvaluateMeeting();
		BeanUtils.copyProperties(smartEvaluateMeetingPage, smartEvaluateMeeting);
		smartEvaluateMeetingService.saveMain(smartEvaluateMeeting, smartEvaluateMeetingPage.getSmartEvaluateMeetingPacpaList(),smartEvaluateMeetingPage.getSmartEvaluateMeetingAnnexList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param smartEvaluateMeetingPage
	 * @return
	 */
	@AutoLog(value = "述责述廉表-编辑")
	@ApiOperation(value="述责述廉表-编辑", notes="述责述廉表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SmartEvaluateMeetingPage smartEvaluateMeetingPage) {
		SmartEvaluateMeeting smartEvaluateMeeting = new SmartEvaluateMeeting();
		BeanUtils.copyProperties(smartEvaluateMeetingPage, smartEvaluateMeeting);
		SmartEvaluateMeeting smartEvaluateMeetingEntity = smartEvaluateMeetingService.getById(smartEvaluateMeeting.getId());
		if(smartEvaluateMeetingEntity==null) {
			return Result.error("未找到对应数据");
		}
		smartEvaluateMeetingService.updateMain(smartEvaluateMeeting, smartEvaluateMeetingPage.getSmartEvaluateMeetingPacpaList(),smartEvaluateMeetingPage.getSmartEvaluateMeetingAnnexList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "述责述廉表-通过id删除")
	@ApiOperation(value="述责述廉表-通过id删除", notes="述责述廉表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		smartEvaluateMeetingService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "述责述廉表-批量删除")
	@ApiOperation(value="述责述廉表-批量删除", notes="述责述廉表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartEvaluateMeetingService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "述责述廉表-通过id查询")
	@ApiOperation(value="述责述廉表-通过id查询", notes="述责述廉表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartEvaluateMeeting smartEvaluateMeeting = smartEvaluateMeetingService.getById(id);
		if(smartEvaluateMeeting==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(smartEvaluateMeeting);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "述责述廉参与人表通过主表ID查询")
	@ApiOperation(value="述责述廉参与人表主表ID查询", notes="述责述廉参与人表-通主表ID查询")
	@GetMapping(value = "/querySmartEvaluateMeetingPacpaByMainId")
	public Result<?> querySmartEvaluateMeetingPacpaListByMainId(@RequestParam(name="id",required=true) String id) {
		List<SmartEvaluateMeetingPacpa> smartEvaluateMeetingPacpaList = smartEvaluateMeetingPacpaService.selectByMainId(id);
		return Result.OK(smartEvaluateMeetingPacpaList);
	}
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "述责述廉附件表通过主表ID查询")
	@ApiOperation(value="述责述廉附件表主表ID查询", notes="述责述廉附件表-通主表ID查询")
	@GetMapping(value = "/querySmartEvaluateMeetingAnnexByMainId")
	public Result<?> querySmartEvaluateMeetingAnnexListByMainId(@RequestParam(name="id",required=true) String id) {
		List<SmartEvaluateMeetingAnnex> smartEvaluateMeetingAnnexList = smartEvaluateMeetingAnnexService.selectByMainId(id);
		return Result.OK(smartEvaluateMeetingAnnexList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param smartEvaluateMeeting
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartEvaluateMeeting smartEvaluateMeeting) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<SmartEvaluateMeeting> queryWrapper = QueryGenerator.initQueryWrapper(smartEvaluateMeeting, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<SmartEvaluateMeeting> queryList = smartEvaluateMeetingService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<SmartEvaluateMeeting> smartEvaluateMeetingList = new ArrayList<SmartEvaluateMeeting>();
      if(oConvertUtils.isEmpty(selections)) {
          smartEvaluateMeetingList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          smartEvaluateMeetingList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<SmartEvaluateMeetingPage> pageList = new ArrayList<SmartEvaluateMeetingPage>();
      for (SmartEvaluateMeeting main : smartEvaluateMeetingList) {
          SmartEvaluateMeetingPage vo = new SmartEvaluateMeetingPage();
          BeanUtils.copyProperties(main, vo);
          List<SmartEvaluateMeetingPacpa> smartEvaluateMeetingPacpaList = smartEvaluateMeetingPacpaService.selectByMainId(main.getId());
          vo.setSmartEvaluateMeetingPacpaList(smartEvaluateMeetingPacpaList);
          List<SmartEvaluateMeetingAnnex> smartEvaluateMeetingAnnexList = smartEvaluateMeetingAnnexService.selectByMainId(main.getId());
          vo.setSmartEvaluateMeetingAnnexList(smartEvaluateMeetingAnnexList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "述责述廉表列表");
      mv.addObject(NormalExcelConstants.CLASS, SmartEvaluateMeetingPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("述责述廉表数据", "导出人:"+sysUser.getRealname(), "述责述廉表"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
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
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          MultipartFile file = entity.getValue();// 获取上传文件对象
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<SmartEvaluateMeetingPage> list = ExcelImportUtil.importExcel(file.getInputStream(), SmartEvaluateMeetingPage.class, params);
              for (SmartEvaluateMeetingPage page : list) {
                  SmartEvaluateMeeting po = new SmartEvaluateMeeting();
                  BeanUtils.copyProperties(page, po);
                  smartEvaluateMeetingService.saveMain(po, page.getSmartEvaluateMeetingPacpaList(),page.getSmartEvaluateMeetingAnnexList());
              }
              return Result.OK("文件导入成功！数据行数:" + list.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.OK("文件导入失败！");
    }

}
