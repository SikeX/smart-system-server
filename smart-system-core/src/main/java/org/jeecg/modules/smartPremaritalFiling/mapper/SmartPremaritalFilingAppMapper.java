package org.jeecg.modules.smartPremaritalFiling.mapper;

import java.util.List;
import org.jeecg.modules.smartPremaritalFiling.entity.SmartPremaritalFilingApp;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 8项规定婚前报备表附表
 * @Author: jeecg-boot
 * @Date:   2021-11-04
 * @Version: V1.0
 */
public interface SmartPremaritalFilingAppMapper extends BaseMapper<SmartPremaritalFilingApp> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<SmartPremaritalFilingApp> selectByMainId(@Param("mainId") String mainId);
}
