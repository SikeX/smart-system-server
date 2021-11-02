package org.jeecg.modules.smartEvaluateMeeting.mapper;

import java.util.List;
import org.jeecg.modules.smartEvaluateMeeting.entity.SmartEvaluateMeetingAnnex;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 述责述廉附件表
 * @Author: jeecg-boot
 * @Date:   2021-11-02
 * @Version: V1.0
 */
public interface SmartEvaluateMeetingAnnexMapper extends BaseMapper<SmartEvaluateMeetingAnnex> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<SmartEvaluateMeetingAnnex> selectByMainId(@Param("mainId") String mainId);
}
