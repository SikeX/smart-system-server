package org.jeecg.modules.smartReportingInformation.entity;

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
 * @Description: 举报信息表
 * @Author: jeecg-boot
 * @Date:   2021-11-12
 * @Version: V1.0
 */
@Data
@TableName("smart_reporting_information")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="smart_reporting_information对象", description="举报信息表")
public class SmartReportingInformation implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**被反映人信息*/
	@Excel(name = "被反映人信息", width = 15)
    @ApiModelProperty(value = "被反映人信息")
    private java.lang.String reflectedInformation;
	/**被反映人单位*/
	@Excel(name = "被反映人单位", width = 15)
    @ApiModelProperty(value = "被反映人单位")
    private java.lang.String reflectedDocumentid;
	/**举报时间*/
	@Excel(name = "举报时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "举报时间")
    private java.util.Date reportingTime;
	/**处理类型*/
	@Excel(name = "处理类型", width = 15, dicCode = "processing_type")
	@Dict(dicCode = "processing_type")
    @ApiModelProperty(value = "处理类型")
    private java.lang.String processingType;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
    //entity实体层
    /**
     * 删除状态（0，正常，1已删除）
     */
    @Excel(name = "删除状态", width = 15,dicCode="del_flag")
    @TableLogic
    private Integer delFlag;
}
