package org.jeecg.modules.SmartPaper.controller;

import java.util.*;
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
import org.jeecg.modules.SmartPaper.entity.SmartPaper;
import org.jeecg.modules.SmartPaper.service.ISmartPaperService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.SmartPaper.vo.ExamPeopleScoreVo;
import org.jeecg.modules.SmartPaper.vo.RandomPeople;
import org.jeecg.modules.SmartPaper.vo.SmartPaperPage;
import org.jeecg.modules.SmartPaper.vo.SmartTriSurveyPage;
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
 * @Description: 试卷表
 * @Author: jeecg-boot
 * @Date:   2021-11-21
 * @Version: V1.0
 */
@Api(tags="试卷表")
@RestController
@RequestMapping("/SmartPaper/smartPaper")
@Slf4j
public class SmartPaperController extends JeecgController<SmartPaper, ISmartPaperService> {
	@Autowired
	private ISmartPaperService smartPaperService;

	/**
	 * 分页列表查询
	 *
	 * @param smartPaper
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "试卷表-分页列表查询")
	@ApiOperation(value="试卷表-分页列表查询", notes="试卷表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartPaper smartPaper,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SmartPaper> queryWrapper = QueryGenerator.initQueryWrapper(smartPaper, req.getParameterMap());
		Page<SmartPaper> page = new Page<SmartPaper>(pageNo, pageSize);
		IPage<SmartPaper> pageList = smartPaperService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	 @AutoLog(value = "三员+走村入户-分页列表查询")
	 @ApiOperation(value="三员+走村入户-分页列表查询", notes="三员+走村入户-分页列表查询")
	 @GetMapping(value = "/triPeoList")
	 public Result<?> querytriPeoList(RandomPeople randomPeople,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		 QueryWrapper<RandomPeople> queryWrapper = QueryGenerator.initQueryWrapper(randomPeople, req.getParameterMap());
		 String paperId = req.getParameter("id");
		 String paperType = req.getParameter("paperType");
		 //System.out.println("####################");
		 //System.out.println(paperType);
		 Result<IPage<RandomPeople>> result = new Result<IPage<RandomPeople>>();

		 Page<RandomPeople> pageList = new Page<RandomPeople>(pageNo,pageSize);

		 pageList = smartPaperService.getTriPeoList(pageList,paperId,paperType);
		 // 获取登录用户信息
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 String userId = sysUser.getId();
		 String userName = sysUser.getRealname();
		 result.setMessage(userId+","+userName);
		 result.setResult(pageList);
		 result.setSuccess(true);
		 return result;
	 }
	 @AutoLog(value = "三员+廉政家访-分页列表查询")
	 @ApiOperation(value="三员+廉政家访-分页列表查询", notes="三员+廉政家访-分页列表查询")
	 @GetMapping(value = "/triPeoGovList")
	 public Result<?> querytriPeoGovList(RandomPeople randomPeople,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		 QueryWrapper<RandomPeople> queryWrapper = QueryGenerator.initQueryWrapper(randomPeople, req.getParameterMap());
		 String paperId = req.getParameter("id");
		 String paperType = req.getParameter("paperType");
		 //System.out.println("####################");
		 //System.out.println(paperType);
		 Result<IPage<RandomPeople>> result = new Result<IPage<RandomPeople>>();

		 Page<RandomPeople> pageList = new Page<RandomPeople>(pageNo,pageSize);

		 pageList = smartPaperService.getTriPeoGovList(pageList,paperId,paperType);
		 // 获取登录用户信息
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 String userId = sysUser.getId();
		 String userName = sysUser.getRealname();
		 result.setMessage(userId+","+userName);
		 result.setResult(pageList);
		 result.setSuccess(true);
		 return result;
	 }
	/**
	 *   添加
	 *
	 * @param smartPaperPage
	 * @return
	 */
	@AutoLog(value = "试卷表-添加")
	@ApiOperation(value="试卷表-添加", notes="试卷表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SmartPaperPage smartPaperPage) {
		System.out.println("####################");
		System.out.println(smartPaperPage);
		smartPaperPage.setPaperType("1");//添加试卷
		smartPaperPage.setPaperStatus("0");
		//默认评分
		smartPaperPage.setIsMark(1);
		smartPaperService.insert(smartPaperPage);
		return Result.OK("添加成功！");
	}

