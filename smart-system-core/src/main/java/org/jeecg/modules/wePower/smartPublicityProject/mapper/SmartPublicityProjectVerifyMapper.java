package org.jeecg.modules.wePower.smartPublicityProject.mapper;

import java.util.List;
import org.jeecg.modules.wePower.smartPublicityProject.entity.SmartPublicityProjectVerify;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 项目审核
 * @Author: jeecg-boot
 * @Date:   2022-03-09
 * @Version: V1.0
 */
public interface SmartPublicityProjectVerifyMapper extends BaseMapper<SmartPublicityProjectVerify> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<SmartPublicityProjectVerify> selectByMainId(@Param("mainId") String mainId);
}
