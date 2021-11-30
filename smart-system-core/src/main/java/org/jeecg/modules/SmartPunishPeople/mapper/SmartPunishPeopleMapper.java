package org.jeecg.modules.SmartPunishPeople.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.SmartPunishPeople.entity.SmartPunishPeople;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.SmartPunishPeople.entity.TypeCount;

/**
 * @Description: 处分人员表
 * @Author: jeecg-boot
 * @Date:   2021-11-07
 * @Version: V1.0
 */
public interface SmartPunishPeopleMapper extends BaseMapper<SmartPunishPeople> {
    //处分人员总数量
    Integer punishPeopleCount();
    //按处分类型统计
    List<TypeCount> punishPeopleCountByType();
    //本月即将解除处分人员数量
    Integer punishPeopleCountByMonth(String currentMonth);
}
