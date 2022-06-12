package org.jeecg.modules.SmartFirstFormPeople.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value="MonthCount", description="评价数量按月统计")
@Data
/**
 * @Description: 按月统计
 * @author: zxh
 * @date: 2021年11月18日 12:31
 */
public class MonthCount {
    /**
     * 月
     */
    private String month;
    /**
     * 该月份收到评价数量
     */
    private Integer count;

}
