package org.jeecg.modules.smartPostFuneralReport.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
 * @Description: 丧事事后报备表
 * @Author: jeecg-boot
 * @Date:   2022-02-26
 * @Version: V1.0
 */
@Data
@TableName("smart_post_funeral_report")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="smart_post_funeral_report对象", description="丧事事后报备表")
public class SmartPostFuneralReport implements Serializable {
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
	/**报备人ID*/
	@Excel(name = "报备人ID", width = 15)
    @ApiModelProperty(value = "报备人ID")
    @Dict(dictTable ="sys_user",dicText = "realname",dicCode = "id")
    private String peopleId;
	/**性别*/
	@Excel(name = "性别", width = 15)
    @ApiModelProperty(value = "性别")
    private String reportSex;
	/**年龄*/
	@Excel(name = "年龄", width = 15)
    @ApiModelProperty(value = "年龄")
    private String reportAge;
	/**政治面貌*/
	@Excel(name = "政治面貌", width = 15)
    @ApiModelProperty(value = "政治面貌")
    private String politics;
	/**工作单位*/
	@Excel(name = "工作单位", width = 15)
    @ApiModelProperty(value = "工作单位")
    @Dict(dictTable ="sys_depart",dicText = "depart_name",dicCode = "id")
    private String departId;
	/**职务*/
	@Excel(name = "职务", width = 15)
    @ApiModelProperty(value = "职务")
    private String zhiwu;
	/**职级*/
	@Excel(name = "职级", width = 15)
    @ApiModelProperty(value = "职级")
    private String zhiji;
	/**联系电话*/
	@Excel(name = "联系电话", width = 15)
    @ApiModelProperty(value = "联系电话")
    private String phone;
	/**逝世人姓名*/
	@Excel(name = "逝世人姓名", width = 15)
    @ApiModelProperty(value = "逝世人姓名")
    private String theDeadName;
	/**与本人关系*/
	@Excel(name = "与本人关系", width = 15)
    @ApiModelProperty(value = "与本人关系")
    private String relation;
	/**葬礼时间*/
	@Excel(name = "葬礼时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "葬礼时间")
    private Date funeralTime;
	/**葬礼地点*/
	@Excel(name = "葬礼地点", width = 15)
    @ApiModelProperty(value = "葬礼地点")
    private String funeralAddress;
	/**宴请人数*/
	@Excel(name = "宴请人数", width = 15)
    @ApiModelProperty(value = "宴请人数")
    private String peopleAccount;
	/**宴请场所名称*/
	@Excel(name = "宴请场所名称", width = 15)
    @ApiModelProperty(value = "宴请场所名称")
    private String groundName;
	/**宴请场所地址*/
	@Excel(name = "宴请场所地址", width = 15)
    @ApiModelProperty(value = "宴请场所地址")
    private String address;
	/**宴请人员范围*/
	@Excel(name = "宴请人员范围", width = 15)
    @ApiModelProperty(value = "宴请人员范围")
    private String peopleType;
	/**葬礼用车数量*/
	@Excel(name = "葬礼用车数量", width = 15)
    @ApiModelProperty(value = "葬礼用车数量")
    private String carAccount;
	/**其中公车数量*/
	@Excel(name = "其中公车数量", width = 15)
    @ApiModelProperty(value = "其中公车数量")
    private String publicCarAccount;
	/**不符合规定收受礼金（元）*/
	@Excel(name = "不符合规定收受礼金（元）", width = 15)
    @ApiModelProperty(value = "不符合规定收受礼金（元）")
    private String unusualMoney;
	/**不符合规定收受礼品件数*/
	@Excel(name = "不符合规定收受礼品件数", width = 15)
    @ApiModelProperty(value = "不符合规定收受礼品件数")
    private String unusualCount;
	/**违规收受礼金礼品处置情况*/
	@Excel(name = "违规收受礼金礼品处置情况", width = 15)
    @ApiModelProperty(value = "违规收受礼金礼品处置情况")
    private String punishContent;
	/**有否其他违规情况*/
	@Excel(name = "有否其他违规情况", width = 15)
    @ApiModelProperty(value = "有否其他违规情况")
    private String isElse;
	/**其他需要说明事项*/
	@Excel(name = "其他需要说明事项", width = 15)
    @ApiModelProperty(value = "其他需要说明事项")
    private String elseState;
	/**删除状态*/
    @TableLogic
	@Excel(name = "删除状态", width = 15)
    @ApiModelProperty(value = "删除状态")
    private Integer delFlag;
	/**报备时间*/
	@Excel(name = "报备时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "报备时间")
    private Date reportTime;
    /**审核状态*/
    @ApiModelProperty(value = "审核状态")
    private String verifyStatus;
    /**口头报备ID*/
    private String preId;
    /**附件*/
    @Excel(name = "附件", width = 15)
    @ApiModelProperty(value = "附件")
    private java.lang.String files;
}
