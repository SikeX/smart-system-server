package org.jeecg.modules.publicityEducation.org.jeecg.modules.smart_video_player.controller;

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
import org.jeecg.modules.publicityEducation.entity.PublicityEducationPacpa;
import org.jeecg.modules.publicityEducation.entity.PublicityEducationAnnex;
import org.jeecg.modules.publicityEducation.entity.PublicityEducation;
import org.jeecg.modules.publicityEducation.vo.PublicityEducationPage;
import org.jeecg.modules.publicityEducation.service.IPublicityEducationService;
import org.jeecg.modules.publicityEducation.service.IPublicityEducationPacpaService;
import org.jeecg.modules.publicityEducation.service.IPublicityEducationAnnexService;
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
 * @Description: 宣传教育主表
 * @Author: jeecg-boot
 * @Date:   2021-12-26
 * @Version: V1.0
 */
@Api(tags="宣传教育主表")
@RestController
@RequestMapping("/publicityEducation/publicityEducation")
@Slf4j
public class PublicityEducationController {
	@Autowired
	private IPublicityEducationService publicityEducationService;
	@Autowired
	private IPublicityEducationPacpaService publicityEducationPacpaService;
	@Autowired
	private IPublicityEducationAnnexService publicityEducationAnnexService;
	
	/**
	 * 分页列表查询
	 *
	 * @param publicityEducation
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "宣传教育主表-分页列表查询")
	@ApiOperation(value="宣传教育主表-分页列表查询", notes="宣传教育主表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PublicityEducation publicityEducation,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<PublicityEducation> queryWrapper = QueryGenerator.initQueryWrapper(publicityEducation, req.getParameterMap());
		Page<PublicityEducation> page = new Page<PublicityEducation>(pageNo, pageSize);
		IPage<PublicityEducation> pageList = publicityEducationService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param publicityEducationPage
	 * @return
	 */
	@AutoLog(value = "宣传教育主表-添加")
	@ApiOperation(value="宣传教育主表-添加", notes="宣传教育主表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PublicityEducationPage publicityEducationPage) {
		PublicityEducation publicityEducation = new PublicityEducation();
		BeanUtils.copyProperties(publicityEducationPage, publicityEducation);
		publicityEducationService.saveMain(publicityEducation, publicityEducationPage.getPublicityEducationPacpaList(),publicityEducationPage.getPublicityEducationAnnexList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param publicityEducationPage
	 * @return
	 */
	@AutoLog(value = "宣传教育主表-编辑")
	@ApiOperation(value="宣传教育主表-编辑", notes="宣传教育主表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PublicityEducationPage publicityEducationPage) {
		PublicityEducation publicityEducation = new PublicityEducation();
		BeanUtils.copyProperties(publicityEducationPage, publicityEducation);
		PublicityEducation publicityEducationEntity = publicityEducationService.getById(publicityEducation.getId());
		if(publicityEducationEntity==null) {
			return Result.error("未找到对应数据");
		}
		publicityEducationService.updateMain(publicityEducation, publicityEducationPage.getPublicityEducationPacpaList(),publicityEducationPage.getPublicityEducationAnnexList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "宣传教育主表-通过id删除")
	@ApiOperation(value="宣传教育主表-通过id删除", notes="宣传教育主表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		publicityEducationService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "宣传教育主表-批量删除")
	@ApiOperation(value="宣传教育主表-批量删除", notes="宣传教育主表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.publicityEducationService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "宣传教育主表-通过id查询")
	@ApiOperation(value="宣传教育主表-通过id查询", notes="宣传教育主表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		PublicityEducation publicityEducation = publicityEducationService.getById(id);
		if(publicityEducation==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(publicityEducation);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "宣传教育参会人员通过主表ID查询")
	@ApiOperation(value="宣传教育参会人员主表ID查询", notes="宣传教育参会人员-通主表ID查询")
	@GetMapping(value = "/queryPublicityEducationPacpaByMainId")
	public Result<?> queryPublicityEducationPacpaListByMainId(@RequestParam(name="id",required=true) String id) {
		List<PublicityEducationPacpa> publicityEducationPacpaList = publicityEducationPacpaService.selectByMainId(id);
		return Result.OK(publicityEducationPacpaList);
	}
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "宣传教育附件表通过主表ID查询")
	@ApiOperation(value="宣传教育附件表主表ID查询", notes="宣传教育附件表-通主表ID查询")
	@GetMapping(value = "/queryPublicityEducationAnnexByMainId")
	public Result<?> queryPublicityEducationAnnexListByMainId(@RequestParam(name="id",required=true) String id) {
		List<PublicityEducationAnnex> publicityEducationAnnexList = publicityEducationAnnexService.selectByMainId(id);
		return Result.OK(publicityEducationAnnexList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param publicityEducation
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PublicityEducation publicityEducation) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<PublicityEducation> queryWrapper = QueryGenerator.initQueryWrapper(publicityEducation, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<PublicityEducation> queryList = publicityEducationService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<PublicityEducation> publicityEducationList = new ArrayList<PublicityEducation>();
      if(oConvertUtils.isEmpty(selections)) {
          publicityEducationList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          publicityEducationList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<PublicityEducationPage> pageList = new ArrayList<PublicityEducationPage>();
      for (PublicityEducation main : publicityEducationList) {
          PublicityEducationPage vo = new PublicityEducationPage();
          BeanUtils.copyProperties(main, vo);
          List<PublicityEducationPacpa> publicityEducationPacpaList = publicityEducationPacpaService.selectByMainId(main.getId());
          vo.setPublicityEducationPacpaList(publicityEducationPacpaList);
          List<PublicityEducationAnnex> publicityEducationAnnexList = publicityEducationAnnexService.selectByMainId(main.getId());
          vo.setPublicityEducationAnnexList(publicityEducationAnnexList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "宣传教育主表列表");
      mv.addObject(NormalExcelConstants.CLASS, PublicityEducationPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("宣传教育主表数据", "导出人:"+sysUser.getRealname(), "宣传教育主表"));
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
              List<PublicityEducationPage> list = ExcelImportUtil.importExcel(file.getInputStream(), PublicityEducationPage.class, params);
              for (PublicityEducationPage page : list) {
                  PublicityEducation po = new PublicityEducation();
                  BeanUtils.copyProperties(page, po);
                  publicityEducationService.saveMain(po, page.getPublicityEducationPacpaList(),page.getPublicityEducationAnnexList());
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
