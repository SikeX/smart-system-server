package org.jeecg.modules.SmartFinanceResult.service.impl;

import org.jeecg.modules.SmartFinanceResult.entity.SmartFinanceResult;
import org.jeecg.modules.SmartFinanceResult.entity.SmartFinanceAnnex;
import org.jeecg.modules.SmartFinanceResult.mapper.SmartFinanceAnnexMapper;
import org.jeecg.modules.SmartFinanceResult.mapper.SmartFinanceResultMapper;
import org.jeecg.modules.SmartFinanceResult.service.ISmartFinanceResultService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 8项规定财物收支表
 * @Author: jeecg-boot
 * @Date:   2021-11-03
 * @Version: V1.0
 */
@Service
public class SmartFinanceResultServiceImpl extends ServiceImpl<SmartFinanceResultMapper, SmartFinanceResult> implements ISmartFinanceResultService {

	@Autowired
	private SmartFinanceResultMapper smartFinanceResultMapper;
	@Autowired
	private SmartFinanceAnnexMapper smartFinanceAnnexMapper;
	
	@Override
	@Transactional
	public void delMain(String id) {
		smartFinanceAnnexMapper.deleteByMainId(id);
		smartFinanceResultMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			smartFinanceAnnexMapper.deleteByMainId(id.toString());
			smartFinanceResultMapper.deleteById(id);
		}
	}
	
}
