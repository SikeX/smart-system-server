package org.jeecg.modules.wePower.smartPublicityProject.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 项目管理
 * @Author: jeecg-boot
 * @Date:   2022-03-09
 * @Version: V1.0
 */
@ApiModel(value="smart_publicity_project对象", description="项目管理")
@Data
@TableName("smart_publicity_project")
public class SmartPublicityProject implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**项目名称*/
	@Excel(name = "项目名称", width = 15)
    @ApiModelProperty(value = "项目名称")
    private java.lang.String title;
	/**项目分类*/
	@Excel(name = "项目分类", width = 15)
    @ApiModelProperty(value = "项目分类")
    private java.lang.String type;
	/**建设单位*/
	@Excel(name = "建设单位", width = 15, dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
    @ApiModelProperty(value = "建设单位")
    private java.lang.String location;
	/**施工单位*/
	@Excel(name = "施工单位", width = 15)
    @ApiModelProperty(value = "施工单位")
    private java.lang.String constructDep;
	/**简要说明*/
	@Excel(name = "简要说明", width = 15)
    @ApiModelProperty(value = "简要说明")
    private java.lang.String projectContent;
	/**合同金额*/
	@Excel(name = "合同金额", width = 15)
    @ApiModelProperty(value = "合同金额")
    private java.lang.String money;
	/**服务年限*/
	@Excel(name = "服务年限", width = 15)
    @ApiModelProperty(value = "服务年限")
    private java.lang.String period;
	/**完成时限*/
	@Excel(name = "完成时限", width = 15)
    @ApiModelProperty(value = "完成时限")
    private java.util.Date endTime;
	/**合同签订日期*/
	@Excel(name = "合同签订日期", width = 15)
    @ApiModelProperty(value = "合同签订日期")
    private java.util.Date signTime;
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
	/**创建部门*/
    @ApiModelProperty(value = "创建部门")
    private java.lang.String sysOrgCode;
	/**村党支部提议文件*/
	@Excel(name = "村党支部提议文件", width = 15)
    @ApiModelProperty(value = "村党支部提议文件")
    private java.lang.String meetFile1;
	/**参会人员*/
	@Excel(name = "参会人员", width = 15)
    @ApiModelProperty(value = "参会人员")
    private java.lang.String people1;
	/**会议照片*/
	@Excel(name = "会议照片", width = 15)
    @ApiModelProperty(value = "会议照片")
    private java.lang.String video1;
	/**村集体经济组织相关材料*/
	@Excel(name = "村集体经济组织相关材料", width = 15)
    @ApiModelProperty(value = "村集体经济组织相关材料")
    private java.lang.String file2;
	/**参会人员*/
	@Excel(name = "参会人员", width = 15)
    @ApiModelProperty(value = "参会人员")
    private java.lang.String people2;
	/**会议照片*/
	@Excel(name = "会议照片", width = 15)
    @ApiModelProperty(value = "会议照片")
    private java.lang.String video2;
	/**合同*/
	@Excel(name = "合同", width = 15)
    @ApiModelProperty(value = "合同")
    private java.lang.String file3;
	/**参会人员*/
	@Excel(name = "参会人员", width = 15)
    @ApiModelProperty(value = "参会人员")
    private java.lang.String people3;
	/**会议照片*/
	@Excel(name = "会议照片", width = 15)
    @ApiModelProperty(value = "会议照片")
    private java.lang.String video3;
	/**验收材料*/
	@Excel(name = "验收材料", width = 15)
    @ApiModelProperty(value = "验收材料")
    private java.lang.String file4;
	/**参会人员*/
	@Excel(name = "参会人员", width = 15)
    @ApiModelProperty(value = "参会人员")
    private java.lang.String people4;
	/**会议照片*/
	@Excel(name = "会议照片", width = 15)
    @ApiModelProperty(value = "会议照片")
    private java.lang.String video4;
	/**删除标志*/
	@Excel(name = "删除标志", width = 15)
    @ApiModelProperty(value = "删除标志")
    private java.lang.Integer delFlag;
	/**村两委会议商议*/
	@Excel(name = "村两委会议商议", width = 15)
    @ApiModelProperty(value = "村两委会议商议")
    private java.lang.String meetFile2;
	/**党员大会审议*/
	@Excel(name = "党员大会审议", width = 15)
    @ApiModelProperty(value = "党员大会审议")
    private java.lang.String meetFile3;
	/**村民会议或者村民代表会议决议*/
	@Excel(name = "村民会议或者村民代表会议决议", width = 15)
    @ApiModelProperty(value = "村民会议或者村民代表会议决议")
    private java.lang.String meetFile4;
}
