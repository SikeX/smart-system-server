package org.jeecg.modules.smartInnerPartyTalk.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 党内谈话附件表
 * @Author: jeecg-boot
 * @Date:   2021-11-01
 * @Version: V1.0
 */
@ApiModel(value="smart_inner_party_annex对象", description="党内谈话附件表")
@Data
@TableName("smart_inner_party_annex")
public class SmartInnerPartyAnnex implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
	/**党内谈话表ID*/
    @ApiModelProperty(value = "党内谈话表ID")
    private java.lang.String innerPartyId;
	/**序号*/
	@Excel(name = "序号", width = 15)
    @ApiModelProperty(value = "序号")
    private java.lang.Integer index;
	/**附件说明*/
	@Excel(name = "附件说明", width = 15)
    @ApiModelProperty(value = "附件说明")
    private java.lang.String annexEpl;
	/**附件文件路径*/
	@Excel(name = "附件文件路径", width = 15)
    @ApiModelProperty(value = "附件文件路径")
    private java.lang.String annexPath;
	/**上传时间*/
	@Excel(name = "上传时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "上传时间")
    private java.util.Date uploadTime;
	/**下载次数*/
	@Excel(name = "下载次数", width = 15)
    @ApiModelProperty(value = "下载次数")
    private java.lang.Integer downloadCount;
}
