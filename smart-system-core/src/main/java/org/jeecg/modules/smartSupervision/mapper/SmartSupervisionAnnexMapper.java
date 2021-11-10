package org.jeecg.modules.smartSupervision.mapper;

import java.util.List;
import org.jeecg.modules.smartSupervision.entity.SmartSupervisionAnnex;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 8项规定监督检查附件表
 * @Author: jeecg-boot
 * @Date:   2021-11-08
 * @Version: V1.0
 */
public interface SmartSupervisionAnnexMapper extends BaseMapper<SmartSupervisionAnnex> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<SmartSupervisionAnnex> selectByMainId(@Param("mainId") String mainId);
}
