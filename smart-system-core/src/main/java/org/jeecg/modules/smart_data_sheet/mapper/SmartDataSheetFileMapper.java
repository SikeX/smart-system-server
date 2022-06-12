package org.jeecg.modules.smart_data_sheet.mapper;

import java.util.List;
import org.jeecg.modules.smart_data_sheet.entity.SmartDataSheetFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 资料库文件
 * @Author: jeecg-boot
 * @Date:   2021-11-11
 * @Version: V1.0
 */
public interface SmartDataSheetFileMapper extends BaseMapper<SmartDataSheetFile> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<SmartDataSheetFile> selectByMainId(@Param("mainId") String mainId);
}
