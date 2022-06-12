package org.jeecg.modules.smartAssessmentMission.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.*;
import org.jeecg.common.aspect.annotation.Dict;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 考核任务被考核单位
 * @Author: jeecg-boot
 * @Date:   2022-02-12
 * @Version: V1.0
 */
@Data
@TableName("smart_assessment_depart")
@ApiModel(value="smart_assessment_depart对象", description="考核任务被考核单位")
public class SmartAssessmentDepart implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
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
	/**考核任务ID*/
    @ApiModelProperty(value = "考核任务ID")
    private java.lang.String missionId;
	/**被考核单位*/
	@Excel(name = "被考核单位", width = 15, dicCode = "id",dicText = "depart_name",dictTable = "sys_depart")
    @Dict(dicCode = "id",dicText = "depart_name",dictTable = "sys_depart")
    @ApiModelProperty(value = "被考核单位")
    private java.lang.String assessmentDepart;
	/**被考核单位登录账号*/
	@Excel(name = "被考核单位登录账号", width = 15, dicCode = "id",dicText = "realname",dictTable = "sys_user")
    @Dict(dicCode = "id",dicText = "realname",dictTable = "sys_user")
    @ApiModelProperty(value = "被考核单位登录账号")
    private java.lang.String departUser;
	/**截止时间*/
	@Excel(name = "截止时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "截止时间")
    private java.util.Date deadline;
	/**签收状态*/
	@Excel(name = "签收状态", width = 15)
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    @ApiModelProperty(value = "签收状态")
    private java.lang.String signStatus;
	/**签收时间*/
	@Excel(name = "签收时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "签收时间")
    private java.util.Date signTime;
	/**签收人*/
	@Excel(name = "签收人", width = 15, dicCode = "id",dicText = "realname",dictTable = "sys_user")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    @Dict(dicCode = "id",dicText = "realname",dictTable = "sys_user")
    @ApiModelProperty(value = "签收人")
    private java.lang.String signUser;
}
