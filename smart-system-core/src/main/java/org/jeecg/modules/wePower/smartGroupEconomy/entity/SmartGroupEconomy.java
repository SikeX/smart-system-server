package org.jeecg.modules.wePower.smartGroupEconomy.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @Description: 农村集体经济组织
 * @Author: jeecg-boot
 * @Date:   2022-02-15
 * @Version: V1.0
 */
@Data
@TableName("smart_group_economy")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="smart_group_economy对象", description="农村集体经济组织")
public class SmartGroupEconomy implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**农村集体经济组织类型*/
	@Excel(name = "农村集体经济组织类型", width = 15, dicCode = "group_economy_type")
	@Dict(dicCode = "group_economy_type")
    @ApiModelProperty(value = "农村集体经济组织类型")
    private java.lang.String type;
	/**农村集体经济组织名称*/
	@Excel(name = "农村集体经济组织名称", width = 15)
    @ApiModelProperty(value = "农村集体经济组织名称")
    private java.lang.String name;
	/**理事长（法定代表人）*/
	@Excel(name = "理事长（法定代表人）", width = 15)
    @ApiModelProperty(value = "理事长（法定代表人）")
    private java.lang.String councilBoard;
	/**理事会成员*/
	@Excel(name = "理事会成员", width = 15)
    @ApiModelProperty(value = "理事会成员")
    private java.lang.String counilMenber;
	/**任期开始时间*/
	@Excel(name = "任期开始时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "任期开始时间")
    private java.util.Date termStartTime;
	/**任期结束时间*/
	@Excel(name = "任期结束时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "任期结束时间")
    private java.util.Date termEndTime;
	/**经理人*/
	@Excel(name = "经理人", width = 15)
    @ApiModelProperty(value = "经理人")
    private java.lang.String manager;
	/**授权额度*/
	@Excel(name = "授权额度", width = 15)
    @ApiModelProperty(value = "授权额度")
    private java.lang.String credit;
	/**监事长*/
	@Excel(name = "监事长", width = 15)
    @ApiModelProperty(value = "监事长")
    private java.lang.String supervisor;
	/**监事会成员*/
	@Excel(name = "监事会成员", width = 15)
    @ApiModelProperty(value = "监事会成员")
    private java.lang.String supervisorMember;
	/**任期开始时间*/
	@Excel(name = "任期开始时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "任期开始时间")
    private java.util.Date supervisorStartTime;
	/**任期结束时间*/
	@Excel(name = "任期结束时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "任期结束时间")
    private java.util.Date supervisorEndTime;
	/**财务成员代表*/
	@Excel(name = "财务成员代表", width = 15)
    @ApiModelProperty(value = "财务成员代表")
    private java.lang.String financialOfficer;
	/**财务成员*/
	@Excel(name = "财务成员", width = 15)
    @ApiModelProperty(value = "财务成员")
    private java.lang.String financialMember;
	/**账户信息*/
	@Excel(name = "账户信息", width = 15)
    @ApiModelProperty(value = "账户信息")
    private java.lang.String accountInfo;
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
	/**删除标志*/
	@Excel(name = "删除标志", width = 15)
    @ApiModelProperty(value = "删除标志")
    private java.lang.Integer delFlag;
}
