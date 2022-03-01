package org.jeecg.modules.smartPostMarriage.vo;

import java.io.Serializable;
import java.util.List;
import org.jeecg.modules.smartPostMarriage.entity.SmartPostMarriageReport;
import org.jeecg.modules.smartPostMarriage.entity.SmartPostMarriageReportFile;
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
 * @Description: 8项规定婚后报备表
 * @Author: jeecg-boot
 * @Date:   2021-11-12
 * @Version: V1.0
 */
@Data
@ApiModel(value="smart_post_marriage_reportPage对象", description="8项规定婚后报备表")
public class SmartPostMarriageReportPage implements Serializable {

	/**主键*/
	@ApiModelProperty(value = "主键")
	private java.lang.String id;

	//婚前报备Id
	private java.lang.String preId;

	/**单位*/
	@Excel(name = "单位", width = 15, dictTable ="sys_depart", dicText = "depart_name", dicCode = "id")
	@ApiModelProperty(value = "单位")
	private java.lang.String workDepartment;
	/**人员工号*/
//	@Excel(name = "人员id", width = 15)
	@ApiModelProperty(value = "人员id")
	private java.lang.String personId;
	/**姓名*/
	@Excel(name = "姓名", width = 15)
	@ApiModelProperty(value = "姓名")
	private java.lang.String name;
	/**性别*/
	@Excel(name = "性别", width = 15, dicCode = "sex")
	@Dict(dicCode = "sex")
	@ApiModelProperty(value = "性别")
	private java.lang.String sex;
	/**年龄*/
	@Excel(name = "年龄", width = 15)
	@ApiModelProperty(value = "年龄")
	private java.lang.Integer age;
	/**政治面貌*/
	@Excel(name = "政治面貌", width = 15, dicCode = "political_status")
	@Dict(dicCode = "political_status")
	@ApiModelProperty(value = "政治面貌")
	private java.lang.String politicsStatus;
	/**职务*/
	@Excel(name = "职务", width = 15, dictTable = "sys_position", dicText = "name", dicCode = "code")
	@Dict(dictTable = "sys_position", dicText = "name", dicCode = "code")
	@ApiModelProperty(value = "职务")
	private java.lang.String job;
	/**职级*/
	@Excel(name = "职级", width = 15, dicCode = "position_rank")
	@Dict(dicCode = "position_rank")
	@ApiModelProperty(value = "职级")
	private java.lang.String jobLevel;
	/**婚宴场所名*/
	@Excel(name = "婚宴场所名", width = 15)
	@ApiModelProperty(value = "婚宴场所名")
	private java.lang.String weddingVenue;
	/**婚宴场所地址*/
	@Excel(name = "婚宴场所地址", width = 15)
	@ApiModelProperty(value = "婚宴场所地址")
	private java.lang.String weddingVenueAddr;
	/**宴请人数*/
	@Excel(name = "宴请人数", width = 15)
	@ApiModelProperty(value = "宴请人数")
	private java.lang.Integer guestsNumber;
	/**宴请人员范围*/
	@Excel(name = "宴请人员范围", width = 15)
	@ApiModelProperty(value = "宴请人员范围")
	private java.lang.String guestsScope;
	/**宴请费用*/
	@Excel(name = "宴请费用", width = 15)
	@ApiModelProperty(value = "宴请费用")
	private java.lang.Integer weddingCost;
	/**婚礼用车数量*/
	@Excel(name = "婚礼用车数量", width = 15)
	@ApiModelProperty(value = "婚礼用车数量")
	private java.lang.Integer weddingCarNumber;
	/**公车数量（婚礼用车中有多少辆公车）*/
	@Excel(name = "公车数量（婚礼用车中有多少辆公车）", width = 15)
	@ApiModelProperty(value = "公车数量（婚礼用车中有多少辆公车）")
	private java.lang.Integer govCarNumber;
	/**不符合规定收受礼金*/
	@Excel(name = "不符合规定收受礼金", width = 15)
	@ApiModelProperty(value = "不符合规定收受礼金")
	private java.lang.Integer illegalMoney;
	/**不符合规定收受礼品件数*/
	@Excel(name = "不符合规定收受礼品件数", width = 15)
	@ApiModelProperty(value = "不符合规定收受礼品件数")
	private java.lang.Integer illegalGiftNumber;
	/**违规收礼品处置情况*/
	@Excel(name = "违规收礼品处置情况", width = 15)
	@ApiModelProperty(value = "违规收礼品处置情况")
	private java.lang.String disposalDescribe;
	/**有无其他违诺行为*/
	@Excel(name = "有无其他违诺行为", width = 15)
	@ApiModelProperty(value = "有无其他违诺行为")
	private java.lang.String otherViolations;
	/**报告时间*/
	@Excel(name = "报告时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "报告时间")
	private java.util.Date reportTime;
	/**联系电话*/
	@Excel(name = "联系电话", width = 15)
	@ApiModelProperty(value = "联系电话")
	private java.lang.String phoneNumber;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
	/**删除状态(0，正常，1，删除）*/
	@ApiModelProperty(value = "删除状态(0，正常，1，删除）")
	private java.lang.Integer delFlag;

	@ExcelCollection(name="8项规定婚后报备宴请发票与附件表")
	@ApiModelProperty(value = "8项规定婚后报备宴请发票与附件表")
	private List<SmartPostMarriageReportFile> smartPostMarriageReportFileList;

	public void setWorkDepartment(String workDepartment) {
		this.workDepartment = workDepartment;
	}

	public String getId() {
		return id;
	}
}
