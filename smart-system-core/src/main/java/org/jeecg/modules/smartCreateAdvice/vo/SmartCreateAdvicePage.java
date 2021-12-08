package org.jeecg.modules.smartCreateAdvice.vo;

import java.util.List;
import org.jeecg.modules.smartCreateAdvice.entity.SmartCreateAdvice;
import org.jeecg.modules.smartCreateAdvice.entity.SmartCreateAdviceAnnex;
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
 * @Description: 制发建议表
 * @Author: jeecg-boot
 * @Date:   2021-11-13
 * @Version: V1.0
 */
@Data
@ApiModel(value="smart_create_advicePage对象", description="制发建议表")
public class SmartCreateAdvicePage {

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
	/**基本情况*/
	@Excel(name = "基本情况", width = 15)
	@ApiModelProperty(value = "基本情况")
    private java.lang.String basicDesc;
	/**存在问题*/
	@Excel(name = "存在问题", width = 15)
	@ApiModelProperty(value = "存在问题")
    private java.lang.String problem;
	/**主要措施*/
	@Excel(name = "主要措施", width = 15)
	@ApiModelProperty(value = "主要措施")
    private java.lang.String mainSolution;
	/**删除状态*/
	@ApiModelProperty(value = "删除状态")
    private java.lang.Integer delFlag;
	/**单位*/
	@Excel(name="单位",width = 15,dictTable ="sys_depart",dicText = "depart_name",dicCode = "id")
	@ApiModelProperty(value = "单位")
    private java.lang.String departId;

	@ExcelCollection(name="制发建议附件表")
	@ApiModelProperty(value = "制发建议附件表")
	private List<SmartCreateAdviceAnnex> smartCreateAdviceAnnexList;
}
