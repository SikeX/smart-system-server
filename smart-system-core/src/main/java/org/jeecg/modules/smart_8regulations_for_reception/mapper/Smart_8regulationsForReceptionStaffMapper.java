package org.jeecg.modules.smart_8regulations_for_reception.mapper;

import java.util.List;
import org.jeecg.modules.smart_8regulations_for_reception.entity.Smart_8regulationsForReceptionStaff;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 八项规定公务接待人员信息
 * @Author: jeecg-boot
 * @Date:   2021-11-08
 * @Version: V1.0
 */
public interface Smart_8regulationsForReceptionStaffMapper extends BaseMapper<Smart_8regulationsForReceptionStaff> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<Smart_8regulationsForReceptionStaff> selectByMainId(@Param("mainId") String mainId);
}
