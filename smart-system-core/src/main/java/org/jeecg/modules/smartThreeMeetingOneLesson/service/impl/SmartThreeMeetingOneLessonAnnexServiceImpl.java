package org.jeecg.modules.smartThreeMeetingOneLesson.service.impl;

import org.jeecg.modules.smartThreeMeetingOneLesson.entity.SmartThreeMeetingOneLessonAnnex;
import org.jeecg.modules.smartThreeMeetingOneLesson.mapper.SmartThreeMeetingOneLessonAnnexMapper;
import org.jeecg.modules.smartThreeMeetingOneLesson.service.ISmartThreeMeetingOneLessonAnnexService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 三会一课附件表
 * @Author: jeecg-boot
 * @Date:   2021-11-14
 * @Version: V1.0
 */
@Service
public class SmartThreeMeetingOneLessonAnnexServiceImpl extends ServiceImpl<SmartThreeMeetingOneLessonAnnexMapper, SmartThreeMeetingOneLessonAnnex> implements ISmartThreeMeetingOneLessonAnnexService {
	
	@Autowired
	private SmartThreeMeetingOneLessonAnnexMapper smartThreeMeetingOneLessonAnnexMapper;
	
	@Override
	public List<SmartThreeMeetingOneLessonAnnex> selectByMainId(String mainId) {
		return smartThreeMeetingOneLessonAnnexMapper.selectByMainId(mainId);
	}
}
