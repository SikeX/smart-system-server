package org.jeecg.modules.smartOrgMeeting.service.impl;

import org.jeecg.modules.smartOrgMeeting.entity.SmartOrgMeetingPacpa;
import org.jeecg.modules.smartOrgMeeting.mapper.SmartOrgMeetingPacpaMapper;
import org.jeecg.modules.smartOrgMeeting.service.ISmartOrgMeetingPacpaService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 组织生活会参会人员表
 * @Author: jeecg-boot
 * @Date:   2021-11-11
 * @Version: V1.0
 */
@Service
public class SmartOrgMeetingPacpaServiceImpl extends ServiceImpl<SmartOrgMeetingPacpaMapper, SmartOrgMeetingPacpa> implements ISmartOrgMeetingPacpaService {
	
	@Autowired
	private SmartOrgMeetingPacpaMapper smartOrgMeetingPacpaMapper;
	
	@Override
	public List<SmartOrgMeetingPacpa> selectByMainId(String mainId) {
		return smartOrgMeetingPacpaMapper.selectByMainId(mainId);
	}
}
