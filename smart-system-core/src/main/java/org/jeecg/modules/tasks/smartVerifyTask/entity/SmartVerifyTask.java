package org.jeecg.modules.tasks.smartVerifyTask.entity;

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
 * @Description: 审核任务表
 * @Author: jeecg-boot
 * @Date:   2021-11-04
 * @Version: V1.0
 */
@Data
@TableName("smart_verify_task")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="smart_verify_task对象", description="审核任务表")
public class SmartVerifyTask implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String flowNo;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
	/**任务类型*/
	@Excel(name = "任务类型", width = 15, dictTable = "smart_verify_type", dicText = "id,type_name", dicCode = "0")
	@Dict(dictTable = "smart_verify_type", dicText = "id,type_name", dicCode = "0")
    @ApiModelProperty(value = "任务类型")
    private java.lang.String taskType;
	/**记录ID*/
	@Excel(name = "记录ID", width = 15)
    @ApiModelProperty(value = "记录ID")
    private java.lang.String recordId;
	/**填报单位*/
	@Excel(name = "填报单位", width = 15)
    @ApiModelProperty(value = "填报单位")
    private java.lang.String fillDepart;
	/**填报人*/
	@Excel(name = "填报人", width = 15)
    @ApiModelProperty(value = "填报人")
    private java.lang.String fillPerson;
	/**审核状态*/
	@Excel(name = "审核状态", width = 15)
    @ApiModelProperty(value = "审核状态")
    private java.lang.String flowStatus;
}
