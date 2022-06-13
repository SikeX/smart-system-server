package org.jeecg.modules.smartPremaritalFiling.vo;

import org.jeecg.modules.smartPremaritalFiling.entity.SmartPremaritalFiling;
import org.jeecg.modules.smartPremaritalFiling.entity.SmartPremaritalFilingApp;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 8项规定婚前报备表
 * @Author: jeecg-boot
 * @Date:   2021-11-13
 * @Version: V1.0
 */
@Data
@ApiModel(value="smart_premarital_filingPage对象", description="8项规定婚前报备表")
public class SmartPremaritalFilingPage implements Serializable {

	/**主键*/
	@ApiModelProperty(value = "主键")
	private java.lang.String id;
	/**人员工号*/
	//@Excel(name = "人员id", width = 15,dictTable = "sys_user",dicText = "realname",dicCode = "id")
	@Dict(dictTable = "sys_user",dicText = "realname",dicCode = "id")
	@ApiModelProperty(value = "人员id")
	private java.lang.String peopleId;
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
	/**单位ID*/
	@Excel(name = "单位", width = 15,dictTable ="sys_depart",dicText = "depart_name",dicCode = "id")
	@ApiModelProperty(value = "单位ID")
	private java.lang.String departId;
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
	/**配偶单位*/
	@Excel(name = "配偶单位", width = 15)
	@ApiModelProperty(value = "配偶单位")
	private java.lang.String spoUnit;
	/**配偶单位职务*/
	@Excel(name = "配偶单位职务", width = 15)
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
	/**结婚人配偶父亲姓名*/
	@Excel(name = "结婚人配偶父亲姓名", width = 15)
	@ApiModelProperty(value = "结婚人配偶父亲姓名")
	private java.lang.String marrySpoMaleName;
	/**结婚人配偶母亲姓名*/
	@Excel(name = "结婚人配偶母亲姓名", width = 15)
	@ApiModelProperty(value = "结婚人配偶母亲姓名")
	private java.lang.String marrySpoFemaleName;
	/**结婚人配偶父亲单位*/
	@Excel(name = "结婚人配偶父亲单位", width = 15)
	@ApiModelProperty(value = "结婚人配偶父亲单位")
	private java.lang.String marrySpoMaleUnit;
	/**结婚人配偶母亲单位*/
	@Excel(name = "结婚人配偶母亲单位", width = 15)
	@ApiModelProperty(value = "结婚人配偶母亲单位")
	private java.lang.String marrySpoFemaleUnit;
	/**结婚人配偶父亲职务*/
	@Excel(name = "结婚人配偶父亲职务", width = 15)
	@ApiModelProperty(value = "结婚人配偶父亲职务")
	private java.lang.String marrySpoMaleUnitPos;
	/**结婚人配偶母亲职务*/
	@Excel(name = "结婚人配偶母亲职务", width = 15)
	@ApiModelProperty(value = "结婚人配偶母亲职务")
	private java.lang.String marrySpoFemaleUnitPos;
//	/**结婚人配偶父母姓名*/
//	@Excel(name = "结婚人配偶父母姓名", width = 15)
//	@ApiModelProperty(value = "结婚人配偶父母姓名")
//	private java.lang.String marrySpoParName;
//	/**结婚人配偶父母单位*/
//	@Excel(name = "结婚人配偶父母单位", width = 15)
//	@ApiModelProperty(value = "结婚人配偶父母单位")
//	private java.lang.String marrySpoParUnit;
//	/**结婚人配偶单位职务*/
//	@Excel(name = "结婚人配偶单位职务", width = 15)
//	@ApiModelProperty(value = "结婚人配偶单位职务")
//	private java.lang.String marrySpoParUnitPos;
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
	/**删除状态*/
	@ApiModelProperty(value = "删除状态")
	private java.lang.Integer delFlag;

	/**审核状态*/
	@ApiModelProperty(value = "审核状态")
	private java.lang.String verifyStatus;
	@ApiModelProperty(value = "创建人")
	private java.lang.String createBy;
	/**附件*/
	@Excel(name = "附件", width = 15)
	@ApiModelProperty(value = "附件")
	private java.lang.String files;

	@ExcelCollection(name="8项规定婚前报备表附表")
	@ApiModelProperty(value = "8项规定婚前报备表附表")
	private List<SmartPremaritalFilingApp> smartPremaritalFilingAppList;

}
