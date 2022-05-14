package org.jeecg.modules.smartAnswerInfo.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author xyy
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="最终评分被考核单位得分对象", description="最终评分被考核单位得分对象")
public class SmartDepartContentScore implements Serializable {

    /**主键-答题信息表ID*/
    @ApiModelProperty(value = "主键")
    private java.lang.String id;

    /**答题考核节点表ID*/
    @ApiModelProperty(value = "答题考核节点主键")
    private java.lang.String answerAssContentId;

    /**单位*/
    @ApiModelProperty(value = "单位")
    private java.lang.String depart;
    /**单位名称*/
    @ApiModelProperty(value = "单位名称")
    private java.lang.String departName;
    @ApiModelProperty(value = "考核任务")
    private java.lang.String missionId;
    /**最低得分*/
    @ApiModelProperty(value = "最低得分")
    private java.lang.Double lowestScore;
    /**最高得分*/
    @ApiModelProperty(value = "最高得分")
    private java.lang.Double highestScore;
    /**平均得分*/
    @ApiModelProperty(value = "平均得分")
    private java.lang.Double averageScore;
    /**最终得分*/
    @ApiModelProperty(value = "最终得分")
    private java.lang.Double finalScore;
    /**总分*/
    @ApiModelProperty(value = "总分")
    private java.lang.Double totalPoints;

}
