package org.jeecg.modules.SmartPunishPeople.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.SmartPunishPeople.entity.SmartPunishPeople;
import org.jeecg.modules.SmartPunishPeople.service.ISmartPunishPeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

 /**
 * @Description: 处分人员表
 * @Author: jeecg-boot
 * @Date:   2021-11-07
 * @Version: V1.0
 */
@Api(tags="处分人员表")
@RestController
@RequestMapping("/SmartPunishPeople/smartPunishPeople")
@Slf4j
public class SmartPunishPeopleController extends JeecgController<SmartPunishPeople, ISmartPunishPeopleService> {
	@Autowired
	private ISmartPunishPeopleService smartPunishPeopleService;
	
	/**
	 * 分页列表查询
	 *
	 * @param smartPunishPeople
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "处分人员表-分页列表查询")
	@ApiOperation(value="处分人员表-分页列表查询", notes="处分人员表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartPunishPeople smartPunishPeople,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SmartPunishPeople> queryWrapper = QueryGenerator.initQueryWrapper(smartPunishPeople, req.getParameterMap());
		Page<SmartPunishPeople> page = new Page<SmartPunishPeople>(pageNo, pageSize);
		IPage<SmartPunishPeople> pageList = smartPunishPeopleService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param smartPunishPeople
	 * @return
	 */
	@AutoLog(value = "处分人员表-添加")
	@ApiOperation(value="处分人员表-添加", notes="处分人员表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SmartPunishPeople smartPunishPeople) {
		smartPunishPeopleService.save(smartPunishPeople);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param smartPunishPeople
	 * @return
	 */
	@AutoLog(value = "处分人员表-编辑")
	@ApiOperation(value="处分人员表-编辑", notes="处分人员表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SmartPunishPeople smartPunishPeople) {
		smartPunishPeopleService.updateById(smartPunishPeople);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "处分人员表-通过id删除")
	@ApiOperation(value="处分人员表-通过id删除", notes="处分人员表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		smartPunishPeopleService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "处分人员表-批量删除")
	@ApiOperation(value="处分人员表-批量删除", notes="处分人员表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartPunishPeopleService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "处分人员表-通过id查询")
	@ApiOperation(value="处分人员表-通过id查询", notes="处分人员表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartPunishPeople smartPunishPeople = smartPunishPeopleService.getById(id);
		if(smartPunishPeople==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(smartPunishPeople);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param smartPunishPeople
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartPunishPeople smartPunishPeople) {
        return super.exportXls(request, smartPunishPeople, SmartPunishPeople.class, "处分人员表");
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
        return super.importExcel(request, response, SmartPunishPeople.class);
    }

}
