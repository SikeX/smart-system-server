package org.jeecg.modules.smartCreateAdvice.service.impl;

import org.jeecg.modules.smartCreateAdvice.entity.SmartCreateAdviceAnnex;
import org.jeecg.modules.smartCreateAdvice.mapper.SmartCreateAdviceAnnexMapper;
import org.jeecg.modules.smartCreateAdvice.service.ISmartCreateAdviceAnnexService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 制发建议附件表
 * @Author: jeecg-boot
 * @Date:   2021-11-13
 * @Version: V1.0
 */
@Service
public class SmartCreateAdviceAnnexServiceImpl extends ServiceImpl<SmartCreateAdviceAnnexMapper, SmartCreateAdviceAnnex> implements ISmartCreateAdviceAnnexService {
	
	@Autowired
	private SmartCreateAdviceAnnexMapper smartCreateAdviceAnnexMapper;
	
	@Override
	public List<SmartCreateAdviceAnnex> selectByMainId(String mainId) {
		return smartCreateAdviceAnnexMapper.selectByMainId(mainId);
	}
}
