package org.jeecg.modules.wePower.smartPublicityProject.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 项目审核
 * @Author: jeecg-boot
 * @Date:   2022-03-09
 * @Version: V1.0
 */
@ApiModel(value="smart_publicity_project_verify对象", description="项目审核")
@Data
@TableName("smart_publicity_project_verify")
public class SmartPublicityProjectVerify implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
	/**项目id*/
    @ApiModelProperty(value = "项目id")
    private java.lang.String projectId;
	/**审核部门*/
	@Excel(name = "审核部门", width = 15, dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
    @ApiModelProperty(value = "审核部门")
    private java.lang.String verifyDepart;
	/**审核意见*/
	@Excel(name = "审核意见", width = 15)
    @ApiModelProperty(value = "审核意见")
    private java.lang.String verifyDesc;
	/**附件*/
	@Excel(name = "附件", width = 15)
    @ApiModelProperty(value = "附件")
    private java.lang.String file;
}
