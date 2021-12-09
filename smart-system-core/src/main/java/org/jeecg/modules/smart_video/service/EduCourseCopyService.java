package org.jeecg.modules.smart_video.service;

import org.jeecg.modules.smart_video.entity.EduCourseCopy;
import org.jeecg.modules.smart_video.entity.vo.CoureseCopyInfoVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-09-11
 */
public interface EduCourseCopyService extends IService<EduCourseCopy> {

    String saveCourseCopyInfo(CoureseCopyInfoVo coureseCopyInfoVo);

    CoureseCopyInfoVo getCourseInfo(String courseId);

    void updateCourseInfo(CoureseCopyInfoVo courseInfoVo);

    void removeCourse(String courseId);
}
