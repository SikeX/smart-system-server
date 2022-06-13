package org.jeecg.modules.smartDemocraticLifeMeeting.mapper;

import java.util.List;
import org.jeecg.modules.smartDemocraticLifeMeeting.entity.SmartDemocraticLifePeople;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 民主生活参会人员表
 * @Author: jeecg-boot
 * @Date:   2022-02-26
 * @Version: V1.0
 */
public interface SmartDemocraticLifePeopleMapper extends BaseMapper<SmartDemocraticLifePeople> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<SmartDemocraticLifePeople> selectByMainId(@Param("mainId") String mainId);
}
