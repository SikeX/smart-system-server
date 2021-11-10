package org.jeecg.modules.smartSupervision.service.impl;

import org.jeecg.modules.smartSupervision.entity.SmartSupervision;
import org.jeecg.modules.smartSupervision.entity.SmartSupervisionAnnex;
import org.jeecg.modules.smartSupervision.mapper.SmartSupervisionAnnexMapper;
import org.jeecg.modules.smartSupervision.mapper.SmartSupervisionMapper;
import org.jeecg.modules.smartSupervision.service.ISmartSupervisionService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 八项规定监督检查表
 * @Author: jeecg-boot
 * @Date:   2021-11-08
 * @Version: V1.0
 */
@Service
public class SmartSupervisionServiceImpl extends ServiceImpl<SmartSupervisionMapper, SmartSupervision> implements ISmartSupervisionService {

	@Autowired
	private SmartSupervisionMapper smartSupervisionMapper;
	@Autowired
	private SmartSupervisionAnnexMapper smartSupervisionAnnexMapper;
	
	@Override
	@Transactional
	public void saveMain(SmartSupervision smartSupervision, List<SmartSupervisionAnnex> smartSupervisionAnnexList) {
		smartSupervisionMapper.insert(smartSupervision);
		if(smartSupervisionAnnexList!=null && smartSupervisionAnnexList.size()>0) {
			for(SmartSupervisionAnnex entity:smartSupervisionAnnexList) {
				//外键设置
				entity.setMainId(smartSupervision.getId());
				smartSupervisionAnnexMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(SmartSupervision smartSupervision,List<SmartSupervisionAnnex> smartSupervisionAnnexList) {
		smartSupervisionMapper.updateById(smartSupervision);
		
		//1.先删除子表数据
		smartSupervisionAnnexMapper.deleteByMainId(smartSupervision.getId());
		
		//2.子表数据重新插入
		if(smartSupervisionAnnexList!=null && smartSupervisionAnnexList.size()>0) {
			for(SmartSupervisionAnnex entity:smartSupervisionAnnexList) {
				//外键设置
				entity.setMainId(smartSupervision.getId());
				smartSupervisionAnnexMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		smartSupervisionAnnexMapper.deleteByMainId(id);
		smartSupervisionMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			smartSupervisionAnnexMapper.deleteByMainId(id.toString());
			smartSupervisionMapper.deleteById(id);
		}
	}
	
}
