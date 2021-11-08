package org.jeecg.modules.smartTripleImportanceOneGreatness.service.impl;

import org.jeecg.modules.smartTripleImportanceOneGreatness.entity.SmartTripleImportanceOneGreatnessDecription;
import org.jeecg.modules.smartTripleImportanceOneGreatness.mapper.SmartTripleImportanceOneGreatnessDecriptionMapper;
import org.jeecg.modules.smartTripleImportanceOneGreatness.service.ISmartTripleImportanceOneGreatnessDecriptionService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 三重一大附件表
 * @Author: jeecg-boot
 * @Date:   2021-11-08
 * @Version: V1.0
 */
@Service
public class SmartTripleImportanceOneGreatnessDecriptionServiceImpl extends ServiceImpl<SmartTripleImportanceOneGreatnessDecriptionMapper, SmartTripleImportanceOneGreatnessDecription> implements ISmartTripleImportanceOneGreatnessDecriptionService {
	
	@Autowired
	private SmartTripleImportanceOneGreatnessDecriptionMapper smartTripleImportanceOneGreatnessDecriptionMapper;
	
	@Override
	public List<SmartTripleImportanceOneGreatnessDecription> selectByMainId(String mainId) {
		return smartTripleImportanceOneGreatnessDecriptionMapper.selectByMainId(mainId);
	}
}
