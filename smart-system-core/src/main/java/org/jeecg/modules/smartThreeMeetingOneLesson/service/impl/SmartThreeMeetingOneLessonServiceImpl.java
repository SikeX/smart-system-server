package org.jeecg.modules.smartThreeMeetingOneLesson.service.impl;

import org.jeecg.modules.smartThreeMeetingOneLesson.entity.SmartThreeMeetingOneLesson;
import org.jeecg.modules.smartThreeMeetingOneLesson.entity.SmartThreeMeetingOneLessonParticipants;
import org.jeecg.modules.smartThreeMeetingOneLesson.entity.SmartThreeMeetingOneLessonAnnex;
import org.jeecg.modules.smartThreeMeetingOneLesson.mapper.SmartThreeMeetingOneLessonParticipantsMapper;
import org.jeecg.modules.smartThreeMeetingOneLesson.mapper.SmartThreeMeetingOneLessonAnnexMapper;
import org.jeecg.modules.smartThreeMeetingOneLesson.mapper.SmartThreeMeetingOneLessonMapper;
import org.jeecg.modules.smartThreeMeetingOneLesson.service.ISmartThreeMeetingOneLessonService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 三会一课
 * @Author: jeecg-boot
 * @Date:   2021-11-14
 * @Version: V1.0
 */
@Service
public class SmartThreeMeetingOneLessonServiceImpl extends ServiceImpl<SmartThreeMeetingOneLessonMapper, SmartThreeMeetingOneLesson> implements ISmartThreeMeetingOneLessonService {

	@Autowired
	private SmartThreeMeetingOneLessonMapper smartThreeMeetingOneLessonMapper;
	@Autowired
	private SmartThreeMeetingOneLessonParticipantsMapper smartThreeMeetingOneLessonParticipantsMapper;
	@Autowired
	private SmartThreeMeetingOneLessonAnnexMapper smartThreeMeetingOneLessonAnnexMapper;

	@Override
	@Transactional
	public void saveMain(SmartThreeMeetingOneLesson smartThreeMeetingOneLesson, List<SmartThreeMeetingOneLessonParticipants> smartThreeMeetingOneLessonParticipantsList,List<SmartThreeMeetingOneLessonAnnex> smartThreeMeetingOneLessonAnnexList) {
		smartThreeMeetingOneLessonMapper.insert(smartThreeMeetingOneLesson);
		if(smartThreeMeetingOneLessonParticipantsList!=null && smartThreeMeetingOneLessonParticipantsList.size()>0) {
			for(SmartThreeMeetingOneLessonParticipants entity:smartThreeMeetingOneLessonParticipantsList) {
				//外键设置
				entity.setParentTableId(smartThreeMeetingOneLesson.getId());
				smartThreeMeetingOneLessonParticipantsMapper.insert(entity);
			}
		}
		if(smartThreeMeetingOneLessonAnnexList!=null && smartThreeMeetingOneLessonAnnexList.size()>0) {
			for(SmartThreeMeetingOneLessonAnnex entity:smartThreeMeetingOneLessonAnnexList) {
				//外键设置
				entity.setParentTableId(smartThreeMeetingOneLesson.getId());
				smartThreeMeetingOneLessonAnnexMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(SmartThreeMeetingOneLesson smartThreeMeetingOneLesson,List<SmartThreeMeetingOneLessonParticipants> smartThreeMeetingOneLessonParticipantsList,List<SmartThreeMeetingOneLessonAnnex> smartThreeMeetingOneLessonAnnexList) {
		smartThreeMeetingOneLessonMapper.updateById(smartThreeMeetingOneLesson);

		//1.先删除子表数据
		smartThreeMeetingOneLessonParticipantsMapper.deleteByMainId(smartThreeMeetingOneLesson.getId());
		smartThreeMeetingOneLessonAnnexMapper.deleteByMainId(smartThreeMeetingOneLesson.getId());

		//2.子表数据重新插入
		if(smartThreeMeetingOneLessonParticipantsList!=null && smartThreeMeetingOneLessonParticipantsList.size()>0) {
			for(SmartThreeMeetingOneLessonParticipants entity:smartThreeMeetingOneLessonParticipantsList) {
				//外键设置
				entity.setParentTableId(smartThreeMeetingOneLesson.getId());
				smartThreeMeetingOneLessonParticipantsMapper.insert(entity);
			}
		}
		if(smartThreeMeetingOneLessonAnnexList!=null && smartThreeMeetingOneLessonAnnexList.size()>0) {
			for(SmartThreeMeetingOneLessonAnnex entity:smartThreeMeetingOneLessonAnnexList) {
				//外键设置
				entity.setParentTableId(smartThreeMeetingOneLesson.getId());
				smartThreeMeetingOneLessonAnnexMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		smartThreeMeetingOneLessonParticipantsMapper.deleteByMainId(id);
		smartThreeMeetingOneLessonAnnexMapper.deleteByMainId(id);
		smartThreeMeetingOneLessonMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			smartThreeMeetingOneLessonParticipantsMapper.deleteByMainId(id.toString());
			smartThreeMeetingOneLessonAnnexMapper.deleteByMainId(id.toString());
			smartThreeMeetingOneLessonMapper.deleteById(id);
		}
	}

	@Override
	public String getDepartIdByOrgCode(String orgCode) {
		return smartThreeMeetingOneLessonMapper.getDepartIdByOrgCode(orgCode);
	}

	@Override
	public List<String> getChildrenIdByOrgCode(String orgCode) {
		return smartThreeMeetingOneLessonMapper.getChildrenIdByOrgCode(orgCode);
	}

}