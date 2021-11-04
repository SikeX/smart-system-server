package org.jeecg.modules.smartTripleImportanceOneGreatness.service.impl;

import org.jeecg.modules.smartTripleImportanceOneGreatness.entity.SmartTripleImportanceOneGreatness;
import org.jeecg.modules.smartTripleImportanceOneGreatness.entity.SmartTripleImportanceOneGreatnessDecription;
import org.jeecg.modules.smartTripleImportanceOneGreatness.mapper.SmartTripleImportanceOneGreatnessDecriptionMapper;
import org.jeecg.modules.smartTripleImportanceOneGreatness.mapper.SmartTripleImportanceOneGreatnessMapper;
import org.jeecg.modules.smartTripleImportanceOneGreatness.service.ISmartTripleImportanceOneGreatnessService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 三重一大表
 * @Author: jeecg-boot
 * @Date:   2021-11-04
 * @Version: V1.0
 */
@Service
public class SmartTripleImportanceOneGreatnessServiceImpl extends ServiceImpl<SmartTripleImportanceOneGreatnessMapper, SmartTripleImportanceOneGreatness> implements ISmartTripleImportanceOneGreatnessService {

	@Autowired
	private SmartTripleImportanceOneGreatnessMapper smartTripleImportanceOneGreatnessMapper;
	@Autowired
	private SmartTripleImportanceOneGreatnessDecriptionMapper smartTripleImportanceOneGreatnessDecriptionMapper;
	
	@Override
	@Transactional
	public void saveMain(SmartTripleImportanceOneGreatness smartTripleImportanceOneGreatness, List<SmartTripleImportanceOneGreatnessDecription> smartTripleImportanceOneGreatnessDecriptionList) {
		smartTripleImportanceOneGreatnessMapper.insert(smartTripleImportanceOneGreatness);
		if(smartTripleImportanceOneGreatnessDecriptionList!=null && smartTripleImportanceOneGreatnessDecriptionList.size()>0) {
			for(SmartTripleImportanceOneGreatnessDecription entity:smartTripleImportanceOneGreatnessDecriptionList) {
				//外键设置
				entity.setMeetingId(smartTripleImportanceOneGreatness.getId());
				smartTripleImportanceOneGreatnessDecriptionMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(SmartTripleImportanceOneGreatness smartTripleImportanceOneGreatness,List<SmartTripleImportanceOneGreatnessDecription> smartTripleImportanceOneGreatnessDecriptionList) {
		smartTripleImportanceOneGreatnessMapper.updateById(smartTripleImportanceOneGreatness);
		
		//1.先删除子表数据
		smartTripleImportanceOneGreatnessDecriptionMapper.deleteByMainId(smartTripleImportanceOneGreatness.getId());
		
		//2.子表数据重新插入
		if(smartTripleImportanceOneGreatnessDecriptionList!=null && smartTripleImportanceOneGreatnessDecriptionList.size()>0) {
			for(SmartTripleImportanceOneGreatnessDecription entity:smartTripleImportanceOneGreatnessDecriptionList) {
				//外键设置
				entity.setMeetingId(smartTripleImportanceOneGreatness.getId());
				smartTripleImportanceOneGreatnessDecriptionMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		smartTripleImportanceOneGreatnessDecriptionMapper.deleteByMainId(id);
		smartTripleImportanceOneGreatnessMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			smartTripleImportanceOneGreatnessDecriptionMapper.deleteByMainId(id.toString());
			smartTripleImportanceOneGreatnessMapper.deleteById(id);
		}
	}
	
}
