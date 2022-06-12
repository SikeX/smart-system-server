package org.jeecg.modules.smartCreateAdvice.mapper;

import java.util.List;
import org.jeecg.modules.smartCreateAdvice.entity.SmartCreateAdviceAnnex;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 制发建议附件表
 * @Author: jeecg-boot
 * @Date:   2021-11-13
 * @Version: V1.0
 */
public interface SmartCreateAdviceAnnexMapper extends BaseMapper<SmartCreateAdviceAnnex> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<SmartCreateAdviceAnnex> selectByMainId(@Param("mainId") String mainId);
}
