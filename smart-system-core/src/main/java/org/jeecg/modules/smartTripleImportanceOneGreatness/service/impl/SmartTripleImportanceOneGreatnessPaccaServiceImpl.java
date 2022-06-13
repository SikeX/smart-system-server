package org.jeecg.modules.smartTripleImportanceOneGreatness.service.impl;

import org.jeecg.modules.smartTripleImportanceOneGreatness.entity.SmartTripleImportanceOneGreatnessPacca;
import org.jeecg.modules.smartTripleImportanceOneGreatness.mapper.SmartTripleImportanceOneGreatnessPaccaMapper;
import org.jeecg.modules.smartTripleImportanceOneGreatness.service.ISmartTripleImportanceOneGreatnessPaccaService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 三重一大参会人员表
 * @Author: jeecg-boot
 * @Date:   2022-02-27
 * @Version: V1.0
 */
@Service
public class SmartTripleImportanceOneGreatnessPaccaServiceImpl extends ServiceImpl<SmartTripleImportanceOneGreatnessPaccaMapper, SmartTripleImportanceOneGreatnessPacca> implements ISmartTripleImportanceOneGreatnessPaccaService {
	
	@Autowired
	private SmartTripleImportanceOneGreatnessPaccaMapper smartTripleImportanceOneGreatnessPaccaMapper;
	
	@Override
	public List<SmartTripleImportanceOneGreatnessPacca> selectByMainId(String mainId) {
		return smartTripleImportanceOneGreatnessPaccaMapper.selectByMainId(mainId);
	}
}
