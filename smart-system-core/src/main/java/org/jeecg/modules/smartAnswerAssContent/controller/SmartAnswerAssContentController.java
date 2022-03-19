package org.jeecg.modules.smartAnswerAssContent.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.smartAnswerAssContent.entity.SmartAnswerAssContent;
import org.jeecg.modules.smartAnswerAssContent.entity.SmartAnswerAssScore;
import org.jeecg.modules.smartAnswerAssContent.entity.SmartAnswerFile;
import org.jeecg.modules.smartAnswerAssContent.service.ISmartAnswerAssContentService;
import org.jeecg.modules.smartAnswerAssContent.service.ISmartAnswerAssScoreService;
import org.jeecg.modules.smartAnswerAssContent.service.ISmartAnswerFileService;
import org.jeecg.modules.smartAnswerInfo.entity.SmartAnswerInfo;
import org.jeecg.modules.smartAnswerInfo.service.ISmartAnswerInfoService;
import org.jeecg.modules.smartAssessmentContent.entity.SmartAssessmentContent;
import org.jeecg.modules.smartAssessmentMission.entity.SmartAssessmentMission;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 答题考核节点表
 * @Author: jeecg-boot
 * @Date:   2022-02-21
 * @Version: V1.0
 */
@Api(tags="答题考核节点表")
@RestController
@RequestMapping("/smartAnswerAssContent/smartAnswerAssContent")
@Slf4j
public class SmartAnswerAssContentController extends JeecgController<SmartAnswerAssContent, ISmartAnswerAssContentService> {

	@Autowired
	private ISmartAnswerAssContentService smartAnswerAssContentService;

	@Autowired
	private ISmartAnswerFileService smartAnswerFileService;

	@Autowired
	private ISmartAnswerAssScoreService smartAnswerAssScoreService;

	@Autowired
	private ISmartAnswerInfoService smartAnswerInfoService;


	/*---------------------------------主表处理-begin-------------------------------------*/

	/**
	 * 分页列表查询
	 * @param smartAnswerAssContent
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "答题考核节点表-分页列表查询")
	@ApiOperation(value="答题考核节点表-分页列表查询", notes="答题考核节点表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartAnswerAssContent smartAnswerAssContent,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SmartAnswerAssContent> queryWrapper = QueryGenerator.initQueryWrapper(smartAnswerAssContent, req.getParameterMap());
		Page<SmartAnswerAssContent> page = new Page<SmartAnswerAssContent>(pageNo, pageSize);
		IPage<SmartAnswerAssContent> pageList = smartAnswerAssContentService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 * 分页列表查询
	 *
	 * @param smartAnswerAssContent
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "考核节点表-分页列表查询")
	@ApiOperation(value="考核节点表-分页列表查询", notes="考核节点表-分页列表查询")
	@GetMapping(value = "/rootList")
	public Result<?> queryRootList(SmartAnswerAssContent smartAnswerAssContent,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		String hasQuery = req.getParameter("hasQuery");
		if(hasQuery != null && "true".equals(hasQuery)){
			QueryWrapper<SmartAnswerAssContent> queryWrapper =  QueryGenerator.initQueryWrapper(smartAnswerAssContent, req.getParameterMap());
			List<SmartAnswerAssContent> list = smartAnswerAssContentService.list(queryWrapper);
			IPage<SmartAnswerAssContent> pageList = new Page<>(1, 10, list.size());
			pageList.setRecords(list);
			return Result.OK(pageList);
		}else{
			String parentId = smartAnswerAssContent.getPid();
			if (oConvertUtils.isEmpty(parentId)) {
				parentId = "0";
			}
			smartAnswerAssContent.setPid(null);
			QueryWrapper<SmartAnswerAssContent> queryWrapper = QueryGenerator.initQueryWrapper(smartAnswerAssContent, req.getParameterMap());
			// 使用 eq 防止模糊查询
			queryWrapper.eq("pid", parentId);
			Page<SmartAnswerAssContent> page = new Page<SmartAnswerAssContent>(pageNo, pageSize);
			IPage<SmartAnswerAssContent> pageList = smartAnswerAssContentService.page(page, queryWrapper);
			return Result.OK(pageList);
		}
	}

	/**
	 * 获取子数据
	 * @param smartAnswerAssContent
	 * @param req
	 * @return
	 */
	@AutoLog(value = "考核节点表-获取子数据")
	@ApiOperation(value="考核节点表-获取子数据", notes="考核节点表-获取子数据")
	@GetMapping(value = "/childList")
	public Result<?> queryPageList(SmartAnswerAssContent smartAnswerAssContent,HttpServletRequest req) {
		QueryWrapper<SmartAnswerAssContent> queryWrapper = QueryGenerator.initQueryWrapper(smartAnswerAssContent, req.getParameterMap());
		List<SmartAnswerAssContent> list = smartAnswerAssContentService.list(queryWrapper);
		IPage<SmartAnswerAssContent> pageList = new Page<>(1, 10, list.size());
		pageList.setRecords(list);
		return Result.OK(pageList);
	}

