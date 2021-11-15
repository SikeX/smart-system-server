package org.jeecg.modules.SmartInnerPartyTalk.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @Description: 党内谈话表
 * @Author: jeecg-boot
 * @Date:   2021-11-10
 * @Version: V1.0
 */
@ApiModel(value="smart_inner_party_talk对象", description="党内谈话表")
@Data
@TableName("smart_inner_party_talk")
public class SmartInnerPartyTalk implements Serializable {
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
	/**单位ID*/
    @ApiModelProperty(value = "单位ID")
    private java.lang.String departId;
	/**会议时间*/
	@Excel(name = "会议时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "会议时间")
    private java.util.Date meetTime;
	/**会议地点*/
	@Excel(name = "会议地点", width = 15)
    @ApiModelProperty(value = "会议地点")
    private java.lang.String meetLocation;
	/**会议名称*/
	@Excel(name = "会议名称", width = 15)
    @ApiModelProperty(value = "会议名称")
    private java.lang.String meetName;
	/**主持人ID*/
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "id")
    @ApiModelProperty(value = "主持人ID")
    private java.lang.String hostId;
    /**主持人姓名*/
    @Excel(name = "主持人姓名", width = 15)
    @ApiModelProperty(value = "主持人姓名")
    private java.lang.String hostName;
	/**受约谈函询人ID*/
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "id")
    @ApiModelProperty(value = "受约谈函询人ID")
    private java.lang.String talkedId;
    /**受约谈函询人姓名*/
    @Excel(name = "受约谈函询人姓名", width = 15)
    @ApiModelProperty(value = "受约谈函询人姓名")
    private java.lang.String talkedName;
	/**受诫勉谈话人ID*/
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "id")
    @ApiModelProperty(value = "受诫勉谈话人ID")
    private java.lang.String inquirerId;
    /**受诫勉谈话人姓名*/
    @Excel(name = "受诫勉谈话人ID", width = 15)
    @ApiModelProperty(value = "受诫勉谈话人姓名")
    private java.lang.String inquirerName;
	/**受党纪处分人ID*/
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "id")
    @ApiModelProperty(value = "受党纪处分人ID")
    private java.lang.String punisherId;
    /**受党纪处分人工姓名*/
    @Excel(name = "受党纪处分人工姓名", width = 15)
    @ApiModelProperty(value = "受党纪处分人工姓名")
    private java.lang.String punisherName;
	/**会议摘要*/
	@Excel(name = "会议摘要", width = 15)
    @ApiModelProperty(value = "会议摘要")
    private java.lang.String abs;
	/**记录人ID*/
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "id")
    @ApiModelProperty(value = "记录人ID")
    private java.lang.String recorderId;
    /**记录人姓名*/
    @Excel(name = "记录人姓名", width = 15)
    @ApiModelProperty(value = "记录人姓名")
    private java.lang.String recorderName;
	/**删除状态（0，未删除；1，删除）*/
    @Excel(name = "删除状态", width = 15,dicCode="del_flag")
    @TableLogic
    private java.lang.Integer delFlag;
}
