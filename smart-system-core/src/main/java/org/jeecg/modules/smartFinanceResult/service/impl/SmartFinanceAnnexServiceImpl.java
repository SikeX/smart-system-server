package org.jeecg.modules.smartFinanceResult.service.impl;

import org.jeecg.modules.smartFinanceResult.entity.SmartFinanceAnnex;
import org.jeecg.modules.smartFinanceResult.mapper.SmartFinanceAnnexMapper;
import org.jeecg.modules.smartFinanceResult.service.ISmartFinanceAnnexService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 8项规定财物收支附件
 * @Author: jeecg-boot
 * @Date:   2021-11-17
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
