package org.jeecg.modules.donation.item.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
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
 * @Description: 捐赠项目
 * @Author: jeecg-boot
 * @Date:   2021-10-22
 * @Version: V1.0
 */
@Data
@TableName("donation_item")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="donation_item对象", description="捐赠项目")
public class DonationItem implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**项目图片*/
	@Excel(name = "项目图片", width = 15)
    @ApiModelProperty(value = "项目图片")
    private java.lang.String donationPic;
	/**捐赠项目名称*/
	@Excel(name = "捐赠项目名称", width = 15)
    @ApiModelProperty(value = "捐赠项目名称")
    private java.lang.String donationItemName;
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
	/**项目状态*/
	@Excel(name = "项目状态", width = 15)
    @ApiModelProperty(value = "项目状态")
    private java.lang.Integer status;
	/**项目分类*/
	@Excel(name = "项目分类", width = 15)
    @ApiModelProperty(value = "项目分类")
    private java.lang.String donationClass;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
	/**捐赠项目描述*/
	@Excel(name = "捐赠项目描述", width = 15)
    @ApiModelProperty(value = "捐赠项目描述")
    private java.lang.String donationItemDesc;
	/**捐赠故事*/
	@Excel(name = "捐赠故事", width = 15)
    @ApiModelProperty(value = "捐赠故事")
    private java.lang.String donationItemStory;
	/**截止日期*/
	@Excel(name = "截止日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "截止日期")
    private java.util.Date endTime;
	/**目标金额*/
	@Excel(name = "目标金额", width = 15)
    @ApiModelProperty(value = "目标金额")
    private java.lang.String targetMoney;
	/**已筹金额*/
	@Excel(name = "已筹金额", width = 15)
    @ApiModelProperty(value = "已筹金额")
    private java.lang.String rasiedMoney;
}
