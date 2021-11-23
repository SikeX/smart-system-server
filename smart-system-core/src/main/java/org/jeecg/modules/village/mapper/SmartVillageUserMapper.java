package org.jeecg.modules.village.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.village.entity.SmartVillageUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.village.entity.SmartVillageTopic;

/**
 * @Description: 村情互动-用户表
 * @Author: jeecg-boot
 * @Date:   2021-11-22
 * @Version: V1.0
 */
public interface SmartVillageUserMapper extends BaseMapper<SmartVillageUser> {
    /**
     * 查询问题集合
     * @param userId
     */
    public SmartVillageUser queryUserByUserId(String userId);

}
