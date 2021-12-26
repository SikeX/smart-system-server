package org.jeecg.modules.wePower.smartPublicityResource.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.wePower.smartPublicityResource.entity.SmartPublicityResource;
import org.jeecg.modules.wePower.smartPublicityResource.service.ISmartPublicityResourceService;

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
 * @Description: 资产资源
 * @Author: jeecg-boot
 * @Date:   2021-12-26
 * @Version: V1.0
 */
@Api(tags="资产资源")
@RestController
@RequestMapping("/smartPublicityResource/smartPublicityResource")
@Slf4j
public class SmartPublicityResourceController extends JeecgController<SmartPublicityResource, ISmartPublicityResourceService> {
	@Autowired
	private ISmartPublicityResourceService smartPublicityResourceService;
	
	/**
	 * 分页列表查询
	 *
	 * @param smartPublicityResource
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "资产资源-分页列表查询")
	@ApiOperation(value="资产资源-分页列表查询", notes="资产资源-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartPublicityResource smartPublicityResource,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SmartPublicityResource> queryWrapper = QueryGenerator.initQueryWrapper(smartPublicityResource, req.getParameterMap());
		Page<SmartPublicityResource> page = new Page<SmartPublicityResource>(pageNo, pageSize);
		IPage<SmartPublicityResource> pageList = smartPublicityResourceService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param smartPublicityResource
	 * @return
	 */
	@AutoLog(value = "资产资源-添加")
	@ApiOperation(value="资产资源-添加", notes="资产资源-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SmartPublicityResource smartPublicityResource) {
		smartPublicityResourceService.save(smartPublicityResource);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param smartPublicityResource
	 * @return
	 */
	@AutoLog(value = "资产资源-编辑")
	@ApiOperation(value="资产资源-编辑", notes="资产资源-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SmartPublicityResource smartPublicityResource) {
		smartPublicityResourceService.updateById(smartPublicityResource);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "资产资源-通过id删除")
	@ApiOperation(value="资产资源-通过id删除", notes="资产资源-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		smartPublicityResourceService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "资产资源-批量删除")
	@ApiOperation(value="资产资源-批量删除", notes="资产资源-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartPublicityResourceService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "资产资源-通过id查询")
	@ApiOperation(value="资产资源-通过id查询", notes="资产资源-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartPublicityResource smartPublicityResource = smartPublicityResourceService.getById(id);
		if(smartPublicityResource==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(smartPublicityResource);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param smartPublicityResource
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartPublicityResource smartPublicityResource) {
        return super.exportXls(request, smartPublicityResource, SmartPublicityResource.class, "资产资源");
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
        return super.importExcel(request, response, SmartPublicityResource.class);
    }

}
