package org.jeecg.modules.smartEvaluateList.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.smartEvaluateList.entity.MonthCount;
import org.jeecg.modules.smartEvaluateList.entity.SmartEvaluateWindow;
import org.jeecg.modules.smartEvaluateList.entity.TypeCount;
import org.jeecg.modules.smartEvaluateList.entity.peopleAvg;
import org.jeecg.modules.smartEvaluateList.mapper.SmartChartMapper;
import org.jeecg.modules.smartEvaluateList.mapper.SmartEvaluateWindowMapper;
import org.jeecg.modules.smartEvaluateList.service.ISmartChartService;
import org.jeecg.modules.smartEvaluateList.service.ISmartEvaluateWindowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 阳光评廉评价
 * @Author: zxh
 * @Date:   2021-12-06
 * @Version: V1.0
 */
@Service
public class SmartChartServiceImpl extends ServiceImpl<SmartChartMapper, peopleAvg> implements ISmartChartService {
   @Autowired SmartChartMapper smartChartMapper;
    /**
     *
     * 按月统计
     * @param year
     *
     */
    @Override
    public List<MonthCount> countByMonth(String year){
        return smartChartMapper.countByMonth(year);
    }

    @Override
    public List<TypeCount> countByGrade(String year) {
        return smartChartMapper.countByGrade(year);
    }

    @Override
    public Page<peopleAvg> avgByPeople(Page<peopleAvg> page, String windowsName){
        return page.setRecords(smartChartMapper.avgByPeople(page,windowsName));
    }

    @Override
    public List<TypeCount> windowsRankByCount(String year) {
        return smartChartMapper.windowsRankByCount(year);
    }

    @Override
    public List<TypeCount> windowsRankByGrade(String year) {
        return smartChartMapper.windowsRankByGrade(year);
    }

    @Override
    public IPage<peopleAvg> windowsByGrade(Page<peopleAvg> page, String windowsName) {
        return page.setRecords(smartChartMapper.windowsByGrade(page,windowsName));
    }
}
