package org.jeecg.modules.SmartPunishPeople.service;

import org.jeecg.modules.SmartPunishPeople.entity.SmartPunishPeople;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.SmartPunishPeople.entity.TypeCount;
import org.jeecg.modules.SmartPunishPeople.entity.punishInfo;
import org.jeecg.modules.SmartPunishPeople.mapper.SmartPunishPeopleMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

/**
 * @Description: 处分人员表
 * @Author: jeecg-boot
 * @Date:   2021-11-07
 * @Version: V1.0
 */
public interface ISmartPunishPeopleService extends IService<SmartPunishPeople> {

    //按处分类型统计
    List<TypeCount> punishPeopleCountByType();

    /**
     * 查询单位主要领导是否被处分
     *
     * @param departId 单位ID
     * @return
     */
    Integer countMainPeopleByDepart(String departId);

    List<punishInfo> sendInformation();

    List<String> getLeadersByDepartId(List<String> departIds);
}
