package org.jeecg.modules.smartThreeMeetingOneLesson.service.impl;

import org.jeecg.modules.smartThreeMeetingOneLesson.entity.SmartThreeMeetingOneLessonParticipants;
import org.jeecg.modules.smartThreeMeetingOneLesson.mapper.SmartThreeMeetingOneLessonParticipantsMapper;
import org.jeecg.modules.smartThreeMeetingOneLesson.service.ISmartThreeMeetingOneLessonParticipantsService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 三会一课参会人员表
 * @Author: jeecg-boot
 * @Date:   2021-11-14
 * @Version: V1.0
 */
@Service
public class SmartThreeMeetingOneLessonParticipantsServiceImpl extends ServiceImpl<SmartThreeMeetingOneLessonParticipantsMapper, SmartThreeMeetingOneLessonParticipants> implements ISmartThreeMeetingOneLessonParticipantsService {

	@Autowired
	private SmartThreeMeetingOneLessonParticipantsMapper smartThreeMeetingOneLessonParticipantsMapper;

	@Override
	public List<SmartThreeMeetingOneLessonParticipants> selectByMainId(String mainId) {
		return smartThreeMeetingOneLessonParticipantsMapper.selectByMainId(mainId);
	}
}
