package org.jeecg.modules.SmartFinanceResult.service.impl;

import org.jeecg.modules.SmartFinanceResult.entity.SmartFinanceAnnex;
import org.jeecg.modules.SmartFinanceResult.mapper.SmartFinanceAnnexMapper;
import org.jeecg.modules.SmartFinanceResult.service.ISmartFinanceAnnexService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 8项规定财物收支附件
 * @Author: jeecg-boot
 * @Date:   2021-11-03
 * @Version: V1.0
 */
@Service
public class SmartFinanceAnnexServiceImpl extends ServiceImpl<SmartFinanceAnnexMapper, SmartFinanceAnnex> implements ISmartFinanceAnnexService {
	
	@Autowired
	private SmartFinanceAnnexMapper smartFinanceAnnexMapper;
	
	@Override
	public List<SmartFinanceAnnex> selectByMainId(String mainId) {
		return smartFinanceAnnexMapper.selectByMainId(mainId);
	}
}
