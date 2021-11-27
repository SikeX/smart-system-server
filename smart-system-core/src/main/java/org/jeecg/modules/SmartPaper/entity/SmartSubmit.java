package org.jeecg.modules.SmartPaper.entity;

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
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 试卷提交表
 * @Author: jeecg-boot
 * @Date:   2021-11-25
 * @Version: V1.0
 */
@Data
@TableName("smart_submit")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="smart_submit对象", description="试卷提交表")
public class SmartSubmit implements Serializable {
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
	/**试卷ID*/
	@Excel(name = "试卷ID", width = 15)
    @ApiModelProperty(value = "试卷ID")
    private String paperId;
	/**试题ID*/
	@Excel(name = "试题ID", width = 15)
    @ApiModelProperty(value = "试题ID")
    private String questionId;
	/**人员ID*/
	@Excel(name = "人员ID", width = 15)
    @ApiModelProperty(value = "人员ID")
    private String userId;
	/**提交答案*/
	@Excel(name = "提交答案", width = 15)
    @ApiModelProperty(value = "提交答案")
    private String submitAnswer;
	/**题型*/
	@Excel(name = "题型", width = 15)
    @ApiModelProperty(value = "题型")
    private String type;
	/**删除状态*/
	@Excel(name = "删除状态", width = 15)
    @TableLogic
    private Integer delFlag;
}
