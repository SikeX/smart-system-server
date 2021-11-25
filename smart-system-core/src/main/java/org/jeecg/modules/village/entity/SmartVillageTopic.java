package org.jeecg.modules.village.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 村情互动-村民提问表
 * @Author: jeecg-boot
 * @Date:   2021-11-22
 * @Version: V1.0
 */
@Data
@TableName("smart_village_topic")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="smart_village_topic对象", description="村情互动-村民提问表")
public class SmartVillageTopic implements Serializable {
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
	/**问题*/
	@Excel(name = "问题", width = 15)
    @ApiModelProperty(value = "问题")
    private String title;
	/**内容*/
	@Excel(name = "内容", width = 15)
    @ApiModelProperty(value = "内容")
    private String content;
	/**浏览次数*/
	@Excel(name = "浏览次数", width = 15)
    @ApiModelProperty(value = "浏览次数")
    private String view;
	/**讨论数*/
	@Excel(name = "讨论数", width = 15)
    @ApiModelProperty(value = "讨论数")
    private String commentCount;
	/**作者*/
	@Excel(name = "作者", width = 15)
    @ApiModelProperty(value = "作者")
    private String userId;
	/**发布时间*/
	@Excel(name = "发布时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "发布时间")
    private Date inTime;
    /**作者对象*/
    @Excel(name = "作者", width = 15)
    @ApiModelProperty(value = "作者对象")
    private SmartVillageUser user;
}
