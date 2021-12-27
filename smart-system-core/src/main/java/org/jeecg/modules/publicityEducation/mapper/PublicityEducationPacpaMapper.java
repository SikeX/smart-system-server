package org.jeecg.modules.publicityEducation.mapper;

import java.util.List;
import org.jeecg.modules.publicityEducation.entity.PublicityEducationPacpa;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 宣传教育参会人员
 * @Author: jeecg-boot
 * @Date:   2021-12-26
 * @Version: V1.0
 */
public interface PublicityEducationPacpaMapper extends BaseMapper<PublicityEducationPacpa> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<PublicityEducationPacpa> selectByMainId(@Param("mainId") String mainId);
}
