package org.jeecg.modules.smartOrgMeeting.mapper;

import java.util.List;
import org.jeecg.modules.smartOrgMeeting.entity.SmartOrgMeetingAnnex;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 组织生活会附件表
 * @Author: jeecg-boot
 * @Date:   2021-11-14
 * @Version: V1.0
 */
public interface SmartOrgMeetingAnnexMapper extends BaseMapper<SmartOrgMeetingAnnex> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<SmartOrgMeetingAnnex> selectByMainId(@Param("mainId") String mainId);
}
