package org.jeecg.modules.smartEvaluateList.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.smartEvaluateList.entity.MonthCount;
import org.jeecg.modules.smartEvaluateList.entity.SmartEvaluateWindow;
import org.jeecg.modules.smartEvaluateList.entity.TypeCount;
import org.jeecg.modules.smartEvaluateList.entity.peopleAvg;

import java.util.List;

/**
 * @Description: 阳光评廉统计
 * @Author: zxh
 * @Date:   2021-11-09
 * @Version: V1.0
 */
public interface SmartChartMapper extends BaseMapper<peopleAvg> {
    /**
    *
    * 按月统计
     * @param year
    *
    */
    List<MonthCount> countByMonth(String year);

    List<TypeCount> countByGrade(String year);

    List<peopleAvg> avgByPeople(Page<peopleAvg> page, String windowsName);
}
