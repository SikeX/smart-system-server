package org.jeecg.modules.interaction.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName smart_village_topic
 */
@Data
public class SmartVillageTopic implements Serializable {
    /**
     * 
     */
    private String id;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建日期
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新日期
     */
    private Date updateTime;

    /**
     * 所属部门
     */
    private String sysOrgCode;

    /**
     * 问题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 浏览次数
     */
    private String view;

    /**
     * 讨论数
     */
    private String commentCount;

    /**
     * 作者
     */
    private String userId;

    /**
     * 发布时间
     */
    private Date inTime;

    /**
     * 状态0:待审核1已审核
     */
    private Integer status;

    private static final long serialVersionUID = 1L;

}