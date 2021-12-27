package org.jeecg.modules.publicityEducation.service.impl;

import org.jeecg.modules.publicityEducation.entity.PublicityEducation;
import org.jeecg.modules.publicityEducation.entity.PublicityEducationPacpa;
import org.jeecg.modules.publicityEducation.entity.PublicityEducationAnnex;
import org.jeecg.modules.publicityEducation.mapper.PublicityEducationPacpaMapper;
import org.jeecg.modules.publicityEducation.mapper.PublicityEducationAnnexMapper;
import org.jeecg.modules.publicityEducation.mapper.PublicityEducationMapper;
import org.jeecg.modules.publicityEducation.service.IPublicityEducationService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 宣传教育主表
 * @Author: jeecg-boot
 * @Date:   2021-12-26
 * @Version: V1.0
 */
@Service
public class PublicityEducationServiceImpl extends ServiceImpl<PublicityEducationMapper, PublicityEducation> implements IPublicityEducationService {

	@Autowired
	private PublicityEducationMapper publicityEducationMapper;
	@Autowired
	private PublicityEducationPacpaMapper publicityEducationPacpaMapper;
	@Autowired
	private PublicityEducationAnnexMapper publicityEducationAnnexMapper;
	
	@Override
	@Transactional
	public void saveMain(PublicityEducation publicityEducation, List<PublicityEducationPacpa> publicityEducationPacpaList,List<PublicityEducationAnnex> publicityEducationAnnexList) {
		publicityEducationMapper.insert(publicityEducation);
		if(publicityEducationPacpaList!=null && publicityEducationPacpaList.size()>0) {
			for(PublicityEducationPacpa entity:publicityEducationPacpaList) {
				//外键设置
				entity.setMainId(publicityEducation.getId());
				publicityEducationPacpaMapper.insert(entity);
			}
		}
		if(publicityEducationAnnexList!=null && publicityEducationAnnexList.size()>0) {
			for(PublicityEducationAnnex entity:publicityEducationAnnexList) {
				//外键设置
				entity.setMainId(publicityEducation.getId());
				publicityEducationAnnexMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(PublicityEducation publicityEducation,List<PublicityEducationPacpa> publicityEducationPacpaList,List<PublicityEducationAnnex> publicityEducationAnnexList) {
		publicityEducationMapper.updateById(publicityEducation);
		
		//1.先删除子表数据
		publicityEducationPacpaMapper.deleteByMainId(publicityEducation.getId());
		publicityEducationAnnexMapper.deleteByMainId(publicityEducation.getId());
		
		//2.子表数据重新插入
		if(publicityEducationPacpaList!=null && publicityEducationPacpaList.size()>0) {
			for(PublicityEducationPacpa entity:publicityEducationPacpaList) {
				//外键设置
				entity.setMainId(publicityEducation.getId());
				publicityEducationPacpaMapper.insert(entity);
			}
		}
		if(publicityEducationAnnexList!=null && publicityEducationAnnexList.size()>0) {
			for(PublicityEducationAnnex entity:publicityEducationAnnexList) {
				//外键设置
				entity.setMainId(publicityEducation.getId());
				publicityEducationAnnexMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		publicityEducationPacpaMapper.deleteByMainId(id);
		publicityEducationAnnexMapper.deleteByMainId(id);
		publicityEducationMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			publicityEducationPacpaMapper.deleteByMainId(id.toString());
			publicityEducationAnnexMapper.deleteByMainId(id.toString());
			publicityEducationMapper.deleteById(id);
		}
	}
	
}
