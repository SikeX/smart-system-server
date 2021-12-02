package org.jeecg.modules.smartReportingInformation.mapper;

import java.util.List;
import org.jeecg.modules.smartReportingInformation.entity.SmartReportingDescription;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 举报附件表
 * @Author: jeecg-boot
 * @Date:   2021-11-29
 * @Version: V1.0
 */
public interface SmartReportingDescriptionMapper extends BaseMapper<SmartReportingDescription> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<SmartReportingDescription> selectByMainId(@Param("mainId") String mainId);
}
