package org.jeecg.modules.smartTripleImportanceOneGreatness.mapper;

import java.util.List;
import org.jeecg.modules.smartTripleImportanceOneGreatness.entity.SmartTripleImportanceOneGreatnessDecription;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 三重一大附件表
 * @Author: jeecg-boot
 * @Date:   2021-11-01
 * @Version: V1.0
 */
public interface SmartTripleImportanceOneGreatnessDecriptionMapper extends BaseMapper<SmartTripleImportanceOneGreatnessDecription> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<SmartTripleImportanceOneGreatnessDecription> selectByMainId(@Param("mainId") String mainId);
}
