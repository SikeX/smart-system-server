package org.jeecg.modules.wePower.smartPublicityFinance.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.wePower.smartPublicityBenifit.entity.SmartPublicityBenifit;
import org.jeecg.modules.wePower.smartPublicityFinance.entity.SmartPublicityFinance;
import org.jeecg.modules.wePower.smartPublicityFinance.service.ISmartPublicityFinanceService;

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
 * @Description: 财务公开
 * @Author: jeecg-boot
 * @Date:   2022-02-15
 * @Version: V1.0
 */
@Api(tags="财务公开")
@RestController
@RequestMapping("/smartPublicityFinance/smartPublicityFinance")
@Slf4j
public class SmartPublicityFinanceController extends JeecgController<SmartPublicityFinance, ISmartPublicityFinanceService> {
	@Autowired
	private ISmartPublicityFinanceService smartPublicityFinanceService;
	
	/**
	 * 分页列表查询
	 *
	 * @param smartPublicityFinance
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "财务公开-分页列表查询")
	@ApiOperation(value="财务公开-分页列表查询", notes="财务公开-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartPublicityFinance smartPublicityFinance,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SmartPublicityFinance> queryWrapper = QueryGenerator.initQueryWrapper(smartPublicityFinance, req.getParameterMap());
		Page<SmartPublicityFinance> page = new Page<SmartPublicityFinance>(pageNo, pageSize);
		IPage<SmartPublicityFinance> pageList = smartPublicityFinanceService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 @AutoLog(value = "农村集体经济组织-分页列表查询")
	 @ApiOperation(value="农村集体经济组织-分页列表查询", notes="农村集体经济组织-分页列表查询")
	 @GetMapping(value = "/listAdmin")
	 public Result<?> queryPageListAdmin(SmartPublicityFinance smartPublicityFinance,
										 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
										 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
										 HttpServletRequest req) {

		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 String orgCode = sysUser.getOrgCode();
		 if ("".equals(orgCode)) {
			 return Result.error("本用户没有操作权限！");
		 }
		 QueryWrapper<SmartPublicityFinance> queryWrapper = QueryGenerator.initQueryWrapper(smartPublicityFinance, req.getParameterMap());
		 queryWrapper.eq("sys_org_code", orgCode);
		 Page<SmartPublicityFinance> page = new Page<SmartPublicityFinance>(pageNo, pageSize);
		 IPage<SmartPublicityFinance> pageList = smartPublicityFinanceService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }
	
	/**
	 *   添加
	 *
	 * @param smartPublicityFinance
	 * @return
	 */
	@AutoLog(value = "财务公开-添加")
	@ApiOperation(value="财务公开-添加", notes="财务公开-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SmartPublicityFinance smartPublicityFinance) {
		smartPublicityFinanceService.save(smartPublicityFinance);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param smartPublicityFinance
	 * @return
	 */
	@AutoLog(value = "财务公开-编辑")
	@ApiOperation(value="财务公开-编辑", notes="财务公开-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SmartPublicityFinance smartPublicityFinance) {
		smartPublicityFinanceService.updateById(smartPublicityFinance);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "财务公开-通过id删除")
	@ApiOperation(value="财务公开-通过id删除", notes="财务公开-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		smartPublicityFinanceService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "财务公开-批量删除")
	@ApiOperation(value="财务公开-批量删除", notes="财务公开-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartPublicityFinanceService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "财务公开-通过id查询")
	@ApiOperation(value="财务公开-通过id查询", notes="财务公开-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartPublicityFinance smartPublicityFinance = smartPublicityFinanceService.getById(id);
		if(smartPublicityFinance==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(smartPublicityFinance);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param smartPublicityFinance
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartPublicityFinance smartPublicityFinance) {
        return super.exportXls(request, smartPublicityFinance, SmartPublicityFinance.class, "财务公开");
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
        return super.importExcel(request, response, SmartPublicityFinance.class);
    }

}
