package org.jeecg.modules.smart_reception.mapper;

import java.util.List;
import org.jeecg.modules.smart_reception.entity.Smart_8Stay;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 住宿信息
 * @Author: jeecg-boot
 * @Date:   2022-02-28
 * @Version: V1.0
 */
public interface Smart_8StayMapper extends BaseMapper<Smart_8Stay> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<Smart_8Stay> selectByMainId(@Param("mainId") String mainId);

}
