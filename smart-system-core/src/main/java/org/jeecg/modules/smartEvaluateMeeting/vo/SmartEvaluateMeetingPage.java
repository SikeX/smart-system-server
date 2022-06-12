package org.jeecg.modules.smartEvaluateMeeting.vo;

import java.io.Serializable;
import java.util.List;
import org.jeecg.modules.smartEvaluateMeeting.entity.SmartEvaluateMeeting;
import org.jeecg.modules.smartEvaluateMeeting.entity.SmartEvaluateMeetingPacpa;
import org.jeecg.modules.smartEvaluateMeeting.entity.SmartEvaluateMeetingAnnex;
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
 * @Description: 述责述廉表
 * @Author: jeecg-boot
 * @Date:   2021-11-11
 * @Version: V1.0
 */
@Data
@ApiModel(value="smart_evaluate_meetingPage对象", description="述责述廉表")
public class SmartEvaluateMeetingPage implements Serializable {

	/**主键*/
	@ApiModelProperty(value = "主键")
    private java.lang.String id;

	@ApiModelProperty(value = "单位ID")
	@Excel(name="单位",width = 15,dictTable ="sys_depart",dicText = "depart_name",dicCode = "id")
    private java.lang.String departId;
	/**会议名称*/
	@Excel(name = "会议名称", width = 15)
	@ApiModelProperty(value = "会议名称")
    private java.lang.String meetingName;
	/**会议地点*/
	@Excel(name = "会议地点", width = 15)
	@ApiModelProperty(value = "会议地点")
    private java.lang.String meetingPlace;
	/**检查时间*/
	@Excel(name = "检查时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "检查时间")
    private java.util.Date checkTime;
	/**对象类型*/
	@Excel(name = "对象类型", width = 15)
	@ApiModelProperty(value = "对象类型")
    private java.lang.String peopleType;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
    private java.lang.String meetingRemarks;
	/**创建人*/
	@ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
	@ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
	@ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
	/**删除状态（0，正常，1已删除）*/
//	@Excel(name = "删除状态（0，正常，1已删除）", width = 15)
	@ApiModelProperty(value = "删除状态（0，正常，1已删除）")
    private java.lang.Integer delFlag;
	/**附件*/
	@Excel(name = "附件", width = 15)
	@ApiModelProperty(value = "附件")
	private java.lang.String files;

	@ExcelCollection(name="述责述廉参与人表")
	@ApiModelProperty(value = "述责述廉参与人表")
	private List<SmartEvaluateMeetingPacpa> smartEvaluateMeetingPacpaList;
	@ExcelCollection(name="述责述廉附件表")
	@ApiModelProperty(value = "述责述廉附件表")
	private List<SmartEvaluateMeetingAnnex> smartEvaluateMeetingAnnexList;

}
