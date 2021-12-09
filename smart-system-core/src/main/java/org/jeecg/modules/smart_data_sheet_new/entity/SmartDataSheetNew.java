package org.jeecg.modules.smart_data_sheet_new.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
 * @Description: 资料库
 * @Author: jeecg-boot
 * @Date:   2021-12-07
 * @Version: V1.0
 */
@Data
@TableName("smart_data_sheet_new")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="smart_data_sheet_new对象", description="资料库")
public class SmartDataSheetNew implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**文件主题*/
	@Excel(name = "文件主题", width = 15)
    @ApiModelProperty(value = "文件主题")
    private java.lang.String name;
	/**文件类型*/
	@Excel(name = "文件类型", width = 15, dicCode = "type_data")
	@Dict(dicCode = "type_data")
    @ApiModelProperty(value = "文件类型")
    private java.lang.Integer type;
	/**发布单位*/
	@Excel(name = "发布单位", width = 15)
    @ApiModelProperty(value = "发布单位")
    private java.lang.String departmentid;
	/**发布人*/
	@Excel(name = "发布人", width = 15, dictTable = "sys_user", dicText = "realname", dicCode = "username")
	@Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    @ApiModelProperty(value = "发布人")
    private java.lang.String publisher;
	/**文件描述*/
	@Excel(name = "文件描述", width = 15)
    @ApiModelProperty(value = "文件描述")
    private java.lang.String describeA;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**上传文件*/
	@Excel(name = "上传文件", width = 15)
    @ApiModelProperty(value = "上传文件")
    private java.lang.String file;
	/**下载次数*/
	@Excel(name = "下载次数", width = 15)
    @ApiModelProperty(value = "下载次数")
    private java.lang.Integer times;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**删除状态*/
	@Excel(name = "删除状态", width = 15,dicCode = "del_flag")
    @ApiModelProperty(value = "删除状态")
    @TableLogic
    private java.lang.Integer delFlag;
}
