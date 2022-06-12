package org.jeecg.modules.interaction.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.interaction.domain.SmartVillageTopic;

import java.util.List;

/**
* @author sike
* @description 针对表【smart_village_topic】的数据库操作Service
* @createDate 2021-11-24 15:19:18
*/
public interface SmartVillageTopicService extends IService<SmartVillageTopic> {

    public Page<SmartVillageTopic> getTopicListPage(Page<SmartVillageTopic> page, String userId);

    public Page<SmartVillageTopic> getVerifyTopicListPage(Page<SmartVillageTopic> page, String userId);

    public Page<SmartVillageTopic> getTopicListByUserId(Page<SmartVillageTopic> page, String userId);

}
