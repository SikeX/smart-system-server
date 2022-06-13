package org.jeecg.modules.smartTripleImportanceOneGreatness.service.impl;

import org.jeecg.modules.smartTripleImportanceOneGreatness.entity.SmartTripleImportanceOneGreatness;
import org.jeecg.modules.smartTripleImportanceOneGreatness.entity.SmartTripleImportanceOneGreatnessPacca;
import org.jeecg.modules.smartTripleImportanceOneGreatness.mapper.SmartTripleImportanceOneGreatnessPaccaMapper;
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
 * @Date:   2022-02-27
 * @Version: V1.0
 */
@Service
public class SmartTripleImportanceOneGreatnessServiceImpl extends ServiceImpl<SmartTripleImportanceOneGreatnessMapper, SmartTripleImportanceOneGreatness> implements ISmartTripleImportanceOneGreatnessService {

	@Autowired
	private SmartTripleImportanceOneGreatnessMapper smartTripleImportanceOneGreatnessMapper;
	@Autowired
	private SmartTripleImportanceOneGreatnessPaccaMapper smartTripleImportanceOneGreatnessPaccaMapper;
	
	@Override
	@Transactional
	public void saveMain(SmartTripleImportanceOneGreatness smartTripleImportanceOneGreatness, List<SmartTripleImportanceOneGreatnessPacca> smartTripleImportanceOneGreatnessPaccaList) {
		smartTripleImportanceOneGreatnessMapper.insert(smartTripleImportanceOneGreatness);
		if(smartTripleImportanceOneGreatnessPaccaList!=null && smartTripleImportanceOneGreatnessPaccaList.size()>0) {
			for(SmartTripleImportanceOneGreatnessPacca entity:smartTripleImportanceOneGreatnessPaccaList) {
				//外键设置
				entity.setParentId(smartTripleImportanceOneGreatness.getId());
				smartTripleImportanceOneGreatnessPaccaMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(SmartTripleImportanceOneGreatness smartTripleImportanceOneGreatness,List<SmartTripleImportanceOneGreatnessPacca> smartTripleImportanceOneGreatnessPaccaList) {
		smartTripleImportanceOneGreatnessMapper.updateById(smartTripleImportanceOneGreatness);
		
		//1.先删除子表数据
		smartTripleImportanceOneGreatnessPaccaMapper.deleteByMainId(smartTripleImportanceOneGreatness.getId());
		
		//2.子表数据重新插入
		if(smartTripleImportanceOneGreatnessPaccaList!=null && smartTripleImportanceOneGreatnessPaccaList.size()>0) {
			for(SmartTripleImportanceOneGreatnessPacca entity:smartTripleImportanceOneGreatnessPaccaList) {
				//外键设置
				entity.setParentId(smartTripleImportanceOneGreatness.getId());
				smartTripleImportanceOneGreatnessPaccaMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		smartTripleImportanceOneGreatnessPaccaMapper.deleteByMainId(id);
		smartTripleImportanceOneGreatnessMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			smartTripleImportanceOneGreatnessPaccaMapper.deleteByMainId(id.toString());
			smartTripleImportanceOneGreatnessMapper.deleteById(id);
		}
	}
	
}
