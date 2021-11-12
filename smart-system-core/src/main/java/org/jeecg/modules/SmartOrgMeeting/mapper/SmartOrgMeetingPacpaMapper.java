package org.jeecg.modules.SmartOrgMeeting.mapper;

import java.util.List;
import org.jeecg.modules.SmartOrgMeeting.entity.SmartOrgMeetingPacpa;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 组织生活会参会人员表
 * @Author: jeecg-boot
 * @Date:   2021-11-03
 * @Version: V1.0
 */
public interface SmartOrgMeetingPacpaMapper extends BaseMapper<SmartOrgMeetingPacpa> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<SmartOrgMeetingPacpa> selectByMainId(@Param("mainId") String mainId);

}
