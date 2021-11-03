package org.jeecg.modules.smartDemocraticLifeMeeting.service.impl;

import org.jeecg.modules.smartDemocraticLifeMeeting.entity.SmartDemocraticLifePeople;
import org.jeecg.modules.smartDemocraticLifeMeeting.mapper.SmartDemocraticLifePeopleMapper;
import org.jeecg.modules.smartDemocraticLifeMeeting.service.ISmartDemocraticLifePeopleService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 民主生活会参会人员表
 * @Author: jeecg-boot
 * @Date:   2021-11-03
 * @Version: V1.0
 */
@Service
public class SmartDemocraticLifePeopleServiceImpl extends ServiceImpl<SmartDemocraticLifePeopleMapper, SmartDemocraticLifePeople> implements ISmartDemocraticLifePeopleService {
	
	@Autowired
	private SmartDemocraticLifePeopleMapper smartDemocraticLifePeopleMapper;
	
	@Override
	public List<SmartDemocraticLifePeople> selectByMainId(String mainId) {
		return smartDemocraticLifePeopleMapper.selectByMainId(mainId);
	}
}
