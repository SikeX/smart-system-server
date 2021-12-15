package org.jeecg.modules.smartOrgMeeting.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 组织生活会参会人员表
 * @Author: jeecg-boot
 * @Date:   2021-11-14
 * @Version: V1.0
 */
@ApiModel(value="smart_org_meeting_pacpa对象", description="组织生活会参会人员表")
@Data
@TableName("smart_org_meeting_pacpa")
public class SmartOrgMeetingPacpa implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
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
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
	/**主表ID*/
    @ApiModelProperty(value = "主表ID")
    private java.lang.String parentId;
	/**参会人员ID*/
    @ApiModelProperty(value = "参会人员ID")
    private java.lang.String pacpaId;
	/**参会人员姓名*/
	@Excel(name = "参会人员姓名", width = 15)
    @ApiModelProperty(value = "参会人员姓名")
    private java.lang.String pacpaName;
	/**删除状态（0，正常，1已删除）*/
    @TableLogic
    @ApiModelProperty(value = "删除状态（0，正常，1已删除）")
    private java.lang.Integer delFlag;
}
