package org.jeecg.modules.smartSupervision.service.impl;

import org.jeecg.modules.smartSupervision.entity.SmartSupervisionAnnex;
import org.jeecg.modules.smartSupervision.mapper.SmartSupervisionAnnexMapper;
import org.jeecg.modules.smartSupervision.service.ISmartSupervisionAnnexService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 8项规定监督检查附件表
 * @Author: jeecg-boot
 * @Date:   2021-11-10
 * @Version: V1.0
 */
@Service
public class SmartSupervisionAnnexServiceImpl extends ServiceImpl<SmartSupervisionAnnexMapper, SmartSupervisionAnnex> implements ISmartSupervisionAnnexService {
	
	@Autowired
	private SmartSupervisionAnnexMapper smartSupervisionAnnexMapper;
	
	@Override
	public List<SmartSupervisionAnnex> selectByMainId(String mainId) {
		return smartSupervisionAnnexMapper.selectByMainId(mainId);
	}
}
