package org.jeecg.modules.wePower.smartPublicityResource.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 资产资源
 * @Author: jeecg-boot
 * @Date:   2021-12-26
 * @Version: V1.0
 */
@Data
@TableName("smart_publicity_resource")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="smart_publicity_resource对象", description="资产资源")
public class SmartPublicityResource implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**资产分类*/
	@Excel(name = "资产分类", width = 15, dicCode = "resource")
	@Dict(dicCode = "resource")
    @ApiModelProperty(value = "资产分类")
    private java.lang.String category;
	/**所属单位*/
	@Excel(name = "所属单位", width = 15, dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
	@Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
    @ApiModelProperty(value = "所属单位")
    private java.lang.String belong;
	/**资产名称*/
	@Excel(name = "资产名称", width = 15)
    @ApiModelProperty(value = "资产名称")
    private java.lang.String name;
	/**位置*/
	@Excel(name = "位置", width = 15)
    @ApiModelProperty(value = "位置")
    private java.lang.String location;
	/**图片*/
	@Excel(name = "图片", width = 15)
    @ApiModelProperty(value = "图片")
    private java.lang.String picture;
	/**上传时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "上传时间")
    private java.util.Date createTime;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**发布单位*/
    @ApiModelProperty(value = "发布单位")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String sysOrgCode;
	/**删除标志*/
	@Excel(name = "删除标志", width = 15)
    @ApiModelProperty(value = "删除标志")
    private java.lang.Integer delFlag;
}
