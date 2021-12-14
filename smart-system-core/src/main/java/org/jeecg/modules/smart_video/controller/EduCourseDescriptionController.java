package org.jeecg.modules.smart_video.controller;


import org.jeecg.modules.smart_video.commonutils.R;
import org.jeecg.modules.smart_video.entity.EduCourseDescription;
import org.jeecg.modules.smart_video.service.EduCourseDescriptionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程概括模块 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-08-21
 */
@Api(description="课程概括")
@RestController
@RequestMapping("/serviceedu/edu-course-description")
@CrossOrigin
public class EduCourseDescriptionController {
    @Autowired
    private EduCourseDescriptionService courseDescriptionService;

    @GetMapping("findAll")
    @ApiOperation(value = "所有课程概括数据库表内容")
    public R list(){
        List<EduCourseDescription> list=courseDescriptionService.list(null);
        return R.ok().data("items", list);
    }
    @ApiOperation(value = "查询课程介绍")
    @GetMapping("findCourseIntroduce")
    public R CourIntrolist(){
        List<EduCourseDescription> list=courseDescriptionService.list(null);
        Object[] a={list.get(0)};
        return R.ok().data("items",a);
    }

    @ApiOperation(value = "查询课程特色")
    @GetMapping("findCourseFeatures")
    public R CourFeatlist(){
        List<EduCourseDescription> list=courseDescriptionService.list(null);
        Object[] a={list.get(1)};
        return R.ok().data("items",a);
    }

    @ApiOperation(value = "查询教学大纲")
    @GetMapping("findSyllabus")
    public R Syllabuslist(){
        List<EduCourseDescription> list=courseDescriptionService.list(null);
        Object[] a={list.get(2)};
        return R.ok().data("items", a);
    }
    @ApiOperation(value = "查询教学日历")
    @GetMapping("findTeaCalendar")
    public R TeaCalendarlist(){
        List<EduCourseDescription> list=courseDescriptionService.list(null);
        Object[] a={list.get(3)};
        return R.ok().data("items", a);
    }

    @ApiOperation(value = "查询参考资料")
    @GetMapping("findReference")
    public R Referencelist(){
        List<EduCourseDescription> list=courseDescriptionService.list(null);
        Object[] a={list.get(4)};
        return R.ok().data("items", a);
    }

    @ApiOperation(value = "根据ID查询数据")
    @GetMapping("course/{id}")
    public R getById(
            @ApiParam(name = "id", value = "ID", required = true)
            @PathVariable String id){

        EduCourseDescription eduCourseDescription = courseDescriptionService.getById(id);
        return R.ok().data("item", eduCourseDescription);
    }


    @ApiOperation(value = "根据ID修改相关内容")
    @PutMapping("{id}")
    public R updateById(
            @ApiParam(name = "id", value = "ID", required = true)
            @PathVariable String id,

            @ApiParam(name = "EdeCourseDescription", value = "对象", required = true)
            @RequestBody EduCourseDescription eduCourseDescription){

        eduCourseDescription.setCourseId(id);
        courseDescriptionService.updateById(eduCourseDescription);
        return R.ok();
    }



    @ApiOperation(value = "根据ID删除课程概括里面相关内容，估计用不到！")
    @DeleteMapping("{id}")
    public R removeById(@PathVariable String id){
        boolean flag= courseDescriptionService.removeById(id);
        if(flag){
            return R.ok();
        }else {
            return R.error();
        }
    }

    @ApiOperation(value = "增加相关内容")
    @PostMapping("addCourse")
    public R addCourse(@RequestBody EduCourseDescription eduCourseDescription) {
        boolean save = courseDescriptionService.save(eduCourseDescription);
        if (save) {
            return R.ok();
        } else {
            return R.error();
        }

    }

    @ApiOperation(value = "修改相关内容")
    @PostMapping("updateCourse")
    public R updateCourse(
            @ApiParam(name = "teacher", value = "对象", required = true)
            @RequestBody EduCourseDescription eduCourseDescription) {

        boolean flag=courseDescriptionService.updateById(eduCourseDescription);
        if(flag) {
            return R.ok();
        }else{return R.error();}
    }
}

