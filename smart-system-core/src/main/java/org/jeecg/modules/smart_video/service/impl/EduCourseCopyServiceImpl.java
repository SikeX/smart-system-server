package org.jeecg.modules.smart_video.service.impl;


import org.jeecg.modules.smart_video.entity.EduCourseCopy;
import org.jeecg.modules.smart_video.entity.vo.CoureseCopyInfoVo;

import org.jeecg.modules.smart_video.mapper.EduCourseCopyMapper;
import org.jeecg.modules.smart_video.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-09-11
 */
@Service
public class EduCourseCopyServiceImpl extends ServiceImpl<EduCourseCopyMapper, EduCourseCopy> implements EduCourseCopyService {

    @Autowired
    private EduVideoCopyService eduVideoCopyService;

    @Autowired
    private EduChapterCopyService eduChapterCopyService;

    @Override
    public String saveCourseCopyInfo(CoureseCopyInfoVo coureseCopyInfoVo) {
        //保存课程基本信息
        EduCourseCopy eduCourseCopy = new EduCourseCopy();
        //course.setStatus(Course.COURSE_DRAFT);
        BeanUtils.copyProperties(coureseCopyInfoVo, eduCourseCopy);
        //boolean resultCourseInfo = this.save(course);
        int insert = baseMapper.insert(eduCourseCopy);
        if(insert==0){
            System.out.println("添加失败");
        }
        String cid = eduCourseCopy.getId();
        return cid;
    }

    @Override
    public CoureseCopyInfoVo getCourseInfo(String courseId) {
        EduCourseCopy eduCourse=baseMapper.selectById(courseId);
        CoureseCopyInfoVo courseInfoVo=new CoureseCopyInfoVo();
        BeanUtils.copyProperties(eduCourse, courseInfoVo);

        return courseInfoVo;
    }

    @Override
    public void updateCourseInfo(CoureseCopyInfoVo courseInfoVo) {
        EduCourseCopy eduCourse=new EduCourseCopy();
        BeanUtils.copyProperties(courseInfoVo, eduCourse);
        int update=baseMapper.updateById(eduCourse);
        if(update==0){
            System.out.println("修改失败");
        }
    }


    //删除课程 包括章节 小节
    @Override
    public void removeCourse(String courseId) {
        //根据课程id删小节
        eduVideoCopyService.removeVideoByCourseId(courseId);
        //根据课程id删章节
        eduChapterCopyService.removeChapterByCourseId(courseId);
        //根据课程id删本身
        int result = baseMapper.deleteById(courseId);
        if(result==0){
            System.out.println("删除失败");
        }

    }
}
