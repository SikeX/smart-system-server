package org.jeecg.modules.wePower.smartPublicityProject.vo;

import java.util.List;

import org.jeecg.modules.wePower.smartPublicityProject.entity.SmartPublicityProjectVerify;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 项目管理
 * @Author: jeecg-boot
 * @Date:   2021-12-26
 * @Version: V1.0
 */
@Data
@ApiModel(value="smart_publicity_projectPage对象", description="项目管理")
public class SmartPublicityProjectPage {

	/**主键*/
	@ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**项目名称*/
	@Excel(name = "项目名称", width = 15)
	@ApiModelProperty(value = "项目名称")
    private java.lang.String title;
	/**建设单位*/
	@Excel(name = "建设单位", width = 15, dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
	@ApiModelProperty(value = "建设单位")
    private java.lang.String location;
	/**合同内容*/
	@Excel(name = "合同内容", width = 15)
	@ApiModelProperty(value = "合同内容")
    private java.lang.String projectContent;
	/**金额*/
	@Excel(name = "金额", width = 15)
	@ApiModelProperty(value = "金额")
    private java.lang.String money;
	/**服务年限*/
	@Excel(name = "服务年限", width = 15)
	@ApiModelProperty(value = "服务年限")
    private java.lang.String period;
	/**完成时限*/
	@Excel(name = "完成时限", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "完成时限")
    private java.util.Date endTime;
	/**签订日期*/
	@Excel(name = "签订日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "签订日期")
    private java.util.Date signTime;
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
	/**创建部门*/
	@ApiModelProperty(value = "创建部门")
    private java.lang.String sysOrgCode;
	/**附件*/
	@Excel(name = "附件", width = 15)
	@ApiModelProperty(value = "附件")
    private java.lang.String file;
	/**删除标志*/
	@Excel(name = "删除标志", width = 15)
	@ApiModelProperty(value = "删除标志")
    private java.lang.Integer delFlag;

	@ExcelCollection(name="项目审核")
	@ApiModelProperty(value = "项目审核")
	private List<SmartPublicityProjectVerify> smartPublicityProjectVerifyList;

}
