package org.jeecg.modules.publicityEducation.mapper;

import java.util.List;
import org.jeecg.modules.publicityEducation.entity.PublicityEducationAnnex;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 宣传教育附件表
 * @Author: jeecg-boot
 * @Date:   2021-12-26
 * @Version: V1.0
 */
public interface PublicityEducationAnnexMapper extends BaseMapper<PublicityEducationAnnex> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<PublicityEducationAnnex> selectByMainId(@Param("mainId") String mainId);
}
