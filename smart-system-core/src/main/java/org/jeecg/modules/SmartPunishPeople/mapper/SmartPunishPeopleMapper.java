package org.jeecg.modules.SmartPunishPeople.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.SmartPunishPeople.entity.SmartPunishPeople;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.SmartPunishPeople.entity.TypeCount;
import org.jeecg.modules.SmartPunishPeople.entity.punishInfo;

/**
 * @Description: 处分人员表
 * @Author: jeecg-boot
 * @Date:   2021-11-07
 * @Version: V1.0
 */
public interface SmartPunishPeopleMapper extends BaseMapper<SmartPunishPeople> {
    //按处分类型统计
    List<TypeCount> punishPeopleCountByType();

    /**
     * 查询单位主要领导是否被处分
     *
     * @param departId 单位ID
     * @return
     */
    Integer countMainPeopleByDepart(@Param("departId") String departId);

    List<punishInfo> sendInformation();

    List<String> getLeadersByDepartId(@Param("departIds") List<String> departIds);
}
