package org.jeecg.modules.village.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.village.entity.SmartVillageTopic;
import org.jeecg.modules.village.entity.SmartVillageComment;

import java.util.List;

/**
 * @Description: 村情互动-村民提问表
 * @Author: jeecg-boot
 * @Date:   2021-11-22
 * @Version: V1.0
 */
public interface ISmartVillageTopicService extends IService<SmartVillageTopic> {

    /**
     * 保存授权 将上次的权限和这次作比较 差异处理提高效率
     */
    public List<SmartVillageTopic> queryTopicList();
    /**
     * 保存授权 将上次的权限和这次作比较 差异处理提高效率
     * @param topicId
     */
    public List<SmartVillageComment> queryCommentList(String topicId);
}
