package org.jeecg.modules.smart_video.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.smart_8regulations_for_reception.entity.Smart_8regulationsForReception;
import org.jeecg.modules.smart_8regulations_for_reception.service.ISmart_8regulationsForReceptionService;
import org.jeecg.modules.smart_8regulations_for_reception.vo.Smart_8regulationsForReceptionPage;
import org.jeecg.modules.smart_data_sheet_new.entity.SmartDataSheetNew;
import org.jeecg.modules.smart_video.commonutils.R;
import org.jeecg.modules.smart_video.entity.*;

import org.jeecg.modules.smart_video.entity.EduCourseCopy;
import org.jeecg.modules.smart_video.entity.vo.CoureseCopyInfoVo;
import org.jeecg.modules.smart_video.service.EduCourseCopyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.jeecg.modules.smart_video.entity.vo.CoureseCopyInfoVo;
import org.jeecg.modules.smart_video.service.EduCourseCopyService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-09-11
 */
@RestController
@RequestMapping("/serviceedu/edu-course-copy")
@CrossOrigin
@Slf4j
@Api(description="课程学习")
public class EduCourseCopyController {
    @Autowired
    private EduCourseCopyService eduCourseCopyService;



    @Autowired
    private EduCourseCopyService courseCopyService;

    //课程列表，查所有课程基本信息
    @GetMapping
    @ApiOperation(value = "课程列表")
    public R getCourseCopyList(){
        List<EduCourseCopy> list =courseCopyService.list(null);
        return R.ok().data("list",list);
    }

    //根据id查询基本信息
    @ApiOperation(value = "根据id查询")
    @GetMapping("getCourseInfo/{courseId}")
//    public R getCourseInfo(@PathVariable String courseId){
//        CoureseCopyInfoVo courseInfoVo=courseCopyService.getCourseInfo(courseId);
//        return R.ok().data("courseInfoVo",courseInfoVo);
//    }
    public Result<?> getCourseInfo(@RequestParam(name="courseId",required=true) String courseId) {
        EduCourseCopy eduCourseCopy = eduCourseCopyService.getById(courseId);
        if(eduCourseCopy==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(eduCourseCopy);
    }

    //修改信息
    @ApiOperation(value = "更新课程")
    @PostMapping("updateCourseInfo")
    public R updateCourseInfo(@RequestBody CoureseCopyInfoVo courseInfoVo){

        courseCopyService.updateCourseInfo(courseInfoVo);
        return R.ok();
    }



    @ApiOperation(value = "新增课程学习")
    @PostMapping("addCourseCopyInfo")
    public R addCourseCopyInfo(
            @ApiParam(name = "CourseInfoForm", value = "实验基本信息", required = true)
            @RequestBody CoureseCopyInfoVo coureseCopyInfoVo){
        log.info(String.valueOf(coureseCopyInfoVo));
        String id = courseCopyService.saveCourseCopyInfo(coureseCopyInfoVo);
        return R.ok().data("courseId",id);
    }
    //删除
    @ApiOperation(value = "根据ID删除")
    @DeleteMapping("{courseId}")
    public R deleteCourse(@PathVariable String courseId){

        courseCopyService.removeCourse(courseId);

        return R.ok();

    }

}

