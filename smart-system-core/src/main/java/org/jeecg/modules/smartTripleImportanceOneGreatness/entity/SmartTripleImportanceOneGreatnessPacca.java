package org.jeecg.modules.smartTripleImportanceOneGreatness.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 三重一大参会人员表
 * @Author: jeecg-boot
 * @Date:   2021-11-13
 * @Version: V1.0
 */
@ApiModel(value="smart_triple_importance_one_greatness_pacca对象", description="三重一大参会人员表")
@Data
@TableName("smart_triple_importance_one_greatness_pacca")
public class SmartTripleImportanceOneGreatnessPacca implements Serializable {
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
	/**主表ID*/
    @ApiModelProperty(value = "主表ID")
    private java.lang.String parentId;
    /**参会人员*/
    /*@Excel(name = "参会人员", width = 15)*/
    @Dict(dictTable = "sys_user",dicText = "realname",dicCode = "realname")
    @ApiModelProperty(value = "参会人员")
    private java.lang.String meetingPeople;
    /**参会人员姓名*/
    @Excel(name = "参会人员姓名", width = 15)
    @ApiModelProperty(value = "参会人员姓名")
    private java.lang.String meetingPeopleName;
	/**删除状态*/
	@Excel(name = "删除状态", width = 15)
    @ApiModelProperty(value = "删除状态")
    private java.lang.Integer delFlag;
}
