package org.jeecg.modules.smartEvaluateMeeting.mapper;

import java.util.List;
import org.jeecg.modules.smartEvaluateMeeting.entity.SmartEvaluateMeetingPacpa;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 述责述廉参与人表
 * @Author: jeecg-boot
 * @Date:   2021-11-11
 * @Version: V1.0
 */
public interface SmartEvaluateMeetingPacpaMapper extends BaseMapper<SmartEvaluateMeetingPacpa> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<SmartEvaluateMeetingPacpa> selectByMainId(@Param("mainId") String mainId);
}
