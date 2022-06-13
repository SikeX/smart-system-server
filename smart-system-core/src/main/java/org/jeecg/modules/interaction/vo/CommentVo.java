package org.jeecg.modules.interaction.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description:
 * @Author: sike
 * @Date: 2021/12/1 0:06
 * @Version: V1.0
 */
@Data
public class CommentVo {
    private String id;

    private String title;

    private String content;

    private String createBy;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;

    private String status;
}
