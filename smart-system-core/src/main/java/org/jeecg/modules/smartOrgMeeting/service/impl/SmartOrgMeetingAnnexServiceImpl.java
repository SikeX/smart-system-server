package org.jeecg.modules.smartOrgMeeting.service.impl;

import org.jeecg.modules.smartOrgMeeting.entity.SmartOrgMeetingAnnex;
import org.jeecg.modules.smartOrgMeeting.mapper.SmartOrgMeetingAnnexMapper;
import org.jeecg.modules.smartOrgMeeting.service.ISmartOrgMeetingAnnexService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 组织生活会附件表
 * @Author: jeecg-boot
 * @Date:   2021-11-14
 * @Version: V1.0
 */
@Service
public class SmartOrgMeetingAnnexServiceImpl extends ServiceImpl<SmartOrgMeetingAnnexMapper, SmartOrgMeetingAnnex> implements ISmartOrgMeetingAnnexService {
	
	@Autowired
	private SmartOrgMeetingAnnexMapper smartOrgMeetingAnnexMapper;
	
	@Override
	public List<SmartOrgMeetingAnnex> selectByMainId(String mainId) {
		return smartOrgMeetingAnnexMapper.selectByMainId(mainId);
	}
}
