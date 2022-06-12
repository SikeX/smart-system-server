package org.jeecg.modules.smartPublicityEducation.service.impl;

import org.jeecg.modules.smartPublicityEducation.entity.SmartPublicityEducationPeople;
import org.jeecg.modules.smartPublicityEducation.mapper.SmartPublicityEducationPeopleMapper;
import org.jeecg.modules.smartPublicityEducation.service.ISmartPublicityEducationPeopleService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 宣传教育参会人员
 * @Author: jeecg-boot
 * @Date:   2021-12-29
 * @Version: V1.0
 */
@Service
public class SmartPublicityEducationPeopleServiceImpl extends ServiceImpl<SmartPublicityEducationPeopleMapper, SmartPublicityEducationPeople> implements ISmartPublicityEducationPeopleService {
	
	@Autowired
	private SmartPublicityEducationPeopleMapper smartPublicityEducationPeopleMapper;
	
	@Override
	public List<SmartPublicityEducationPeople> selectByMainId(String mainId) {
		return smartPublicityEducationPeopleMapper.selectByMainId(mainId);
	}
}
