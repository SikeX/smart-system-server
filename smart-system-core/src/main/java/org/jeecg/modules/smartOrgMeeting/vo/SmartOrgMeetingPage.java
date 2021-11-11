package org.jeecg.modules.smartOrgMeeting.vo;

import java.util.List;
import org.jeecg.modules.smartOrgMeeting.entity.SmartOrgMeeting;
import org.jeecg.modules.smartOrgMeeting.entity.SmartOrgMeetingPacpa;
import org.jeecg.modules.smartOrgMeeting.entity.SmartOrgMeetingAnnex;
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
 * @Description: 组织生活会
 * @Author: jeecg-boot
 * @Date:   2021-11-11
 * @Version: V1.0
 */
@Data
@ApiModel(value="smart_org_meetingPage对象", description="组织生活会")
public class SmartOrgMeetingPage {

	/**主键*/
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
    private java.util.Date reportTime;
	/**主持人工号*/
	@Excel(name = "主持人工号", width = 15)
	@ApiModelProperty(value = "主持人工号")
    private java.lang.String hostId;
	/**会议记录人工号*/
	@Excel(name = "会议记录人工号", width = 15)
	@ApiModelProperty(value = "会议记录人工号")
    private java.lang.String recorderId;
	/**会议内容摘要*/
	@Excel(name = "会议内容摘要", width = 15)
	@ApiModelProperty(value = "会议内容摘要")
    private java.lang.String summary;
	/**会议记录*/
	@Excel(name = "会议记录", width = 15)
	@ApiModelProperty(value = "会议记录")
    private java.lang.String record;
	/**创建人工号（未用）*/
	@Excel(name = "创建人工号（未用）", width = 15)
	@ApiModelProperty(value = "创建人工号（未用）")
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

	@ExcelCollection(name="组织生活会参会人员表")
	@ApiModelProperty(value = "组织生活会参会人员表")
	private List<SmartOrgMeetingPacpa> smartOrgMeetingPacpaList;
	@ExcelCollection(name="组织生活会附件表")
	@ApiModelProperty(value = "组织生活会附件表")
	private List<SmartOrgMeetingAnnex> smartOrgMeetingAnnexList;

}
