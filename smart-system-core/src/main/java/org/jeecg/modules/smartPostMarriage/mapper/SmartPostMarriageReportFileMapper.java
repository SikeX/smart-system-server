package org.jeecg.modules.smartPostMarriage.mapper;

import java.util.List;
import org.jeecg.modules.smartPostMarriage.entity.SmartPostMarriageReportFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 8项规定婚后报备宴请发票与附件表
 * @Author: jeecg-boot
 * @Date:   2021-11-10
 * @Version: V1.0
 */
public interface SmartPostMarriageReportFileMapper extends BaseMapper<SmartPostMarriageReportFile> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<SmartPostMarriageReportFile> selectByMainId(@Param("mainId") String mainId);
}
