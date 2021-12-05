package org.jeecg.modules.smart_video.service.impl;

import org.jeecg.modules.smart_video.client.OssClient;
import org.jeecg.modules.smart_video.client.VodClient;
import org.jeecg.modules.smart_video.entity.EduVideoCopy;
import org.jeecg.modules.smart_video.mapper.EduVideoCopyMapper;
import org.jeecg.modules.smart_video.service.EduVideoCopyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-09-11
 */
@Service
public class EduVideoCopyServiceImpl extends ServiceImpl<EduVideoCopyMapper, EduVideoCopy> implements EduVideoCopyService {
    @Autowired
    private VodClient vodClient;

    @Autowired
    private OssClient ossClient;
    //根据课程id删除小节（包括小节中的视频和文件）
    @Override
    public void removeVideoByCourseId(String courseId) {
        // 1根据课程id查出所有视频id
        QueryWrapper<EduVideoCopy> wrapperVideo = new QueryWrapper<>();
        wrapperVideo.eq("course_id", courseId);
        wrapperVideo.select("video_source_id");
        List<EduVideoCopy> eduVideoList = baseMapper.selectList(wrapperVideo);
        //2根据课程id查出所有文件url
        QueryWrapper<EduVideoCopy> wrapperVideotwo = new QueryWrapper<>();
        wrapperVideotwo.eq("course_id", courseId);
        wrapperVideotwo.select("word_one_url");
        List<EduVideoCopy> eduVideoCopyList=baseMapper.selectList(wrapperVideotwo);

        List<String> videoSourceIdList = new ArrayList<>();
        for (int i = 0; i < eduVideoList.size(); i++) {
            EduVideoCopy video = eduVideoList.get(i);
            String videoSourceId = video.getVideoSourceId();
            if (!StringUtils.isEmpty(videoSourceId)) {
                //放到list集合中
                videoSourceIdList.add(videoSourceId);
            }
        }
        //调用vod服务删除远程视频
        if(videoSourceIdList.size() > 0){
            vodClient.deleteBatch(videoSourceIdList);
        }

        List<String> videoWJList = new ArrayList<>();
        for (int i = 0; i < eduVideoCopyList.size(); i++) {
            EduVideoCopy video = eduVideoCopyList.get(i);
            String wordOneUrl = video.getWordOneUrl();
            if (!StringUtils.isEmpty(wordOneUrl)) {
                int index = wordOneUrl.indexOf("/");
                //根据第一个点的位置 获得第二个/的位置
                index = wordOneUrl.indexOf("/", index + 1);
                //根据第二点的位置 获得第三个/的位置
                index = wordOneUrl.indexOf("/", index + 1);
                //根据第三个、的位置，截取字符串。得到结果 result
                String result = wordOneUrl.substring(index + 1);
                //放到list集合中
                videoWJList.add(result);
            }
        }
        //调用Oss服务删除远程文件
        if(videoWJList.size() > 0){
            ossClient.delete(videoWJList);
        }

        QueryWrapper<EduVideoCopy> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        baseMapper.delete(wrapper);
    }


}
