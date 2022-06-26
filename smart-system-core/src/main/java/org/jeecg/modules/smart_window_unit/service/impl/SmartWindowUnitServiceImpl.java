package org.jeecg.modules.smart_window_unit.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.smartReportingInformation.entity.SysRole;
import org.jeecg.modules.smart_window_people.entity.SmartWindowPeople;
import org.jeecg.modules.smart_window_people.service.ISmartWindowPeopleService;
import org.jeecg.modules.smart_window_unit.entity.SmartWindowUnit;
import org.jeecg.modules.smart_window_unit.mapper.SmartWindowUnitMapper;
import org.jeecg.modules.smart_window_unit.service.ISmartWindowUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 窗口单位
 * @Author: jeecg-boot
 * @Date:   2021-12-02
 * @Version: V1.0
 */
@Service
public class SmartWindowUnitServiceImpl extends ServiceImpl<SmartWindowUnitMapper, SmartWindowUnit> implements ISmartWindowUnitService {
    @Autowired
    private SmartWindowUnitMapper smartWindowUnitMapper;


    @Override
    public String getDepartNameById(String id) {
        return smartWindowUnitMapper.getDepartNameById(id);
    }

    public String getUserNameById(String id) {
        return smartWindowUnitMapper.getUserNameById(id);
    }

    @Override
    public List<SysRole> getUser(){
        return smartWindowUnitMapper.getUser();
    }

}
