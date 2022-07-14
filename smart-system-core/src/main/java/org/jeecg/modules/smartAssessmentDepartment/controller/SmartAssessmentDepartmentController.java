package org.jeecg.modules.smartAssessmentDepartment.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.smartAssessmentDepartment.entity.SmartAssessmentDepartment;
import org.jeecg.modules.smartAssessmentDepartment.service.ISmartAssessmentDepartmentService;
import org.jeecg.modules.smartAssessmentTeam.entity.SmartAssessmentTeam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 负责评分的考核单位
 * @Author: jeecg-boot
 * @Date:   2022-03-01
 * @Version: V1.0
 */
@Api(tags="负责评分的考核单位")
@RestController
@RequestMapping("/smartAssessmentDepartment/smartAssessmentDepartment")
@Slf4j
public class SmartAssessmentDepartmentController extends JeecgController<SmartAssessmentDepartment, ISmartAssessmentDepartmentService> {
	@Autowired
	private ISmartAssessmentDepartmentService smartAssessmentDepartmentService;
	
	/**
	 * 分页列表查询
	 *
	 * @param smartAssessmentDepartment
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "负责评分的考核单位-分页列表查询")
	@ApiOperation(value="负责评分的考核单位-分页列表查询", notes="负责评分的考核单位-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartAssessmentDepartment smartAssessmentDepartment,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SmartAssessmentDepartment> queryWrapper = QueryGenerator.initQueryWrapper(smartAssessmentDepartment, req.getParameterMap());
		Page<SmartAssessmentDepartment> page = new Page<SmartAssessmentDepartment>(pageNo, pageSize);
		IPage<SmartAssessmentDepartment> pageList = smartAssessmentDepartmentService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 * 查询登录用户所属考核单位
	 *
	 * @return
	 */
	@AutoLog(value = "负责评分的考核单位-分页列表查询")
	@ApiOperation(value="负责评分的考核单位-分页列表查询", notes="负责评分的考核单位-分页列表查询")
	@GetMapping(value = "/listMyDepartment")
	public Result<?> queryPageList(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SmartAssessmentDepartment> queryWrapper = new QueryWrapper<>();
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		//  查询自己所属的考核组信息
		queryWrapper.eq("depart_id", sysUser.getDepartId()).eq("depart_user", sysUser.getId());
		Page<SmartAssessmentDepartment> page = new Page<SmartAssessmentDepartment>(pageNo, pageSize);
		IPage<SmartAssessmentDepartment> pageList = smartAssessmentDepartmentService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 * 查询字典值
	 *
	 * @param req
	 * @return
	 */
	@AutoLog(value = "负责评分的考核单位-查询字典值")
	@ApiOperation(value="负责评分的考核单位-查询字典值", notes="负责评分的考核单位-查询字典值")
	@GetMapping(value = "/dict")
	public Result<?> queryDicList(HttpServletRequest req) {
		List<DictModel> dictItems = smartAssessmentDepartmentService.getDictItems();
		if (dictItems == null || dictItems.size() == 0) {
			return Result.error("暂时没有评分考核单位信息,请先添加");
		}
		return Result.OK(dictItems);
	}

	/**
	 * 考核单位负责单位重复值校验
	 *
	 * @return
	 */
	@GetMapping(value = "/duplicateCheck")
	@ApiOperation("考核组负责单位重复值校验")
	public Result<Object> doDuplicateCheckWithDelFlag(@RequestParam(name = "departIds", required = true) String departIds,
													  @RequestParam(name = "dataId", required = false) String dataId,
													  HttpServletRequest request) {
		Long num = null;

		log.info("----duplicate check------："+ departIds);
		QueryWrapper<SmartAssessmentDepartment> queryWrapper = new QueryWrapper<>();
		String[] departIdList = departIds.split(",");
		for (String departId : departIdList) {
			queryWrapper.or().like("responsible_depart", departId);
		}
		if (StringUtils.isNotBlank(dataId)) {
			// [2].编辑页面校验
			queryWrapper.and(qw -> qw.ne("id", dataId));
			num = smartAssessmentDepartmentService.count(queryWrapper);
		} else {
			// [1].添加页面校验
			num = smartAssessmentDepartmentService.count(queryWrapper);
		}

		if (num == null || num == 0) {
			// 该值可用
			return Result.ok("该值可用！");
		} else {
			// 该值不可用
			log.info("该值不可用，系统中已存在！");
			return Result.error("负责单位与别的考核单位有重复！");
		}
	}
	
	/**
	 *   添加
	 *
	 * @param smartAssessmentDepartment
	 * @return
	 */
	@AutoLog(value = "负责评分的考核单位-添加")
	@ApiOperation(value="负责评分的考核单位-添加", notes="负责评分的考核单位-添加")
	@PostMapping(value = "/add")
	@Transactional(rollbackFor = Exception.class)
	public Result<?> add(@RequestBody SmartAssessmentDepartment smartAssessmentDepartment) {
		smartAssessmentDepartmentService.save(smartAssessmentDepartment);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param smartAssessmentDepartment
	 * @return
	 */
	@AutoLog(value = "负责评分的考核单位-编辑")
	@ApiOperation(value="负责评分的考核单位-编辑", notes="负责评分的考核单位-编辑")
	@PutMapping(value = "/edit")
	@Transactional(rollbackFor = Exception.class)
	public Result<?> edit(@RequestBody SmartAssessmentDepartment smartAssessmentDepartment) {
		smartAssessmentDepartmentService.updateById(smartAssessmentDepartment);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "负责评分的考核单位-通过id删除")
	@ApiOperation(value="负责评分的考核单位-通过id删除", notes="负责评分的考核单位-通过id删除")
	@DeleteMapping(value = "/delete")
	@Transactional(rollbackFor = Exception.class)
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		smartAssessmentDepartmentService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "负责评分的考核单位-批量删除")
	@ApiOperation(value="负责评分的考核单位-批量删除", notes="负责评分的考核单位-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	@Transactional(rollbackFor = Exception.class)
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartAssessmentDepartmentService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "负责评分的考核单位-通过id查询")
	@ApiOperation(value="负责评分的考核单位-通过id查询", notes="负责评分的考核单位-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartAssessmentDepartment smartAssessmentDepartment = smartAssessmentDepartmentService.getById(id);
		if(smartAssessmentDepartment==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(smartAssessmentDepartment);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param smartAssessmentDepartment
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartAssessmentDepartment smartAssessmentDepartment) {
        return super.exportXls(request, smartAssessmentDepartment, SmartAssessmentDepartment.class, "负责评分的考核单位");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
	@Transactional(rollbackFor = Exception.class)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, SmartAssessmentDepartment.class);
    }

}
