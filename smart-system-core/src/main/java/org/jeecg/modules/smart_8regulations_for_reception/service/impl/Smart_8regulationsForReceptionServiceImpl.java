package org.jeecg.modules.smart_8regulations_for_reception.service.impl;

import org.jeecg.modules.smart_8regulations_for_reception.entity.Smart_8regulationsForReception;
import org.jeecg.modules.smart_8regulations_for_reception.entity.Smart_8regulationsForReceptionStaff;
import org.jeecg.modules.smart_8regulations_for_reception.entity.Smart_8regulationsForReceptiondStaff;
import org.jeecg.modules.smart_8regulations_for_reception.entity.Smart_8regulationsForReceptionActivity;
import org.jeecg.modules.smart_8regulations_for_reception.entity.Smart_8regulationsForReceptionAppendix;
import org.jeecg.modules.smart_8regulations_for_reception.mapper.Smart_8regulationsForReceptionStaffMapper;
import org.jeecg.modules.smart_8regulations_for_reception.mapper.Smart_8regulationsForReceptiondStaffMapper;
import org.jeecg.modules.smart_8regulations_for_reception.mapper.Smart_8regulationsForReceptionActivityMapper;
import org.jeecg.modules.smart_8regulations_for_reception.mapper.Smart_8regulationsForReceptionAppendixMapper;
import org.jeecg.modules.smart_8regulations_for_reception.mapper.Smart_8regulationsForReceptionMapper;
import org.jeecg.modules.smart_8regulations_for_reception.service.ISmart_8regulationsForReceptionService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 八项规定公务接待
 * @Author: jeecg-boot
 * @Date:   2021-11-08
 * @Version: V1.0
 */
@Service
public class Smart_8regulationsForReceptionServiceImpl extends ServiceImpl<Smart_8regulationsForReceptionMapper, Smart_8regulationsForReception> implements ISmart_8regulationsForReceptionService {

	@Autowired
	private Smart_8regulationsForReceptionMapper smart_8regulationsForReceptionMapper;
	@Autowired
	private Smart_8regulationsForReceptionStaffMapper smart_8regulationsForReceptionStaffMapper;
	@Autowired
	private Smart_8regulationsForReceptiondStaffMapper smart_8regulationsForReceptiondStaffMapper;
	@Autowired
	private Smart_8regulationsForReceptionActivityMapper smart_8regulationsForReceptionActivityMapper;
	@Autowired
	private Smart_8regulationsForReceptionAppendixMapper smart_8regulationsForReceptionAppendixMapper;
	
