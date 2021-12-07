package org.jeecg.modules.smartReportingInformation.service.impl;

import org.jeecg.modules.smartReportingInformation.entity.SmartReportingInformation;
import org.jeecg.modules.smartReportingInformation.entity.SmartReportingSurvey;
import org.jeecg.modules.smartReportingInformation.entity.SmartReportingDescription;
import org.jeecg.modules.smartReportingInformation.mapper.SmartReportingSurveyMapper;
import org.jeecg.modules.smartReportingInformation.mapper.SmartReportingDescriptionMapper;
import org.jeecg.modules.smartReportingInformation.mapper.SmartReportingInformationMapper;
import org.jeecg.modules.smartReportingInformation.service.ISmartReportingInformationService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 举报信息表
 * @Author: jeecg-boot
 * @Date:   2021-11-29
 * @Version: V1.0
 */
@Service
public class SmartReportingInformationServiceImpl extends ServiceImpl<SmartReportingInformationMapper, SmartReportingInformation> implements ISmartReportingInformationService {

	@Autowired
	private SmartReportingInformationMapper smartReportingInformationMapper;
	@Autowired
	private SmartReportingSurveyMapper smartReportingSurveyMapper;
	@Autowired
	private SmartReportingDescriptionMapper smartReportingDescriptionMapper;

	@Override
	@Transactional
	public void saveMain(SmartReportingInformation smartReportingInformation, List<SmartReportingSurvey> smartReportingSurveyList,List<SmartReportingDescription> smartReportingDescriptionList) {
//		smartReportingInformation.setPhoto(smartReportingInformation.getPhotoList().toString());
		smartReportingInformationMapper.insert(smartReportingInformation);
		if(smartReportingSurveyList!=null && smartReportingSurveyList.size()>0) {
			for(SmartReportingSurvey entity:smartReportingSurveyList) {
				//外键设置
				entity.setReportingId(smartReportingInformation.getId());
				smartReportingSurveyMapper.insert(entity);
			}
		}
		if(smartReportingDescriptionList!=null && smartReportingDescriptionList.size()>0) {
			for(SmartReportingDescription entity:smartReportingDescriptionList) {
				//外键设置
				entity.setReportingId(smartReportingInformation.getId());
				smartReportingDescriptionMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(SmartReportingInformation smartReportingInformation,List<SmartReportingSurvey> smartReportingSurveyList,List<SmartReportingDescription> smartReportingDescriptionList) {
		smartReportingInformationMapper.updateById(smartReportingInformation);

		//1.先删除子表数据
		smartReportingSurveyMapper.deleteByMainId(smartReportingInformation.getId());
		smartReportingDescriptionMapper.deleteByMainId(smartReportingInformation.getId());

		//2.子表数据重新插入
		if(smartReportingSurveyList!=null && smartReportingSurveyList.size()>0) {
			for(SmartReportingSurvey entity:smartReportingSurveyList) {
				//外键设置
				entity.setReportingId(smartReportingInformation.getId());
				smartReportingSurveyMapper.insert(entity);
			}
		}
		if(smartReportingDescriptionList!=null && smartReportingDescriptionList.size()>0) {
			for(SmartReportingDescription entity:smartReportingDescriptionList) {
				//外键设置
				entity.setReportingId(smartReportingInformation.getId());
				smartReportingDescriptionMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		smartReportingSurveyMapper.deleteByMainId(id);
		smartReportingDescriptionMapper.deleteByMainId(id);
		smartReportingInformationMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			smartReportingSurveyMapper.deleteByMainId(id.toString());
			smartReportingDescriptionMapper.deleteByMainId(id.toString());
			smartReportingInformationMapper.deleteById(id);
		}
	}

}