	/**
	 * 批量查询子节点
	 * @param parentIds 父ID（多个采用半角逗号分割）
	 * @return 返回 IPage
	 * @param parentIds
	 * @return
	 */
	@AutoLog(value = "考核节点表-批量获取子数据")
	@ApiOperation(value="考核节点表-批量获取子数据", notes="考核节点表-批量获取子数据")
	@GetMapping("/getChildListBatch")
	public Result getChildListBatch(@RequestParam("parentIds") String parentIds) {
		try {
			QueryWrapper<SmartAnswerAssContent> queryWrapper = new QueryWrapper<>();
			List<String> parentIdList = Arrays.asList(parentIds.split(","));
			queryWrapper.in("pid", parentIdList);
			List<SmartAnswerAssContent> list = smartAnswerAssContentService.list(queryWrapper);
			IPage<SmartAnswerAssContent> pageList = new Page<>(1, 10, list.size());
			pageList.setRecords(list);
			return Result.OK(pageList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Result.error("批量查询子节点失败：" + e.getMessage());
		}
	}


	/**
     *   添加
     * @param smartAnswerAssContent
     * @return
     */
    @AutoLog(value = "答题考核节点表-添加")
    @ApiOperation(value="答题考核节点表-添加", notes="答题考核节点表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SmartAnswerAssContent smartAnswerAssContent) {
        smartAnswerAssContentService.save(smartAnswerAssContent);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     * @param smartAnswerAssContent
     * @return
     */
    @AutoLog(value = "答题考核节点表-编辑")
    @ApiOperation(value="答题考核节点表-编辑", notes="答题考核节点表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SmartAnswerAssContent smartAnswerAssContent) {
        smartAnswerAssContentService.updateById(smartAnswerAssContent);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     * @param id
     * @return
     */
    @AutoLog(value = "答题考核节点表-通过id删除")
    @ApiOperation(value="答题考核节点表-通过id删除", notes="答题考核节点表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name="id",required=true) String id) {
        smartAnswerAssContentService.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @AutoLog(value = "答题考核节点表-批量删除")
    @ApiOperation(value="答题考核节点表-批量删除", notes="答题考核节点表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        this.smartAnswerAssContentService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 导出
     * @return
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartAnswerAssContent smartAnswerAssContent) {
        return super.exportXls(request, smartAnswerAssContent, SmartAnswerAssContent.class, "答题考核节点表");
    }

    /**
     * 导入
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, SmartAnswerAssContent.class);
    }
	/*---------------------------------主表处理-end-------------------------------------*/


    /*--------------------------------子表处理-要点答题附件-begin----------------------------------------------*/
	/**
	 * 通过主表ID查询
	 * @return
	 */
	@AutoLog(value = "要点答题附件-通过主表ID查询")
	@ApiOperation(value="要点答题附件-通过主表ID查询", notes="要点答题附件-通过主表ID查询")
	@GetMapping(value = "/listSmartAnswerFileByMainId")
    public Result<?> listSmartAnswerFileByMainId(SmartAnswerFile smartAnswerFile,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest req) {
        QueryWrapper<SmartAnswerFile> queryWrapper = QueryGenerator.initQueryWrapper(smartAnswerFile, req.getParameterMap());
        Page<SmartAnswerFile> page = new Page<SmartAnswerFile>(pageNo, pageSize);
        IPage<SmartAnswerFile> pageList = smartAnswerFileService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

	/**
	 * 添加
	 * @param smartAnswerFile
	 * @return
	 */
	@AutoLog(value = "要点答题附件-添加")
	@ApiOperation(value="要点答题附件-添加", notes="要点答题附件-添加")
	@PostMapping(value = "/addSmartAnswerFile")
	public Result<?> addSmartAnswerFile(@RequestBody SmartAnswerFile smartAnswerFile) {
		// 获取登录用户信息，可以用来查询单位部门信息
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		// 存储上报人ID
		smartAnswerFile.setUploadUser(sysUser.getId());
		smartAnswerFileService.save(smartAnswerFile);

		// 答题考核节点要点状态+1
		SmartAnswerAssContent assContent = smartAnswerAssContentService.getById(smartAnswerFile.getMainId());
		assContent.setContentStatus(assContent.getContentStatus() + 1);
		smartAnswerAssContentService.updateById(assContent);
		return Result.OK("添加成功！");
	}

    /**
	 * 编辑
	 * @param smartAnswerFile
	 * @return
	 */
	@AutoLog(value = "要点答题附件-编辑")
	@ApiOperation(value="要点答题附件-编辑", notes="要点答题附件-编辑")
	@PutMapping(value = "/editSmartAnswerFile")
	public Result<?> editSmartAnswerFile(@RequestBody SmartAnswerFile smartAnswerFile) {
		smartAnswerFileService.updateById(smartAnswerFile);
		return Result.OK("编辑成功!");
	}

	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "要点答题附件-通过id删除")
	@ApiOperation(value="要点答题附件-通过id删除", notes="要点答题附件-通过id删除")
	@DeleteMapping(value = "/deleteSmartAnswerFile")
	public Result<?> deleteSmartAnswerFile(@RequestParam(name="id",required=true) String id,
										   @RequestParam(name="mainId",required=true) String mainId) {
		// 答题考核节点要点状态 -1
		SmartAnswerAssContent assContent = smartAnswerAssContentService.getById(mainId);
		assContent.setContentStatus(assContent.getContentStatus() - 1);
		smartAnswerAssContentService.updateById(assContent);
		smartAnswerFileService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "要点答题附件-批量删除")
	@ApiOperation(value="要点答题附件-批量删除", notes="要点答题附件-批量删除")
	@DeleteMapping(value = "/deleteBatchSmartAnswerFile")
	public Result<?> deleteBatchSmartAnswerFile(@RequestParam(name="ids",required=true) String ids,
                                                @RequestParam(name="mainId",required=true) String mainId,
                                                @RequestParam(name="count",required=true) Integer count) {
        // 答题考核节点要点状态 -count
        SmartAnswerAssContent assContent = smartAnswerAssContentService.getById(mainId);
        assContent.setContentStatus(assContent.getContentStatus() - count);
        smartAnswerAssContentService.updateById(assContent);
	    this.smartAnswerFileService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

    /**
     * 导出
     * @return
     */
    @RequestMapping(value = "/exportSmartAnswerFile")
    public ModelAndView exportSmartAnswerFile(HttpServletRequest request, SmartAnswerFile smartAnswerFile) {
		 // Step.1 组装查询条件
		 QueryWrapper<SmartAnswerFile> queryWrapper = QueryGenerator.initQueryWrapper(smartAnswerFile, request.getParameterMap());
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		 // Step.2 获取导出数据
		 List<SmartAnswerFile> pageList = smartAnswerFileService.list(queryWrapper);
		 List<SmartAnswerFile> exportList = null;

		 // 过滤选中数据
		 String selections = request.getParameter("selections");
		 if (oConvertUtils.isNotEmpty(selections)) {
			 List<String> selectionList = Arrays.asList(selections.split(","));
			 exportList = pageList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
		 } else {
			 exportList = pageList;
		 }

		 // Step.3 AutoPoi 导出Excel
		 ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		 mv.addObject(NormalExcelConstants.FILE_NAME, "要点答题附件"); //此处设置的filename无效 ,前端会重更新设置一下
		 mv.addObject(NormalExcelConstants.CLASS, SmartAnswerFile.class);
		 mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("要点答题附件报表", "导出人:" + sysUser.getRealname(), "要点答题附件"));
		 mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
		 return mv;
    }

    /**
     * 导入
     * @return
     */
    @RequestMapping(value = "/importSmartAnswerFile/{mainId}")
    public Result<?> importSmartAnswerFile(HttpServletRequest request, HttpServletResponse response, @PathVariable("mainId") String mainId) {
		 MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		 Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		 for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			 MultipartFile file = entity.getValue();// 获取上传文件对象
			 ImportParams params = new ImportParams();
			 params.setTitleRows(2);
			 params.setHeadRows(1);
			 params.setNeedSave(true);
			 try {
				 List<SmartAnswerFile> list = ExcelImportUtil.importExcel(file.getInputStream(), SmartAnswerFile.class, params);
				 for (SmartAnswerFile temp : list) {
                    temp.setMainId(mainId);
				 }
				 long start = System.currentTimeMillis();
				 smartAnswerFileService.saveBatch(list);
				 log.info("消耗时间" + (System.currentTimeMillis() - start) + "毫秒");
				 return Result.OK("文件导入成功！数据行数：" + list.size());
			 } catch (Exception e) {
				 log.error(e.getMessage(), e);
				 return Result.error("文件导入失败:" + e.getMessage());
			 } finally {
				 try {
					 file.getInputStream().close();
				 } catch (IOException e) {
					 e.printStackTrace();
				 }
			 }
		 }
		 return Result.error("文件导入失败！");
    }

    /*--------------------------------子表处理-要点答题附件-end----------------------------------------------*/

    /*--------------------------------子表处理-答题评分表-begin----------------------------------------------*/
	/**
	 * 通过主表ID查询
	 * @return
	 */
	@AutoLog(value = "答题评分表-通过主表ID查询")
	@ApiOperation(value="答题评分表-通过主表ID查询", notes="答题评分表-通过主表ID查询")
	@GetMapping(value = "/listSmartAnswerAssScoreByMainId")
    public Result<?> listSmartAnswerAssScoreByMainId(SmartAnswerAssScore smartAnswerAssScore,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest req) {
        QueryWrapper<SmartAnswerAssScore> queryWrapper = QueryGenerator.initQueryWrapper(smartAnswerAssScore, req.getParameterMap());
        Page<SmartAnswerAssScore> page = new Page<SmartAnswerAssScore>(pageNo, pageSize);
        IPage<SmartAnswerAssScore> pageList = smartAnswerAssScoreService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

	/**
	 * 修改最终成绩得分方式
	 *
	 * @param missionId
	 * @param contentId
	 * @param scoreType
	 * @return
	 */
	@AutoLog(value = "答题信息表-修改最终成绩得分方式")
	@ApiOperation(value = "答题信息表-修改最终成绩得分方式", notes = "答题信息表-修改最终成绩得分方式")
	@GetMapping(value = "/changeFinalScore")
	public Result<?> changeFinalScore(@RequestParam(name="missionId",required=true) String missionId,
									  @RequestParam(name="contentId",required=true) String contentId,
									  @RequestParam(name="scoreType",required=true) String scoreType) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		// 查询考核任务下的所有答题信息记录
		QueryWrapper<SmartAnswerInfo> answerInfoQueryWrapper = new QueryWrapper<>();
		answerInfoQueryWrapper.select("id").eq("mission_id", missionId);
		List<SmartAnswerInfo> answerInfoList = smartAnswerInfoService.list(answerInfoQueryWrapper);
		List<String> mainIdList = new ArrayList<>();
		answerInfoList.forEach(smartAnswerInfo -> mainIdList.add(smartAnswerInfo.getId()));

		// 查询相关的所有答题考核节点
		QueryWrapper<SmartAnswerAssContent> assContentQueryWrapper = new QueryWrapper<>();
		assContentQueryWrapper.in("main_id", mainIdList).eq("ass_content_id", contentId);
		List<SmartAnswerAssContent> assContentList = smartAnswerAssContentService.list(assContentQueryWrapper);
		for (SmartAnswerAssContent smartAnswerAssContent : assContentList) {
			double increment = 0;
			// 更新节点最终成绩
			if ("low".equals(scoreType)) {
				// 计算增量
				increment = smartAnswerAssContent.getLowestScore() - smartAnswerAssContent.getFinalScore();
				smartAnswerAssContent.setFinalScore(smartAnswerAssContent.getLowestScore());
			} else if ("high".equals(scoreType)) {
				increment = smartAnswerAssContent.getHighestScore() - smartAnswerAssContent.getFinalScore();
				smartAnswerAssContent.setFinalScore(smartAnswerAssContent.getHighestScore());
			} else if ("ave".equals(scoreType)) {
				increment = smartAnswerAssContent.getAverageScore() - smartAnswerAssContent.getFinalScore();
				smartAnswerAssContent.setFinalScore(smartAnswerAssContent.getAverageScore());
			} else {
				return Result.error("最终评分类型有误!");
			}
			// 更新当前节点信息
			smartAnswerAssContentService.updateById(smartAnswerAssContent);

			// 更新上级节点最终成绩
			updateSuperiorScore(smartAnswerAssContent, increment);
		}

		// TODO: 更新答题信息表中的总分

		return Result.OK("修改成功!");
	}

	/**
	 * 更新上级成绩
	 *
	 * @param smartAnswerAssContent
	 * @param increment              成绩增量
	 */
	private void updateSuperiorScore(SmartAnswerAssContent smartAnswerAssContent, double increment) {
		QueryWrapper<SmartAnswerAssContent> queryWrapper = new QueryWrapper<>();
		while (oConvertUtils.isNotEmpty(smartAnswerAssContent.getPid())) {
			// 查找该考核要点的上级
			queryWrapper.clear();
			queryWrapper.eq("ass_content_id", smartAnswerAssContent.getPid()).eq("main_id", smartAnswerAssContent.getMainId());
			smartAnswerAssContent = smartAnswerAssContentService.getOne(queryWrapper);
			if (oConvertUtils.isEmpty(smartAnswerAssContent)) {
				break;
			}
			smartAnswerAssContent.setFinalScore(smartAnswerAssContent.getFinalScore() + increment);
			smartAnswerAssContentService.updateById(smartAnswerAssContent);
		}
	}

	/**
	 * 添加
	 * @param smartAnswerAssScore
	 * @return
	 */
	@AutoLog(value = "答题评分表-添加")
	@ApiOperation(value="答题评分表-添加", notes="答题评分表-添加")
	@PostMapping(value = "/addSmartAnswerAssScore")
	public Result<?> addSmartAnswerAssScore(@RequestBody SmartAnswerAssScore smartAnswerAssScore) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		smartAnswerAssScore.setRatingUser(sysUser.getId());
		smartAnswerAssScoreService.save(smartAnswerAssScore);

		SmartAnswerAssContent answerAssContent = smartAnswerAssContentService.getById(smartAnswerAssScore.getMainId());
		// 如果大于最高分则更新最高分和最终得分
		if (smartAnswerAssScore.getScore() > answerAssContent.getHighestScore()) {
			answerAssContent.setHighestScore(smartAnswerAssScore.getScore());
			// 先更新上级最终得分
			updateSuperiorScore(answerAssContent, smartAnswerAssScore.getScore() - answerAssContent.getFinalScore());

			// 默认取最高得分为最终得分
			answerAssContent.setFinalScore(smartAnswerAssScore.getScore());
		}
		if (smartAnswerAssScore.getScore() < answerAssContent.getLowestScore() || answerAssContent.getLowestScore() == 0) {
			// 如果小于最低分或最低分为0则更新最低分
			answerAssContent.setLowestScore(smartAnswerAssScore.getScore());
		}
		// 平均分应该是所有人评分的平均分，但好像只有两个评分
		answerAssContent.setAverageScore((answerAssContent.getHighestScore() + answerAssContent.getLowestScore())/ 2);
		// 更新考核内容节点分数
		smartAnswerAssContentService.updateById(answerAssContent);
		return Result.OK("添加成功！");
	}

    /**
	 * 编辑
	 * @param smartAnswerAssScore
	 * @return
	 */
	@AutoLog(value = "答题评分表-编辑")
	@ApiOperation(value="答题评分表-编辑", notes="答题评分表-编辑")
	@PutMapping(value = "/editSmartAnswerAssScore")
	public Result<?> editSmartAnswerAssScore(@RequestBody SmartAnswerAssScore smartAnswerAssScore) {
		smartAnswerAssScoreService.updateById(smartAnswerAssScore);

		// TODO: 如果更新的分数原来是最高分呢或者最低分呢
		QueryWrapper<SmartAnswerAssScore> scoreQueryWrapper = new QueryWrapper<>();
		scoreQueryWrapper.select("max(score) as score").eq("main_id", smartAnswerAssScore.getMainId());
		SmartAnswerAssScore maxScore = smartAnswerAssScoreService.getOne(scoreQueryWrapper);

		scoreQueryWrapper.clear();
		scoreQueryWrapper.select("min(score) as score").eq("main_id", smartAnswerAssScore.getMainId());
		SmartAnswerAssScore minScore = smartAnswerAssScoreService.getOne(scoreQueryWrapper);

		scoreQueryWrapper.clear();
		scoreQueryWrapper.select("avg(score) as score").eq("main_id", smartAnswerAssScore.getMainId());
		SmartAnswerAssScore avgScore = smartAnswerAssScoreService.getOne(scoreQueryWrapper);


		SmartAnswerAssContent answerAssContent = smartAnswerAssContentService.getById(smartAnswerAssScore.getMainId());
		// 如果最高成绩改变了，则更新上级节点成绩
		if (!maxScore.getScore().equals(answerAssContent.getFinalScore())) {
			updateSuperiorScore(answerAssContent, maxScore.getScore() - answerAssContent.getFinalScore());
		}
		answerAssContent.setHighestScore(maxScore.getScore());
		answerAssContent.setFinalScore(maxScore.getScore());
		answerAssContent.setLowestScore(minScore.getScore());
		answerAssContent.setAverageScore(avgScore.getScore());
		// 更新考核内容节点分数
		smartAnswerAssContentService.updateById(answerAssContent);

		return Result.OK("编辑成功!");
	}

	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "答题评分表-通过id删除")
	@ApiOperation(value="答题评分表-通过id删除", notes="答题评分表-通过id删除")
	@DeleteMapping(value = "/deleteSmartAnswerAssScore")
	public Result<?> deleteSmartAnswerAssScore(@RequestParam(name="id",required=true) String id) {
		smartAnswerAssScoreService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "答题评分表-批量删除")
	@ApiOperation(value="答题评分表-批量删除", notes="答题评分表-批量删除")
	@DeleteMapping(value = "/deleteBatchSmartAnswerAssScore")
	public Result<?> deleteBatchSmartAnswerAssScore(@RequestParam(name="ids",required=true) String ids) {
	    this.smartAnswerAssScoreService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

    /**
     * 导出
     * @return
     */
    @RequestMapping(value = "/exportSmartAnswerAssScore")
    public ModelAndView exportSmartAnswerAssScore(HttpServletRequest request, SmartAnswerAssScore smartAnswerAssScore) {
		 // Step.1 组装查询条件
		 QueryWrapper<SmartAnswerAssScore> queryWrapper = QueryGenerator.initQueryWrapper(smartAnswerAssScore, request.getParameterMap());
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		 // Step.2 获取导出数据
		 List<SmartAnswerAssScore> pageList = smartAnswerAssScoreService.list(queryWrapper);
		 List<SmartAnswerAssScore> exportList = null;

		 // 过滤选中数据
		 String selections = request.getParameter("selections");
		 if (oConvertUtils.isNotEmpty(selections)) {
			 List<String> selectionList = Arrays.asList(selections.split(","));
			 exportList = pageList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
		 } else {
			 exportList = pageList;
		 }

		 // Step.3 AutoPoi 导出Excel
		 ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		 mv.addObject(NormalExcelConstants.FILE_NAME, "答题评分表"); //此处设置的filename无效 ,前端会重更新设置一下
		 mv.addObject(NormalExcelConstants.CLASS, SmartAnswerAssScore.class);
		 mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("答题评分表报表", "导出人:" + sysUser.getRealname(), "答题评分表"));
		 mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
		 return mv;
    }

    /**
     * 导入
     * @return
     */
    @RequestMapping(value = "/importSmartAnswerAssScore/{mainId}")
    public Result<?> importSmartAnswerAssScore(HttpServletRequest request, HttpServletResponse response, @PathVariable("mainId") String mainId) {
		 MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		 Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		 for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			 MultipartFile file = entity.getValue();// 获取上传文件对象
			 ImportParams params = new ImportParams();
			 params.setTitleRows(2);
			 params.setHeadRows(1);
			 params.setNeedSave(true);
			 try {
				 List<SmartAnswerAssScore> list = ExcelImportUtil.importExcel(file.getInputStream(), SmartAnswerAssScore.class, params);
				 for (SmartAnswerAssScore temp : list) {
                    temp.setMainId(mainId);
				 }
				 long start = System.currentTimeMillis();
				 smartAnswerAssScoreService.saveBatch(list);
				 log.info("消耗时间" + (System.currentTimeMillis() - start) + "毫秒");
				 return Result.OK("文件导入成功！数据行数：" + list.size());
			 } catch (Exception e) {
				 log.error(e.getMessage(), e);
				 return Result.error("文件导入失败:" + e.getMessage());
			 } finally {
				 try {
					 file.getInputStream().close();
				 } catch (IOException e) {
					 e.printStackTrace();
				 }
			 }
		 }
		 return Result.error("文件导入失败！");
    }

    /*--------------------------------子表处理-答题评分表-end----------------------------------------------*/




}
