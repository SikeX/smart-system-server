package org.jeecg.modules.smartMessage.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.smartMessage.entity.SmartMessageTemplate;
import org.jeecg.modules.smartMessage.service.ISmartMessageTemplateService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 通知模板
 * @Author: jeecg-boot
 * @Date:   2021-11-28
 * @Version: V1.0
 */
@Api(tags="通知模板")
@RestController
@RequestMapping("/smartMessageTemplate/smartMessageTemplate")
@Slf4j
public class SmartMessageTemplateController extends JeecgController<SmartMessageTemplate, ISmartMessageTemplateService> {
	@Autowired
	private ISmartMessageTemplateService smartMessageTemplateService;
	
	/**
	 * 分页列表查询
	 *
	 * @param smartMessageTemplate
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "通知模板-分页列表查询")
	@ApiOperation(value="通知模板-分页列表查询", notes="通知模板-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartMessageTemplate smartMessageTemplate,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SmartMessageTemplate> queryWrapper = QueryGenerator.initQueryWrapper(smartMessageTemplate, req.getParameterMap());
		Page<SmartMessageTemplate> page = new Page<SmartMessageTemplate>(pageNo, pageSize);
		IPage<SmartMessageTemplate> pageList = smartMessageTemplateService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param smartMessageTemplate
	 * @return
	 */
	@AutoLog(value = "通知模板-添加")
	@ApiOperation(value="通知模板-添加", notes="通知模板-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SmartMessageTemplate smartMessageTemplate) {
		smartMessageTemplateService.save(smartMessageTemplate);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param smartMessageTemplate
	 * @return
	 */
	@AutoLog(value = "通知模板-编辑")
	@ApiOperation(value="通知模板-编辑", notes="通知模板-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SmartMessageTemplate smartMessageTemplate) {
		smartMessageTemplateService.updateById(smartMessageTemplate);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "通知模板-通过id删除")
	@ApiOperation(value="通知模板-通过id删除", notes="通知模板-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		smartMessageTemplateService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "通知模板-批量删除")
	@ApiOperation(value="通知模板-批量删除", notes="通知模板-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartMessageTemplateService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "通知模板-通过id查询")
	@ApiOperation(value="通知模板-通过id查询", notes="通知模板-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartMessageTemplate smartMessageTemplate = smartMessageTemplateService.getById(id);
		if(smartMessageTemplate==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(smartMessageTemplate);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param smartMessageTemplate
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartMessageTemplate smartMessageTemplate) {
        return super.exportXls(request, smartMessageTemplate, SmartMessageTemplate.class, "通知模板");
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
        return super.importExcel(request, response, SmartMessageTemplate.class);
    }

}
