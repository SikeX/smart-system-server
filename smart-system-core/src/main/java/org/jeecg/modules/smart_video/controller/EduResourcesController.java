package org.jeecg.modules.smart_video.controller;


import org.jeecg.modules.smart_video.commonutils.R;
import org.jeecg.modules.smart_video.exceptionhandler.GuliException;
import org.jeecg.modules.smart_video.client.OssClient;
import org.jeecg.modules.smart_video.entity.EduResources;
import org.jeecg.modules.smart_video.service.EduResourcesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-10-16
 */
@RestController
@RequestMapping("/serviceedu/edu-resources")
@CrossOrigin
@Api(description="教学资源")
public class EduResourcesController {

    @Autowired
    private EduResourcesService resourcesService;
    @Autowired
    private OssClient ossClient;

    @ApiOperation(value = "查询所有教学资源")
    @GetMapping("findAll")
    public R list() {
        List<EduResources> list = resourcesService.list(null);
        return R.ok().data("items", list);
    }

    @ApiOperation(value = "添加教学资源")
    @PostMapping("addResources")
    public R addResources(@RequestBody EduResources eduResources) {
        boolean save = resourcesService.save(eduResources);
        if (save) {
            return R.ok();
        } else {
            return R.error();
        }
    }
    //根据id查询
    @ApiOperation(value = "根据ID查询")
    @GetMapping("getResourceInfo/{Id}")
    public R getChapterInfo(@PathVariable String Id){
        EduResources eduresources = resourcesService.getById(Id);
        return R.ok().data("resources", eduresources);
    }
    //删除操作，同时删除对应的阿里云文件
    @DeleteMapping("{id}")
    public R deleteVideo(@PathVariable String id){
        //根据id获取文件url
        EduResources eduresources = resourcesService.getById(id);
        String wordOneUrl=eduresources.getWordOneUrl();
        //判断是否有有文件url
        if(!StringUtils.isEmpty(wordOneUrl)){
            //如果有url要进行编码！！！
            try {
                wordOneUrl = URLEncoder.encode( wordOneUrl, "UTF-8" );
                wordOneUrl = URLEncoder.encode( wordOneUrl, "UTF-8" );
                //System.out.println(wordOneUrl);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                throw new GuliException(20001,"，删除文件url时出错!");
            }
            //根据文件url，远程实现文件删除
            R resultt = ossClient.removeAlyWj(wordOneUrl);
            if(resultt.getCode()==20001){
                throw new GuliException(20001,"删除文件失败，请检查是否启动Oss服务，熔断器....");
            }
        }
        //删除小节
        resourcesService.removeById(id);
        return R.ok();
    }

}

