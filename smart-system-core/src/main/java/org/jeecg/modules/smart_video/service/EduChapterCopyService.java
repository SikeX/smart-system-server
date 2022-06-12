package org.jeecg.modules.smart_video.service;

import org.jeecg.modules.smart_video.entity.EduChapterCopy;
import org.jeecg.modules.smart_video.entity.chaptercopy.ChapterCopyVo;
import com.baomidou.mybatisplus.extension.service.IService;


import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-09-11
 */
public interface EduChapterCopyService extends IService<EduChapterCopy> {

    List<ChapterCopyVo> getChapterVideoByCourseId(String courseId);

    boolean deleteChapter(String chapterId);

    void removeChapterByCourseId(String courseId);
}
