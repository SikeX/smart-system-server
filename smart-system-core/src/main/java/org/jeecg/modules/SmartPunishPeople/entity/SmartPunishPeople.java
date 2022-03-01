package org.jeecg.modules.SmartPunishPeople.entity;

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
 * @Description: 处分人员表
 * @Author: jeecg-boot
 * @Date:   2021-11-07
 * @Version: V1.0
 */
@Data
@TableName("smart_punish_people")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="smart_punish_people对象", description="处分人员表")
public class SmartPunishPeople implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**创建人*/
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
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
	/**处分人ID*/
	@Dict(dictTable = "sys_user", dicText = "realname", dicCode = "id")
    @ApiModelProperty(value = "处分人ID")
    private java.lang.String punishId;
	/**处分人姓名*/
	@Excel(name = "处分人姓名", width = 15)
    @ApiModelProperty(value = "处分人姓名")
    private java.lang.String punishName;
	/**单位ID*/
    /*@Excel(name="单位",width = 15,dictTable ="sys_depart",dicText = "depart_name",dicCode = "id")*/
    @ApiModelProperty(value = "单位")
    private java.lang.String departId;
	/**单位*/
    @Excel(name="单位",width = 15,dictTable ="sys_depart",dicText = "depart_name",dicCode = "org_code")
    @Dict(dictTable ="sys_depart",dicText = "depart_name",dicCode = "org_code")
    @ApiModelProperty(value = "单位")
    private java.lang.String departCode;
	/**职务*/
    @Excel(name = "职务", width = 15,dictTable ="sys_position",dicText = "name",dicCode = "code")
    @Dict(dictTable ="sys_position",dicText = "name",dicCode = "code")
    @ApiModelProperty(value = "职务")
    private java.lang.String position;
	/**职级*/
	@Excel(name = "职级", width = 15,dicCode = "position_rank")
    @ApiModelProperty(value = "职级")
    @Dict(dicCode = "position_rank")
    private java.lang.String positionRank;
	/**手机号*/
	@Excel(name = "手机号", width = 15)
    @ApiModelProperty(value = "手机号")
    private java.lang.String phone;
	/**处分类型*/
	@Excel(name = "处分类型", width = 15)
    @ApiModelProperty(value = "处分类型")
    @Dict(dicCode = "punish_type")
    private java.lang.String punishType;
    /**处分开始时间*/
    @Excel(name = "处分开始时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "处分开始时间")
    private java.util.Date beginTime;
	/**解除处分时间*/
	@Excel(name = "解除处分时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "解除处分时间")
    private java.util.Date removeTime;
    /**处分状态*/
    @Dict(dicCode = "punish_statu")
    private java.lang.Integer statu;
    /**删除状态*/
    @TableLogic
    private java.lang.Integer delFlag;
}
