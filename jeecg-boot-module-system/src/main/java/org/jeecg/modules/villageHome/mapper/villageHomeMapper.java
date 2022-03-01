package org.jeecg.modules.villageHome.mapper;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.interaction.domain.SmartVillageTopic;
import org.jeecg.modules.villageHome.entity.villageHome;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 乡镇户口表
 * @Author: jeecg-boot
 * @Date:   2021-12-24
 * @Version: V1.0
 */
public interface villageHomeMapper extends BaseMapper<villageHome> {
    String getHostByHomeCode(@Param("homeCode") String homeCode);
}
