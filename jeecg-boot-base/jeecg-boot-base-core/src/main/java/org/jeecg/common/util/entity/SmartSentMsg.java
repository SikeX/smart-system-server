package org.jeecg.common.util.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @Description: TODO
 * @author: lord
 * @date: 2021年12月04日 10:26
 */
@Data
@TableName("smart_sent_msg")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="smart_sent_msg对象", description="已发送的短信")
public class SmartSentMsg implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
    /**创建日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
    /**发送人所属部门*/
    @ApiModelProperty(value = "发送人所属部门")
    private java.lang.String sysOrgCode;
    /**发送人*/
    @Excel(name = "发送人", width = 15)
    @ApiModelProperty(value = "发送人")
    private java.lang.String sendFrom;
    /**接收人*/
    @Excel(name = "接收人", width = 15)
    @ApiModelProperty(value = "接收人")
    private java.lang.String receiver;
    /**接收人电话号码*/
    @Excel(name = "接收人电话号码", width = 15)
    @ApiModelProperty(value = "接收人电话号码")
    private java.lang.String receiverPhone;
    /**内容*/
    @Excel(name = "内容", width = 15)
    @ApiModelProperty(value = "内容")
    private java.lang.String content;
    /**发送时间*/
    @Excel(name = "发送时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "发送时间")
    private java.util.Date sendTime;
    /**发送类型*/
    @Excel(name = "发送类型", width = 15)
    @ApiModelProperty(value = "发送类型")
    private java.lang.String sendType;
    /**标题*/
    @Excel(name = "标题", width = 15)
    @ApiModelProperty(value = "标题")
    private java.lang.String tittle;
}
