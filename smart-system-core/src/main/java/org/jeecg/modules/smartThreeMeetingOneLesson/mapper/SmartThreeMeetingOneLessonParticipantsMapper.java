package org.jeecg.modules.smartThreeMeetingOneLesson.mapper;

import java.util.List;
import org.jeecg.modules.smartThreeMeetingOneLesson.entity.SmartThreeMeetingOneLessonParticipants;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 三会一课参会人员表
 * @Author: jeecg-boot
 * @Date:   2021-11-14
 * @Version: V1.0
 */
public interface SmartThreeMeetingOneLessonParticipantsMapper extends BaseMapper<SmartThreeMeetingOneLessonParticipants> {

	public boolean deleteByMainId(@Param("mainId") String mainId);

	public List<SmartThreeMeetingOneLessonParticipants> selectByMainId(@Param("mainId") String mainId);
}
