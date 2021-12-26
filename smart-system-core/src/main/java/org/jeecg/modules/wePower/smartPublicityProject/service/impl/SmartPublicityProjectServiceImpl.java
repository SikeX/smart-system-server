package org.jeecg.modules.wePower.smartPublicityProject.service.impl;

import org.jeecg.modules.wePower.smartPublicityProject.entity.SmartPublicityProject;
import org.jeecg.modules.wePower.smartPublicityProject.entity.SmartPublicityProjectVerify;
import org.jeecg.modules.wePower.smartPublicityProject.mapper.SmartPublicityProjectVerifyMapper;
import org.jeecg.modules.wePower.smartPublicityProject.mapper.SmartPublicityProjectMapper;
import org.jeecg.modules.wePower.smartPublicityProject.service.ISmartPublicityProjectService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 项目管理
 * @Author: jeecg-boot
 * @Date:   2021-12-26
 * @Version: V1.0
 */
@Service
public class SmartPublicityProjectServiceImpl extends ServiceImpl<SmartPublicityProjectMapper, SmartPublicityProject> implements ISmartPublicityProjectService {

	@Autowired
	private SmartPublicityProjectMapper smartPublicityProjectMapper;
	@Autowired
	private SmartPublicityProjectVerifyMapper smartPublicityProjectVerifyMapper;
	
	@Override
	@Transactional
	public void saveMain(SmartPublicityProject smartPublicityProject, List<SmartPublicityProjectVerify> smartPublicityProjectVerifyList) {
		smartPublicityProjectMapper.insert(smartPublicityProject);
		if(smartPublicityProjectVerifyList!=null && smartPublicityProjectVerifyList.size()>0) {
			for(SmartPublicityProjectVerify entity:smartPublicityProjectVerifyList) {
				//外键设置
				entity.setProjectId(smartPublicityProject.getId());
				smartPublicityProjectVerifyMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(SmartPublicityProject smartPublicityProject,List<SmartPublicityProjectVerify> smartPublicityProjectVerifyList) {
		smartPublicityProjectMapper.updateById(smartPublicityProject);
		
		//1.先删除子表数据
		smartPublicityProjectVerifyMapper.deleteByMainId(smartPublicityProject.getId());
		
		//2.子表数据重新插入
		if(smartPublicityProjectVerifyList!=null && smartPublicityProjectVerifyList.size()>0) {
			for(SmartPublicityProjectVerify entity:smartPublicityProjectVerifyList) {
				//外键设置
				entity.setProjectId(smartPublicityProject.getId());
				smartPublicityProjectVerifyMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		smartPublicityProjectVerifyMapper.deleteByMainId(id);
		smartPublicityProjectMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			smartPublicityProjectVerifyMapper.deleteByMainId(id.toString());
			smartPublicityProjectMapper.deleteById(id);
		}
	}
	
}
