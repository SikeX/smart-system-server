package org.jeecg.modules.smart_video.controller;

import org.jeecg.modules.smart_video.commonutils.R;

import org.jeecg.modules.smart_video.entity.EduChapterCopy;
import org.jeecg.modules.smart_video.entity.chaptercopy.ChapterCopyVo;
import org.jeecg.modules.smart_video.service.EduChapterCopyService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@RequestMapping("/serviceedu/edu-chapter-copy")
@CrossOrigin
@Api(description="课程章节")
public class EduChapterCopyController {
    @Autowired
    private EduChapterCopyService chapterCopyService;

    //课程大纲列表，根据id进行查询
    @ApiOperation(value = "嵌套章节数据列表")
    @GetMapping("getChapterVideo/{courseId}")
    public R getChapterVideo(
            @ApiParam(name = "courseId", value = "ID", required = true)
            @PathVariable String courseId){

        List<ChapterCopyVo> List = chapterCopyService.getChapterVideoByCourseId(courseId);
        return R.ok().data("allChapterVideo", List);
    }

    //添加
    @ApiOperation(value = "新增课程章节")
    @PostMapping("addChapter")
    public R addChapter(@RequestBody EduChapterCopy eduChapter){

        chapterCopyService.save(eduChapter);
        return R.ok();
    }

    //根据id查询
    @ApiOperation(value = "根据ID查询章节")
    @GetMapping("getChapterInfo/{chapterId}")
    public R getChapterInfo(@PathVariable String chapterId){

        EduChapterCopy eduChapter  = chapterCopyService.getById(chapterId);
        return R.ok().data("chapter", eduChapter);
    }

    //修改
    @ApiOperation(value = "章节")
    @PostMapping("updateChapter")
    public R updateChapter(@RequestBody EduChapterCopy eduChapter){

        chapterCopyService.updateById(eduChapter);
        return R.ok();
    }

    //删除
    @ApiOperation(value = "根据ID删除章节")
    @DeleteMapping("{chapterId}")
    public R deleteChapter(@PathVariable String chapterId){

        boolean result = chapterCopyService.deleteChapter(chapterId);
        if(result){
            return R.ok();
        }else{
            return R.error().message("删除失败");
        }
    }



}

