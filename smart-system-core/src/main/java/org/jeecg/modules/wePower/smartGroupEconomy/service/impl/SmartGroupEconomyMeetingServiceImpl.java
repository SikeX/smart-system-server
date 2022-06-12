package org.jeecg.modules.wePower.smartGroupEconomy.service.impl;

import org.jeecg.modules.wePower.smartGroupEconomy.entity.SmartGroupEconomyMeeting;
import org.jeecg.modules.wePower.smartGroupEconomy.mapper.SmartGroupEconomyMeetingMapper;
import org.jeecg.modules.wePower.smartGroupEconomy.service.ISmartGroupEconomyMeetingService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 集体经济组织会议
 * @Author: jeecg-boot
 * @Date:   2022-03-06
 * @Version: V1.0
 */
@Service
public class SmartGroupEconomyMeetingServiceImpl extends ServiceImpl<SmartGroupEconomyMeetingMapper, SmartGroupEconomyMeeting> implements ISmartGroupEconomyMeetingService {
	
	@Autowired
	private SmartGroupEconomyMeetingMapper smartGroupEconomyMeetingMapper;
	
	@Override
	public List<SmartGroupEconomyMeeting> selectByMainId(String mainId) {
		return smartGroupEconomyMeetingMapper.selectByMainId(mainId);
	}
}
