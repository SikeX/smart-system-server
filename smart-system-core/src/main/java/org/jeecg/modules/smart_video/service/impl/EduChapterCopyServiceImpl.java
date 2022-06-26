package org.jeecg.modules.smart_video.service.impl;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.smart_video.commonutils.R;
import org.jeecg.modules.smart_video.entity.EduChapterCopy;
import org.jeecg.modules.smart_video.entity.EduVideoCopy;
import org.jeecg.modules.smart_video.entity.chaptercopy.ChapterCopyVo;
import org.jeecg.modules.smart_video.entity.chaptercopy.VideoCopyVo;
import org.jeecg.modules.smart_video.mapper.EduChapterCopyMapper;
import org.jeecg.modules.smart_video.service.EduChapterCopyService;
import org.jeecg.modules.smart_video.service.EduVideoCopyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-09-11
 */
@Service
public class EduChapterCopyServiceImpl extends ServiceImpl<EduChapterCopyMapper, EduChapterCopy> implements EduChapterCopyService {

    @Autowired
    private EduVideoCopyServiceImpl eduVideoCopyService;

    @Override
    public List<ChapterCopyVo> getChapterVideoByCourseId(String courseId) {
        //1.根据id查询课程里面的所有章节

        QueryWrapper<EduChapterCopy> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("course_id", courseId);
        //queryWrapper1.orderByAsc("sort", "id");
        List<EduChapterCopy> eduChapterList = baseMapper.selectList(queryWrapper1);

        //2.根据id查询里面所有的小节
        QueryWrapper<EduVideoCopy> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("course_id", courseId);
        queryWrapper2.orderByAsc("sort", "id");
        List<EduVideoCopy> eduVideoList = eduVideoCopyService.list(queryWrapper2);
        //创建list集合，用于最终封装数据
        List<ChapterCopyVo> finalList =new ArrayList<>();

        //3.遍历查询章节list进行封装
        for (int i = 0; i < eduChapterList.size(); i++) {
            EduChapterCopy eduChapter = eduChapterList.get(i);
            ChapterCopyVo chapterVo = new ChapterCopyVo();
            BeanUtils.copyProperties(eduChapter, chapterVo);
            finalList.add(chapterVo);
            //4.遍历查询小节list进行封装

            //创建集合，用于封装章节的小节
            List<VideoCopyVo> videoVoList= new ArrayList<>();
            for (int m = 0;  m < eduVideoList.size(); m++) {
                EduVideoCopy eduVideo =eduVideoList.get(m);
                //判断小节里面的chapter——id和章节里面id是否一样
                if(eduVideo.getChapterId().equals(eduChapter.getId())){
                    VideoCopyVo videoVo=new VideoCopyVo();
                    BeanUtils.copyProperties(eduVideo, videoVo);

                    videoVoList.add(videoVo);
                }
            }
            chapterVo.setChildren(videoVoList);
        }

        return finalList;
    }

    @Override
    public boolean deleteChapter(String chapterId) {
        //根绝chapterid章节id 查询小节表，如果查询数据，不进行删除
        QueryWrapper<EduVideoCopy> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id",chapterId);
        List<EduVideoCopy> video = eduVideoCopyService.list(wrapper);
        List<String> IdList = new ArrayList<>();
        for (EduVideoCopy A : video){
            IdList.add(A.getId());
        }
        Long count = eduVideoCopyService.count(wrapper);
        if(count>0){//查出小节不进行删除
            for(String ID : IdList){
                eduVideoCopyService.removeById(ID);
            }
            this.deleteChapter(chapterId);
            return true;
        }else{//不能查出数据，进行删除
            int result = baseMapper.deleteById(chapterId);
            return result>0;

        }
    }

    @Override
    public void removeChapterByCourseId(String courseId) {
        QueryWrapper<EduChapterCopy> wrapper=new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        baseMapper.delete(wrapper);
    }
}
