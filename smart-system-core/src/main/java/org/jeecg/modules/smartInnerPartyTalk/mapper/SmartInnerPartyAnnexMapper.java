package org.jeecg.modules.smartInnerPartyTalk.mapper;

import java.util.List;
import org.jeecg.modules.smartInnerPartyTalk.entity.SmartInnerPartyAnnex;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 党内谈话附件表
 * @Author: jeecg-boot
 * @Date:   2021-11-01
 * @Version: V1.0
 */
public interface SmartInnerPartyAnnexMapper extends BaseMapper<SmartInnerPartyAnnex> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<SmartInnerPartyAnnex> selectByMainId(@Param("mainId") String mainId);
}
