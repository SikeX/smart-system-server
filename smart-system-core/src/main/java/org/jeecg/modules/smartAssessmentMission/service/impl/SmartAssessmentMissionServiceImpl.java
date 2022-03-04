package org.jeecg.modules.smartAssessmentMission.service.impl;

import org.jeecg.modules.smartAssessmentMission.entity.SmartAssessmentMission;
import org.jeecg.modules.smartAssessmentMission.entity.SmartAssessmentDepart;
import org.jeecg.modules.smartAssessmentMission.mapper.SmartAssessmentDepartMapper;
import org.jeecg.modules.smartAssessmentMission.mapper.SmartAssessmentMissionMapper;
import org.jeecg.modules.smartAssessmentMission.service.ISmartAssessmentMissionService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 考核任务表
 * @Author: jeecg-boot
 * @Date:   2022-02-12
 * @Version: V1.0
 */
@Service
public class SmartAssessmentMissionServiceImpl extends ServiceImpl<SmartAssessmentMissionMapper, SmartAssessmentMission> implements ISmartAssessmentMissionService {

	@Autowired
	private SmartAssessmentMissionMapper smartAssessmentMissionMapper;
	@Autowired
	private SmartAssessmentDepartMapper smartAssessmentDepartMapper;
	
	@Override
	@Transactional
	public void delMain(String id) {
		smartAssessmentDepartMapper.deleteByMainId(id);
		smartAssessmentMissionMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			smartAssessmentDepartMapper.deleteByMainId(id.toString());
			smartAssessmentMissionMapper.deleteById(id);
		}
	}
	
}
