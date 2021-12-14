package org.jeecg.modules.smart_video.client;

import org.jeecg.modules.smart_video.commonutils.R;
//import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

//@FeignClient(name ="service-vod", fallback = VodFileDegradeFeignClient.class)
@Component
public interface VodClient {

    //根据视频id删除阿里云视频
    //@PathVariable("id")要带参数，否则会出错
    @DeleteMapping("/eduvod/video/removeAlyVideo/{id}")
    public R removeAlyVideo(@PathVariable("id") String id);



    //定义删除多个视频的方法

    //删除多个视频
    //参数是多个id
    @DeleteMapping("/eduvod/video/delete-batch")
    public R deleteBatch(@RequestParam("videoIdList") List<String> videoIdList);
}