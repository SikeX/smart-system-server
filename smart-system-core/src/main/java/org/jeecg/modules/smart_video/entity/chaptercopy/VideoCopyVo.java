package org.jeecg.modules.smart_video.entity.chaptercopy;


import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "小节信息")
@Data
public class VideoCopyVo {
    private String id;
    private String title;
    private String videoSourceId;
    private String wordOneUrl;
    private String wordOneName;
    private String videoOriginalName;
}
