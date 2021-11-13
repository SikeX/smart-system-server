package org.jeecg.modules.smart_8regulations_for_reception.mapper;

import java.util.List;
import org.jeecg.modules.smart_8regulations_for_reception.entity.Smart_8regulationsForReceptiondStaff;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 八项规定公务接待陪同人员信息
 * @Author: jeecg-boot
 * @Date:   2021-11-11
 * @Version: V1.0
 */
public interface Smart_8regulationsForReceptiondStaffMapper extends BaseMapper<Smart_8regulationsForReceptiondStaff> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<Smart_8regulationsForReceptiondStaff> selectByMainId(@Param("mainId") String mainId);
}
