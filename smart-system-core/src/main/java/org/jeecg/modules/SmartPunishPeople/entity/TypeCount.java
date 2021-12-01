package org.jeecg.modules.SmartPunishPeople.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;

@ApiModel(value="TypeCount", description="处分人员类型统计")
@Data
/**
 * @Description: TODO
 * @author: scott
 * @date: 2021年11月18日 12:31
 */
public class TypeCount {
    /**
     * 类型
     */
    @ApiModelProperty(value = "处分类型")
    @Dict(dicCode = "punish_type")
    private String type;
    /**
     * 该类型人员数量
     */
    private Integer value;

}
