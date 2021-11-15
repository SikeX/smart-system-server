package org.jeecg.modules.tasks.smartVerifyDetail.controller;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.tasks.smartVerifyDetail.entity.SmartVerifyDetail;
import org.jeecg.modules.tasks.smartVerifyDetail.service.ISmartVerifyDetailService;

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
 * @Description: 审核详情表
 * @Author: jeecg-boot
 * @Date:   2021-11-06
 * @Version: V1.0
 */
@Api(tags="审核详情表")
@RestController
@RequestMapping("/smartVerifyDetail/smartVerifyDetail")
@Slf4j
public class SmartVerifyDetailController extends JeecgController<SmartVerifyDetail, ISmartVerifyDetailService> {
	@Autowired
	private ISmartVerifyDetailService smartVerifyDetailService;
	
	/**
	 * 分页列表查询
	 *
	 * @param smartVerifyDetail
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "审核详情表-分页列表查询")
	@ApiOperation(value="审核详情表-分页列表查询", notes="审核详情表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartVerifyDetail smartVerifyDetail,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SmartVerifyDetail> queryWrapper = QueryGenerator.initQueryWrapper(smartVerifyDetail, req.getParameterMap());
		Page<SmartVerifyDetail> page = new Page<SmartVerifyDetail>(pageNo, pageSize);
		IPage<SmartVerifyDetail> pageList = smartVerifyDetailService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param smartVerifyDetail
	 * @return
	 */
	@AutoLog(value = "审核详情表-添加")
	@ApiOperation(value="审核详情表-添加", notes="审核详情表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SmartVerifyDetail smartVerifyDetail) {
		smartVerifyDetailService.save(smartVerifyDetail);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param smartVerifyDetail
	 * @return
	 */
	@AutoLog(value = "审核详情表-编辑")
	@ApiOperation(value="审核详情表-编辑", notes="审核详情表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SmartVerifyDetail smartVerifyDetail) {
		smartVerifyDetailService.updateById(smartVerifyDetail);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "审核详情表-通过id删除")
	@ApiOperation(value="审核详情表-通过id删除", notes="审核详情表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		smartVerifyDetailService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "审核详情表-批量删除")
	@ApiOperation(value="审核详情表-批量删除", notes="审核详情表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartVerifyDetailService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "审核详情表-通过id查询")
	@ApiOperation(value="审核详情表-通过id查询", notes="审核详情表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartVerifyDetail smartVerifyDetail = smartVerifyDetailService.getById(id);
		if(smartVerifyDetail==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(smartVerifyDetail);
	}

	 @AutoLog(value = "审核详情表-通过flowNo和审核人通过查询")
	 @ApiOperation(value="审核详情表-通过flowNo和审核人通过查询", notes="审核详情表-通过flowNo和审核人通过查询")
	 @GetMapping(value = "/queryByflowNo")
	 public Result<?> queryByflowNo(@RequestParam(name="flowNo",required=true) String flowNo) {
		QueryWrapper<SmartVerifyDetail> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("flow_no",flowNo);
		queryWrapper.eq("audit_status",3);

		 List<SmartVerifyDetail> smartVerifyDetailList = smartVerifyDetailService.list(queryWrapper);
//		 if(smartVerifyDetail==null) {
//			 return Result.error("未找到对应数据");
//		 }
		 return Result.OK(smartVerifyDetailList);
	 }

    /**
    * 导出excel
    *
    * @param request
    * @param smartVerifyDetail
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartVerifyDetail smartVerifyDetail) {
        return super.exportXls(request, smartVerifyDetail, SmartVerifyDetail.class, "审核详情表");
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
        return super.importExcel(request, response, SmartVerifyDetail.class);
    }

}
