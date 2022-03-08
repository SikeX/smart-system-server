package org.jeecg.modules.smartAnswerAssContent.mapper;

import java.util.List;
import org.jeecg.modules.smartAnswerAssContent.entity.SmartAnswerFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 要点答题附件
 * @Author: jeecg-boot
 * @Date:   2022-02-21
 * @Version: V1.0
 */
public interface SmartAnswerFileMapper extends BaseMapper<SmartAnswerFile> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<SmartAnswerFile> selectByMainId(@Param("mainId") String mainId);

}
