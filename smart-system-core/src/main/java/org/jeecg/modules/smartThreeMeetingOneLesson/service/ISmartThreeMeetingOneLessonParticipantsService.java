package org.jeecg.modules.smartThreeMeetingOneLesson.service;

import org.jeecg.modules.smartThreeMeetingOneLesson.entity.SmartThreeMeetingOneLessonParticipants;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 三会一课参会人员表
 * @Author: jeecg-boot
 * @Date:   2021-11-14
 * @Version: V1.0
 */
public interface ISmartThreeMeetingOneLessonParticipantsService extends IService<SmartThreeMeetingOneLessonParticipants> {

	public List<SmartThreeMeetingOneLessonParticipants> selectByMainId(String mainId);
}
