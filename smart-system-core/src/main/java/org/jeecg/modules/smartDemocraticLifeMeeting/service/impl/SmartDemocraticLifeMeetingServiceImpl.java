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
 * @Date:   2021-11-03
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
