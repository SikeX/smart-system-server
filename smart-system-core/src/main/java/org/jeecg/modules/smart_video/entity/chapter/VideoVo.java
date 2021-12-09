package org.jeecg.modules.smart_video.entity.chapter;


import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "小节信息")
@Data
public class VideoVo {

    private String id;
    private String title;
    private String videoSourceId;
    private String wordOneUrl;
}
