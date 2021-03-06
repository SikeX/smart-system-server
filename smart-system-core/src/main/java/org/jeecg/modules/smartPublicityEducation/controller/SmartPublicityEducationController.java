package org.jeecg.modules.smartPublicityEducation.controller;

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
import org.jeecg.modules.smartPublicityEducation.entity.SmartPublicityEducationPeople;
import org.jeecg.modules.smartPublicityEducation.entity.SmartPublicityEducation;
import org.jeecg.modules.smartPublicityEducation.vo.SmartPublicityEducationPage;
import org.jeecg.modules.smartPublicityEducation.service.ISmartPublicityEducationService;
import org.jeecg.modules.smartPublicityEducation.service.ISmartPublicityEducationPeopleService;
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
 * @Description: ????????????
 * @Author: jeecg-boot
 * @Date:   2021-12-29
 * @Version: V1.0
 */
@Api(tags="????????????")
@RestController
@RequestMapping("/smartPublicityEducation/smartPublicityEducation")
@Slf4j
public class SmartPublicityEducationController {
	@Autowired
	private ISmartPublicityEducationService smartPublicityEducationService;
	@Autowired
	private ISmartPublicityEducationPeopleService smartPublicityEducationPeopleService;
	
	/**
	 * ??????????????????
	 *
	 * @param smartPublicityEducation
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "????????????-??????????????????")
	@ApiOperation(value="????????????-??????????????????", notes="????????????-??????????????????")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartPublicityEducation smartPublicityEducation,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SmartPublicityEducation> queryWrapper = QueryGenerator.initQueryWrapper(smartPublicityEducation, req.getParameterMap());
		Page<SmartPublicityEducation> page = new Page<SmartPublicityEducation>(pageNo, pageSize);
		IPage<SmartPublicityEducation> pageList = smartPublicityEducationService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   ??????
	 *
	 * @param smartPublicityEducationPage
	 * @return
	 */
	@AutoLog(value = "????????????-??????")
	@ApiOperation(value="????????????-??????", notes="????????????-??????")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SmartPublicityEducationPage smartPublicityEducationPage) {
		SmartPublicityEducation smartPublicityEducation = new SmartPublicityEducation();
		BeanUtils.copyProperties(smartPublicityEducationPage, smartPublicityEducation);
		smartPublicityEducationService.saveMain(smartPublicityEducation, smartPublicityEducationPage.getSmartPublicityEducationPeopleList());
		return Result.OK("???????????????");
	}
	
	/**
	 *  ??????
	 *
	 * @param smartPublicityEducationPage
	 * @return
	 */
	@AutoLog(value = "????????????-??????")
	@ApiOperation(value="????????????-??????", notes="????????????-??????")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SmartPublicityEducationPage smartPublicityEducationPage) {
		SmartPublicityEducation smartPublicityEducation = new SmartPublicityEducation();
		BeanUtils.copyProperties(smartPublicityEducationPage, smartPublicityEducation);
		SmartPublicityEducation smartPublicityEducationEntity = smartPublicityEducationService.getById(smartPublicityEducation.getId());
		if(smartPublicityEducationEntity==null) {
			return Result.error("?????????????????????");
		}
		smartPublicityEducationService.updateMain(smartPublicityEducation, smartPublicityEducationPage.getSmartPublicityEducationPeopleList());
		return Result.OK("????????????!");
	}
	
	/**
	 *   ??????id??????
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "????????????-??????id??????")
	@ApiOperation(value="????????????-??????id??????", notes="????????????-??????id??????")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		smartPublicityEducationService.delMain(id);
		return Result.OK("????????????!");
	}
	
	/**
	 *  ????????????
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "????????????-????????????")
	@ApiOperation(value="????????????-????????????", notes="????????????-????????????")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartPublicityEducationService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("?????????????????????");
	}
	
	/**
	 * ??????id??????
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "????????????-??????id??????")
	@ApiOperation(value="????????????-??????id??????", notes="????????????-??????id??????")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartPublicityEducation smartPublicityEducation = smartPublicityEducationService.getById(id);
		if(smartPublicityEducation==null) {
			return Result.error("?????????????????????");
		}
		return Result.OK(smartPublicityEducation);

	}
	
	/**
	 * ??????id??????
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "????????????????????????????????????ID??????")
	@ApiOperation(value="??????????????????????????????ID??????", notes="????????????????????????-?????????ID??????")
	@GetMapping(value = "/querySmartPublicityEducationPeopleByMainId")
	public Result<?> querySmartPublicityEducationPeopleListByMainId(@RequestParam(name="id",required=true) String id) {
		List<SmartPublicityEducationPeople> smartPublicityEducationPeopleList = smartPublicityEducationPeopleService.selectByMainId(id);
		return Result.OK(smartPublicityEducationPeopleList);
	}

    /**
    * ??????excel
    *
    * @param request
    * @param smartPublicityEducation
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartPublicityEducation smartPublicityEducation) {
      // Step.1 ??????????????????????????????
      QueryWrapper<SmartPublicityEducation> queryWrapper = QueryGenerator.initQueryWrapper(smartPublicityEducation, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 ??????????????????
      List<SmartPublicityEducation> queryList = smartPublicityEducationService.list(queryWrapper);
      // ??????????????????
      String selections = request.getParameter("selections");
      List<SmartPublicityEducation> smartPublicityEducationList = new ArrayList<SmartPublicityEducation>();
      if(oConvertUtils.isEmpty(selections)) {
          smartPublicityEducationList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          smartPublicityEducationList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 ??????pageList
      List<SmartPublicityEducationPage> pageList = new ArrayList<SmartPublicityEducationPage>();
      for (SmartPublicityEducation main : smartPublicityEducationList) {
          SmartPublicityEducationPage vo = new SmartPublicityEducationPage();
          BeanUtils.copyProperties(main, vo);
          List<SmartPublicityEducationPeople> smartPublicityEducationPeopleList = smartPublicityEducationPeopleService.selectByMainId(main.getId());
          vo.setSmartPublicityEducationPeopleList(smartPublicityEducationPeopleList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi ??????Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "??????????????????");
      mv.addObject(NormalExcelConstants.CLASS, SmartPublicityEducationPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("??????????????????", "?????????:"+sysUser.getRealname(), "????????????"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
    }

    /**
    * ??????excel????????????
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
          MultipartFile file = entity.getValue();// ????????????????????????
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<SmartPublicityEducationPage> list = ExcelImportUtil.importExcel(file.getInputStream(), SmartPublicityEducationPage.class, params);
              for (SmartPublicityEducationPage page : list) {
                  SmartPublicityEducation po = new SmartPublicityEducation();
                  BeanUtils.copyProperties(page, po);
                  smartPublicityEducationService.saveMain(po, page.getSmartPublicityEducationPeopleList());
              }
              return Result.OK("?????????????????????????????????:" + list.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("??????????????????:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.OK("?????????????????????");
    }

}
