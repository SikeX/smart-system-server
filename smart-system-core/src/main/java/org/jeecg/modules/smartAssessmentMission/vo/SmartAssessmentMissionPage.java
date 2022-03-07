package org.jeecg.modules.smartAssessmentMission.vo;

import java.util.List;
import org.jeecg.modules.smartAssessmentMission.entity.SmartAssessmentMission;
import org.jeecg.modules.smartAssessmentMission.entity.SmartAssessmentDepart;
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
 * @Description: 考核任务表
 * @Author: jeecg-boot
 * @Date:   2022-02-12
 * @Version: V1.0
 */
@Data
@ApiModel(value="smart_assessment_missionPage对象", description="考核任务表")
public class SmartAssessmentMissionPage {

	/**主键*/
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
	/**任务名称*/
	@Excel(name = "任务名称", width = 15)
	@ApiModelProperty(value = "任务名称")
    private java.lang.String missionName;
	/**考核年份*/
	@Excel(name = "考核年份", width = 15)
	@ApiModelProperty(value = "考核年份")
    private java.lang.String assessmentYear;
	/**考核时间*/
	@Excel(name = "考核时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "考核时间")
    private java.util.Date assessmentTime;
	/**总分*/
	@Excel(name = "总分", width = 15)
	@ApiModelProperty(value = "总分")
    private java.lang.Integer totalPoint;
	/**任务状态*/
	@Excel(name = "任务状态", width = 15)
	@ApiModelProperty(value = "任务状态")
    private java.lang.String missionStatus;
	/**考核要点总数*/
	@Excel(name = "考核要点总数", width = 15)
	@ApiModelProperty(value = "考核要点总数")
    private java.lang.Integer keyPointsAmount;

	@ExcelCollection(name="考核任务被考核单位")
	@ApiModelProperty(value = "考核任务被考核单位")
	private List<SmartAssessmentDepart> smartAssessmentDepartList;

}
