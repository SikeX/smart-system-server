package org.jeecg.modules.wePower.smartPublicityFinance.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 财务公开
 * @Author: jeecg-boot
 * @Date:   2022-02-15
 * @Version: V1.0
 */
@Data
@TableName("smart_publicity_finance")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="smart_publicity_finance对象", description="财务公开")
public class SmartPublicityFinance implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**财务信息*/
	@Excel(name = "财务信息", width = 15)
    @ApiModelProperty(value = "财务信息")
    private java.lang.String financeInfo;
	/**重大事项*/
	@Excel(name = "重大事项", width = 15)
    @ApiModelProperty(value = "重大事项")
    private java.lang.String bigEvent;
	/**财物材料附件*/
	@Excel(name = "财物材料附件", width = 15)
    @ApiModelProperty(value = "财物材料附件")
    private java.lang.String materialFile;
	/**理事长、监事长、会计确认表*/
	@Excel(name = "理事长、监事长、会计确认表", width = 15)
    @ApiModelProperty(value = "理事长、监事长、会计确认表")
    private java.lang.String confirmFile;
	/**重大事项表*/
	@Excel(name = "重大事项表", width = 15)
    @ApiModelProperty(value = "重大事项表")
    private java.lang.String bigEventFile;
	/**删除标志*/
	@Excel(name = "删除标志", width = 15)
    @ApiModelProperty(value = "删除标志")
    private java.lang.Integer delFlg;
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
}
