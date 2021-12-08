package org.jeecg.modules.tasks.taskType.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.tasks.taskType.entity.SmartVerifyType;
import org.jeecg.modules.tasks.taskType.service.ISmartVerifyTypeService;

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
 * @Description: 审核任务类型编码表
 * @Author: jeecg-boot
 * @Date:   2021-11-04
 * @Version: V1.0
 */
@Api(tags="审核任务类型编码表")
@RestController
@RequestMapping("/taskType/smartVerifyType")
@Slf4j
public class SmartVerifyTypeController extends JeecgController<SmartVerifyType, ISmartVerifyTypeService> {
	@Autowired
	private ISmartVerifyTypeService smartVerifyTypeService;
	
	/**
	 * 分页列表查询
	 *
	 * @param smartVerifyType
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "审核任务类型编码表-分页列表查询")
	@ApiOperation(value="审核任务类型编码表-分页列表查询", notes="审核任务类型编码表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartVerifyType smartVerifyType,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SmartVerifyType> queryWrapper = QueryGenerator.initQueryWrapper(smartVerifyType, req.getParameterMap());
		Page<SmartVerifyType> page = new Page<SmartVerifyType>(pageNo, pageSize);
		IPage<SmartVerifyType> pageList = smartVerifyTypeService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param smartVerifyType
	 * @return
	 */
	@AutoLog(value = "审核任务类型编码表-添加")
	@ApiOperation(value="审核任务类型编码表-添加", notes="审核任务类型编码表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SmartVerifyType smartVerifyType) {
		smartVerifyTypeService.save(smartVerifyType);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param smartVerifyType
	 * @return
	 */
	@AutoLog(value = "审核任务类型编码表-编辑")
	@ApiOperation(value="审核任务类型编码表-编辑", notes="审核任务类型编码表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SmartVerifyType smartVerifyType) {
		smartVerifyTypeService.updateById(smartVerifyType);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "审核任务类型编码表-通过id删除")
	@ApiOperation(value="审核任务类型编码表-通过id删除", notes="审核任务类型编码表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		smartVerifyTypeService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "审核任务类型编码表-批量删除")
	@ApiOperation(value="审核任务类型编码表-批量删除", notes="审核任务类型编码表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartVerifyTypeService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "审核任务类型编码表-通过id查询")
	@ApiOperation(value="审核任务类型编码表-通过id查询", notes="审核任务类型编码表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartVerifyType smartVerifyType = smartVerifyTypeService.getById(id);
		if(smartVerifyType==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(smartVerifyType);
	}

	 /**
	  * 通过typeName查询
	  *
	  * @param name
	  * @return
	  */
	 @AutoLog(value = "审核任务类型编码表-通过typeName查询")
	 @ApiOperation(value="审核任务类型编码表-通过id查询", notes="审核任务类型编码表-通过id查询")
	 @GetMapping(value = "/queryByTypeName")
	 public Result<?> queryByTypeName(@RequestParam(name="typeName",required=true) String typeName) {

		 QueryWrapper<SmartVerifyType> queryWrapper = new QueryWrapper<>();
		 queryWrapper.eq("type_name", typeName);
		 String typeDesc = smartVerifyTypeService.getOne(queryWrapper).getTypeDesc();
		 return Result.OK(typeDesc);
	 }

    /**
    * 导出excel
    *
    * @param request
    * @param smartVerifyType
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartVerifyType smartVerifyType) {
        return super.exportXls(request, smartVerifyType, SmartVerifyType.class, "审核任务类型编码表");
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
        return super.importExcel(request, response, SmartVerifyType.class);
    }

}
