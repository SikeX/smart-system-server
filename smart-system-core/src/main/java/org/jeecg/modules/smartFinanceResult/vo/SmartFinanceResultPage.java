package org.jeecg.modules.smartFinanceResult.vo;

import java.io.Serializable;
import java.util.List;
import org.jeecg.modules.smartFinanceResult.entity.SmartFinanceResult;
import org.jeecg.modules.smartFinanceResult.entity.SmartFinanceAnnex;
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
 * @Description: 8项规定财物收支表
 * @Author: jeecg-boot
 * @Date:   2021-11-17
 * @Version: V1.0
 */
@Data
@ApiModel(value="smart_finance_resultPage对象", description="8项规定财物收支表")
public class SmartFinanceResultPage implements Serializable {

	/**主键*/
	@ApiModelProperty(value = "主键")
    private java.lang.String id;
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
	/**单位*/
	@Excel(name="单位",width = 15,dictTable ="sys_depart",dicText = "depart_name",dicCode = "id")
	@ApiModelProperty(value = "单位")
    private java.lang.String departId;
	/**标题*/
	@Excel(name = "标题", width = 15)
	@ApiModelProperty(value = "标题")
    private java.lang.String title;
	/**收支类型*/
	@Excel(name = "收支类型", width = 15)
	@ApiModelProperty(value = "收支类型")
    private java.lang.String financeType;
	/**收支时间*/
	@Excel(name = "收支时间", width = 20)
	@ApiModelProperty(value = "收支时间")
    private java.lang.String financeTime;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**创建人ID*/
	@ApiModelProperty(value = "创建人ID")
    private java.lang.String creatorId;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
	@ApiModelProperty(value = "创建人")
    private java.lang.String creator;
	/**删除状态（0，正常，1已删除）*/

	@ApiModelProperty(value = "删除状态（0，正常，1已删除）")
    private java.lang.Integer delFlag;
	/**审核状态*/
	@Excel(name = "审核状态", width = 15)
	@ApiModelProperty(value = "审核状态")
    private java.lang.Integer verifyStatus;

	@ExcelCollection(name="8项规定财物收支附件")
	@ApiModelProperty(value = "8项规定财物收支附件")
	private List<SmartFinanceAnnex> smartFinanceAnnexList;

}
