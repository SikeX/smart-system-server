package org.jeecg.modules.smart_reception.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.jeecgframework.poi.excel.annotation.Excel;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 公务接待2.0
 * @Author: jeecg-boot
 * @Date:   2022-02-28
 * @Version: V1.0
 */
@Data
@TableName("smart_reception")
@ApiModel(value="smart_reception对象", description="公务接待2.0")
public class SmartReception implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**来访事由*/
    @Excel(name = "来访事由", width = 15)
    @ApiModelProperty(value = "来访事由")
    private java.lang.String reason;
	/**接待对象单位*/
    @Excel(name = "接待对象单位", width = 15)
    @ApiModelProperty(value = "接待对象单位")
    private java.lang.String receptionDepartmentId;
	/**接待单位*/
    @Excel(name = "接待单位", width = 15, dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
    @ApiModelProperty(value = "接待单位")
    private java.lang.String departmentId;
	/**到访时间*/
    @Excel(name = "到访时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "到访时间")
    private java.util.Date startTime;
	/**结束时间*/
    @Excel(name = "结束时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "结束时间")
    private java.util.Date endTime;
	/**来访人数量*/
    @Excel(name = "来访人数量", width = 15)
    @ApiModelProperty(value = "来访人数量")
    private java.lang.Integer visitorsNum;
	/**总开销*/
    @Excel(name = "总开销", width = 15)
    @ApiModelProperty(value = "总开销")
    private java.lang.Integer cost;
	/**是否住宿*/
    @Excel(name = "是否住宿", width = 15, dicCode = "stay")
    @Dict(dicCode = "stay")
    @ApiModelProperty(value = "是否住宿")
    private java.lang.Integer stay;
	/**附件上传*/
    @Excel(name = "附件上传", width = 15)
    @ApiModelProperty(value = "附件上传")
    private java.lang.String filesPc;
	/**高拍仪附件上传*/
    @Excel(name = "高拍仪附件上传", width = 15)
    @ApiModelProperty(value = "高拍仪附件上传")
    private java.lang.String files;
	/**创建人*/
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
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
	/**删除状态*/
    @Excel(name = "删除状态", width = 15)
    @ApiModelProperty(value = "删除状态")
    private java.lang.Integer delFlag;
}
