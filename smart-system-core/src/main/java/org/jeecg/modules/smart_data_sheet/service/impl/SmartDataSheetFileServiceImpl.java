package org.jeecg.modules.smart_data_sheet.service.impl;

import org.jeecg.modules.smart_data_sheet.entity.SmartDataSheetFile;
import org.jeecg.modules.smart_data_sheet.mapper.SmartDataSheetFileMapper;
import org.jeecg.modules.smart_data_sheet.service.ISmartDataSheetFileService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 资料库文件
 * @Author: jeecg-boot
 * @Date:   2021-11-11
 * @Version: V1.0
 */
@Service
public class SmartDataSheetFileServiceImpl extends ServiceImpl<SmartDataSheetFileMapper, SmartDataSheetFile> implements ISmartDataSheetFileService {
	
	@Autowired
	private SmartDataSheetFileMapper smartDataSheetFileMapper;
	
	@Override
	public List<SmartDataSheetFile> selectByMainId(String mainId) {
		return smartDataSheetFileMapper.selectByMainId(mainId);
	}
}
