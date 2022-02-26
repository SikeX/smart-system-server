package org.jeecg.modules.smartDemocraticLifeMeeting.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 民主生活会表
 * @Author: jeecg-boot
 * @Date:   2022-02-26
 * @Version: V1.0
 */
@ApiModel(value="smart_democratic_life_meeting对象", description="民主生活会表")
@Data
@TableName("smart_democratic_life_meeting")
public class SmartDemocraticLifeMeeting implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**单位*/
	@Excel(name = "单位", width = 15)
    @ApiModelProperty(value = "单位")
    private java.lang.String departId;
	/**会议名称*/
	@Excel(name = "会议名称", width = 15)
    @ApiModelProperty(value = "会议名称")
    private java.lang.String meetingName;
	/**会议地点*/
	@Excel(name = "会议地点", width = 15)
    @ApiModelProperty(value = "会议地点")
    private java.lang.String address;
	/**会议时间*/
	@Excel(name = "会议时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "会议时间")
    private java.util.Date meetingTime;
	/**上报时间*/
	@Excel(name = "上报时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "上报时间")
    private java.util.Date reportingTime;
	/**主持人ID*/
	@Excel(name = "主持人ID", width = 15)
    @ApiModelProperty(value = "主持人ID")
    private java.lang.String hostId;
	/**主持人姓名*/
	@Excel(name = "主持人姓名", width = 15)
    @ApiModelProperty(value = "主持人姓名")
    private java.lang.String hostName;
	/**会议记录人ID*/
	@Excel(name = "会议记录人ID", width = 15)
    @ApiModelProperty(value = "会议记录人ID")
    private java.lang.String recorderId;
	/**会议记录人姓名*/
	@Excel(name = "会议记录人姓名", width = 15)
    @ApiModelProperty(value = "会议记录人姓名")
    private java.lang.String recorderName;
	/**会议内容摘要*/
	@Excel(name = "会议内容摘要", width = 15)
    @ApiModelProperty(value = "会议内容摘要")
    private java.lang.String summary;
	/**会议记录*/
	@Excel(name = "会议记录", width = 15)
    @ApiModelProperty(value = "会议记录")
    private java.lang.String record;
	/**创建人ID*/
	@Excel(name = "创建人ID", width = 15)
    @ApiModelProperty(value = "创建人ID")
    private java.lang.String creatorId;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**删除状态（0，正常，1已删除）*/
	@Excel(name = "删除状态（0，正常，1已删除）", width = 15)
    @ApiModelProperty(value = "删除状态（0，正常，1已删除）")
    private java.lang.Integer delFlag;
	/**审核状态*/
	@Excel(name = "审核状态", width = 15)
    @ApiModelProperty(value = "审核状态")
    private java.lang.String verifyStatus;
	/**附件说明*/
	@Excel(name = "附件说明", width = 15)
    @ApiModelProperty(value = "附件说明")
    private java.lang.String explanation;
	/**附件文件*/
	@Excel(name = "附件文件", width = 15)
    @ApiModelProperty(value = "附件文件")
    private java.lang.String path;
}
