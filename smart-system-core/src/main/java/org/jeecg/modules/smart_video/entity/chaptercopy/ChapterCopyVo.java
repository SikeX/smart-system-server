package org.jeecg.modules.smart_video.entity.chaptercopy;


import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@ApiModel(value = "课程章节信息")
@Data
public class ChapterCopyVo {
    private String id;

    private String title;

    //表示小节
    private List<VideoCopyVo> children = new ArrayList<>();
}
