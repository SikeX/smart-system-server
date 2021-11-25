package org.jeecg.modules.village.service;

import org.jeecg.modules.village.entity.SmartVillageUser;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.village.entity.SmartVillageTopic;

import java.util.List;

/**
 * @Description: 村情互动-用户表
 * @Author: jeecg-boot
 * @Date:   2021-11-22
 * @Version: V1.0
 */
public interface ISmartVillageUserService extends IService<SmartVillageUser> {

    /**
     * 保存授权 将上次的权限和这次作比较 差异处理提高效率
     * @param userId
     */
    SmartVillageUser queryUserByUserId(String userId);
}
