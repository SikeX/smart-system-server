package org.jeecg.modules.smartDemocraticLifeMeeting.service.impl;

import org.jeecg.modules.smartDemocraticLifeMeeting.entity.SmartDemocraticLifeMeeting;
import org.jeecg.modules.smartDemocraticLifeMeeting.entity.SmartDemocraticLifePeople;
import org.jeecg.modules.smartDemocraticLifeMeeting.entity.SmartDemocraticLifeEnclosure;
import org.jeecg.modules.smartDemocraticLifeMeeting.mapper.SmartDemocraticLifePeopleMapper;
import org.jeecg.modules.smartDemocraticLifeMeeting.mapper.SmartDemocraticLifeEnclosureMapper;
import org.jeecg.modules.smartDemocraticLifeMeeting.mapper.SmartDemocraticLifeMeetingMapper;
import org.jeecg.modules.smartDemocraticLifeMeeting.service.ISmartDemocraticLifeMeetingService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 民主生活会表
 * @Author: jeecg-boot
 * @Date:   2021-11-17
 * @Version: V1.0
 */
@Service
public class SmartDemocraticLifeMeetingServiceImpl extends ServiceImpl<SmartDemocraticLifeMeetingMapper, SmartDemocraticLifeMeeting> implements ISmartDemocraticLifeMeetingService {

	@Autowired
	private SmartDemocraticLifeMeetingMapper smartDemocraticLifeMeetingMapper;
	@Autowired
	private SmartDemocraticLifePeopleMapper smartDemocraticLifePeopleMapper;
	@Autowired
	private SmartDemocraticLifeEnclosureMapper smartDemocraticLifeEnclosureMapper;
	
	@Override
	@Transactional
	public void saveMain(SmartDemocraticLifeMeeting smartDemocraticLifeMeeting, List<SmartDemocraticLifePeople> smartDemocraticLifePeopleList,List<SmartDemocraticLifeEnclosure> smartDemocraticLifeEnclosureList) {
		smartDemocraticLifeMeetingMapper.insert(smartDemocraticLifeMeeting);
		if(smartDemocraticLifePeopleList!=null && smartDemocraticLifePeopleList.size()>0) {
			for(SmartDemocraticLifePeople entity:smartDemocraticLifePeopleList) {
				//外键设置
				entity.setMeetingId(smartDemocraticLifeMeeting.getId());
				smartDemocraticLifePeopleMapper.insert(entity);
			}
		}
		if(smartDemocraticLifeEnclosureList!=null && smartDemocraticLifeEnclosureList.size()>0) {
			for(SmartDemocraticLifeEnclosure entity:smartDemocraticLifeEnclosureList) {
				//外键设置
				entity.setMeetingId(smartDemocraticLifeMeeting.getId());
				smartDemocraticLifeEnclosureMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(SmartDemocraticLifeMeeting smartDemocraticLifeMeeting,List<SmartDemocraticLifePeople> smartDemocraticLifePeopleList,List<SmartDemocraticLifeEnclosure> smartDemocraticLifeEnclosureList) {
		smartDemocraticLifeMeetingMapper.updateById(smartDemocraticLifeMeeting);
		
		//1.先删除子表数据
		smartDemocraticLifePeopleMapper.deleteByMainId(smartDemocraticLifeMeeting.getId());
		smartDemocraticLifeEnclosureMapper.deleteByMainId(smartDemocraticLifeMeeting.getId());
		
		//2.子表数据重新插入
		if(smartDemocraticLifePeopleList!=null && smartDemocraticLifePeopleList.size()>0) {
			for(SmartDemocraticLifePeople entity:smartDemocraticLifePeopleList) {
				//外键设置
				entity.setMeetingId(smartDemocraticLifeMeeting.getId());
				smartDemocraticLifePeopleMapper.insert(entity);
			}
		}
		if(smartDemocraticLifeEnclosureList!=null && smartDemocraticLifeEnclosureList.size()>0) {
			for(SmartDemocraticLifeEnclosure entity:smartDemocraticLifeEnclosureList) {
				//外键设置
				entity.setMeetingId(smartDemocraticLifeMeeting.getId());
				smartDemocraticLifeEnclosureMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		smartDemocraticLifePeopleMapper.deleteByMainId(id);
		smartDemocraticLifeEnclosureMapper.deleteByMainId(id);
		smartDemocraticLifeMeetingMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			smartDemocraticLifePeopleMapper.deleteByMainId(id.toString());
			smartDemocraticLifeEnclosureMapper.deleteByMainId(id.toString());
			smartDemocraticLifeMeetingMapper.deleteById(id);
		}
	}
	
}
