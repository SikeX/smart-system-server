package org.jeecg.modules.smartPostMarriage.vo;

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
 * @Date:   2021-11-10
 * @Version: V1.0
 */
@Data
@ApiModel(value="smart_post_marriage_reportPage对象", description="8项规定婚后报备表")
public class SmartPostMarriageReportPage {

	/**主键*/
	@ApiModelProperty(value = "主键")
    private String id;
	/**人员工号*/
	@Excel(name = "人员工号", width = 15)
	@ApiModelProperty(value = "人员工号")
    private String workNo;
	/**姓名*/
	@Excel(name = "姓名", width = 15)
	@ApiModelProperty(value = "姓名")
    private String name;
	/**性别*/
	@Excel(name = "性别", width = 15)
	@ApiModelProperty(value = "性别")
    private String sex;
	/**年龄*/
	@Excel(name = "年龄", width = 15)
	@ApiModelProperty(value = "年龄")
    private Integer age;
	/**政治面貌*/
	@Excel(name = "政治面貌", width = 15)
	@ApiModelProperty(value = "政治面貌")
    private String politicsStatus;
	/**单位id*/
	@Excel(name = "单位id", width = 15)
	@ApiModelProperty(value = "单位id")
    private String workDepartment;
	/**职务*/
	@Excel(name = "职务", width = 15)
	@ApiModelProperty(value = "职务")
    private String job;
	/**职级*/
	@Excel(name = "职级", width = 15)
	@ApiModelProperty(value = "职级")
    private String jobLevel;
	/**婚宴场所名*/
	@Excel(name = "婚宴场所名", width = 15)
	@ApiModelProperty(value = "婚宴场所名")
    private String weddingVenue;
	/**婚宴场所地址*/
	@Excel(name = "婚宴场所地址", width = 15)
	@ApiModelProperty(value = "婚宴场所地址")
    private String weddingVenueAddr;
	/**宴请人数*/
	@Excel(name = "宴请人数", width = 15)
	@ApiModelProperty(value = "宴请人数")
    private Integer guestsNumber;
	/**宴请人员范围*/
	@Excel(name = "宴请人员范围", width = 15)
	@ApiModelProperty(value = "宴请人员范围")
    private String guestsScope;
	/**宴请费用*/
	@Excel(name = "宴请费用", width = 15)
	@ApiModelProperty(value = "宴请费用")
    private Integer weddingCost;
	/**婚礼用车数量*/
	@Excel(name = "婚礼用车数量", width = 15)
	@ApiModelProperty(value = "婚礼用车数量")
    private Integer weddingCarNumber;
	/**公车数量（婚礼用车中有多少辆公车）*/
	@Excel(name = "公车数量（婚礼用车中有多少辆公车）", width = 15)
	@ApiModelProperty(value = "公车数量（婚礼用车中有多少辆公车）")
    private Integer govCarNumber;
	/**不符合规定收受礼金*/
	@Excel(name = "不符合规定收受礼金", width = 15)
	@ApiModelProperty(value = "不符合规定收受礼金")
    private Integer illegalMoney;
	/**不符合规定收受礼品件数*/
	@Excel(name = "不符合规定收受礼品件数", width = 15)
	@ApiModelProperty(value = "不符合规定收受礼品件数")
    private Integer illegalGiftNumber;
	/**违规收礼品处置情况*/
	@Excel(name = "违规收礼品处置情况", width = 15)
	@ApiModelProperty(value = "违规收礼品处置情况")
    private String disposalDescribe;
	/**有无其他违诺行为*/
	@Excel(name = "有无其他违诺行为", width = 15)
	@ApiModelProperty(value = "有无其他违诺行为")
    private String otherViolations;
	/**报告时间*/
	@Excel(name = "报告时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "报告时间")
    private Date reportTime;
	/**联系电话*/
	@Excel(name = "联系电话", width = 15)
	@ApiModelProperty(value = "联系电话")
    private String phoneNumber;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**删除状态(0，正常，1，删除）*/
	@Excel(name = "删除状态(0，正常，1，删除）", width = 15)
	@ApiModelProperty(value = "删除状态(0，正常，1，删除）")
    private Integer delFlag;

	@ExcelCollection(name="8项规定婚后报备宴请发票与附件表")
	@ApiModelProperty(value = "8项规定婚后报备宴请发票与附件表")
	private List<SmartPostMarriageReportFile> smartPostMarriageReportFileList;

	public void setWorkDepartment(String workDepartment) {
		this.workDepartment = workDepartment;
	}
}
