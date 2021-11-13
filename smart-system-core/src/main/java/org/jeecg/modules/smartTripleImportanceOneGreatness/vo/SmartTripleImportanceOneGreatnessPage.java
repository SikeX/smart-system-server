package org.jeecg.modules.smartTripleImportanceOneGreatness.vo;

import java.util.List;
import org.jeecg.modules.smartTripleImportanceOneGreatness.entity.SmartTripleImportanceOneGreatness;
import org.jeecg.modules.smartTripleImportanceOneGreatness.entity.SmartTripleImportanceOneGreatnessDescription;
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
 * @Description: 三重一大表
 * @Author: jeecg-boot
 * @Date:   2021-11-12
 * @Version: V1.0
 */
@Data
@ApiModel(value="smart_triple_importance_one_greatnessPage对象", description="三重一大表")
public class SmartTripleImportanceOneGreatnessPage {

	/**主键*/
	@ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**单位*/
	@Excel(name = "单位", width = 15)
	@ApiModelProperty(value = "单位")
    private java.lang.String documentid;
	/**名称*/
	@Excel(name = "名称", width = 15)
	@ApiModelProperty(value = "名称")
    private java.lang.String meetingName;
	/**地点*/
	@Excel(name = "地点", width = 15)
	@ApiModelProperty(value = "地点")
    private java.lang.String meetingPlace;
	/**时间*/
	@Excel(name = "时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "时间")
    private java.util.Date meetingStarttime;
	/**类型*/
	@Excel(name = "类型", width = 15, dicCode = "meeting_type")
    @Dict(dicCode = "meeting_type")
	@ApiModelProperty(value = "类型")
    private java.lang.String meetingType;
	/**参会人数*/
	@Excel(name = "参会人数", width = 15)
	@ApiModelProperty(value = "参会人数")
    private java.lang.Integer meetingNumber;
	/**参会人员*/
	@Excel(name = "参会人员", width = 15)
	@ApiModelProperty(value = "参会人员")
    private java.lang.String meetingPeople;
	/**主持人*/
	@Excel(name = "主持人", width = 15)
	@ApiModelProperty(value = "主持人")
    private java.lang.String meetingHoster;
	/**记录人*/
	@Excel(name = "记录人", width = 15)
	@ApiModelProperty(value = "记录人")
    private java.lang.String meetingRecorer;
	/**会议内容摘要*/
	@Excel(name = "会议内容摘要", width = 15)
	@ApiModelProperty(value = "会议内容摘要")
    private java.lang.String meetingAbstract;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
    private java.lang.String meetingRemarks;
	/**创建人*/
	@ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**删除状态（0正常，1已删除）*/
	@Excel(name = "删除状态（0正常，1已删除）", width = 15)
	@ApiModelProperty(value = "删除状态（0正常，1已删除）")
    private java.lang.Integer delFlag;

	@ExcelCollection(name="三重一大附件表")
	@ApiModelProperty(value = "三重一大附件表")
	private List<SmartTripleImportanceOneGreatnessDescription> smartTripleImportanceOneGreatnessDescriptionList;

}
