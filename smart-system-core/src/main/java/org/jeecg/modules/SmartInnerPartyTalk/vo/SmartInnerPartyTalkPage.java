package org.jeecg.modules.SmartInnerPartyTalk.vo;

import java.util.List;
import org.jeecg.modules.SmartInnerPartyTalk.entity.SmartInnerPartyTalk;
import org.jeecg.modules.SmartInnerPartyTalk.entity.SmartInnerPartyPacpa;
import org.jeecg.modules.SmartInnerPartyTalk.entity.SmartInnerPartyAnnex;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 党内谈话表
 * @Author: jeecg-boot
 * @Date:   2021-11-05
 * @Version: V1.0
 */
@Data
@ApiModel(value="smart_inner_party_talkPage对象", description="党内谈话表")
public class SmartInnerPartyTalkPage {

	/**主键*/
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
	/**单位*/
	@Excel(name = "单位", width = 15, dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
	@ApiModelProperty(value = "单位")
    private java.lang.String deptId;
	/**会议时间*/
	@Excel(name = "会议时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "会议时间")
    private java.util.Date meetTime;
	/**会议地点*/
	@Excel(name = "会议地点", width = 15)
	@ApiModelProperty(value = "会议地点")
    private java.lang.String location;
	/**会议名称*/
	@Excel(name = "会议名称", width = 15)
	@ApiModelProperty(value = "会议名称")
    private java.lang.String name;
	/**主持人工号*/
	@Excel(name = "主持人工号", width = 15)
	@ApiModelProperty(value = "主持人工号")
    private java.lang.String hostNo;
	/**受约谈函询人工号*/
	@Excel(name = "受约谈函询人工号", width = 15)
	@ApiModelProperty(value = "受约谈函询人工号")
    private java.lang.String talkNo;
	/**受诫勉谈话人工号*/
	@Excel(name = "受诫勉谈话人工号", width = 15)
	@ApiModelProperty(value = "受诫勉谈话人工号")
    private java.lang.String inquirNo;
	/**受党纪处分人工号*/
	@Excel(name = "受党纪处分人工号", width = 15)
	@ApiModelProperty(value = "受党纪处分人工号")
    private java.lang.String punishNo;
	/**会议摘要*/
	@Excel(name = "会议摘要", width = 15)
	@ApiModelProperty(value = "会议摘要")
    private java.lang.String abs;
	/**记录人工号*/
	@Excel(name = "记录人工号", width = 15)
	@ApiModelProperty(value = "记录人工号")
    private java.lang.String recorderNo;
	/**创建人工号*/
	@Excel(name = "创建人工号", width = 15)
	@ApiModelProperty(value = "创建人工号")
    private java.lang.String createrNo;

	@ExcelCollection(name="党内谈话参与人表")
	@ApiModelProperty(value = "党内谈话参与人表")
	private List<SmartInnerPartyPacpa> smartInnerPartyPacpaList;
	@ExcelCollection(name="党内谈话附件表")
	@ApiModelProperty(value = "党内谈话附件表")
	private List<SmartInnerPartyAnnex> smartInnerPartyAnnexList;

}