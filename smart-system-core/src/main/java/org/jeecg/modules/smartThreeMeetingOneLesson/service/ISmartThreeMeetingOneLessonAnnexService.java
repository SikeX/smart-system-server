package org.jeecg.modules.smartThreeMeetingOneLesson.service;

import org.jeecg.modules.smartThreeMeetingOneLesson.entity.SmartThreeMeetingOneLessonAnnex;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 三会一课附件表
 * @Author: jeecg-boot
 * @Date:   2021-11-14
 * @Version: V1.0
 */
public interface ISmartThreeMeetingOneLessonAnnexService extends IService<SmartThreeMeetingOneLessonAnnex> {

	public List<SmartThreeMeetingOneLessonAnnex> selectByMainId(String mainId);
}
