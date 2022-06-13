package org.jeecg.modules.interaction.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName smart_village_comment
 */
@TableName(value ="smart_village_comment")
@Data
public class SmartVillageComment implements Serializable {
    /**
     * 
     */
    @TableId
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
     * 回复内容
     */
    private String content;

    /**
     * 问题ID 
     */
    private String topicId;

    /**
     * 回复人ID
     */
    private String userId;

    /**
     * 回复时间
     */
    private Date inTime;

    private String status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}