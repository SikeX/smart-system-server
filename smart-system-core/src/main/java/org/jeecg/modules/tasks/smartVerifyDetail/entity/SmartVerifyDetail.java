package org.jeecg.modules.tasks.smartVerifyDetail.entity;

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
 * @Description: 审核详情表
 * @Author: jeecg-boot
 * @Date:   2021-11-06
 * @Version: V1.0
 */
@Data
@TableName("smart_verify_detail")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="smart_verify_detail对象", description="审核详情表")
public class SmartVerifyDetail implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**审核人*/
    @ApiModelProperty(value = "审核人")
    private java.lang.String auditPerson;
	/**审核时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "审核时间")
    private java.util.Date auditTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String auditDepart;
	/**审批编号*/
	@Excel(name = "审批编号", width = 15)
    @ApiModelProperty(value = "审批编号")
    private java.lang.String flowNo;
	/**审核状态*/
	@Excel(name = "审核状态", width = 15)
    @ApiModelProperty(value = "审核状态")
    private Integer auditStatus;
	/**审核备注*/
	@Excel(name = "审核备注", width = 15)
    @ApiModelProperty(value = "审核备注")
    private java.lang.String remark;
}
