package org.jeecg.modules.smartCreateAdvice.service.impl;

import org.jeecg.modules.smartCreateAdvice.entity.SmartCreateAdvice;
import org.jeecg.modules.smartCreateAdvice.entity.SmartCreateAdviceAnnex;
import org.jeecg.modules.smartCreateAdvice.mapper.SmartCreateAdviceAnnexMapper;
import org.jeecg.modules.smartCreateAdvice.mapper.SmartCreateAdviceMapper;
import org.jeecg.modules.smartCreateAdvice.service.ISmartCreateAdviceService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 制发建议表
 * @Author: jeecg-boot
 * @Date:   2021-11-13
 * @Version: V1.0
 */
@Service
public class SmartCreateAdviceServiceImpl extends ServiceImpl<SmartCreateAdviceMapper, SmartCreateAdvice> implements ISmartCreateAdviceService {

	@Autowired
	private SmartCreateAdviceMapper smartCreateAdviceMapper;
	@Autowired
	private SmartCreateAdviceAnnexMapper smartCreateAdviceAnnexMapper;
	
	@Override
	@Transactional
	public void saveMain(SmartCreateAdvice smartCreateAdvice, List<SmartCreateAdviceAnnex> smartCreateAdviceAnnexList) {
		smartCreateAdviceMapper.insert(smartCreateAdvice);
		if(smartCreateAdviceAnnexList!=null && smartCreateAdviceAnnexList.size()>0) {
			for(SmartCreateAdviceAnnex entity:smartCreateAdviceAnnexList) {
				//外键设置
				entity.setMainId(smartCreateAdvice.getId());
				smartCreateAdviceAnnexMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(SmartCreateAdvice smartCreateAdvice,List<SmartCreateAdviceAnnex> smartCreateAdviceAnnexList) {
		smartCreateAdviceMapper.updateById(smartCreateAdvice);
		
		//1.先删除子表数据
		smartCreateAdviceAnnexMapper.deleteByMainId(smartCreateAdvice.getId());
		
		//2.子表数据重新插入
		if(smartCreateAdviceAnnexList!=null && smartCreateAdviceAnnexList.size()>0) {
			for(SmartCreateAdviceAnnex entity:smartCreateAdviceAnnexList) {
				//外键设置
				entity.setMainId(smartCreateAdvice.getId());
				smartCreateAdviceAnnexMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		smartCreateAdviceAnnexMapper.deleteByMainId(id);
		smartCreateAdviceMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			smartCreateAdviceAnnexMapper.deleteByMainId(id.toString());
			smartCreateAdviceMapper.deleteById(id);
		}
	}
	
}
