package org.jeecg.modules.wePower.smartPublicityBenifit.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.wePower.smartPublicityBenifit.entity.SmartPublicityBenifit;
import org.jeecg.modules.wePower.smartPublicityBenifit.service.ISmartPublicityBenifitService;

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
 * @Description: 惠民公示
 * @Author: jeecg-boot
 * @Date:   2021-12-26
 * @Version: V1.0
 */
@Api(tags="惠民公示")
@RestController
@RequestMapping("/smartPublicityBenifit/smartPublicityBenifit")
@Slf4j
public class SmartPublicityBenifitController extends JeecgController<SmartPublicityBenifit, ISmartPublicityBenifitService> {
	@Autowired
	private ISmartPublicityBenifitService smartPublicityBenifitService;
	
	/**
	 * 分页列表查询
	 *
	 * @param smartPublicityBenifit
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "惠民公示-分页列表查询")
	@ApiOperation(value="惠民公示-分页列表查询", notes="惠民公示-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartPublicityBenifit smartPublicityBenifit,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SmartPublicityBenifit> queryWrapper = QueryGenerator.initQueryWrapper(smartPublicityBenifit, req.getParameterMap());
		Page<SmartPublicityBenifit> page = new Page<SmartPublicityBenifit>(pageNo, pageSize);
		IPage<SmartPublicityBenifit> pageList = smartPublicityBenifitService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param smartPublicityBenifit
	 * @return
	 */
	@AutoLog(value = "惠民公示-添加")
	@ApiOperation(value="惠民公示-添加", notes="惠民公示-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SmartPublicityBenifit smartPublicityBenifit) {
		smartPublicityBenifitService.save(smartPublicityBenifit);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param smartPublicityBenifit
	 * @return
	 */
	@AutoLog(value = "惠民公示-编辑")
	@ApiOperation(value="惠民公示-编辑", notes="惠民公示-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SmartPublicityBenifit smartPublicityBenifit) {
		smartPublicityBenifitService.updateById(smartPublicityBenifit);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "惠民公示-通过id删除")
	@ApiOperation(value="惠民公示-通过id删除", notes="惠民公示-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		smartPublicityBenifitService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "惠民公示-批量删除")
	@ApiOperation(value="惠民公示-批量删除", notes="惠民公示-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartPublicityBenifitService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "惠民公示-通过id查询")
	@ApiOperation(value="惠民公示-通过id查询", notes="惠民公示-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartPublicityBenifit smartPublicityBenifit = smartPublicityBenifitService.getById(id);
		if(smartPublicityBenifit==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(smartPublicityBenifit);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param smartPublicityBenifit
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartPublicityBenifit smartPublicityBenifit) {
        return super.exportXls(request, smartPublicityBenifit, SmartPublicityBenifit.class, "惠民公示");
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
        return super.importExcel(request, response, SmartPublicityBenifit.class);
    }

}
