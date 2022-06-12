package org.jeecg.modules.smartAssessmentContent.entity;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 考核节点表
 * @Author: jeecg-boot
 * @Date:   2022-02-12
 * @Version: V1.0
 */
@Data
@TableName("smart_assessment_content")
@ApiModel(value="smart_assessment_content对象", description="考核节点表")
public class SmartAssessmentContent implements Serializable {
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
	/**父级节点*/
	@Excel(name = "父级节点", width = 15, dictTable = "smart_assessment_content", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "父级节点")
    private java.lang.String pid;
	/**是否有子节点*/
	@Excel(name = "是否有子节点", width = 15, dicCode = "yn")
	@Dict(dicCode = "yn")
    @ApiModelProperty(value = "是否有子节点")
    private java.lang.String hasChild;
	/**考核任务*/
	@Excel(name = "考核任务", width = 15, dictTable = "smart_assessment_mission", dicText = "mission_name", dicCode = "id")
    @ApiModelProperty(value = "考核任务")
    private java.lang.String missionId;
	/**名称*/
	@Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
    private java.lang.String name;
	/**分值*/
	@Excel(name = "分值", width = 15)
    @ApiModelProperty(value = "分值")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private java.lang.Integer point;
	/**填报说明*/
	@Excel(name = "填报说明", width = 15)
    @ApiModelProperty(value = "填报说明")
    private java.lang.String instructions;
	/**评分考核单位*/
	@Excel(name = "评分考核单位")
    @ApiModelProperty(value = "评分考核单位")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private java.lang.String assDepart;
	/**考核组*/
	@Excel(name = "考核组", width = 15, dictTable = "smart_assessment_team", dicText = "team_name", dicCode = "id")
	@Dict(dictTable = "smart_assessment_team", dicText = "team_name", dicCode = "id")
    @ApiModelProperty(value = "考核组")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private java.lang.String assTeam;
	/**排序*/
    @ApiModelProperty(value = "排序")
    private java.lang.Double sortNo;
	/**是否考核要点*/
	@Excel(name = "是否考核要点", width = 15, dicCode = "yn")
    @ApiModelProperty(value = "是否考核要点")
    private java.lang.Integer isKey;
}
