package org.jeecg.modules.smartPublicityEducation.mapper;

import java.util.List;
import org.jeecg.modules.smartPublicityEducation.entity.SmartPublicityEducationPeople;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 宣传教育参会人员
 * @Author: jeecg-boot
 * @Date:   2021-12-29
 * @Version: V1.0
 */
public interface SmartPublicityEducationPeopleMapper extends BaseMapper<SmartPublicityEducationPeople> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<SmartPublicityEducationPeople> selectByMainId(@Param("mainId") String mainId);
}
