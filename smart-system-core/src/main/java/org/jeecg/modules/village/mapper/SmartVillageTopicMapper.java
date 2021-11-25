package org.jeecg.modules.village.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.village.entity.SmartVillageUser;
import org.jeecg.modules.village.entity.SmartVillageTopic;
import org.jeecg.modules.village.entity.SmartVillageComment;

import java.util.List;

/**
 * @Description: 村情互动-村民提问表
 * @Author: jeecg-boot
 * @Date:   2021-11-22
 * @Version: V1.0
 */
public interface SmartVillageTopicMapper extends BaseMapper<SmartVillageTopic> {
    /**
     * 查询问题集合
     */
    public List<SmartVillageTopic> queryTopicList();
    /**
     * 查询问题集合
     * @param userId
     */
    public SmartVillageUser queryUserByUserId(String userId);
    /**
     * 查询问题集合
     * @param topicId
     */
    public List<SmartVillageComment> queryCommentList(String topicId);

}
