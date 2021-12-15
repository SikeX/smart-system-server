package org.jeecg.modules.smartDemocraticLifeMeeting.vo;

import java.io.Serializable;
import java.util.List;
import org.jeecg.modules.smartDemocraticLifeMeeting.entity.SmartDemocraticLifeMeeting;
import org.jeecg.modules.smartDemocraticLifeMeeting.entity.SmartDemocraticLifePeople;
import org.jeecg.modules.smartDemocraticLifeMeeting.entity.SmartDemocraticLifeEnclosure;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 民主生活会表
 * @Author: jeecg-boot
 * @Date:   2021-11-17
 * @Version: V1.0
 */
@Data
@ApiModel(value="smart_democratic_life_meetingPage对象", description="民主生活会表")
public class SmartDemocraticLifeMeetingPage implements Serializable {

	/**主键*/
	@ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**单位*/
	@Excel(name="单位",width = 15,dictTable ="sys_depart",dicText = "depart_name",dicCode = "id")
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

	@ApiModelProperty(value = "删除状态（0，正常，1已删除）")
    private java.lang.Integer delFlag;
	/**审核状态*/
	@Excel(name = "审核状态", width = 15)
	@ApiModelProperty(value = "审核状态")
    private java.lang.String verifyStatus;

	@ExcelCollection(name="民主生活参会人员表")
	@ApiModelProperty(value = "民主生活参会人员表")
	private List<SmartDemocraticLifePeople> smartDemocraticLifePeopleList;
	@ExcelCollection(name="民主生活会附件表")
	@ApiModelProperty(value = "民主生活会附件表")
	private List<SmartDemocraticLifeEnclosure> smartDemocraticLifeEnclosureList;

}
