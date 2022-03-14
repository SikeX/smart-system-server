package org.jeecg.modules.villageHome.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * @Description: 乡镇户口表
 * @Author: jeecg-boot
 * @Date:   2021-12-24
 * @Version: V1.0
 */
@Data
@TableName("smart_village_home")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="smart_village_home对象", description="乡镇户口表")
public class vHome implements Serializable {
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
    /**所属镇*/

    @Excel(name = "所属镇", width = 15,dictTable = "sys_depart",dicText = "depart_name",dicCode = "id")
    @ApiModelProperty(value = "所属镇")
    private String zhenId;
	/**所属乡*/
	@Excel(name = "所属村", width = 15,dictTable = "sys_depart",dicText = "depart_name",dicCode = "id")
    @ApiModelProperty(value = "所属村")
    private String departId;
	/**户籍编号*/
	@Excel(name = "户籍编号", width = 15)
    @ApiModelProperty(value = "户籍编号")
    private String homeCode;
	/**户主姓*/
	@Excel(name = "户主姓", width = 15)
    @ApiModelProperty(value = "户主姓")
    private String homeSurname;
	/**户主*/
	@Excel(name = "身份证号", width = 15)
    @ApiModelProperty(value = "户主")
    private String idnumber;
	/**家庭地址*/
	@Excel(name = "家庭地址", width = 15)
    @ApiModelProperty(value = "家庭地址")
    private String address;

    private String realname;

    private String phone;

    private List<SysUser> userList;

    private String role;
}
