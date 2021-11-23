package org.jeecg.modules.system.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description:
 * @Author: sike
 * @Date: 2021/11/17 17:13
 * @Version: V1.0
 */
public class TaskManageModel {
    private java.lang.String id;
    /**通告id*/
    private java.lang.String anntId;
    /**用户id*/
    private java.lang.String userId;

    private String userName;

    private String userDepartName;

    private Integer isDelay;
    /**阅读状态（0未读，1已读）*/
    private java.lang.String readFlag;
    /**阅读时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date readTime;
    /**创建人*/
    private java.lang.String createBy;
    /**创建时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;
    /**更新人*/
    private java.lang.String updateBy;
    /**更新时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date updateTime;
}
