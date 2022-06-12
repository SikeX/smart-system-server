package org.jeecg.modules.villageHome.mapper;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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

    @Select("select * from smart_village_home where home_code = #{homeCode}")
    villageHome getByHomeCode(@Param("homeCode") String homeCode);
}
