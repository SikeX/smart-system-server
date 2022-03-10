package org.jeecg.modules.SmartTripeoQuestion.entity;

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
 * @Description: 三员+问题清单
 * @Author: jeecg-boot
 * @Date:   2022-03-04
 * @Version: V1.0
 */
@Data
@TableName("smart_tripeo_question")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="smart_tripeo_question对象", description="三员+问题清单")
public class SmartTripeoQuestion implements Serializable {
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
	/**走访人ID*/
	@Excel(name = "走访人", width = 15,dictTable = "sys_user",dicCode = "id",dicText = "realname")
    @Dict(dictTable = "sys_user",dicCode = "id",dicText = "realname")
    @ApiModelProperty(value = "走访人")
    private java.lang.String visiterId;
	/**被走访人ID*/
	@Excel(name = "被走访人", width = 15,dictTable = "sys_user",dicCode = "id",dicText = "realname")
    @Dict(dictTable = "sys_user",dicCode = "id",dicText = "realname")
    @ApiModelProperty(value = "被走访人")
    private java.lang.String intervieweeId;
	/**地点*/
	@Excel(name = "地点", width = 15)
    @ApiModelProperty(value = "地点")
    private java.lang.String place;
	/**问题描述*/
	@Excel(name = "主要问题", width = 15)
    @ApiModelProperty(value = "主要问题")
    private java.lang.String question;
	/**问卷ID*/
	@Excel(name = "问卷", width = 15,dictTable = "smart_paper",dicCode = "id",dicText = "paper_name")
    @Dict(dictTable = "smart_paper",dicCode = "id",dicText = "paper_name")
    @ApiModelProperty(value = "问卷")
    private java.lang.String surveyId;
    /**删除状态（0，未删除；1，删除）*/
    @TableLogic
    private java.lang.Integer delFlag;
}
