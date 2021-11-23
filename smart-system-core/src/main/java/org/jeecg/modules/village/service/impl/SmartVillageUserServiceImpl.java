package org.jeecg.modules.village.service.impl;

import org.jeecg.modules.village.entity.SmartVillageUser;
import org.jeecg.modules.village.mapper.SmartVillageUserMapper;
import org.jeecg.modules.village.service.ISmartVillageUserService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 村情互动-用户表
 * @Author: jeecg-boot
 * @Date:   2021-11-22
 * @Version: V1.0
 */
@Service
public class SmartVillageUserServiceImpl extends ServiceImpl<SmartVillageUserMapper, SmartVillageUser> implements ISmartVillageUserService {

    @Override
    public SmartVillageUser queryUserByUserId(String userId) {
        System.out.println(this.baseMapper.queryUserByUserId(userId));
        return this.baseMapper.queryUserByUserId(userId);
    }
}
