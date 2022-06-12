package org.jeecg.modules.smart_8regulations_for_reception.mapper;

import java.util.List;
import org.jeecg.modules.smart_8regulations_for_reception.entity.Smart_8regulationsForReceptionActivity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 八项规定公务接待公务活动项目
 * @Author: jeecg-boot
 * @Date:   2021-11-11
 * @Version: V1.0
 */
public interface Smart_8regulationsForReceptionActivityMapper extends BaseMapper<Smart_8regulationsForReceptionActivity> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<Smart_8regulationsForReceptionActivity> selectByMainId(@Param("mainId") String mainId);
}
