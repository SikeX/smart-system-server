package org.jeecg.modules.smartAssessmentTeam.controller;

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
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.smartAssessmentMission.entity.SmartAssessmentDepart;
import org.jeecg.modules.smartAssessmentTeam.entity.SmartAssessmentTeam;
import org.jeecg.modules.smartAssessmentTeam.service.ISmartAssessmentTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 考核组
 * @Author: jeecg-boot
 * @Date:   2022-02-12
 * @Version: V1.0
 */
@Api(tags="考核组")
@RestController
@RequestMapping("/smartAssessmentTeam/smartAssessmentTeam")
@Slf4j
public class SmartAssessmentTeamController extends JeecgController<SmartAssessmentTeam, ISmartAssessmentTeamService> {
	@Autowired
	private ISmartAssessmentTeamService smartAssessmentTeamService;
	
	/**
	 * 分页列表查询
	 *
	 * @param smartAssessmentTeam
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "考核组-分页列表查询")
	@ApiOperation(value="考核组-分页列表查询", notes="考核组-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartAssessmentTeam smartAssessmentTeam,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SmartAssessmentTeam> queryWrapper = QueryGenerator.initQueryWrapper(smartAssessmentTeam, req.getParameterMap());
		Page<SmartAssessmentTeam> page = new Page<SmartAssessmentTeam>(pageNo, pageSize);
		IPage<SmartAssessmentTeam> pageList = smartAssessmentTeamService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 * 查询自己所属的考核组
	 *
	 * @return
	 */
	@AutoLog(value = "考核组-查询自己所属的考核组")
	@ApiOperation(value="考核组-查询自己所属的考核组", notes="考核组-查询自己所属的考核组")
	@GetMapping(value = "/listMyTeam")
	public Result<?> queryMyTeamPageList(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
										 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
										 HttpServletRequest req) {
		QueryWrapper<SmartAssessmentTeam> queryWrapper = new QueryWrapper<>();
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		//  查询自己所属的考核组信息
		queryWrapper.or().eq("team_leader", sysUser.getId())
				.or().like("deputy_team_Leader", sysUser.getId())
				.or().like("members", sysUser.getId());
		Page<SmartAssessmentTeam> page = new Page<SmartAssessmentTeam>(pageNo, pageSize);
		IPage<SmartAssessmentTeam> pageList = smartAssessmentTeamService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param smartAssessmentTeam
	 * @return
	 */
	@AutoLog(value = "考核组-添加")
	@ApiOperation(value="考核组-添加", notes="考核组-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SmartAssessmentTeam smartAssessmentTeam) {
		String departs = smartAssessmentTeam.getDeparts();
		String[] departList = departs.split(",");
		smartAssessmentTeam.setDepartAmount(departList.length);
		smartAssessmentTeamService.save(smartAssessmentTeam);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param smartAssessmentTeam
	 * @return
	 */
	@AutoLog(value = "考核组-编辑")
	@ApiOperation(value="考核组-编辑", notes="考核组-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SmartAssessmentTeam smartAssessmentTeam) {
		String departs = smartAssessmentTeam.getDeparts();
		String[] departList = departs.split(",");
		smartAssessmentTeam.setDepartAmount(departList.length);
		smartAssessmentTeamService.updateById(smartAssessmentTeam);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "考核组-通过id删除")
	@ApiOperation(value="考核组-通过id删除", notes="考核组-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		smartAssessmentTeamService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "考核组-批量删除")
	@ApiOperation(value="考核组-批量删除", notes="考核组-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartAssessmentTeamService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 考核组负责单位重复值校验
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
		QueryWrapper<SmartAssessmentTeam> queryWrapper = new QueryWrapper<>();

		if (StringUtils.isNotBlank(dataId)) {
			// [2].编辑页面校验
			queryWrapper.ne("id", dataId);
		} else {
			// [1].添加页面校验
		}

		String[] departIdList = departIds.split(",");
		if (departIdList.length > 0) {
			queryWrapper.and(qw -> {
				for (String departId : departIdList) {
					qw.or().like("departs", departId);
				}
			});
		}

		num = smartAssessmentTeamService.count(queryWrapper);

		if (num == null || num == 0) {
			// 该值可用
			return Result.ok("该值可用！");
		} else {
			// 该值不可用
			log.info("该值不可用，系统中已存在！");
			return Result.error("负责单位与别的考核组有重复！");
		}
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "考核组-通过id查询")
	@ApiOperation(value="考核组-通过id查询", notes="考核组-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartAssessmentTeam smartAssessmentTeam = smartAssessmentTeamService.getById(id);
		if(smartAssessmentTeam==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(smartAssessmentTeam);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param smartAssessmentTeam
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartAssessmentTeam smartAssessmentTeam) {
        return super.exportXls(request, smartAssessmentTeam, SmartAssessmentTeam.class, "考核组");
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
        return super.importExcel(request, response, SmartAssessmentTeam.class);
    }

}
