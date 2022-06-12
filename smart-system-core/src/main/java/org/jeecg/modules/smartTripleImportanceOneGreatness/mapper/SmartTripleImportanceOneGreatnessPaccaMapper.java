package org.jeecg.modules.smartTripleImportanceOneGreatness.mapper;

import java.util.List;
import org.jeecg.modules.smartTripleImportanceOneGreatness.entity.SmartTripleImportanceOneGreatnessPacca;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 三重一大参会人员表
 * @Author: jeecg-boot
 * @Date:   2021-11-13
 * @Version: V1.0
 */
public interface SmartTripleImportanceOneGreatnessPaccaMapper extends BaseMapper<SmartTripleImportanceOneGreatnessPacca> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<SmartTripleImportanceOneGreatnessPacca> selectByMainId(@Param("mainId") String mainId);
}
