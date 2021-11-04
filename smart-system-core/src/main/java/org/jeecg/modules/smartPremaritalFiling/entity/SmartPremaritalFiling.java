package org.jeecg.modules.smartPremaritalFiling.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
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

/**
 * @Description: 8项规定婚前报备表
 * @Author: jeecg-boot
 * @Date:   2021-11-04
 * @Version: V1.0
 */
@ApiModel(value="smart_premarital_filing对象", description="8项规定婚前报备表")
@Data
@TableName("smart_premarital_filing")
public class SmartPremaritalFiling implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**人员工号*/
	@Excel(name = "人员工号", width = 15)
    @ApiModelProperty(value = "人员工号")
    private java.lang.String peopleNo;
	/**人员姓名*/
	@Excel(name = "人员姓名", width = 15)
    @ApiModelProperty(value = "人员姓名")
    private java.lang.String peopleName;
	/**人员性别*/
	@Excel(name = "人员性别", width = 15, dicCode = "	sex")
    @Dict(dicCode = "	sex")
    @ApiModelProperty(value = "人员性别")
    private java.lang.String peopleSex;
	/**人员年龄*/
	@Excel(name = "人员年龄", width = 15)
    @ApiModelProperty(value = "人员年龄")
    private java.lang.Integer peopleAge;
	/**政治面貌*/
	@Excel(name = "政治面貌", width = 15, dicCode = "political_status")
    @Dict(dicCode = "political_status")
    @ApiModelProperty(value = "政治面貌")
    private java.lang.String politicCou;
	/**工作单位*/
	@Excel(name = "工作单位", width = 15)
    @ApiModelProperty(value = "工作单位")
    private java.lang.String workUnit;
	/**职务*/
	@Excel(name = "职务", width = 15, dictTable = "sys_position", dicText = "name", dicCode = "code")
    @Dict(dictTable = "sys_position", dicText = "name", dicCode = "code")
    @ApiModelProperty(value = "职务")
    private java.lang.String post;
	/**职级*/
	@Excel(name = "职级", width = 15, dicCode = "position_rank")
    @Dict(dicCode = "position_rank")
    @ApiModelProperty(value = "职级")
    private java.lang.String postRank;
	/**配偶姓名*/
	@Excel(name = "配偶姓名", width = 15)
    @ApiModelProperty(value = "配偶姓名")
    private java.lang.String spoName;
	/**配偶单位职务*/
	@Excel(name = "配偶单位职务", width = 15, dictTable = "sys_position", dicText = "name", dicCode = "code")
    @Dict(dictTable = "sys_position", dicText = "name", dicCode = "code")
    @ApiModelProperty(value = "配偶单位职务")
    private java.lang.String spoUnitPos;
	/**配偶政治面貌*/
	@Excel(name = "配偶政治面貌", width = 15)
    @ApiModelProperty(value = "配偶政治面貌")
    private java.lang.String spoPoliticCou;
	/**结婚人姓名*/
	@Excel(name = "结婚人姓名", width = 15)
    @ApiModelProperty(value = "结婚人姓名")
    private java.lang.String marriedName;
	/**与本人关系*/
	@Excel(name = "与本人关系", width = 15)
    @ApiModelProperty(value = "与本人关系")
    private java.lang.String relationWithMyself;
	/**婚姻登记时间*/
	@Excel(name = "婚姻登记时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "婚姻登记时间")
    private java.util.Date marryRegistTime;
	/**婚礼时间*/
	@Excel(name = "婚礼时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "婚礼时间")
    private java.util.Date weddingTime;
	/**是否同城同地合办*/
	@Excel(name = "是否同城同地合办", width = 15)
    @ApiModelProperty(value = "是否同城同地合办")
    private java.lang.String isSameOrganized;
	/**拟宴请人数*/
	@Excel(name = "拟宴请人数", width = 15)
    @ApiModelProperty(value = "拟宴请人数")
    private java.lang.Integer guestsNumber;
	/**婚宴场所名称*/
	@Excel(name = "婚宴场所名称", width = 15)
    @ApiModelProperty(value = "婚宴场所名称")
    private java.lang.String banqPlaceName;
	/**婚宴场所地址*/
	@Excel(name = "婚宴场所地址", width = 15)
    @ApiModelProperty(value = "婚宴场所地址")
    private java.lang.String banqPlaceAddr;
	/**宴请人员范围*/
	@Excel(name = "宴请人员范围", width = 15)
    @ApiModelProperty(value = "宴请人员范围")
    private java.lang.String banqPersonScope;
	/**拟用婚礼车辆来源*/
	@Excel(name = "拟用婚礼车辆来源", width = 15)
    @ApiModelProperty(value = "拟用婚礼车辆来源")
    private java.lang.String proCarsSource;
	/**拟用婚礼车辆数量*/
	@Excel(name = "拟用婚礼车辆数量", width = 15)
    @ApiModelProperty(value = "拟用婚礼车辆数量")
    private java.lang.String proCarsNum;
	/**结婚人配偶姓名*/
	@Excel(name = "结婚人配偶姓名", width = 15)
    @ApiModelProperty(value = "结婚人配偶姓名")
    private java.lang.String marrySpoName;
	/**结婚人配偶单位*/
	@Excel(name = "结婚人配偶单位", width = 15)
    @ApiModelProperty(value = "结婚人配偶单位")
    private java.lang.String marrySpoUnit;
	/**结婚人配偶单位职务*/
	@Excel(name = "结婚人配偶单位职务", width = 15, dictTable = "sys_position", dicText = "name", dicCode = "code")
    @Dict(dictTable = "sys_position", dicText = "name", dicCode = "code")
    @ApiModelProperty(value = "结婚人配偶单位职务")
    private java.lang.String marrySpoUnitPos;
	/**结婚人配偶父母姓名*/
	@Excel(name = "结婚人配偶父母姓名", width = 15)
    @ApiModelProperty(value = "结婚人配偶父母姓名")
    private java.lang.String marrySpoParName;
	/**结婚人配偶父母单位职务*/
	@Excel(name = "结婚人配偶父母单位职务", width = 15, dictTable = "sys_position", dicText = "name", dicCode = "code")
    @Dict(dictTable = "sys_position", dicText = "name", dicCode = "code")
    @ApiModelProperty(value = "结婚人配偶父母单位职务")
    private java.lang.String marrySpoParUnitPos;
	/**其他需要说明的事情*/
	@Excel(name = "其他需要说明的事情", width = 15)
    @ApiModelProperty(value = "其他需要说明的事情")
    private java.lang.String otherMattersExp;
	/**报告时间*/
	@Excel(name = "报告时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "报告时间")
    private java.util.Date reportTime;
	/**联系电话*/
	@Excel(name = "联系电话", width = 15)
    @ApiModelProperty(value = "联系电话")
    private java.lang.String contactNumber;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
}
