package org.jeecg.modules.smartPostFuneralReport.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @Author scott
 * @since 2018-12-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 真实姓名
     */
    @Excel(name = "真实姓名", width = 15)
    private String realname;

    /**
     * 生日
     */
    @Excel(name = "出生日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    private String age;
    /**
     * 性别（1：男 2：女）
     */
    @Excel(name = "性别", width = 15,dicCode="sex")
    @Dict(dicCode = "sex")
    private Integer sex;

    /**
     * 电话
     */
    @TableField(value = "phone",updateStrategy = FieldStrategy.IGNORED)
    @Excel(name = "手机号码", width = 15)
    private String phone;
    /**
     * 部门code(当前选择登录部门)
     */
    @Excel(name="单位",width = 15,dictTable ="sys_depart",dicText = "depart_name",dicCode = "org_code")
    @Dict(dictTable ="sys_depart",dicText = "depart_name",dicCode = "org_code")
    private String orgCode;

    /**
     * 职务，关联职务表
     */
    @Excel(name = "职务", width = 15,dictTable ="sys_position",dicText = "name",dicCode = "code")
    @Dict(dictTable ="sys_position",dicText = "name",dicCode = "code")
    private String post;

    /**
     * 职级
     */
    @Excel(name = "职级", width = 15,dicCode="position_rank")
    @ApiModelProperty(value = "职级")
    @Dict(dicCode = "position_rank")
    private String positionRank;


    /**
     * 政治面貌
     */
    @Excel(name = "政治面貌", width = 15,dicCode="political_status")
    @ApiModelProperty(value = "政治面貌")
    @Dict(dicCode = "political_status")
    private String politicalStatus;

}
