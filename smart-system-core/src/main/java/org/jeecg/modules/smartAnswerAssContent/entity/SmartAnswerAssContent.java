package org.jeecg.modules.smartAnswerAssContent.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.jeecgframework.poi.excel.annotation.Excel;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 答题考核节点表
 * @Author: jeecg-boot
 * @Date:   2022-02-21
 * @Version: V1.0
 */
@Data
@TableName("smart_answer_ass_content")
@ApiModel(value="smart_answer_ass_content对象", description="答题考核节点表")
public class SmartAnswerAssContent implements Serializable {
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
	/**主表ID*/
    @Excel(name = "主表ID", width = 15)
    @ApiModelProperty(value = "主表ID")
    private java.lang.String mainId;
	/**父级节点*/
    @Excel(name = "父级节点", width = 15)
    @ApiModelProperty(value = "父级节点")
    private java.lang.String pid;
	/**是否有子节点*/
    @Excel(name = "是否有子节点", width = 15)
    @ApiModelProperty(value = "是否有子节点")
    private java.lang.String hasChild;
	/**考核内容节点*/
    @Excel(name = "考核内容节点", width = 15)
    @Dict(dictTable = "smart_assessment_content", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "考核内容节点")
    private java.lang.String assContentId;
	/**要点状态*/
    @Excel(name = "要点状态", width = 15)
    @ApiModelProperty(value = "要点状态")
    private java.lang.Integer contentStatus;
    /**是否考核要点*/
    @Excel(name = "是否考核要点", width = 15)
    @ApiModelProperty(value = "是否考核要点")
    private java.lang.Integer isKey;
	/**最低得分*/
    @Excel(name = "最低得分", width = 15)
    @ApiModelProperty(value = "最低得分")
    private java.lang.Double lowestScore;
	/**最高得分*/
    @Excel(name = "最高得分", width = 15)
    @ApiModelProperty(value = "最高得分")
    private java.lang.Double highestScore;
	/**平均得分*/
    @Excel(name = "平均得分", width = 15)
    @ApiModelProperty(value = "平均得分")
    private java.lang.Double averageScore;
	/**最终得分*/
    @Excel(name = "最终得分", width = 15)
    @ApiModelProperty(value = "最终得分")
    private java.lang.Double finalScore;
}
