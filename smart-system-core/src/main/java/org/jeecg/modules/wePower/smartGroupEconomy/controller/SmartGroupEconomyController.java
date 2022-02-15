package org.jeecg.modules.wePower.smartGroupEconomy.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.wePower.smartGroupEconomy.entity.SmartGroupEconomy;
import org.jeecg.modules.wePower.smartGroupEconomy.service.ISmartGroupEconomyService;

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
 * @Description: 农村集体经济组织
 * @Author: jeecg-boot
 * @Date:   2022-02-15
 * @Version: V1.0
 */
@Api(tags="农村集体经济组织")
@RestController
@RequestMapping("/smartGroupEconomy/smartGroupEconomy")
@Slf4j
public class SmartGroupEconomyController extends JeecgController<SmartGroupEconomy, ISmartGroupEconomyService> {
	@Autowired
	private ISmartGroupEconomyService smartGroupEconomyService;
	
	/**
	 * 分页列表查询
	 *
	 * @param smartGroupEconomy
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "农村集体经济组织-分页列表查询")
	@ApiOperation(value="农村集体经济组织-分页列表查询", notes="农村集体经济组织-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartGroupEconomy smartGroupEconomy,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SmartGroupEconomy> queryWrapper = QueryGenerator.initQueryWrapper(smartGroupEconomy, req.getParameterMap());
		Page<SmartGroupEconomy> page = new Page<SmartGroupEconomy>(pageNo, pageSize);
		IPage<SmartGroupEconomy> pageList = smartGroupEconomyService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param smartGroupEconomy
	 * @return
	 */
	@AutoLog(value = "农村集体经济组织-添加")
	@ApiOperation(value="农村集体经济组织-添加", notes="农村集体经济组织-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SmartGroupEconomy smartGroupEconomy) {
		smartGroupEconomyService.save(smartGroupEconomy);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param smartGroupEconomy
	 * @return
	 */
	@AutoLog(value = "农村集体经济组织-编辑")
	@ApiOperation(value="农村集体经济组织-编辑", notes="农村集体经济组织-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SmartGroupEconomy smartGroupEconomy) {
		smartGroupEconomyService.updateById(smartGroupEconomy);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "农村集体经济组织-通过id删除")
	@ApiOperation(value="农村集体经济组织-通过id删除", notes="农村集体经济组织-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		smartGroupEconomyService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "农村集体经济组织-批量删除")
	@ApiOperation(value="农村集体经济组织-批量删除", notes="农村集体经济组织-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartGroupEconomyService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "农村集体经济组织-通过id查询")
	@ApiOperation(value="农村集体经济组织-通过id查询", notes="农村集体经济组织-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartGroupEconomy smartGroupEconomy = smartGroupEconomyService.getById(id);
		if(smartGroupEconomy==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(smartGroupEconomy);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param smartGroupEconomy
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartGroupEconomy smartGroupEconomy) {
        return super.exportXls(request, smartGroupEconomy, SmartGroupEconomy.class, "农村集体经济组织");
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
        return super.importExcel(request, response, SmartGroupEconomy.class);
    }

}
