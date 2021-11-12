package org.jeecg.modules.SmartFinanceResult.mapper;

import java.util.List;
import org.jeecg.modules.SmartFinanceResult.entity.SmartFinanceAnnex;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 8项规定财物收支附件
 * @Author: jeecg-boot
 * @Date:   2021-11-03
 * @Version: V1.0
 */
public interface SmartFinanceAnnexMapper extends BaseMapper<SmartFinanceAnnex> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<SmartFinanceAnnex> selectByMainId(@Param("mainId") String mainId);

}
