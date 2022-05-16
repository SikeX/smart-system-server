package org.jeecg.modules.SmartPunishPeople.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.SmartPunishPeople.entity.SmartPunishPeople;
import org.jeecg.modules.SmartPunishPeople.entity.TypeCount;
import org.jeecg.modules.SmartPunishPeople.entity.punishInfo;
import org.jeecg.modules.SmartPunishPeople.mapper.SmartPunishPeopleMapper;
import org.jeecg.modules.SmartPunishPeople.service.ISmartPunishPeopleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @Description: 处分人员表
 * @Author: jeecg-boot
 * @Date:   2021-11-07
 * @Version: V1.0
 */
@Service
public class SmartPunishPeopleServiceImpl extends ServiceImpl<SmartPunishPeopleMapper, SmartPunishPeople> implements ISmartPunishPeopleService {
    @Autowired
    private SmartPunishPeopleMapper smartPunishPeopleMapper;
    //按处分类型统计
    @Override
    public List<TypeCount> punishPeopleCountByType() {
        return smartPunishPeopleMapper.punishPeopleCountByType();
    }

    @Override
    public Integer countMainPeopleByDepart(String departId) {
        return smartPunishPeopleMapper.countMainPeopleByDepart(departId);
    }

    @Override
    public List<punishInfo> sendInformation() {
        return smartPunishPeopleMapper.sendInformation();
    }

    @Override
    public List<String> getLeadersByOrgCode(String departCode) {
        return smartPunishPeopleMapper.getLeadersByOrgCode(departCode);
    }
}