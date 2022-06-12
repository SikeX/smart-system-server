package org.jeecg.modules.wePower.smartGroupEconomy.service.impl;

import org.jeecg.modules.wePower.smartGroupEconomy.entity.SmartGroupEconomy;
import org.jeecg.modules.wePower.smartGroupEconomy.mapper.SmartGroupEconomyPeopleMapper;
import org.jeecg.modules.wePower.smartGroupEconomy.mapper.SmartGroupEconomyMeetingMapper;
import org.jeecg.modules.wePower.smartGroupEconomy.mapper.SmartGroupEconomyMapper;
import org.jeecg.modules.wePower.smartGroupEconomy.service.ISmartGroupEconomyService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.Collection;

/**
 * @Description: 农村集体经济组织
 * @Author: jeecg-boot
 * @Date:   2022-03-06
 * @Version: V1.0
 */
@Service
public class SmartGroupEconomyServiceImpl extends ServiceImpl<SmartGroupEconomyMapper, SmartGroupEconomy> implements ISmartGroupEconomyService {

	@Autowired
	private SmartGroupEconomyMapper smartGroupEconomyMapper;
	@Autowired
	private SmartGroupEconomyPeopleMapper smartGroupEconomyPeopleMapper;
	@Autowired
	private SmartGroupEconomyMeetingMapper smartGroupEconomyMeetingMapper;
	
	@Override
	@Transactional
	public void delMain(String id) {
		smartGroupEconomyPeopleMapper.deleteByMainId(id);
		smartGroupEconomyMeetingMapper.deleteByMainId(id);
		smartGroupEconomyMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			smartGroupEconomyPeopleMapper.deleteByMainId(id.toString());
			smartGroupEconomyMeetingMapper.deleteByMainId(id.toString());
			smartGroupEconomyMapper.deleteById(id);
		}
	}
	
}
