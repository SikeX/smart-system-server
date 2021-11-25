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

        List<SmartVillageTopic> list = new ArrayList<>();

        list = smartVillageTopicMapper.getTopicListByDepartId(page,departId);

        log.info(String.valueOf(list));

        return page.setRecords(smartVillageTopicMapper.getTopicListByDepartId(page,departId));
    }

}