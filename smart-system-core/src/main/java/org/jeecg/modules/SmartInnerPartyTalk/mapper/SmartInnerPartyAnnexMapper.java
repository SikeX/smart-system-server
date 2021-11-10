package org.jeecg.modules.SmartInnerPartyTalk.mapper;

import java.util.List;
import org.jeecg.modules.SmartInnerPartyTalk.entity.SmartInnerPartyAnnex;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 党内谈话附件表
 * @Author: jeecg-boot
 * @Date:   2021-11-05
 * @Version: V1.0
 */
public interface SmartInnerPartyAnnexMapper extends BaseMapper<SmartInnerPartyAnnex> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<SmartInnerPartyAnnex> selectByMainId(@Param("mainId") String mainId);
}
