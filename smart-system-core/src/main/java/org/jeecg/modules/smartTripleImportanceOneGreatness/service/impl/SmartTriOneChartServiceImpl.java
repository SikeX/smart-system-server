package org.jeecg.modules.smartTripleImportanceOneGreatness.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.smartTripleImportanceOneGreatness.entity.TypeCount;
import org.jeecg.modules.smartTripleImportanceOneGreatness.mapper.SmartTriOneChartMapper;
import org.jeecg.modules.smartTripleImportanceOneGreatness.service.ISmartTriOneChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 三重一大统计
 * @Author: zxh
 * @Date:   2021-12-06
 * @Version: V1.0
 */
@Service
public class SmartTriOneChartServiceImpl extends ServiceImpl<SmartTriOneChartMapper, TypeCount> implements ISmartTriOneChartService {

    @Autowired
    private SmartTriOneChartMapper smartTriOneChartMapper;
    @Override
    public List<TypeCount> countByVerifyStatus() {
        return smartTriOneChartMapper.countByVerifyStatus();
    }
}
