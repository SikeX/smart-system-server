package org.jeecg.modules.smartReportingInformation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: TODO
 * @author: lord
 * @date: 2021年11月30日 16:02
 */
@Data
@TableName("smart_job")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="smart_job对象", description="定时任务信息表")
public class SmartJob implements Serializable {
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
    /**任务类型*/
    @Excel(name = "任务类型", width = 15, dicCode = "job_type")
    @Dict(dicCode = "job_type")
    @ApiModelProperty(value = "任务类型")
    private String jobType;
    /**任务名称*/
    @Excel(name = "任务名称", width = 15)
    @ApiModelProperty(value = "任务名称")
    private String jobName;
    /**任务描述*/
    @Excel(name = "任务描述", width = 15)
    @ApiModelProperty(value = "任务描述")
    private String jobDescribe;
    /**任务状态*/
    @Excel(name = "任务状态", width = 15)
    @ApiModelProperty(value = "任务状态")
    private String jobStatus;
    /**是否每日提醒*/
    @Excel(name = "是否每日提醒", width = 15, dicCode = "is_loop")
    @Dict(dicCode = "is_loop")
    @ApiModelProperty(value = "是否每日提醒")
    private String isLoop;
    /**执行时间(日）*/
    @Excel(name = "执行时间(日）", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "执行时间(日）")
    private Date executeTimeDay;
    /**执行时间(时）*/
    @Excel(name = "执行时间(时）", width = 15)
    @ApiModelProperty(value = "执行时间(时）")
    private String executeTimeHour;
    /**提醒全体人员*/
    @Excel(name = "提醒全体人员", width = 15, dicCode = "is_loop")
    @Dict(dicCode = "is_loop")
    @ApiModelProperty(value = "提醒全体人员")
    private String isToAll;
    /**提醒人员*/
    @Excel(name = "提醒人员", width = 15, dictTable = "sys_user", dicText = "realname", dicCode = "username")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    @ApiModelProperty(value = "提醒人员")
    private String toUser;
    /**提醒类型*/
    @Excel(name = "提醒类型", width = 15, dicCode = "msgType")
    @Dict(dicCode = "msgType")
    @ApiModelProperty(value = "提醒类型")
    private String type;
    /**模板名*/
    @Excel(name = "模板名", width = 15)
    @ApiModelProperty(value = "模板名")
    private String templateName;
    /**模板内容*/
    @Excel(name = "模板内容", width = 15)
    @ApiModelProperty(value = "模板内容")
    private String templateContent;
    /**job bean*/
    @Excel(name = "job bean", width = 15)
    @ApiModelProperty(value = "job bean")
    private String jobBean;


    public void test(){
        executeTimeDay.getTime();
    }

    public void setJobBean() {

        this.jobBean = IdWorker.getIdStr();
    }


}
