package org.jeecg.modules.smart_reception.mapper;

import java.util.List;
import org.jeecg.modules.smart_reception.entity.Smart_8Visitor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 来访人员信息表
 * @Author: jeecg-boot
 * @Date:   2022-02-28
 * @Version: V1.0
 */
public interface Smart_8VisitorMapper extends BaseMapper<Smart_8Visitor> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<Smart_8Visitor> selectByMainId(@Param("mainId") String mainId);

}
