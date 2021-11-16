package org.jeecg.modules.smartReportingInformation.service.impl;

import org.jeecg.modules.smartReportingInformation.entity.SmartReportingSurvey;
import org.jeecg.modules.smartReportingInformation.mapper.SmartReportingSurveyMapper;
import org.jeecg.modules.smartReportingInformation.service.ISmartReportingSurveyService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 举报调查表
 * @Author: jeecg-boot
 * @Date:   2021-11-14
 * @Version: V1.0
 */
@Service
public class SmartReportingSurveyServiceImpl extends ServiceImpl<SmartReportingSurveyMapper, SmartReportingSurvey> implements ISmartReportingSurveyService {
	
	@Autowired
	private SmartReportingSurveyMapper smartReportingSurveyMapper;
	
	@Override
	public List<SmartReportingSurvey> selectByMainId(String mainId) {
		return smartReportingSurveyMapper.selectByMainId(mainId);
	}
}
