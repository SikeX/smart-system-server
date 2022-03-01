package org.jeecg.modules.smart_reception.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.jeecg.common.aspect.annotation.Dict;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 用餐情况
 * @Author: jeecg-boot
 * @Date:   2022-02-28
 * @Version: V1.0
 */
@Data
@TableName("smart_8_dining")
@ApiModel(value="smart_8_dining对象", description="用餐情况")
public class Smart_8Dining implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**用餐地名称*/
	@Excel(name = "用餐地名称", width = 15)
    @ApiModelProperty(value = "用餐地名称")
    private java.lang.String name;
	/**用餐时间*/
	@Excel(name = "用餐时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "用餐时间")
    private java.util.Date etime;
	/**用餐总人数*/
	@Excel(name = "用餐总人数", width = 15)
    @ApiModelProperty(value = "用餐总人数")
    private java.lang.Integer num;
	/**陪同人数*/
	@Excel(name = "陪同人数", width = 15)
    @ApiModelProperty(value = "陪同人数")
    private java.lang.String numR;
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
	/**外键*/
    @ApiModelProperty(value = "外键")
    private java.lang.String mainId;
}
