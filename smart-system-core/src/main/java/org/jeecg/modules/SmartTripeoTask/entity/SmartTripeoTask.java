package org.jeecg.modules.SmartTripeoTask.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 三员+任务清单
 * @Author: jeecg-boot
 * @Date:   2022-03-04
 * @Version: V1.0
 */
@Data
@TableName("smart_tripeo_task")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="smart_tripeo_task对象", description="三员+任务清单")
public class SmartTripeoTask implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
    @Excel(name = "创建日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
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
	/**问题ID*/
	@Excel(name = "针对问题", width = 15, dictTable = "smart_tripeo_question", dicText = "question", dicCode = "id")
	@Dict(dictTable = "smart_tripeo_question", dicText = "question", dicCode = "id")
    @ApiModelProperty(value = "针对问题")
    private java.lang.String questionId;
	/**任务名称*/
	@Excel(name = "任务名称", width = 15)
    @ApiModelProperty(value = "任务名称")
    private java.lang.String taskName;
	/**类别*/
	@Excel(name = "类别", width = 15,dicCode="tripeo_task_type")
    @Dict(dicCode="tripeo_task_type")
    @ApiModelProperty(value = "类别")
    private java.lang.String taskType;
	/**牵头领导*/
	@Excel(name = "牵头领导", width = 15,dicCode = "id",dictTable="sys_user",dicText="realname")
    @Dict(dicCode = "id",dictTable="sys_user",dicText="realname")
    @ApiModelProperty(value = "牵头领导")
    private java.lang.String leader;
	/**牵头部门*/
	@Excel(name = "牵头部门", width = 15,dicCode = "id",dictTable="sys_depart",dicText="depart_name")
    @Dict(dicCode = "id",dictTable="sys_depart",dicText="depart_name")
    @ApiModelProperty(value = "牵头部门")
    private java.lang.String leadDept;
	/**责任部门*/
	@Excel(name = "责任部门", width = 15,dicCode = "id",dictTable="sys_depart",dicText="depart_name")
    @Dict(dicCode = "id",dictTable="sys_depart",dicText="depart_name")
    @ApiModelProperty(value = "责任部门")
    private java.lang.String dutyDept;
	/**具体任务*/
	@Excel(name = "具体任务", width = 15)
    @ApiModelProperty(value = "具体任务")
    private java.lang.String taskDesc;
	/**完成时限*/
	@Excel(name = "完成时限", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "完成时限")
    private java.util.Date endTime;
	/**是否完成*/
	@Excel(name = "是否完成", width = 15,dicCode="yn")
    @ApiModelProperty(value = "是否完成")
    @Dict(dicCode="yn")
    private java.lang.Integer isComplete;
    /**删除状态（0，未删除；1，删除）*/
    @TableLogic
    private java.lang.Integer delFlag;
}
