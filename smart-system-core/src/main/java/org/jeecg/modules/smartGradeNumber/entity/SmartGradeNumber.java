package org.jeecg.modules.smartGradeNumber.entity;

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
 * @Description: 成绩分布人数表
 * @Author: jeecg-boot
 * @Date:   2021-11-24
 * @Version: V1.0
 */
@Data
@TableName("smart_grade_number")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="smart_grade_number对象", description="成绩分布人数表")
public class SmartGradeNumber implements Serializable {
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
	/**优秀人数*/
	@Excel(name = "优秀人数", width = 15)
    @ApiModelProperty(value = "优秀人数")
    private java.lang.Integer excellentNumber;
	/**良好人数*/
	@Excel(name = "良好人数", width = 15)
    @ApiModelProperty(value = "良好人数")
    private java.lang.Integer goodNumber;
	/**及格人数*/
	@Excel(name = "及格人数", width = 15)
    @ApiModelProperty(value = "及格人数")
    private java.lang.Integer passNumber;
	/**不及格人数*/
	@Excel(name = "不及格人数", width = 15)
    @ApiModelProperty(value = "不及格人数")
    private java.lang.Integer failNumber;
}
