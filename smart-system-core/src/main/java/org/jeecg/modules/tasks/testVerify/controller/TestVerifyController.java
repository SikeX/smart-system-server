package org.jeecg.modules.tasks.testVerify.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.tasks.smartVerifyDetail.entity.SmartVerifyDetail;
import org.jeecg.modules.tasks.smartVerifyDetail.service.ISmartVerifyDetailService;
import org.jeecg.modules.tasks.smartVerifyTask.service.SmartVerify;
import org.jeecg.modules.tasks.testVerify.service.ITestVerifyService;
import org.jeecg.modules.tasks.taskType.entity.SmartVerifyType;
import org.jeecg.modules.tasks.taskType.service.ISmartVerifyTypeService;
import org.jeecg.modules.tasks.smartVerifyTask.entity.SmartVerifyTask;
import org.jeecg.modules.tasks.smartVerifyTask.service.ISmartVerifyTaskService;
import org.jeecg.modules.tasks.testVerify.entity.TestVerify;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 审核测试
 * @Author: jeecg-boot
 * @Date:   2021-11-06
 * @Version: V1.0
 */
@Api(tags="审核测试")
@RestController
@RequestMapping("/testVerify/testVerify")
@Slf4j
public class TestVerifyController extends JeecgController<TestVerify, ITestVerifyService> {

	@Autowired
	private ISysBaseAPI sysBaseAPI;

	@Autowired
	private ITestVerifyService testVerifyService;

	@Autowired
	private ISmartVerifyTaskService smartVerifyTaskService;

	@Autowired
	private ISmartVerifyTypeService smartVerifyTypeService;

	@Autowired
	private ISmartVerifyDetailService smartVerifyDetailService;

	@Autowired
	private SmartVerify smartVerify;

	public String verifyType = "婚前报备";
	
	/**
	 * 分页列表查询
	 *
	 * @param testVerify
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "审核测试-分页列表查询")
	@ApiOperation(value="审核测试-分页列表查询", notes="审核测试-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(TestVerify testVerify,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TestVerify> queryWrapper = QueryGenerator.initQueryWrapper(testVerify, req.getParameterMap());
		Page<TestVerify> page = new Page<TestVerify>(pageNo, pageSize);
		IPage<TestVerify> pageList = testVerifyService.page(page, queryWrapper);

		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param testVerify
	 * @return
	 */
	@AutoLog(value = "审核测试-添加")
	@ApiOperation(value="审核测试-添加", notes="审核测试-添加")
	@PostMapping(value = "/add")
	@Transactional
	public Result<?> add(@RequestBody TestVerify testVerify) {
		testVerifyService.save(testVerify);
		log.info(String.valueOf(testVerify));
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		log.info(String.valueOf(sysUser));
//		log.info(sysUser.getDepartIds());
//		log.info(sysUser.getOrgCode());
		smartVerify.addVerifyRecord(testVerify.getId(),verifyType);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param testVerify
	 * @return
	 */
	@AutoLog(value = "审核测试-编辑")
	@ApiOperation(value="审核测试-编辑", notes="审核测试-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody TestVerify testVerify) {
		testVerifyService.updateById(testVerify);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "审核测试-通过id删除")
	@ApiOperation(value="审核测试-通过id删除", notes="审核测试-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		testVerifyService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "审核测试-批量删除")
	@ApiOperation(value="审核测试-批量删除", notes="审核测试-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.testVerifyService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "审核测试-通过id查询")
	@ApiOperation(value="审核测试-通过id查询", notes="审核测试-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		TestVerify testVerify = testVerifyService.getById(id);
		if(testVerify==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(testVerify);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param testVerify
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TestVerify testVerify) {
        return super.exportXls(request, testVerify, TestVerify.class, "审核测试");
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
        return super.importExcel(request, response, TestVerify.class);
    }

}
