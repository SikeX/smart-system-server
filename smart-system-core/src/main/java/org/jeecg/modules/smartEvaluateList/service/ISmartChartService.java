package org.jeecg.modules.smartEvaluateList.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.SmartPaper.vo.SmartMyExamVo;
import org.jeecg.modules.smartEvaluateList.entity.MonthCount;
import org.jeecg.modules.smartEvaluateList.entity.SmartEvaluateWindow;
import org.jeecg.modules.smartEvaluateList.entity.TypeCount;
import org.jeecg.modules.smartEvaluateList.entity.peopleAvg;

import java.util.List;

/**
 * @Description: 阳光评廉评价
 * @Author: zxh
 * @Date:   2021-12-06
 * @Version: V1.0
 */
public interface ISmartChartService extends IService<peopleAvg> {
    /**
     *
     * 按月统计
     * @param year
     *
     */
    List<MonthCount> countByMonth(String year);

    List<TypeCount> countByGrade(String year);

    Page<peopleAvg> avgByPeople(Page<peopleAvg> page,String windowsName);

    List<TypeCount> windowsRankByCount(String year);

    List<TypeCount> windowsRankByGrade(String year);
}
