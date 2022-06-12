package org.jeecg.modules.smartTripleImportanceOneGreatness.mapper;

import java.util.List;
import org.jeecg.modules.smartTripleImportanceOneGreatness.entity.SmartTripleImportanceOneGreatnessDescription;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 三重一大附件表
 * @Author: jeecg-boot
 * @Date:   2021-11-13
 * @Version: V1.0
 */
public interface SmartTripleImportanceOneGreatnessDescriptionMapper extends BaseMapper<SmartTripleImportanceOneGreatnessDescription> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<SmartTripleImportanceOneGreatnessDescription> selectByMainId(@Param("mainId") String mainId);
}
