package org.jeecg.modules.smartEvaluateList.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value="MonthCount", description="评价数量按月统计")
@Data
/**
 * @Description: 按月统计
 * @author: zxh
 * @date: 2021年11月18日 12:31
 */
public class MonthCountEight {
    /**
     * 月
     */
    private String month;
    /**
     * 该月份收到任务数量
     */
    private Integer count;
    /**
     * 该月份未通过任务数量
     */
    private Integer countNot;

}
