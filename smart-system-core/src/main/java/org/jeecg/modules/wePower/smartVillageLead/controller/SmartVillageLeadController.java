package org.jeecg.modules.wePower.smartVillageLead.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.wePower.smartVillageLead.entity.SmartVillageLead;
import org.jeecg.modules.wePower.smartVillageLead.service.ISmartVillageLeadService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

//import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 村（社区）领导班子
 * @Author: jeecg-boot
 * @Date:   2021-12-24
 * @Version: V1.0
 */
@Api(tags="村（社区）领导班子")
@RestController
@RequestMapping("/smartVillageLead/smartVillageLead")
@Slf4j
public class SmartVillageLeadController extends JeecgController<SmartVillageLead, ISmartVillageLeadService> {
	@Autowired
	private ISmartVillageLeadService smartVillageLeadService;
	
	/**
	 * 分页列表查询
	 *
	 * @param smartVillageLead
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "村（社区）领导班子-分页列表查询")
	@ApiOperation(value="村（社区）领导班子-分页列表查询", notes="村（社区）领导班子-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartVillageLead smartVillageLead,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SmartVillageLead> queryWrapper = QueryGenerator.initQueryWrapper(smartVillageLead, req.getParameterMap());
		Page<SmartVillageLead> page = new Page<SmartVillageLead>(pageNo, pageSize);
		IPage<SmartVillageLead> pageList = smartVillageLeadService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param smartVillageLead
	 * @return
	 */
	@AutoLog(value = "村（社区）领导班子-添加")
	@ApiOperation(value="村（社区）领导班子-添加", notes="村（社区）领导班子-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SmartVillageLead smartVillageLead) {
		smartVillageLeadService.save(smartVillageLead);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param smartVillageLead
	 * @return
	 */
	@AutoLog(value = "村（社区）领导班子-编辑")
	@ApiOperation(value="村（社区）领导班子-编辑", notes="村（社区）领导班子-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SmartVillageLead smartVillageLead) {
		smartVillageLeadService.updateById(smartVillageLead);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "村（社区）领导班子-通过id删除")
	@ApiOperation(value="村（社区）领导班子-通过id删除", notes="村（社区）领导班子-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		smartVillageLeadService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "村（社区）领导班子-批量删除")
	@ApiOperation(value="村（社区）领导班子-批量删除", notes="村（社区）领导班子-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartVillageLeadService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "村（社区）领导班子-通过id查询")
	@ApiOperation(value="村（社区）领导班子-通过id查询", notes="村（社区）领导班子-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartVillageLead smartVillageLead = smartVillageLeadService.getById(id);
		if(smartVillageLead==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(smartVillageLead);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param smartVillageLead
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartVillageLead smartVillageLead) {
        return super.exportXls(request, smartVillageLead, SmartVillageLead.class, "村（社区）领导班子");
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
        return super.importExcel(request, response, SmartVillageLead.class);
    }

}
