package org.jeecg.modules.tasks.smartVerifyTask.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.tasks.smartVerifyDetail.entity.SmartVerifyDetail;
import org.jeecg.modules.tasks.smartVerifyTask.entity.SmartVerifyTask;
import org.jeecg.modules.tasks.smartVerifyTask.mapper.SmartVerifyTaskMapper;
import org.jeecg.modules.tasks.smartVerifyTask.mapper.VerifyTaskListPageMapper;
import org.jeecg.modules.tasks.smartVerifyTask.service.ISmartVerifyTaskService;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.tasks.smartVerifyTask.vo.VerifyTaskListPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 审核任务表
 * @Author: jeecg-boot
 * @Date:   2021-11-04
 * @Version: V1.0
 */
@Api(tags="审核任务表")
@RestController
@RequestMapping("/tasks/smartVerifyTask")
@Slf4j
public class SmartVerifyTaskController extends JeecgController<SmartVerifyTask, ISmartVerifyTaskService> {
	@Autowired
	private ISmartVerifyTaskService smartVerifyTaskService;

	@Autowired
	private SmartVerifyTaskMapper smartVerifyTaskMapper;

	@Autowired
	private ISysBaseAPI sysBaseAPI;

	@Autowired
	private VerifyTaskListPageMapper verifyTaskListPageMapper;
	
	/**
	 * 分页列表查询
	 *
	 * @param smartVerifyTask
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "审核任务表-分页列表查询")
	@ApiOperation(value="审核任务表-分页列表查询", notes="审核任务表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(VerifyTaskListPage verifyTaskListPage,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {

		MPJLambdaWrapper<VerifyTaskListPage> mpjLambdaWrapper = new MPJLambdaWrapper<VerifyTaskListPage>();
		mpjLambdaWrapper.selectAll(SmartVerifyTask.class)
				.select(SmartVerifyDetail::getFlowNo,SmartVerifyDetail::getAuditDepart,SmartVerifyDetail::getAuditPerson,
						SmartVerifyDetail::getAuditStatus, SmartVerifyDetail::getAuditTime, SmartVerifyDetail::getRemark)
				.innerJoin(SmartVerifyDetail.class, SmartVerifyDetail::getFlowNo, SmartVerifyTask::getFlowNo)
				.eq(SmartVerifyDetail::getAuditStatus, 2);

		Page<VerifyTaskListPage> page = new Page<VerifyTaskListPage>(pageNo, pageSize);
		IPage<VerifyTaskListPage> pageList = smartVerifyTaskMapper.selectJoinPage(page, VerifyTaskListPage.class,
				mpjLambdaWrapper);

		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		log.info(sysUser.getDepartIds());
		// log.info(sysBaseAPI.getDepartIdsByOrgCode(sysUser.getOrgCode()));

		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param smartVerifyTask
	 * @return
	 */
	@AutoLog(value = "审核任务表-添加")
	@ApiOperation(value="审核任务表-添加", notes="审核任务表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SmartVerifyTask smartVerifyTask) {
		smartVerifyTaskService.save(smartVerifyTask);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param smartVerifyTask
	 * @return
	 */
	@AutoLog(value = "审核任务表-编辑")
	@ApiOperation(value="审核任务表-编辑", notes="审核任务表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SmartVerifyTask smartVerifyTask) {
		smartVerifyTaskService.updateById(smartVerifyTask);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "审核任务表-通过id删除")
	@ApiOperation(value="审核任务表-通过id删除", notes="审核任务表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		smartVerifyTaskService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "审核任务表-批量删除")
	@ApiOperation(value="审核任务表-批量删除", notes="审核任务表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartVerifyTaskService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "审核任务表-通过id查询")
	@ApiOperation(value="审核任务表-通过id查询", notes="审核任务表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartVerifyTask smartVerifyTask = smartVerifyTaskService.getById(id);
		if(smartVerifyTask==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(smartVerifyTask);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param smartVerifyTask
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartVerifyTask smartVerifyTask) {
        return super.exportXls(request, smartVerifyTask, SmartVerifyTask.class, "审核任务表");
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
        return super.importExcel(request, response, SmartVerifyTask.class);
    }

}
