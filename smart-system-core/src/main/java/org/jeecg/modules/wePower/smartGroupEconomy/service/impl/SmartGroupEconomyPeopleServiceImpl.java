package org.jeecg.modules.wePower.smartGroupEconomy.service.impl;

import org.jeecg.modules.wePower.smartGroupEconomy.entity.SmartGroupEconomyPeople;
import org.jeecg.modules.wePower.smartGroupEconomy.mapper.SmartGroupEconomyPeopleMapper;
import org.jeecg.modules.wePower.smartGroupEconomy.service.ISmartGroupEconomyPeopleService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 集体经济组织人员
 * @Author: jeecg-boot
 * @Date:   2022-03-06
 * @Version: V1.0
 */
@Service
public class SmartGroupEconomyPeopleServiceImpl extends ServiceImpl<SmartGroupEconomyPeopleMapper, SmartGroupEconomyPeople> implements ISmartGroupEconomyPeopleService {
	
	@Autowired
	private SmartGroupEconomyPeopleMapper smartGroupEconomyPeopleMapper;
	
	@Override
	public List<SmartGroupEconomyPeople> selectByMainId(String mainId) {
		return smartGroupEconomyPeopleMapper.selectByMainId(mainId);
	}
}