	@Override
	@Transactional
	public void saveMain(Smart_8regulationsForReception smart_8regulationsForReception, List<Smart_8regulationsForReceptionStaff> smart_8regulationsForReceptionStaffList,List<Smart_8regulationsForReceptiondStaff> smart_8regulationsForReceptiondStaffList,List<Smart_8regulationsForReceptionActivity> smart_8regulationsForReceptionActivityList,List<Smart_8regulationsForReceptionAppendix> smart_8regulationsForReceptionAppendixList) {
		smart_8regulationsForReceptionMapper.insert(smart_8regulationsForReception);
		if(smart_8regulationsForReceptionStaffList!=null && smart_8regulationsForReceptionStaffList.size()>0) {
			for(Smart_8regulationsForReceptionStaff entity:smart_8regulationsForReceptionStaffList) {
				//外键设置
				entity.setMainId(smart_8regulationsForReception.getId());
				smart_8regulationsForReceptionStaffMapper.insert(entity);
			}
		}
		if(smart_8regulationsForReceptiondStaffList!=null && smart_8regulationsForReceptiondStaffList.size()>0) {
			for(Smart_8regulationsForReceptiondStaff entity:smart_8regulationsForReceptiondStaffList) {
				//外键设置
				entity.setMainId(smart_8regulationsForReception.getId());
				smart_8regulationsForReceptiondStaffMapper.insert(entity);
			}
		}
		if(smart_8regulationsForReceptionActivityList!=null && smart_8regulationsForReceptionActivityList.size()>0) {
			for(Smart_8regulationsForReceptionActivity entity:smart_8regulationsForReceptionActivityList) {
				//外键设置
				entity.setMainId(smart_8regulationsForReception.getId());
				smart_8regulationsForReceptionActivityMapper.insert(entity);
			}
		}
		if(smart_8regulationsForReceptionAppendixList!=null && smart_8regulationsForReceptionAppendixList.size()>0) {
			for(Smart_8regulationsForReceptionAppendix entity:smart_8regulationsForReceptionAppendixList) {
				//外键设置
				entity.setMianId(smart_8regulationsForReception.getId());
				smart_8regulationsForReceptionAppendixMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(Smart_8regulationsForReception smart_8regulationsForReception,List<Smart_8regulationsForReceptionStaff> smart_8regulationsForReceptionStaffList,List<Smart_8regulationsForReceptiondStaff> smart_8regulationsForReceptiondStaffList,List<Smart_8regulationsForReceptionActivity> smart_8regulationsForReceptionActivityList,List<Smart_8regulationsForReceptionAppendix> smart_8regulationsForReceptionAppendixList) {
		smart_8regulationsForReceptionMapper.updateById(smart_8regulationsForReception);
		
		//1.先删除子表数据
		smart_8regulationsForReceptionStaffMapper.deleteByMainId(smart_8regulationsForReception.getId());
		smart_8regulationsForReceptiondStaffMapper.deleteByMainId(smart_8regulationsForReception.getId());
		smart_8regulationsForReceptionActivityMapper.deleteByMainId(smart_8regulationsForReception.getId());
		smart_8regulationsForReceptionAppendixMapper.deleteByMainId(smart_8regulationsForReception.getId());
		
		//2.子表数据重新插入
		if(smart_8regulationsForReceptionStaffList!=null && smart_8regulationsForReceptionStaffList.size()>0) {
			for(Smart_8regulationsForReceptionStaff entity:smart_8regulationsForReceptionStaffList) {
				//外键设置
				entity.setMainId(smart_8regulationsForReception.getId());
				smart_8regulationsForReceptionStaffMapper.insert(entity);
			}
		}
		if(smart_8regulationsForReceptiondStaffList!=null && smart_8regulationsForReceptiondStaffList.size()>0) {
			for(Smart_8regulationsForReceptiondStaff entity:smart_8regulationsForReceptiondStaffList) {
				//外键设置
				entity.setMainId(smart_8regulationsForReception.getId());
				smart_8regulationsForReceptiondStaffMapper.insert(entity);
			}
		}
		if(smart_8regulationsForReceptionActivityList!=null && smart_8regulationsForReceptionActivityList.size()>0) {
			for(Smart_8regulationsForReceptionActivity entity:smart_8regulationsForReceptionActivityList) {
				//外键设置
				entity.setMainId(smart_8regulationsForReception.getId());
				smart_8regulationsForReceptionActivityMapper.insert(entity);
			}
		}
		if(smart_8regulationsForReceptionAppendixList!=null && smart_8regulationsForReceptionAppendixList.size()>0) {
			for(Smart_8regulationsForReceptionAppendix entity:smart_8regulationsForReceptionAppendixList) {
				//外键设置
				entity.setMianId(smart_8regulationsForReception.getId());
				smart_8regulationsForReceptionAppendixMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		smart_8regulationsForReceptionStaffMapper.deleteByMainId(id);
		smart_8regulationsForReceptiondStaffMapper.deleteByMainId(id);
		smart_8regulationsForReceptionActivityMapper.deleteByMainId(id);
		smart_8regulationsForReceptionAppendixMapper.deleteByMainId(id);
		smart_8regulationsForReceptionMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			smart_8regulationsForReceptionStaffMapper.deleteByMainId(id.toString());
			smart_8regulationsForReceptiondStaffMapper.deleteByMainId(id.toString());
			smart_8regulationsForReceptionActivityMapper.deleteByMainId(id.toString());
			smart_8regulationsForReceptionAppendixMapper.deleteByMainId(id.toString());
			smart_8regulationsForReceptionMapper.deleteById(id);
		}
	}
	
}
