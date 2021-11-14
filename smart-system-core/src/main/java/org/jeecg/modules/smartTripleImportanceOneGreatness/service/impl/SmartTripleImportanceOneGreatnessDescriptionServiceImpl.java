package org.jeecg.modules.smartTripleImportanceOneGreatness.service.impl;

import org.jeecg.modules.smartTripleImportanceOneGreatness.entity.SmartTripleImportanceOneGreatnessDescription;
import org.jeecg.modules.smartTripleImportanceOneGreatness.mapper.SmartTripleImportanceOneGreatnessDescriptionMapper;
import org.jeecg.modules.smartTripleImportanceOneGreatness.service.ISmartTripleImportanceOneGreatnessDescriptionService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 三重一大附件表
 * @Author: jeecg-boot
 * @Date:   2021-11-12
 * @Version: V1.0
 */
@Service
public class SmartTripleImportanceOneGreatnessDescriptionServiceImpl extends ServiceImpl<SmartTripleImportanceOneGreatnessDescriptionMapper, SmartTripleImportanceOneGreatnessDescription> implements ISmartTripleImportanceOneGreatnessDescriptionService {
	
	@Autowired
	private SmartTripleImportanceOneGreatnessDescriptionMapper smartTripleImportanceOneGreatnessDescriptionMapper;
	
	@Override
	public List<SmartTripleImportanceOneGreatnessDescription> selectByMainId(String mainId) {
		return smartTripleImportanceOneGreatnessDescriptionMapper.selectByMainId(mainId);
	}
}
