package org.jeecg.modules.wePower.smartGroupEconomy.mapper;

import java.util.List;
import org.jeecg.modules.wePower.smartGroupEconomy.entity.SmartGroupEconomyPeople;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 集体经济组织人员
 * @Author: jeecg-boot
 * @Date:   2022-03-06
 * @Version: V1.0
 */
public interface SmartGroupEconomyPeopleMapper extends BaseMapper<SmartGroupEconomyPeople> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<SmartGroupEconomyPeople> selectByMainId(@Param("mainId") String mainId);

}
