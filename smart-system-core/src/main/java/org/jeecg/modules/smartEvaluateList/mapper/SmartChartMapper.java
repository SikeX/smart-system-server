package org.jeecg.modules.smartEvaluateList.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.smartEvaluateList.entity.MonthCount;
import org.jeecg.modules.smartEvaluateList.entity.MonthCountEight;
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
    List<MonthCount> countByMonth(@Param("year")String year);
    /**
     *
     * 按月统计
     * @param year
     *
     */
    List<MonthCountEight> countEight(@Param("year")String year);
    /**
     *
     * 按评分统计
     * @param year
     *
     */
    List<TypeCount> countByGrade(@Param("year") String year);
    /**
     *
     * 按评分统计
     * @param year
     *
     */
    List<TypeCount> countByType(@Param("year") String year);
    /**
     *
     * 按人员统计人均分
     *
     *
     */
    List<peopleAvg> avgByPeople(@Param("page") Page<peopleAvg> page, @Param("windowsName") String windowsName);
    /**
     *
     * 窗口评价次数排名
     *
     *
     */
    List<TypeCount> windowsRankByCount(@Param("year") String year);
    /**
     *
     * 窗口评分次数排名
     *
     *
     */
    List<TypeCount> windowsRankByGrade(@Param("year") String year);

    List<peopleAvg> windowsByGrade(@Param("page") Page<peopleAvg> page, @Param("windowsName") String windowsName);
}
