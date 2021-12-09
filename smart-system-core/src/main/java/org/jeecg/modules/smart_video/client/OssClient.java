package org.jeecg.modules.smart_video.client;


import org.jeecg.modules.smart_video.commonutils.R;

//import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

//@FeignClient(name ="service-oss", fallback = OssFileDegradeFeignClient.class)
@Component
public interface OssClient {

    //根据文件url删除阿里云文件
    //@PathVariable("url")要带参数，否则会出错
    @DeleteMapping("/eduoss/fileoss/removeAlyWj/{url}")
    public R removeAlyWj(@PathVariable("url") String url);


    //删除多个文件
    //参数是多个url
    @DeleteMapping("/eduoss/fileoss/delete-Wj")
    public R delete(@RequestParam("WjList") List<String> WjList);
}


