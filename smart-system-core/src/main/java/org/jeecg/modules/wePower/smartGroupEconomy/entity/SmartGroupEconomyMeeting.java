package org.jeecg.modules.wePower.smartGroupEconomy.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.jeecg.common.aspect.annotation.Dict;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 集体经济组织会议
 * @Author: jeecg-boot
 * @Date:   2022-03-06
 * @Version: V1.0
 */
@Data
@TableName("smart_group_economy_meeting")
@ApiModel(value="smart_group_economy_meeting对象", description="集体经济组织会议")
public class SmartGroupEconomyMeeting implements Serializable {
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
	/**附件1*/
	@Excel(name = "附件1", width = 15)
    @ApiModelProperty(value = "附件1")
    private java.lang.String file1;
	/**参会人员*/
	@Excel(name = "参会人员", width = 15)
    @Dict(dicCode = "id",dicText = "name",dictTable = "smart_group_economy_people")
    @ApiModelProperty(value = "参会人员")
    private java.lang.String people1;
	/**视频*/
	@Excel(name = "视频", width = 15)
    @ApiModelProperty(value = "视频")
    private java.lang.String video1;
	/**附件2*/
	@Excel(name = "附件2", width = 15)
    @ApiModelProperty(value = "附件2")
    private java.lang.String file2;
	/**参会人员*/
	@Excel(name = "参会人员", width = 15)
    @Dict(dicCode = "id",dicText = "name",dictTable = "smart_group_economy_people")
    @ApiModelProperty(value = "参会人员")
    private java.lang.String people2;
	/**视频*/
	@Excel(name = "视频", width = 15)
    @ApiModelProperty(value = "视频")
    private java.lang.String video2;
	/**附件3*/
	@Excel(name = "附件3", width = 15)
    @ApiModelProperty(value = "附件3")
    private java.lang.String file3;
	/**参会人员*/
	@Excel(name = "参会人员", width = 15)
    @Dict(dicCode = "id",dicText = "name",dictTable = "smart_group_economy_people")
    @ApiModelProperty(value = "参会人员")
    private java.lang.String people3;
	/**视频*/
	@Excel(name = "视频", width = 15)
    @ApiModelProperty(value = "视频")
    private java.lang.String video3;
	/**主表id*/
    @ApiModelProperty(value = "主表id")
    private java.lang.String mainId;
}
