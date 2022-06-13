package org.jeecg.common.system.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

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
public class VillageRelationModel implements Serializable {

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
    @Excel(name = "亲属关系", width = 15,dicCode="home_relation")
    @ApiModelProperty(value = "亲属关系")
    @Dict(dicCode = "home_relation")
    private Integer homeRelation;
}
