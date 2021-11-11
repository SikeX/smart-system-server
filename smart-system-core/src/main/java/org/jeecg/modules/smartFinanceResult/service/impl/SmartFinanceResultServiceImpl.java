package org.jeecg.modules.smartFinanceResult.service.impl;

import org.jeecg.modules.smartFinanceResult.entity.SmartFinanceResult;
import org.jeecg.modules.smartFinanceResult.entity.SmartFinanceAnnex;
import org.jeecg.modules.smartFinanceResult.mapper.SmartFinanceAnnexMapper;
import org.jeecg.modules.smartFinanceResult.mapper.SmartFinanceResultMapper;
import org.jeecg.modules.smartFinanceResult.service.ISmartFinanceResultService;
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
 * @Date:   2021-11-11
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
	public void saveMain(SmartFinanceResult smartFinanceResult, List<SmartFinanceAnnex> smartFinanceAnnexList) {
		smartFinanceResultMapper.insert(smartFinanceResult);
		if(smartFinanceAnnexList!=null && smartFinanceAnnexList.size()>0) {
			for(SmartFinanceAnnex entity:smartFinanceAnnexList) {
				//外键设置
				entity.setParentId(smartFinanceResult.getId());
				smartFinanceAnnexMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(SmartFinanceResult smartFinanceResult,List<SmartFinanceAnnex> smartFinanceAnnexList) {
		smartFinanceResultMapper.updateById(smartFinanceResult);
		
		//1.先删除子表数据
		smartFinanceAnnexMapper.deleteByMainId(smartFinanceResult.getId());
		
		//2.子表数据重新插入
		if(smartFinanceAnnexList!=null && smartFinanceAnnexList.size()>0) {
			for(SmartFinanceAnnex entity:smartFinanceAnnexList) {
				//外键设置
				entity.setParentId(smartFinanceResult.getId());
				smartFinanceAnnexMapper.insert(entity);
			}
		}
	}

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
