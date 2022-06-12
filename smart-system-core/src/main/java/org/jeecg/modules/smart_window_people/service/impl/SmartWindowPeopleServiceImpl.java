package org.jeecg.modules.smart_window_people.service.impl;

import org.jeecg.modules.smart_window_people.entity.SmartWindowPeople;
import org.jeecg.modules.smart_window_people.mapper.SmartWindowPeopleMapper;
import org.jeecg.modules.smart_window_people.service.ISmartWindowPeopleService;
import org.jeecg.modules.smart_window_unit.mapper.SmartWindowUnitMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 窗口人员管理
 * @Author: jeecg-boot
 * @Date:   2021-12-02
 * @Version: V1.0
 */
@Service
public class SmartWindowPeopleServiceImpl extends ServiceImpl<SmartWindowPeopleMapper, SmartWindowPeople> implements ISmartWindowPeopleService {
    @Autowired
    private SmartWindowPeopleMapper smartWindowPeopleMapper;
    @Override
    public String getPidByDepartmentId(String id) {
        return smartWindowPeopleMapper.getPidByDepartmentId(id);
    }
    public String getDepartmentNameByDepartmentId(String id) {
        return smartWindowPeopleMapper.getDepartmentNameByDepartmentId(id);
    }
    public String getDepartNameById(String id) {
        return smartWindowPeopleMapper.getDepartNameById(id);
    }
}
