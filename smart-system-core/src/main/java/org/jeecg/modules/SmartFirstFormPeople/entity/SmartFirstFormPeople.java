package org.jeecg.modules.SmartFirstFormPeople.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @Description: 执行第一种形态人员表
 * @Author: jeecg-boot
 * @Date:   2021-11-08
 * @Version: V1.0
 */
@Data
@TableName("smart_first_form_people")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="smart_first_form_people对象", description="执行第一种形态人员表")
public class SmartFirstFormPeople implements Serializable {
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
    @Excel(name = "单位ID", width = 15)
    @ApiModelProperty(value = "单位ID")
    private java.lang.String departId;
	/**案件标题*/
	@Excel(name = "案件标题", width = 15)
    @ApiModelProperty(value = "案件标题")
    private java.lang.String caseName;
	/**案件（线索来源）*/
	@Excel(name = "案件（线索来源）", width = 15)
    @ApiModelProperty(value = "案件（线索来源）")
    private java.lang.String caseSource;
	/**被谈话人工号*/
	@Excel(name = "被谈话人工号", width = 15)
    @ApiModelProperty(value = "被谈话人工号")
    private java.lang.String intervieweeNo;
	/**被谈话人单位*/
	@Excel(name = "被谈话人单位", width = 15)
    @ApiModelProperty(value = "被谈话人单位")
    private java.lang.String intervieweeDept;
	/**被谈话人姓名*/
	@Excel(name = "被谈话人姓名", width = 15)
    @ApiModelProperty(value = "被谈话人姓名")
    private java.lang.String intervieweeName;
	/**被谈话人性别*/
	@Excel(name = "被谈话人性别", width = 15)
    @ApiModelProperty(value = "被谈话人性别")
    private java.lang.String intervieweeSex;
	/**被谈话人民族*/
	@Excel(name = "被谈话人民族", width = 15)
    @ApiModelProperty(value = "被谈话人民族")
    private java.lang.String intervieweeEthnicity;
	/**被谈话人政治面貌*/
	@Excel(name = "被谈话人政治面貌", width = 15)
    @ApiModelProperty(value = "被谈话人政治面貌")
    private java.lang.String intervieweePolsta;
	/**被谈话人入党时间*/
    @Excel(name = "被谈话人入党时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "被谈话人入党时间")
    private java.util.Date intervieweeJpt;
	/**被谈话人职务*/
	@Excel(name = "被谈话人职务", width = 15)
    @ApiModelProperty(value = "被谈话人职务")
    private java.lang.String intervieweePost;
	/**被谈话人职级*/
	@Excel(name = "被谈话人职级", width = 15)
    @ApiModelProperty(value = "被谈话人职级")
    private java.lang.String intervieweePostrank;
	/**是否党政正职*/
	@Excel(name = "是否党政正职", width = 15, dicCode = "yn")
	@Dict(dicCode = "yn")
    @ApiModelProperty(value = "是否党政正职")
    private java.lang.String principal;
	/**是否国家检查队形*/
	@Excel(name = "是否国家检查队形", width = 15, dicCode = "yn")
	@Dict(dicCode = "yn")
    @ApiModelProperty(value = "是否国家检查队形")
    private java.lang.String country;
	/**干部管理权限*/
	@Excel(name = "干部管理权限", width = 15)
    @ApiModelProperty(value = "干部管理权限")
    private java.lang.String authority;
	/**是否是纪检监察干部*/
	@Excel(name = "是否是纪检监察干部", width = 15, dicCode = "yn")
	@Dict(dicCode = "yn")
    @ApiModelProperty(value = "是否是纪检监察干部")
    private java.lang.String supervision;
	/**谈话人工号*/
	@Excel(name = "谈话人工号", width = 15)
    @ApiModelProperty(value = "谈话人工号")
    private java.lang.String talkerNo;
	/**谈话人单位*/
	@Excel(name = "谈话人单位", width = 15)
    @ApiModelProperty(value = "谈话人单位")
    private java.lang.String talkerDept;
	/**谈话人姓名*/
	@Excel(name = "谈话人姓名", width = 15)
    @ApiModelProperty(value = "谈话人姓名")
    private java.lang.String talkerName;
	/**谈话人职务*/
	@Excel(name = "谈话人职务", width = 15)
    @ApiModelProperty(value = "谈话人职务")
    private java.lang.String talkerPost;
	/**谈话人职级*/
	@Excel(name = "谈话人职级", width = 15)
    @ApiModelProperty(value = "谈话人职级")
    private java.lang.String talkerPostrank;
	/**简要案情*/
	@Excel(name = "简要案情", width = 15)
    @ApiModelProperty(value = "简要案情")
    private java.lang.String caseAbs;
	/**办理部门*/
	@Excel(name = "办理部门", width = 15, dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
	@Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
    @ApiModelProperty(value = "办理部门")
    private java.lang.String handlerDepart;
	/**办理状态*/
	@Excel(name = "办理状态", width = 15)
    @ApiModelProperty(value = "办理状态")
    @Dict(dicCode = "processing_type")
    private java.lang.String state;
	/**类型*/
	@Excel(name = "类型", width = 15)
    @ApiModelProperty(value = "类型")
    @Dict(dicCode = "first_form_type")
    private java.lang.String type;
	/**情形*/
	@Excel(name = "情形", width = 15)
    @ApiModelProperty(value = "情形")
    private java.lang.String situation;
	/**谈话时间*/
	@Excel(name = "谈话时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "谈话时间")
    private java.util.Date talkTime;
	/**组织措施*/
	@Excel(name = "组织措施", width = 15)
    @ApiModelProperty(value = "组织措施")
    private java.lang.String measures;
	/**采取组织措施决定机关*/
	@Excel(name = "采取组织措施决定机关", width = 15)
    @ApiModelProperty(value = "采取组织措施决定机关")
    private java.lang.String decisionOrgan;
	/**附件*/
	@Excel(name = "附件", width = 15)
    @ApiModelProperty(value = "附件")
    private java.lang.String annex;
    /**删除状态*/
    @Excel(name = "删除状态", width = 15,dicCode="del_flag")
    @TableLogic
    private java.lang.Integer delFlag;
}
