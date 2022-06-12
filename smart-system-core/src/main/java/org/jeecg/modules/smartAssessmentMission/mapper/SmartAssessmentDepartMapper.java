package org.jeecg.modules.smartAssessmentMission.mapper;

import java.util.List;
import org.jeecg.modules.smartAssessmentMission.entity.SmartAssessmentDepart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 考核任务被考核单位
 * @Author: jeecg-boot
 * @Date:   2022-02-12
 * @Version: V1.0
 */
public interface SmartAssessmentDepartMapper extends BaseMapper<SmartAssessmentDepart> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<SmartAssessmentDepart> selectByMainId(@Param("mainId") String mainId);

}
