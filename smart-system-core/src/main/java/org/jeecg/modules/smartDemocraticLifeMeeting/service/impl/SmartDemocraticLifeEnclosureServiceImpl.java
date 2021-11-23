package org.jeecg.modules.smartDemocraticLifeMeeting.service.impl;

import org.jeecg.modules.smartDemocraticLifeMeeting.entity.SmartDemocraticLifeEnclosure;
import org.jeecg.modules.smartDemocraticLifeMeeting.mapper.SmartDemocraticLifeEnclosureMapper;
import org.jeecg.modules.smartDemocraticLifeMeeting.service.ISmartDemocraticLifeEnclosureService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 民主生活会附件表
 * @Author: jeecg-boot
 * @Date:   2021-11-17
 * @Version: V1.0
 */
@Service
public class SmartDemocraticLifeEnclosureServiceImpl extends ServiceImpl<SmartDemocraticLifeEnclosureMapper, SmartDemocraticLifeEnclosure> implements ISmartDemocraticLifeEnclosureService {
	
	@Autowired
	private SmartDemocraticLifeEnclosureMapper smartDemocraticLifeEnclosureMapper;
	
	@Override
	public List<SmartDemocraticLifeEnclosure> selectByMainId(String mainId) {
		return smartDemocraticLifeEnclosureMapper.selectByMainId(mainId);
	}
}
