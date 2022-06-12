package org.jeecg.modules.smart_8regulations_for_reception.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
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

/**
 * @Description: 八项规定公务接待
 * @Author: jeecg-boot
 * @Date:   2021-11-11
 * @Version: V1.0
 */
@ApiModel(value="smart_8regulations_for_reception对象", description="八项规定公务接待")
@Data
@TableName("smart_8regulations_for_reception")
public class Smart_8regulationsForReception implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**单位ID*/
	@Excel(name = "单位ID", width = 15)
    @ApiModelProperty(value = "单位ID")
    private java.lang.String departmentid;
	/**接待时间*/
	@Excel(name = "接待时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "接待时间")
    private java.util.Date receptionTime;
	/**接待对象单位ID*/
	@Excel(name = "接待对象单位ID", width = 15)
    @ApiModelProperty(value = "接待对象单位ID")
    private java.lang.String receptionDepartmentId;
	/**人员数量*/
	@Excel(name = "人员数量", width = 15)
    @ApiModelProperty(value = "人员数量")
    private java.lang.Integer number;
	/**陪同人员数量*/
	@Excel(name = "陪同人员数量", width = 15)
    @ApiModelProperty(value = "陪同人员数量")
    private java.lang.Integer receptionNumber;
	/**总开销金额*/
	@Excel(name = "总开销金额", width = 15)
    @ApiModelProperty(value = "总开销金额")
    private java.lang.Double cost;
	/**创建人id*/
	@Excel(name = "创建人id", width = 15)
    @ApiModelProperty(value = "创建人id")
    private java.lang.String createId;
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
	/**删除状态*/
	@Excel(name = "删除状态", width = 15,dicCode = "del_flag")
    @TableLogic
    private  InterruptedException delFlag;
}
