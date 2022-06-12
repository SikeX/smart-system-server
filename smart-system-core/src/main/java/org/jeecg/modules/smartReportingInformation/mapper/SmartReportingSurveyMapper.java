package org.jeecg.modules.smartReportingInformation.mapper;

import java.util.List;
import org.jeecg.modules.smartReportingInformation.entity.SmartReportingSurvey;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 举报调查表
 * @Author: jeecg-boot
 * @Date:   2021-11-29
 * @Version: V1.0
 */
public interface SmartReportingSurveyMapper extends BaseMapper<SmartReportingSurvey> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<SmartReportingSurvey> selectByMainId(@Param("mainId") String mainId);
}
