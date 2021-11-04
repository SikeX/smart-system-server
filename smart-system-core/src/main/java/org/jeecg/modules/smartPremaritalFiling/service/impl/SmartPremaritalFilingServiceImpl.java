package org.jeecg.modules.smartPremaritalFiling.service.impl;

import org.jeecg.modules.smartPremaritalFiling.entity.SmartPremaritalFiling;
import org.jeecg.modules.smartPremaritalFiling.entity.SmartPremaritalFilingApp;
import org.jeecg.modules.smartPremaritalFiling.mapper.SmartPremaritalFilingAppMapper;
import org.jeecg.modules.smartPremaritalFiling.mapper.SmartPremaritalFilingMapper;
import org.jeecg.modules.smartPremaritalFiling.service.ISmartPremaritalFilingService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 8项规定婚前报备表
 * @Author: jeecg-boot
 * @Date:   2021-11-04
 * @Version: V1.0
 */
@Service
public class SmartPremaritalFilingServiceImpl extends ServiceImpl<SmartPremaritalFilingMapper, SmartPremaritalFiling> implements ISmartPremaritalFilingService {

	@Autowired
	private SmartPremaritalFilingMapper smartPremaritalFilingMapper;
	@Autowired
	private SmartPremaritalFilingAppMapper smartPremaritalFilingAppMapper;
	
	@Override
	@Transactional
	public void saveMain(SmartPremaritalFiling smartPremaritalFiling, List<SmartPremaritalFilingApp> smartPremaritalFilingAppList) {
		smartPremaritalFilingMapper.insert(smartPremaritalFiling);
		if(smartPremaritalFilingAppList!=null && smartPremaritalFilingAppList.size()>0) {
			for(SmartPremaritalFilingApp entity:smartPremaritalFilingAppList) {
				//外键设置
				entity.setParentId(smartPremaritalFiling.getId());
				smartPremaritalFilingAppMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(SmartPremaritalFiling smartPremaritalFiling,List<SmartPremaritalFilingApp> smartPremaritalFilingAppList) {
		smartPremaritalFilingMapper.updateById(smartPremaritalFiling);
		
		//1.先删除子表数据
		smartPremaritalFilingAppMapper.deleteByMainId(smartPremaritalFiling.getId());
		
		//2.子表数据重新插入
		if(smartPremaritalFilingAppList!=null && smartPremaritalFilingAppList.size()>0) {
			for(SmartPremaritalFilingApp entity:smartPremaritalFilingAppList) {
				//外键设置
				entity.setParentId(smartPremaritalFiling.getId());
				smartPremaritalFilingAppMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		smartPremaritalFilingAppMapper.deleteByMainId(id);
		smartPremaritalFilingMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			smartPremaritalFilingAppMapper.deleteByMainId(id.toString());
			smartPremaritalFilingMapper.deleteById(id);
		}
	}
	
}
