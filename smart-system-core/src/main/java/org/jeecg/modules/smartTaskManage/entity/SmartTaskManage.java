package org.jeecg.modules.smartTaskManage.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
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
 * @Description: 定时任务管理
 * @Author: jeecg-boot
 * @Date:   2021-11-09
 * @Version: V1.0
 */
@Data
@TableName("smart_task_manage")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="smart_task_manage对象", description="定时任务管理")
public class SmartTaskManage implements Serializable {
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
	/**任务名*/
	@Excel(name = "任务名", width = 15)
    @ApiModelProperty(value = "任务名")
    private java.lang.String taskName;
	/**任务描述*/
	@Excel(name = "任务描述", width = 15)
    @ApiModelProperty(value = "任务描述")
    private java.lang.String taskDesc;
	/**使用中的模板名*/
	@Excel(name = "使用中的模板名", width = 15)
    @ApiModelProperty(value = "使用中的模板名")
    private java.lang.String templateName;
	/**使用中的模板编码*/
	@Excel(name = "使用中的模板编码", width = 15)
    @ApiModelProperty(value = "使用中的模板编码")
    private java.lang.String templateCode;
	/**发送方式*/
	@Excel(name = "发送方式", width = 15, dicCode = "msgType")
	@Dict(dicCode = "msgType")
    @ApiModelProperty(value = "发送方式")
    private java.lang.String sendType;
	/**任务状态*/
	@Excel(name = "任务状态", width = 15)
    @ApiModelProperty(value = "任务状态")
    private java.lang.String status;
	/**模板内容*/
	@Excel(name = "模板内容", width = 15)
    @ApiModelProperty(value = "模板内容")
    private java.lang.String templateContent;
}
