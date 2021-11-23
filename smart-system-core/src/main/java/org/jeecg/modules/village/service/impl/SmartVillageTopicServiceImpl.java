package org.jeecg.modules.village.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.village.entity.SmartVillageUser;
import org.jeecg.modules.village.entity.SmartVillageTopic;
import org.jeecg.modules.village.mapper.SmartVillageTopicMapper;
import org.jeecg.modules.village.service.ISmartVillageTopicService;
import org.jeecg.modules.village.entity.SmartVillageComment;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 村情互动-村民提问表
 * @Author: jeecg-boot
 * @Date:   2021-11-22
 * @Version: V1.0
 */
@Service
public class SmartVillageTopicServiceImpl extends ServiceImpl<SmartVillageTopicMapper, SmartVillageTopic> implements ISmartVillageTopicService {

    @Override
    public List<SmartVillageTopic> queryTopicList()
    {
        List<SmartVillageTopic> topicList = this.baseMapper.queryTopicList();
        for(SmartVillageTopic smartVillageTopic : topicList){
            if(smartVillageTopic.getUserId()!=null)
            {
                SmartVillageUser smartVillageUser = this.baseMapper.queryUserByUserId(smartVillageTopic.getUserId());
                smartVillageTopic.setUser(smartVillageUser);
            }
        }
        return topicList;
    }

    @Override
    public List<SmartVillageComment> queryCommentList(String topicId)
    {
        return this.baseMapper.queryCommentList(topicId);
    }
}
