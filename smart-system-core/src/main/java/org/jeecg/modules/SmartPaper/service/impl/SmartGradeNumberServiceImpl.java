package org.jeecg.modules.SmartPaper.service.impl;

import org.jeecg.modules.SmartPaper.entity.SmartGradeNumber;
import org.jeecg.modules.SmartPaper.mapper.SmartGradeNumberMapper;
import org.jeecg.modules.SmartPaper.service.ISmartGradeNumberService;
import org.jeecg.modules.SmartPunishPeople.mapper.SmartPunishPeopleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 成绩分布人数表
 * @Author: jeecg-boot
 * @Date:   2021-11-24
 * @Version: V1.0
 */
@Service
public class SmartGradeNumberServiceImpl extends ServiceImpl<SmartGradeNumberMapper, SmartGradeNumber> implements ISmartGradeNumberService {
    @Autowired
    private SmartGradeNumberMapper smartGradeNumberMapper;
    //优秀总数量
    @Override
    public Integer excellentCount(double excellent_line) {
        return smartGradeNumberMapper.excellentCount(excellent_line);
    }
    //良好总数量
    @Override
    public Integer goodCount(double good_line) {
        return smartGradeNumberMapper.goodCount(good_line);
    }
    //及格总数量
    @Override
    public Integer passCount() {
        return smartGradeNumberMapper.passCount();
    }
    //不及格总数量
    @Override
    public Integer failCount() {
        return smartGradeNumberMapper.failCount();
    }
}
