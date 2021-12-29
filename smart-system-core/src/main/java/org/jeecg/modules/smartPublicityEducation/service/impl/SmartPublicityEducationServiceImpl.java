package org.jeecg.modules.smartPublicityEducation.service.impl;

import org.jeecg.modules.smartPublicityEducation.entity.SmartPublicityEducation;
import org.jeecg.modules.smartPublicityEducation.entity.SmartPublicityEducationPeople;
import org.jeecg.modules.smartPublicityEducation.mapper.SmartPublicityEducationPeopleMapper;
import org.jeecg.modules.smartPublicityEducation.mapper.SmartPublicityEducationMapper;
import org.jeecg.modules.smartPublicityEducation.service.ISmartPublicityEducationService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 宣传教育
 * @Author: jeecg-boot
 * @Date:   2021-12-29
 * @Version: V1.0
 */
@Service
public class SmartPublicityEducationServiceImpl extends ServiceImpl<SmartPublicityEducationMapper, SmartPublicityEducation> implements ISmartPublicityEducationService {

	@Autowired
	private SmartPublicityEducationMapper smartPublicityEducationMapper;
	@Autowired
	private SmartPublicityEducationPeopleMapper smartPublicityEducationPeopleMapper;
	
	@Override
	@Transactional
	public void saveMain(SmartPublicityEducation smartPublicityEducation, List<SmartPublicityEducationPeople> smartPublicityEducationPeopleList) {
		smartPublicityEducationMapper.insert(smartPublicityEducation);
		if(smartPublicityEducationPeopleList!=null && smartPublicityEducationPeopleList.size()>0) {
			for(SmartPublicityEducationPeople entity:smartPublicityEducationPeopleList) {
				//外键设置
				entity.setMainId(smartPublicityEducation.getId());
				smartPublicityEducationPeopleMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(SmartPublicityEducation smartPublicityEducation,List<SmartPublicityEducationPeople> smartPublicityEducationPeopleList) {
		smartPublicityEducationMapper.updateById(smartPublicityEducation);
		
		//1.先删除子表数据
		smartPublicityEducationPeopleMapper.deleteByMainId(smartPublicityEducation.getId());
		
		//2.子表数据重新插入
		if(smartPublicityEducationPeopleList!=null && smartPublicityEducationPeopleList.size()>0) {
			for(SmartPublicityEducationPeople entity:smartPublicityEducationPeopleList) {
				//外键设置
				entity.setMainId(smartPublicityEducation.getId());
				smartPublicityEducationPeopleMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		smartPublicityEducationPeopleMapper.deleteByMainId(id);
		smartPublicityEducationMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			smartPublicityEducationPeopleMapper.deleteByMainId(id.toString());
			smartPublicityEducationMapper.deleteById(id);
		}
	}
	
}
