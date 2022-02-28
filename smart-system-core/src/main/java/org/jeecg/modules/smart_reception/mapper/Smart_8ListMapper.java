package org.jeecg.modules.smart_reception.mapper;

import java.util.List;
import org.jeecg.modules.smart_reception.entity.Smart_8List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 接待清单
 * @Author: jeecg-boot
 * @Date:   2022-02-28
 * @Version: V1.0
 */
public interface Smart_8ListMapper extends BaseMapper<Smart_8List> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<Smart_8List> selectByMainId(@Param("mainId") String mainId);

}
