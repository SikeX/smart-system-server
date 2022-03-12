package org.jeecg.modules.smartAnswerAssContent.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.jeecg.common.aspect.annotation.Dict;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 答题评分表
 * @Author: jeecg-boot
 * @Date:   2022-02-21
 * @Version: V1.0
 */
@Data
@TableName("smart_answer_ass_score")
@ApiModel(value="smart_answer_ass_score对象", description="答题评分表")
public class SmartAnswerAssScore implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
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
	/**主表ID*/
    @ApiModelProperty(value = "主表ID")
    private java.lang.String mainId;
	/**评分人*/
	@Excel(name = "评分人", width = 15)
    @ApiModelProperty(value = "评分人")
    private java.lang.String ratingUser;
    /**评分人*/
    @Excel(name = "评分人所属考核组或考核单位ID", width = 15)
    @ApiModelProperty(value = "评分人所属考核组或考核单位ID")
    private java.lang.String roleId;
    /**评分人*/
    @Excel(name = "评分人所属类别", width = 15)
    @ApiModelProperty(value = "评分人所属类别")
    private java.lang.Integer roleType;
	/**评分*/
	@Excel(name = "评分", width = 15)
    @ApiModelProperty(value = "评分")
    private java.lang.Double score;
	/**评语*/
	@Excel(name = "评语", width = 15)
    @ApiModelProperty(value = "评语")
    private java.lang.String comment;
	/**评分时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "评分时间")
    private java.util.Date createTime;
}
