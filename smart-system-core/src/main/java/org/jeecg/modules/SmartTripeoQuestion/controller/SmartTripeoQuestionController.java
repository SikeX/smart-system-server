package org.jeecg.modules.SmartTripeoQuestion.controller;

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
import org.jeecg.modules.SmartTripeoQuestion.entity.SmartTripeoQuestion;
import org.jeecg.modules.SmartTripeoQuestion.service.ISmartTripeoQuestionService;

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

 /**
 * @Description: 三员+问题清单
 * @Author: jeecg-boot
 * @Date:   2022-03-04
 * @Version: V1.0
 */
@Api(tags="三员+问题清单")
@RestController
@RequestMapping("/SmartTripeoQuestion/smartTripeoQuestion")
@Slf4j
public class SmartTripeoQuestionController extends JeecgController<SmartTripeoQuestion, ISmartTripeoQuestionService> {
	@Autowired
	private ISmartTripeoQuestionService smartTripeoQuestionService;
	
	/**
	 * 分页列表查询
	 *
	 * @param smartTripeoQuestion
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "三员+问题清单-分页列表查询")
	@ApiOperation(value="三员+问题清单-分页列表查询", notes="三员+问题清单-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartTripeoQuestion smartTripeoQuestion,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SmartTripeoQuestion> queryWrapper = QueryGenerator.initQueryWrapper(smartTripeoQuestion, req.getParameterMap());
		Page<SmartTripeoQuestion> page = new Page<SmartTripeoQuestion>(pageNo, pageSize);
		IPage<SmartTripeoQuestion> pageList = smartTripeoQuestionService.page(page, queryWrapper);
		System.out.println("questionList"+pageList.getRecords());
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param smartTripeoQuestion
	 * @return
	 */
	@AutoLog(value = "三员+问题清单-添加")
	@ApiOperation(value="三员+问题清单-添加", notes="三员+问题清单-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SmartTripeoQuestion smartTripeoQuestion) {
		smartTripeoQuestionService.save(smartTripeoQuestion);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param smartTripeoQuestion
	 * @return
	 */
	@AutoLog(value = "三员+问题清单-编辑")
	@ApiOperation(value="三员+问题清单-编辑", notes="三员+问题清单-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SmartTripeoQuestion smartTripeoQuestion) {
		smartTripeoQuestionService.updateById(smartTripeoQuestion);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "三员+问题清单-通过id删除")
	@ApiOperation(value="三员+问题清单-通过id删除", notes="三员+问题清单-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		smartTripeoQuestionService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "三员+问题清单-批量删除")
	@ApiOperation(value="三员+问题清单-批量删除", notes="三员+问题清单-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartTripeoQuestionService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "三员+问题清单-通过id查询")
	@ApiOperation(value="三员+问题清单-通过id查询", notes="三员+问题清单-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartTripeoQuestion smartTripeoQuestion = smartTripeoQuestionService.getById(id);
		if(smartTripeoQuestion==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(smartTripeoQuestion);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param smartTripeoQuestion
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartTripeoQuestion smartTripeoQuestion) {
        return super.exportXls(request, smartTripeoQuestion, SmartTripeoQuestion.class, "三员+问题清单");
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
        return super.importExcel(request, response, SmartTripeoQuestion.class);
    }

}
