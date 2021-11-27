package org.jeecg.modules.smartOrgMeeting.service.impl;

import org.jeecg.modules.smartOrgMeeting.entity.SmartOrgMeeting;
import org.jeecg.modules.smartOrgMeeting.entity.SmartOrgMeetingPacpa;
import org.jeecg.modules.smartOrgMeeting.entity.SmartOrgMeetingAnnex;
import org.jeecg.modules.smartOrgMeeting.mapper.SmartOrgMeetingPacpaMapper;
import org.jeecg.modules.smartOrgMeeting.mapper.SmartOrgMeetingAnnexMapper;
import org.jeecg.modules.smartOrgMeeting.mapper.SmartOrgMeetingMapper;
import org.jeecg.modules.smartOrgMeeting.service.ISmartOrgMeetingService;
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
 * @Date:   2021-11-14
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
	public void saveMain(SmartOrgMeeting smartOrgMeeting, List<SmartOrgMeetingPacpa> smartOrgMeetingPacpaList,List<SmartOrgMeetingAnnex> smartOrgMeetingAnnexList) {
		smartOrgMeetingMapper.insert(smartOrgMeeting);
		if(smartOrgMeetingPacpaList!=null && smartOrgMeetingPacpaList.size()>0) {
			for(SmartOrgMeetingPacpa entity:smartOrgMeetingPacpaList) {
				//外键设置
				entity.setParentId(smartOrgMeeting.getId());
				smartOrgMeetingPacpaMapper.insert(entity);
			}
		}
		if(smartOrgMeetingAnnexList!=null && smartOrgMeetingAnnexList.size()>0) {
			for(SmartOrgMeetingAnnex entity:smartOrgMeetingAnnexList) {
				//外键设置
				entity.setParentId(smartOrgMeeting.getId());
				smartOrgMeetingAnnexMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(SmartOrgMeeting smartOrgMeeting,List<SmartOrgMeetingPacpa> smartOrgMeetingPacpaList,List<SmartOrgMeetingAnnex> smartOrgMeetingAnnexList) {
		smartOrgMeetingMapper.updateById(smartOrgMeeting);
		
		//1.先删除子表数据
		smartOrgMeetingPacpaMapper.deleteByMainId(smartOrgMeeting.getId());
		smartOrgMeetingAnnexMapper.deleteByMainId(smartOrgMeeting.getId());
		
		//2.子表数据重新插入
		if(smartOrgMeetingPacpaList!=null && smartOrgMeetingPacpaList.size()>0) {
			for(SmartOrgMeetingPacpa entity:smartOrgMeetingPacpaList) {
				//外键设置
				entity.setParentId(smartOrgMeeting.getId());
				smartOrgMeetingPacpaMapper.insert(entity);
			}
		}
		if(smartOrgMeetingAnnexList!=null && smartOrgMeetingAnnexList.size()>0) {
			for(SmartOrgMeetingAnnex entity:smartOrgMeetingAnnexList) {
				//外键设置
				entity.setParentId(smartOrgMeeting.getId());
				smartOrgMeetingAnnexMapper.insert(entity);
			}
		}
	}

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
