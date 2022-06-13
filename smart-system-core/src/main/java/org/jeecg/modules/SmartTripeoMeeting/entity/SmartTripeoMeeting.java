package org.jeecg.modules.SmartTripeoMeeting.entity;

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
 * @Description: 三员+会议开展
 * @Author: jeecg-boot
 * @Date:   2022-03-05
 * @Version: V1.0
 */
@Data
@TableName("smart_tripeo_meeting")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="smart_tripeo_meeting对象", description="三员+会议开展")
public class SmartTripeoMeeting implements Serializable {
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
	/**针对任务*/
	@Excel(name = "针对任务", width = 15,dictTable="smart_tripeo_task",dicCode="id",dicText="task_name")
    @Dict(dictTable="smart_tripeo_task",dicCode="id",dicText="task_name")
    @ApiModelProperty(value = "针对任务")
    private java.lang.String taskId;
	/**会议名称*/
	@Excel(name = "会议名称", width = 15)
    @ApiModelProperty(value = "会议名称")
    private java.lang.String meetingName;
	/**会议时间*/
	@Excel(name = "会议时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "会议时间")
    private java.util.Date meetingTime;
	/**附件*/
    @ApiModelProperty(value = "附件")
    private java.lang.String files;
    /**删除状态（0，未删除；1，删除）*/
    @TableLogic
    private java.lang.Integer delFlag;
}
