package org.jeecg.modules.smart_video.controller;


import org.jeecg.modules.smart_video.commonutils.R;
import org.jeecg.modules.smart_video.exceptionhandler.GuliException;
import org.jeecg.modules.smart_video.client.OssClient;
import org.jeecg.modules.smart_video.client.VodClient;
import org.jeecg.modules.smart_video.entity.EduVideoCopy;

import org.jeecg.modules.smart_video.service.EduVideoCopyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-09-11
 */
@RestController
@RequestMapping("/serviceedu/edu-video-copy")
@CrossOrigin
@Api(description="课程小节")
public class EduVideoCopyController {
    @Autowired
    private EduVideoCopyService eduVideoCopyService;
    @Autowired
    private VodClient vodClient;

    @Autowired
    private OssClient ossClient;

    //添加小节
    @PostMapping("addVideo")
    public R addVideo(@RequestBody EduVideoCopy eduVideo){

        eduVideoCopyService.save(eduVideo);
        return R.ok();
    }


    //根据id查询
    @ApiOperation(value = "根据ID查询")
    @GetMapping("getVideoInfo/{videoId}")
    public R getChapterInfo(@PathVariable String videoId){
        EduVideoCopy eduVideo  = eduVideoCopyService.getById(videoId);
        return R.ok().data("video", eduVideo);
    }
    //删除小节,删除小节里面对应的阿里云视频和阿里云文件
    @DeleteMapping("{id}")
    public R deleteVideo(@PathVariable String id){
        //根据小节id获取视频id，调用方法实现视频删除
        EduVideoCopy eduVideo = eduVideoCopyService.getById(id);
        String videoSourceId = eduVideo.getVideoSourceId();
        String wordOneUrl=eduVideo.getWordOneUrl();
        //判断是否有有视频id
        if(!StringUtils.isEmpty(videoSourceId)){
            //根据视频id，远程实现视频删除
            R result = vodClient.removeAlyVideo(videoSourceId);
            if(result.getCode()==20001){
                throw new GuliException(20001,"删除视频失败，请检查是否启动Vod服务，熔断器....");
            }
        }
        //判断是否有有文件url
        if(!StringUtils.isEmpty(wordOneUrl)){
            //如果有url要进行编码！！！
            try {
                wordOneUrl = URLEncoder.encode( wordOneUrl, "UTF-8" );
                wordOneUrl = URLEncoder.encode( wordOneUrl, "UTF-8" );
                //System.out.println(wordOneUrl);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
               throw new GuliException(20001,"删除课程小节，删除文件url时出错，请检查eduvideocopycontroller中删除小节方法");
            }
            //根据文件url，远程实现文件删除
            R resultt = ossClient.removeAlyWj(wordOneUrl);
            if(resultt.getCode()==20001){
                throw new GuliException(20001,"删除文件失败，请检查是否启动Oss服务，熔断器....");
            }
        }

        //删除小节
        eduVideoCopyService.removeById(id);
        return R.ok();
    }

    //修改
    @ApiOperation(value = "章节")
    @PostMapping("updateVideo")
    public R updateChapter(@RequestBody EduVideoCopy eduVideo){
        eduVideoCopyService.updateById(eduVideo);
        return R.ok();
    }

}

