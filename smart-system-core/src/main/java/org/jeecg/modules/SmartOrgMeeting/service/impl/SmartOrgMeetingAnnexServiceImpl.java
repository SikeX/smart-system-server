package org.jeecg.modules.SmartOrgMeeting.service.impl;

import org.jeecg.modules.SmartOrgMeeting.entity.SmartOrgMeetingAnnex;
import org.jeecg.modules.SmartOrgMeeting.mapper.SmartOrgMeetingAnnexMapper;
import org.jeecg.modules.SmartOrgMeeting.service.ISmartOrgMeetingAnnexService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 组织生活会附件表
 * @Author: jeecg-boot
 * @Date:   2021-11-03
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
