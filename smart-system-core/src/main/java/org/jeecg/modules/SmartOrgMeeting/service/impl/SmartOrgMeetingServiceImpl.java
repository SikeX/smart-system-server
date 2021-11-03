package org.jeecg.modules.SmartOrgMeeting.service.impl;

import org.jeecg.modules.SmartOrgMeeting.entity.SmartOrgMeeting;
import org.jeecg.modules.SmartOrgMeeting.entity.SmartOrgMeetingPacpa;
import org.jeecg.modules.SmartOrgMeeting.entity.SmartOrgMeetingAnnex;
import org.jeecg.modules.SmartOrgMeeting.mapper.SmartOrgMeetingPacpaMapper;
import org.jeecg.modules.SmartOrgMeeting.mapper.SmartOrgMeetingAnnexMapper;
import org.jeecg.modules.SmartOrgMeeting.mapper.SmartOrgMeetingMapper;
import org.jeecg.modules.SmartOrgMeeting.service.ISmartOrgMeetingService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 组织生活会
 * @Author: jeecg-boot
 * @Date:   2021-11-03
 * @Version: V1.0
 */
@Service
public class SmartOrgMeetingServiceImpl extends ServiceImpl<SmartOrgMeetingMapper, SmartOrgMeeting> implements ISmartOrgMeetingService {

	@Autowired
	private SmartOrgMeetingMapper smartOrgMeetingMapper;
	@Autowired
	private SmartOrgMeetingPacpaMapper smartOrgMeetingPacpaMapper;
	@Autowired
	private SmartOrgMeetingAnnexMapper smartOrgMeetingAnnexMapper;
	
	@Override
	@Transactional
	public void delMain(String id) {
		smartOrgMeetingPacpaMapper.deleteByMainId(id);
		smartOrgMeetingAnnexMapper.deleteByMainId(id);
		smartOrgMeetingMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			smartOrgMeetingPacpaMapper.deleteByMainId(id.toString());
			smartOrgMeetingAnnexMapper.deleteByMainId(id.toString());
			smartOrgMeetingMapper.deleteById(id);
		}
	}
	
}
