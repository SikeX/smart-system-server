package org.jeecg.modules.interaction.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.modules.common.vo.SysDepVo;
import org.jeecg.modules.interaction.domain.SmartVillageTopic;
import org.jeecg.modules.interaction.service.SmartVillageTopicService;
import org.jeecg.modules.interaction.mapper.SmartVillageTopicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
* @author sike
* @description 针对表【smart_village_topic】的数据库操作Service实现
* @createDate 2021-11-24 15:19:18
*/
@Service
@Slf4j
public class SmartVillageTopicServiceImpl extends ServiceImpl<SmartVillageTopicMapper, SmartVillageTopic>
implements SmartVillageTopicService{

    @Resource
    SmartVillageTopicMapper smartVillageTopicMapper;

    @Autowired
    private ISysBaseAPI sysBaseAPI;

    @Override
    public Page<SmartVillageTopic> getTopicListPage(Page<SmartVillageTopic> page, String userId){

        String departId = sysBaseAPI.getDepartIdByUserId(userId);

        return page.setRecords(smartVillageTopicMapper.getTopicListByDepartId(page,departId));
    }

    @Override
    public Page<SmartVillageTopic> getVerifyTopicListPage(Page<SmartVillageTopic> page, String userId){

        String departId = sysBaseAPI.getDepartIdByUserId(userId);

        return page.setRecords(smartVillageTopicMapper.getVerifyTopicListByDepartId(page,departId));
    }

    @Override
    public Page<SmartVillageTopic> getTopicListByUserId(Page<SmartVillageTopic> page, String userId){
        log.info(String.valueOf("what "+userId));
        String departId = sysBaseAPI.getDepartIdByUserId(userId);
        List<String> subDepIds = sysBaseAPI.getSubDepIdsByDepId(departId);
        log.info(String.valueOf(subDepIds));
        return page.setRecords(smartVillageTopicMapper.getTopicListByDepIds(page, subDepIds));
    }

}