	 /**
	  *   添加
	  *
	  * @param smartPaperPage
	  * @return
	  */
	 @AutoLog(value = "问卷-添加")
	 @ApiOperation(value="问卷-添加", notes="问卷-添加")
	 @PostMapping(value = "/addSurvey")
	 public Result<?> addSurvey(@RequestBody SmartPaperPage smartPaperPage) {
		 //System.out.println("####################");
		 //System.out.println(smartPaperPage);
		 smartPaperPage.setPaperType("2");//添加调查问卷
		 smartPaperPage.setPaperStatus("0");
		 smartPaperService.insert(smartPaperPage);
		 return Result.OK("添加成功！");
	 }

	 /**
	  *   添加
	  *
	  * @param smartTriSurveyPage
	  * @return
	  */
	 @AutoLog(value = "三员+走村入户问卷-添加")
	 @ApiOperation(value="三员+走村入户问卷-添加", notes="三员+走村入户问卷-添加")
	 @PostMapping(value = "/addTriSurvey")
	 public Result<?> addTriSurvey(@RequestBody SmartTriSurveyPage smartTriSurveyPage) {
		 //System.out.println("####################");
		 //System.out.println(smartPaperPage);
		 smartTriSurveyPage.setPaperType("3");//添加调查问卷
		 smartTriSurveyPage.setPaperStatus("2");
		 smartPaperService.insertTriSurvey(smartTriSurveyPage);
		 return Result.OK("添加成功！");
	 }

     @AutoLog(value = "三员+廉政家访问卷-添加")
     @ApiOperation(value="三员+廉政家访问卷-添加", notes="三员+廉政家访问卷-添加")
     @PostMapping(value = "/addTriGovSurvey")
     public Result<?> addTriGovSurvey(@RequestBody SmartTriSurveyPage smartTriSurveyPage) {
         //System.out.println("####################");
         //System.out.println(smartPaperPage);
         smartTriSurveyPage.setPaperType("4");//添加调查问卷
         smartTriSurveyPage.setPaperStatus("2");
         smartPaperService.insertTriGovSurvey(smartTriSurveyPage);
         return Result.OK("添加成功！");
     }
	/**
	 *  编辑
	 *
	 * @param smartPaperPage
	 * @return
	 */
	@AutoLog(value = "试卷表-编辑")
	@ApiOperation(value="试卷表-编辑", notes="试卷表-编辑")
	@PutMapping(value = "/edit/{id}")
	public Result<?> edit(@PathVariable("id") String id, @RequestBody SmartPaperPage smartPaperPage) {
		System.out.println("####################");
		System.out.println(smartPaperPage);
		System.out.println(id);
		smartPaperService.updatePaperById(id,smartPaperPage);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "试卷表-通过id删除")
	@ApiOperation(value="试卷表-通过id删除", notes="试卷表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		int res = (smartPaperService.deletePaper(id)).getCode();
		if (res == 200) {
			return Result.OK("删除成功!");
		}else {
			return Result.error("error");
		}
	}
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "试卷表-批量删除")
	@ApiOperation(value="试卷表-批量删除", notes="试卷表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartPaperService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "试卷表-通过id查询")
	@ApiOperation(value="试卷表-通过id查询", notes="试卷表-通过id查询")
	@GetMapping(value = "/getPaperById")
	public Result<?> queryById( @RequestParam(name="id",required=true) String id) {
		//System.out.println(id);
		SmartPaperPage smartPaperPage = smartPaperService.getPaperById(id);
		if(smartPaperPage==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(smartPaperPage);
	}
    /**
    * 导出excel
    *
    * @param request
    * @param smartPaper
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartPaper smartPaper) {
        return super.exportXls(request, smartPaper, SmartPaper.class, "试卷表");
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
        return super.importExcel(request, response, SmartPaper.class);
    }

}
