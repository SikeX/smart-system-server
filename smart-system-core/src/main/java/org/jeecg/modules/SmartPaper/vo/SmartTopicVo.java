package org.jeecg.modules.SmartPaper.vo;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 试题表
 * @Author: jeecg-boot
 * @Date:   2021-11-21
 * @Version: V1.0
 */
@Data
@AllArgsConstructor //全参构造函数
@NoArgsConstructor  //无参构造函数
@ApiModel(value="smart_question对象", description="试题表")
public class SmartTopicVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键*/
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
    /**试题题型*/
    @Excel(name = "试题题型", width = 15, dicCode = "question_type")
    @Dict(dicCode = "question_type")
    @ApiModelProperty(value = "试题题型")
    private String topicType;
    /**试题题干*/
    @Excel(name = "试题题干", width = 15)
    @ApiModelProperty(value = "试题题干")
    private String question;
    /**选项*/
    @Excel(name = "选项", width = 15)
    @ApiModelProperty(value = "选项")
    private String choice;
    /**分数*/
    @Excel(name = "分数", width = 15)
    @ApiModelProperty(value = "分数")
    private Integer score;
    /**正确答案*/
    @Excel(name = "正确答案", width = 15)
    @ApiModelProperty(value = "正确答案")
    private String correctAnswer;
    /**删除状态*/
    @Excel(name = "删除状态", width = 15)
    @TableLogic
    private Integer delFlag;
}
