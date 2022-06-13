package org.jeecg.modules.smartReportingInformation.vo;

import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import org.jeecg.modules.smartReportingInformation.entity.SmartReportingInformation;
import org.jeecg.modules.smartReportingInformation.entity.SmartReportingSurvey;
import org.jeecg.modules.smartReportingInformation.entity.SmartReportingDescription;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 举报信息表
 * @Author: jeecg-boot
 * @Date:   2021-11-29
 * @Version: V1.0
 */
@Data
@ApiModel(value="smart_reporting_informationPage对象", description="举报信息表")
public class SmartReportingInformationPage {

	/**主键*/
	@ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**被反映人信息*/
	@Excel(name = "被反映人信息", width = 15)
	@ApiModelProperty(value = "被反映人信息")
    private java.lang.String reflectedInformation;
	/**被反映人单位*/
	@Excel(name = "被反映人单位", width = 15)
	@ApiModelProperty(value = "被反映人单位")
    private java.lang.String reflectedDocumentid;
	/**主要问题*/
	@Excel(name = "主要问题", width = 15)
	@ApiModelProperty(value = "主要问题")
    private java.lang.String majorProblem;

	/**照片*/

	@ApiModelProperty(value = "照片")
    private java.lang.String photo;
//	@TableField(exist = false)
//	private List photoList;

	/**附件*/

	@ApiModelProperty(value = "附件")
    private java.lang.String description;
	/**举报时间*/

	@Excel(name = "举报时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "举报时间")
    private java.util.Date reportingTime;
	/**处理状态*/
	@Excel(name = "处理状态", width = 15, dicCode = "processing_result")
    @Dict(dicCode = "processing_result")
	@ApiModelProperty(value = "处理状态")
    private java.lang.String processingResult;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**删除状态*/
	/*@Excel(name = "删除状态", width = 15)*/
	@ApiModelProperty(value = "删除状态")
    private java.lang.Integer delFlag;
	/**举报人姓名*/
	@Excel(name = "举报人姓名", width = 15)
	@ApiModelProperty(value = "举报人姓名")
    private java.lang.String reportingName;
	/**联系电话*/
	@Excel(name = "联系电话", width = 15)
	@ApiModelProperty(value = "联系电话")
    private java.lang.String contactNumber;
	/**调查意见*/
	@Excel(name = "调查意见", width = 15)
	@ApiModelProperty(value = "调查意见")
    private java.lang.String surveyIdea;



	@ExcelCollection(name="举报调查表")
	@ApiModelProperty(value = "举报调查表")
	private List<SmartReportingSurvey> smartReportingSurveyList;
	@ExcelCollection(name="举报附件表")
	@ApiModelProperty(value = "举报附件表")
	private List<SmartReportingDescription> smartReportingDescriptionList;

}
