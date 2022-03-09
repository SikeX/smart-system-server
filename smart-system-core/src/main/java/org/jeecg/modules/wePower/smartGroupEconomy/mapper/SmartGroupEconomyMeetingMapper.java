package org.jeecg.modules.wePower.smartGroupEconomy.mapper;

import java.util.List;
import org.jeecg.modules.wePower.smartGroupEconomy.entity.SmartGroupEconomyMeeting;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 集体经济组织会议
 * @Author: jeecg-boot
 * @Date:   2022-03-06
 * @Version: V1.0
 */
public interface SmartGroupEconomyMeetingMapper extends BaseMapper<SmartGroupEconomyMeeting> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<SmartGroupEconomyMeeting> selectByMainId(@Param("mainId") String mainId);

}
