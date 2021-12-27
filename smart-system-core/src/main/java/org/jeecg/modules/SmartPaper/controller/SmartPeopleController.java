package org.jeecg.modules.SmartPaper.controller;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;



import lombok.extern.slf4j.Slf4j;


import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.SmartFirstFormPeople.entity.SmartFirstFormPeople;
import org.jeecg.modules.SmartPaper.entity.SmartPeople;
import org.jeecg.modules.SmartPaper.service.ISmartPeopleService;
import org.jeecg.modules.SmartPaper.vo.ExamPeopleScoreVo;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 考试参加人员表
 * @Author: jeecg-boot
 * @Date:   2021-11-24
 * @Version: V1.0
 */
@Api(tags="考试参加人员表")
@RestController
@RequestMapping("/SmartPaper/smartPeople")
@Slf4j
public class SmartPeopleController extends JeecgController<SmartPeople, ISmartPeopleService> {
    @Autowired
    private ISmartPeopleService smartPeopleService;
    @Autowired
    private BaseCommonService baseCommonService;
    /**
     * 分页列表查询
     *
     * @param smartPeople
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "考试参加人员表-分页列表查询")
    @ApiOperation(value="考试参加人员表-分页列表查询", notes="考试参加人员表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SmartPeople smartPeople,
                                   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<SmartPeople> queryWrapper = QueryGenerator.initQueryWrapper(smartPeople, req.getParameterMap());
        Page<SmartPeople> page = new Page<SmartPeople>(pageNo, pageSize);
        IPage<SmartPeople> pageList = smartPeopleService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     *   添加
     *
     * @param smartPeople
     * @return
     */
    @AutoLog(value = "考试参加人员表-添加")
    @ApiOperation(value="考试参加人员表-添加", notes="考试参加人员表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SmartPeople smartPeople) {
        smartPeopleService.save(smartPeople);
        return Result.OK("添加成功！");
    }



    /**
     *  编辑
     *
     * @param smartPeople
     * @return
     */
    @AutoLog(value = "考试参加人员表-编辑")
    @ApiOperation(value="考试参加人员表-编辑", notes="考试参加人员表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SmartPeople smartPeople) {
        smartPeopleService.updateById(smartPeople);
        return Result.OK("编辑成功!");
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "考试参加人员表-通过id删除")
    @ApiOperation(value="考试参加人员表-通过id删除", notes="考试参加人员表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name="id",required=true) String id) {
        smartPeopleService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     *  批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "考试参加人员表-批量删除")
    @ApiOperation(value="考试参加人员表-批量删除", notes="考试参加人员表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        this.smartPeopleService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "考试参加人员表-通过id查询")
    @ApiOperation(value="考试参加人员表-通过id查询", notes="考试参加人员表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
        SmartPeople smartPeople = smartPeopleService.getById(id);
        if(smartPeople==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(smartPeople);
    }

    @GetMapping(value = "/getScoreByExamId")
    public Result<?> getScoreByExamId(@RequestParam(name="examId",required=true) String examId,
                                      @RequestParam(name="pageNo",defaultValue = "1") Integer pageNo,
                                      @RequestParam(name="pageSize",defaultValue = "10") Integer pageSize) {

        Result<IPage<ExamPeopleScoreVo>> result = new Result<IPage<ExamPeopleScoreVo>>();

        Page<ExamPeopleScoreVo> pageList = new Page<ExamPeopleScoreVo>(pageNo,pageSize);

        pageList = smartPeopleService.getScoreByExamId(pageList, examId);
        result.setResult(pageList);
        result.setSuccess(true);
        return result;
    }

    /**
     * 导出excel
     *
     * @param request
     * @param smartPeople
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartPeople smartPeople) {
        return super.exportXls(request, smartPeople, SmartPeople.class, "考试参加人员表");
    }
    /**
     * 导出成绩单
     *
     * @param req
     * @param examPeopleScoreVo
     */
    @RequestMapping(value = "/exportExamGradeXls")
    public ModelAndView exportExamGradeXls(HttpServletRequest req,
                                           HttpServletResponse response, ExamPeopleScoreVo examPeopleScoreVo,
                                  @RequestParam(name="examId",required=true) String examId,
                                  @RequestParam(name="title",required=true) String title,
                                  @RequestParam(name="pageNo",defaultValue = "1") Integer pageNo,
                                  @RequestParam(name="pageSize",defaultValue = "10") Integer pageSize) throws IOException {
        // Step.1 组装查询条件
        QueryWrapper<ExamPeopleScoreVo> queryWrapper = QueryGenerator.initQueryWrapper(examPeopleScoreVo, req.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        // Step.2 获取导出数据
        Page<ExamPeopleScoreVo> page = new Page<ExamPeopleScoreVo>(pageNo,pageSize);
        List<ExamPeopleScoreVo> pageList = (smartPeopleService.getScoreByExamId(page,examId)).getRecords();
        List<ExamPeopleScoreVo> exportList = null;
        System.out.println("##############################");
        exportList = pageList;
        System.out.println(exportList);
        for(int i=0;i<exportList.size();i++){
            ExamPeopleScoreVo examPeople = exportList.get(i);
            Integer isMark = examPeople.getIsMark();
            String score = examPeople.getExamGrade();
            if(score.equals("-1")){
                examPeople.setExamGrade("未参加");
            }else if(score.equals("0") && isMark == 0){
                examPeople.setExamGrade("已参与调查");
            }
        }
        System.out.println(exportList);

        // 过滤选中数据
/*        String selections = request.getParameter("selections");
        if (oConvertUtils.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            exportList = pageList.stream().filter(item -> selectionList.contains(item.getDeptId())).collect(Collectors.toList());
        } else {
            exportList = pageList;
        }*/

        // Step.3 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, title); //此处设置的filename无效 ,前端会重更新设置一下
        mv.addObject(NormalExcelConstants.CLASS, ExamPeopleScoreVo.class);
        //update-begin--Author:liusq  Date:20210126 for：图片导出报错，ImageBasePath未设置--------------------
        ExportParams exportParams=new ExportParams(title + "报表", "导出人:" + sysUser.getRealname(), title);
        //update-end--Author:liusq  Date:20210126 for：图片导出报错，ImageBasePath未设置----------------------
        mv.addObject(NormalExcelConstants.PARAMS,exportParams);
        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);

        // List深拷贝，否则返回前端会没数据
        List<ExamPeopleScoreVo> newPageList = ObjectUtil.cloneByStream(exportList);

        baseCommonService.addExportLog(mv.getModel(), "成绩单", req, response);

        mv.addObject(NormalExcelConstants.DATA_LIST, newPageList);
        return mv;
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
        return super.importExcel(request, response, SmartPeople.class);
    }

}
