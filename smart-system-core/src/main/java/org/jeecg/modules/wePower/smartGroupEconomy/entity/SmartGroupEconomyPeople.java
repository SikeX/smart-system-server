package org.jeecg.modules.wePower.smartGroupEconomy.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.jeecg.common.aspect.annotation.Dict;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 集体经济组织人员
 * @Author: jeecg-boot
 * @Date:   2022-03-06
 * @Version: V1.0
 */
@Data
@TableName("smart_group_economy_people")
@ApiModel(value="smart_group_economy_people对象", description="集体经济组织人员")
public class SmartGroupEconomyPeople implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
	/**所在村*/
	@Excel(name = "所在村", width = 15)
    @Dict(dicCode = "id",dicText = "depart_name",dictTable = "sys_depart")
    @ApiModelProperty(value = "所在村")
    private java.lang.String depart;
	/**姓名*/
	@Excel(name = "姓名", width = 15)
    @ApiModelProperty(value = "姓名")
    private java.lang.String name;
	/**职务*/
	@Excel(name = "职务", width = 15)
    @ApiModelProperty(value = "职务")
    private java.lang.String job;
	/**任期开始时间*/
	@Excel(name = "任期开始时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "任期开始时间")
    private java.util.Date termStartDate;
	/**任期结束时间*/
	@Excel(name = "任期结束时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "任期结束时间")
    private java.util.Date termEndDate;
	/**身份证号*/
	@Excel(name = "身份证号", width = 15)
    @ApiModelProperty(value = "身份证号")
    private java.lang.String idNumber;
	/**照片*/
	@Excel(name = "照片", width = 15)
    @ApiModelProperty(value = "照片")
    private java.lang.String pic;
	/**主表id*/
    @ApiModelProperty(value = "主表id")
    private java.lang.String mainId;

    private java.lang.String faceToken;

}
