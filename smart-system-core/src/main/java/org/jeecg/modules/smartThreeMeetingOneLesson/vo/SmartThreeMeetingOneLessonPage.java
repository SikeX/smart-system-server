package org.jeecg.modules.smartThreeMeetingOneLesson.vo;

import java.io.Serializable;
import java.util.List;
import org.jeecg.modules.smartThreeMeetingOneLesson.entity.SmartThreeMeetingOneLesson;
import org.jeecg.modules.smartThreeMeetingOneLesson.entity.SmartThreeMeetingOneLessonParticipants;
import org.jeecg.modules.smartThreeMeetingOneLesson.entity.SmartThreeMeetingOneLessonAnnex;
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
 * @Description: 三会一课
 * @Author: jeecg-boot
 * @Date:   2021-11-14
 * @Version: V1.0
 */
@Data
@ApiModel(value="smart_three_meeting_one_lessonPage对象", description="三会一课")
public class SmartThreeMeetingOneLessonPage implements Serializable {

	/**主键*/
	@ApiModelProperty(value = "主键")
	private java.lang.String id;
	/**单位*/
	@Excel(name="单位",width = 15,dictTable ="sys_depart",dicText = "depart_name",dicCode = "id")
	@ApiModelProperty(value = "单位")
	private java.lang.String departmentId;
	/**主持人id*/
	@Excel(name = "主持人id", width = 15)
	@ApiModelProperty(value = "主持人id")
	private java.lang.String hostId;
	/**主持人*/
	@Excel(name = "主持人", width = 15)
	@ApiModelProperty(value = "主持人")
	private java.lang.String hostName;
	/**记录人id*/
	@Excel(name = "记录人id", width = 15)
	@ApiModelProperty(value = "记录人id")
	private java.lang.String recorderId;
	/**记录人*/
	@Excel(name = "记录人", width = 15)
	@ApiModelProperty(value = "记录人")
	private java.lang.String recorderName;
	/**地点*/
	@Excel(name = "地点", width = 15)
	@ApiModelProperty(value = "地点")
	private java.lang.String place;
	/**时间*/
	@Excel(name = "时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "时间")
	private java.util.Date time;
	/**类型*/
	@Excel(name = "类型", width = 15, dicCode = "shyk")
	@Dict(dicCode = "shyk")
	@ApiModelProperty(value = "类型")
	private java.lang.String type;
	/**主题*/
	@Excel(name = "主题", width = 15)
	@ApiModelProperty(value = "主题")
	private java.lang.String theme;
	/**内容摘要*/
	@Excel(name = "内容摘要", width = 15)
	@ApiModelProperty(value = "内容摘要")
	private java.lang.String content;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
	private java.lang.String remark;
	/**创建人id*/
	@Excel(name = "创建人id", width = 15)
	@ApiModelProperty(value = "创建人id")
	private java.lang.String founderId;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
	@ApiModelProperty(value = "创建人")
	private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "创建日期")
	private java.util.Date createTime;
	/**删除标志*/

	@ApiModelProperty(value = "删除标志")
	private java.lang.Integer delFlag;

	@ExcelCollection(name="三会一课参会人员表")
	@ApiModelProperty(value = "三会一课参会人员表")
	private List<SmartThreeMeetingOneLessonParticipants> smartThreeMeetingOneLessonParticipantsList;
	@ExcelCollection(name="三会一课附件表")
	@ApiModelProperty(value = "三会一课附件表")
	private List<SmartThreeMeetingOneLessonAnnex> smartThreeMeetingOneLessonAnnexList;

}
