package org.jeecg.modules.wePower.smartEvadeRelation.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.wePower.smartEvadeRelation.entity.SmartEvadeRelation;
import org.jeecg.modules.wePower.smartEvadeRelation.service.ISmartEvadeRelationService;

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
 * @Description: 回避关系
 * @Author: jeecg-boot
 * @Date:   2022-02-21
 * @Version: V1.0
 */
@Api(tags="回避关系")
@RestController
@RequestMapping("/smartEvadeRelation/smartEvadeRelation")
@Slf4j
public class SmartEvadeRelationController extends JeecgController<SmartEvadeRelation, ISmartEvadeRelationService> {
	@Autowired
	private ISmartEvadeRelationService smartEvadeRelationService;
	
	/**
	 * 分页列表查询
	 *
	 * @param smartEvadeRelation
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "回避关系-分页列表查询")
	@ApiOperation(value="回避关系-分页列表查询", notes="回避关系-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartEvadeRelation smartEvadeRelation,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SmartEvadeRelation> queryWrapper = QueryGenerator.initQueryWrapper(smartEvadeRelation, req.getParameterMap());
		Page<SmartEvadeRelation> page = new Page<SmartEvadeRelation>(pageNo, pageSize);
		IPage<SmartEvadeRelation> pageList = smartEvadeRelationService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param smartEvadeRelation
	 * @return
	 */
	@AutoLog(value = "回避关系-添加")
	@ApiOperation(value="回避关系-添加", notes="回避关系-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SmartEvadeRelation smartEvadeRelation) {
		smartEvadeRelationService.save(smartEvadeRelation);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param smartEvadeRelation
	 * @return
	 */
	@AutoLog(value = "回避关系-编辑")
	@ApiOperation(value="回避关系-编辑", notes="回避关系-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SmartEvadeRelation smartEvadeRelation) {
		smartEvadeRelationService.updateById(smartEvadeRelation);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "回避关系-通过id删除")
	@ApiOperation(value="回避关系-通过id删除", notes="回避关系-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		smartEvadeRelationService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "回避关系-批量删除")
	@ApiOperation(value="回避关系-批量删除", notes="回避关系-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartEvadeRelationService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "回避关系-通过id查询")
	@ApiOperation(value="回避关系-通过id查询", notes="回避关系-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartEvadeRelation smartEvadeRelation = smartEvadeRelationService.getById(id);
		if(smartEvadeRelation==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(smartEvadeRelation);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param smartEvadeRelation
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartEvadeRelation smartEvadeRelation) {
        return super.exportXls(request, smartEvadeRelation, SmartEvadeRelation.class, "回避关系");
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
        return super.importExcel(request, response, SmartEvadeRelation.class);
    }

}
