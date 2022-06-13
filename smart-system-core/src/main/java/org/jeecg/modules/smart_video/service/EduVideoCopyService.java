package org.jeecg.modules.smart_video.service;

import org.jeecg.modules.smart_video.entity.EduVideoCopy;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-09-11
 */
public interface EduVideoCopyService extends IService<EduVideoCopy> {

    void removeVideoByCourseId(String courseId);
}
