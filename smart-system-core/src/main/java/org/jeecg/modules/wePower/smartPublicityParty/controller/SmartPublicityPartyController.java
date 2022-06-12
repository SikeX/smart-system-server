package org.jeecg.modules.wePower.smartPublicityParty.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.wePower.smartPublicityFinance.entity.SmartPublicityFinance;
import org.jeecg.modules.wePower.smartPublicityParty.entity.SmartPublicityParty;
import org.jeecg.modules.wePower.smartPublicityParty.service.ISmartPublicityPartyService;

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
 * @Description: 党务村（居）公开
 * @Author: jeecg-boot
 * @Date:   2021-12-26
 * @Version: V1.0
 */
@Api(tags="党务村（居）公开")
@RestController
@RequestMapping("/smartPublicityParty/smartPublicityParty")
@Slf4j
public class SmartPublicityPartyController extends JeecgController<SmartPublicityParty, ISmartPublicityPartyService> {
	@Autowired
	private ISmartPublicityPartyService smartPublicityPartyService;
	
	/**
	 * 分页列表查询
	 *
	 * @param smartPublicityParty
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "党务村（居）公开-分页列表查询")
	@ApiOperation(value="党务村（居）公开-分页列表查询", notes="党务村（居）公开-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartPublicityParty smartPublicityParty,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SmartPublicityParty> queryWrapper = QueryGenerator.initQueryWrapper(smartPublicityParty, req.getParameterMap());
		Page<SmartPublicityParty> page = new Page<SmartPublicityParty>(pageNo, pageSize);
		IPage<SmartPublicityParty> pageList = smartPublicityPartyService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 @AutoLog(value = "农村集体经济组织-分页列表查询")
	 @ApiOperation(value="农村集体经济组织-分页列表查询", notes="农村集体经济组织-分页列表查询")
	 @GetMapping(value = "/listAdmin")
	 public Result<?> queryPageListAdmin(SmartPublicityParty smartPublicityParty,
										 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
										 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
										 HttpServletRequest req) {

		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 String orgCode = sysUser.getOrgCode();
		 if ("".equals(orgCode)) {
			 return Result.error("本用户没有操作权限！");
		 }
		 QueryWrapper<SmartPublicityParty> queryWrapper = QueryGenerator.initQueryWrapper(smartPublicityParty, req.getParameterMap());
		 queryWrapper.eq("sys_org_code", orgCode);
		 Page<SmartPublicityParty> page = new Page<SmartPublicityParty>(pageNo, pageSize);
		 IPage<SmartPublicityParty> pageList = smartPublicityPartyService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }
	
	/**
	 *   添加
	 *
	 * @param smartPublicityParty
	 * @return
	 */
	@AutoLog(value = "党务村（居）公开-添加")
	@ApiOperation(value="党务村（居）公开-添加", notes="党务村（居）公开-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SmartPublicityParty smartPublicityParty) {
		smartPublicityPartyService.save(smartPublicityParty);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param smartPublicityParty
	 * @return
	 */
	@AutoLog(value = "党务村（居）公开-编辑")
	@ApiOperation(value="党务村（居）公开-编辑", notes="党务村（居）公开-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SmartPublicityParty smartPublicityParty) {
		smartPublicityPartyService.updateById(smartPublicityParty);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "党务村（居）公开-通过id删除")
	@ApiOperation(value="党务村（居）公开-通过id删除", notes="党务村（居）公开-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		smartPublicityPartyService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "党务村（居）公开-批量删除")
	@ApiOperation(value="党务村（居）公开-批量删除", notes="党务村（居）公开-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartPublicityPartyService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "党务村（居）公开-通过id查询")
	@ApiOperation(value="党务村（居）公开-通过id查询", notes="党务村（居）公开-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartPublicityParty smartPublicityParty = smartPublicityPartyService.getById(id);
		if(smartPublicityParty==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(smartPublicityParty);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param smartPublicityParty
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartPublicityParty smartPublicityParty) {
        return super.exportXls(request, smartPublicityParty, SmartPublicityParty.class, "党务村（居）公开");
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
        return super.importExcel(request, response, SmartPublicityParty.class);
    }

}
