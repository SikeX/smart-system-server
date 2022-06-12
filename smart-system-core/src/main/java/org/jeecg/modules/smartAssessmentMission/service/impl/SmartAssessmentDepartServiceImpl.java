package org.jeecg.modules.smartAssessmentMission.service.impl;

import org.jeecg.modules.smartAssessmentMission.entity.SmartAssessmentDepart;
import org.jeecg.modules.smartAssessmentMission.mapper.SmartAssessmentDepartMapper;
import org.jeecg.modules.smartAssessmentMission.service.ISmartAssessmentDepartService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 考核任务被考核单位
 * @Author: jeecg-boot
 * @Date:   2022-02-12
 * @Version: V1.0
 */
@Service
public class SmartAssessmentDepartServiceImpl extends ServiceImpl<SmartAssessmentDepartMapper, SmartAssessmentDepart> implements ISmartAssessmentDepartService {
	
	@Autowired
	private SmartAssessmentDepartMapper smartAssessmentDepartMapper;
	
	@Override
	public List<SmartAssessmentDepart> selectByMainId(String mainId) {
		return smartAssessmentDepartMapper.selectByMainId(mainId);
	}
}
