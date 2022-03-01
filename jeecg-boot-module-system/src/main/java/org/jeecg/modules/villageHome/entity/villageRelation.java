package org.jeecg.modules.villageHome.entity;

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
@TableName("smart_village_relation")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class villageRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 真实姓名
     */
    @Excel(name = "户籍编号", width = 15)
    @ApiModelProperty(value = "户籍编号")
    private String homeCode;

    /**
     * 登录账号
     */
    @Excel(name = "户主身份证号", width = 15)
    @ApiModelProperty(value = "户主身份证号")
    private String hostIdnumber;

    /**
     * 真实姓名
     */
    @Excel(name = "身份证号", width = 15)
    @ApiModelProperty(value = "身份证号")
    private String idnumber;

    /**
     * 户籍关系
     */
    @Excel(name = "亲属关系", width = 15)
    @ApiModelProperty(value = "亲属关系")
    @Dict(dicCode = "home_relation")
    private Integer homeRelation;
}
