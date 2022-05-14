package org.jeecg.modules.SmartPunishPeople.service;

import org.jeecg.modules.SmartPunishPeople.entity.SmartPunishPeople;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.SmartPunishPeople.entity.TypeCount;
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
    //处分人员总数量
    Integer punishPeopleCount();
    //按处分类型统计
    List<TypeCount> punishPeopleCountByType();
    //本月即将解除处分人员数量
    Integer punishPeopleCountByMonth(String currentMonth);

    /**
     * 查询单位主要领导是否被处分
     *
     * @param departId 单位ID
     * @return
     */
    Integer countMainPeopleByDepart(String departId);
}
