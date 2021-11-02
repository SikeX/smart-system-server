package org.jeecg.modules.smartInnerPartyTalk.mapper;

import java.util.List;
import org.jeecg.modules.smartInnerPartyTalk.entity.SmartInnerPartyPacpa;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 党内谈话参与人表
 * @Author: jeecg-boot
 * @Date:   2021-11-01
 * @Version: V1.0
 */
public interface SmartInnerPartyPacpaMapper extends BaseMapper<SmartInnerPartyPacpa> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<SmartInnerPartyPacpa> selectByMainId(@Param("mainId") String mainId);
}
