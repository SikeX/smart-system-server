package org.jeecg.modules.smartFuneralReport.entity;

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
 * @Description: 丧事口头报备表
 * @Author: jeecg-boot
 * @Date:   2022-02-26
 * @Version: V1.0
 */
@Data
@TableName("smart_funeral_report")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="smart_funeral_report对象", description="丧事口头报备表")
public class SmartFuneralReport implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
	/**报备人ID*/
	@Excel(name = "报备人", width = 15,dictTable ="sys_user",dicText = "realname",dicCode = "id")
    @ApiModelProperty(value = "报备人")
    @Dict(dictTable ="sys_user",dicText = "realname",dicCode = "id")
    private String peopleId;
	/**工作单位*/
	@Excel(name = "工作单位", width = 15,dictTable ="sys_depart",dicText = "depart_name",dicCode = "id")
    @ApiModelProperty(value = "工作单位")
    @Dict(dictTable ="sys_depart",dicText = "depart_name",dicCode = "id")
    private String departId;
	/**操办时间*/
	@Excel(name = "操办时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "操办时间")
    private Date funeralTime;
	/**地点*/
	@Excel(name = "地点", width = 15)
    @ApiModelProperty(value = "地点")
    private String address;
	/**宴请人数*/
	@Excel(name = "宴请人数", width = 15)
    @ApiModelProperty(value = "宴请人数")
    private String peopleAccount;
	/**宴请人员范围*/
	@Excel(name = "宴请人员范围", width = 15)
    @ApiModelProperty(value = "宴请人员范围")
    private String peopleType;
	/**删除标志*/
    @TableLogic
//	@Excel(name = "删除标志", width = 15)
    @ApiModelProperty(value = "删除标志")
    private Integer delFlag;
	/**报告时间*/
	@Excel(name = "报告时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "报告时间")
    private Date reportTime;
    /**审核状态*/
    @ApiModelProperty(value = "审核状态")
    private String verifyStatus;
    private Integer ifReport;
    /**附件*/
//    @Excel(name = "附件", width = 15)
    @ApiModelProperty(value = "附件")
    private java.lang.String files;
}
