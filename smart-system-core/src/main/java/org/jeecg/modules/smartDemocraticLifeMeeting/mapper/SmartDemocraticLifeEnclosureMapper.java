package org.jeecg.modules.smartDemocraticLifeMeeting.mapper;

import java.util.List;
import org.jeecg.modules.smartDemocraticLifeMeeting.entity.SmartDemocraticLifeEnclosure;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 民主生活会附件表
 * @Author: jeecg-boot
 * @Date:   2021-11-17
 * @Version: V1.0
 */
public interface SmartDemocraticLifeEnclosureMapper extends BaseMapper<SmartDemocraticLifeEnclosure> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<SmartDemocraticLifeEnclosure> selectByMainId(@Param("mainId") String mainId);
}
