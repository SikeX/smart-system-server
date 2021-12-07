package org.jeecg.modules.smartEvaluateList.entity;

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
 * @Description: 阳光评廉表
 * @Author: jeecg-boot
 * @Date:   2021-11-09
 * @Version: V1.0
 */
@Data
@TableName("smart_evaluate_window")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="smart_evaluate_window对象", description="阳光评廉表")
public class SmartEvaluateWindow implements Serializable {
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
	/**主管单位*/
	@Excel(name = "主管单位", width = 15)
    @ApiModelProperty(value = "主管单位")
    private java.lang.String exeDept;
	/**政务服务大厅名称*/
	@Excel(name = "政务服务大厅名称", width = 15)
    @ApiModelProperty(value = "政务服务大厅名称")
    private java.lang.String windowsName;
	/**人员名称*/
	@Excel(name = "人员名称", width = 15)
    @ApiModelProperty(value = "人员名称")
    private java.lang.String personName;
	/**评价结果*/
	@Excel(name = "评价结果", width = 15)
    @ApiModelProperty(value = "评价结果")
    @Dict(dicCode = "evaluate_grade")
    private java.lang.String evaluateResult;
	/**评价人*/
	@Excel(name = "评价人", width = 15)
    @ApiModelProperty(value = "评价人")
    private java.lang.String evaluateName;
	/**评价人手机号*/
	@Excel(name = "评价人手机号", width = 15)
    @ApiModelProperty(value = "评价人手机号")
    private java.lang.String evaluatePhone;
	/**评价时间*/
	@Excel(name = "评价时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "评价时间")
    private java.util.Date evaluateTime;
    /**意见*/
    @Excel(name = "意见", width = 30)
    @ApiModelProperty(value = "意见")
    private java.lang.String evaluateOpinion;
	/**删除状态*/
    @TableLogic
    private java.lang.Integer delFlag;
}
