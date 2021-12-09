package org.jeecg.modules.smartEvaluateList.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;

@ApiModel(value="TypeCount", description="评分类型统计")
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

    private String keyName;
    /**
     * 该类型数量
     */
    private Integer value;

}
