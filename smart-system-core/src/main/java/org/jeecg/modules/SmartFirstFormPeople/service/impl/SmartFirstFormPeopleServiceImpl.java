package org.jeecg.modules.SmartFirstFormPeople.service.impl;

import org.jeecg.modules.SmartFirstFormPeople.entity.FirstFormInfo;
import org.jeecg.modules.SmartFirstFormPeople.entity.SmartFirstFormPeople;
import org.jeecg.modules.SmartFirstFormPeople.mapper.SmartFirstFormPeopleMapper;
import org.jeecg.modules.SmartFirstFormPeople.service.ISmartFirstFormPeopleService;
import org.jeecg.modules.smartEvaluateList.entity.MonthCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 执行第一种形态人员表
 * @Author: jeecg-boot
 * @Date:   2021-11-08
 * @Version: V1.0
 */
@Service
public class SmartFirstFormPeopleServiceImpl extends ServiceImpl<SmartFirstFormPeopleMapper, SmartFirstFormPeople> implements ISmartFirstFormPeopleService {

    @Autowired
    private SmartFirstFormPeopleMapper smartFirstFormPeopleMapper;
    @Override
    public List<MonthCount> statistics(String year,String departCode) {
        return smartFirstFormPeopleMapper.statistics(year,departCode);
    }

    @Override
    public List<FirstFormInfo> sendInformation() {
        return smartFirstFormPeopleMapper.sendInformation();
    }

    @Override
    public List<String> getLeadersByDepartId(List<String> departIds) {
        return smartFirstFormPeopleMapper.getLeadersByDepartId(departIds);
    }


}
