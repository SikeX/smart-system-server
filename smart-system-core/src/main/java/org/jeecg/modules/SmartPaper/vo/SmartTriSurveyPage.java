package org.jeecg.modules.SmartPaper.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Description: 试卷表
 * @Author: jeecg-boot
 * @Date:   2021-11-21
 * @Version: V1.0
 */
@Data
@ApiModel(value="smart_paperPage对象", description="试卷表")
public class SmartTriSurveyPage {
    /**主键*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
    /**试卷类型*/
    @Excel(name = "试卷类型", width = 15, dicCode = "paper_type")
    @Dict(dicCode = "paper_type")
    @ApiModelProperty(value = "试卷类型")
    private String paperType;
    /**试卷名称*/
    @Excel(name = "试卷名称", width = 15)
    @ApiModelProperty(value = "试卷名称")
    private String paperName;
    /**试卷状态*/
    @Excel(name = "试卷状态", width = 15, dicCode = "paper_status")
    @Dict(dicCode = "paper_status")
    @ApiModelProperty(value = "试卷状态")
    private String paperStatus;
    /**命卷人*/
    @ApiModelProperty(value = "命卷人")
    private String createBy;
    @ApiModelProperty(value = "命卷人姓名")
    private java.lang.String creatorName;
    /**命卷日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "命卷日期")
    private Date createTime;
    /**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
    /**更新日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
    /**题目数量*/
    @Excel(name = "题目数量", width = 15)
    @ApiModelProperty(value = "题目数量")
    private String topicNum;
    /**总分*/
    @Excel(name = "总分", width = 15)
    @ApiModelProperty(value = "总分")
    private String totalScore;
    /**及格线*/
    @Excel(name = "及格线", width = 15)
    @ApiModelProperty(value = "及格线")
    private String passMark;
    /**答题时间*/
    @Excel(name = "答题时间", width = 15)
    @ApiModelProperty(value = "答题时间")
    private Integer time;
    /**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
    /**是否评分*/
    private Integer isMark;
    /**删除状态*/
    @TableLogic
    private Integer delFlag;
    @ApiModelProperty(value = "试题表")
    private List<SmartTopicVo> smartTopicVoList;
    private Integer selectedCount;
}
