package org.jeecg.modules.interaction.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.interaction.domain.SmartVillageTopic;

import java.util.List;


/**
* @author sike
* @description 针对表【smart_village_topic】的数据库操作Mapper
* @createDate 2021-11-24 15:19:18
* @Entity org.jeecg.modules.interaction.domain.SmartVillageTopic
*/
public interface SmartVillageTopicMapper extends BaseMapper<SmartVillageTopic> {

    // 通过用户id获取话题列表
    List<SmartVillageTopic> getTopicListByDepartId(Page<SmartVillageTopic> page, @Param("departId") String departId);

    List<SmartVillageTopic> getVerifyTopicListByDepartId(Page<SmartVillageTopic> page, @Param("departId") String departId);

    List<SmartVillageTopic> getTopicListByDepIds(Page<SmartVillageTopic> page, @Param("depIds") List<String> depIds);


}
