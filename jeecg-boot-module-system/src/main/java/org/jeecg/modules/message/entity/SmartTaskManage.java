package org.jeecg.modules.message.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 任务管理
 * @Author: jeecg-boot
 * @Date:   2021-11-09
 * @Version: V1.0
 */
@Data
@TableName("smart_task_manage")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="smart_task_manage对象", description="任务管理")
public class SmartTaskManage implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
	/**任务名*/
	@Excel(name = "任务名", width = 15)
    @ApiModelProperty(value = "任务名")
    private String taskName;
	/**任务描述*/
	@Excel(name = "任务描述", width = 15)
    @ApiModelProperty(value = "任务描述")
    private String taskDesc;
	/**使用中的模板名*/
	@Excel(name = "使用中的模板名", width = 15)
    @ApiModelProperty(value = "使用中的模板名")
    private String templateName;
	/**使用中的模板编码*/
	@Excel(name = "使用中的模板编码", width = 15)
    @ApiModelProperty(value = "使用中的模板编码")
    private String templateCode;
	/**发送方式*/
	@Excel(name = "发送方式", width = 15)
    @ApiModelProperty(value = "发送方式")
    private String sendType;
	/**任务状态*/
	@Excel(name = "任务状态", width = 15)
    @ApiModelProperty(value = "任务状态")
    private String status;
	/**模板内容*/
	@Excel(name = "模板内容", width = 15)
    @ApiModelProperty(value = "模板内容")
    private String templateContent;

    public String getStatus() {
        return status;
    }

    public String getTemplateContent() {
        return templateContent;
    }

    public String getSendType() {
        return sendType;
    }

    public String getTaskName() {
        return taskName;
    }
}
