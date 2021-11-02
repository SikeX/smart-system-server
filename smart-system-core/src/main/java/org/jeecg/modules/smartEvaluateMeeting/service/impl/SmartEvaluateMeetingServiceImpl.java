package org.jeecg.modules.smartEvaluateMeeting.service.impl;

import org.jeecg.modules.smartEvaluateMeeting.entity.SmartEvaluateMeeting;
import org.jeecg.modules.smartEvaluateMeeting.entity.SmartEvaluateMeetingPacpa;
import org.jeecg.modules.smartEvaluateMeeting.entity.SmartEvaluateMeetingAnnex;
import org.jeecg.modules.smartEvaluateMeeting.mapper.SmartEvaluateMeetingPacpaMapper;
import org.jeecg.modules.smartEvaluateMeeting.mapper.SmartEvaluateMeetingAnnexMapper;
import org.jeecg.modules.smartEvaluateMeeting.mapper.SmartEvaluateMeetingMapper;
import org.jeecg.modules.smartEvaluateMeeting.service.ISmartEvaluateMeetingService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 述责述廉表
 * @Author: jeecg-boot
 * @Date:   2021-11-02
 * @Version: V1.0
 */
@Service
public class SmartEvaluateMeetingServiceImpl extends ServiceImpl<SmartEvaluateMeetingMapper, SmartEvaluateMeeting> implements ISmartEvaluateMeetingService {

	@Autowired
	private SmartEvaluateMeetingMapper smartEvaluateMeetingMapper;
	@Autowired
	private SmartEvaluateMeetingPacpaMapper smartEvaluateMeetingPacpaMapper;
	@Autowired
	private SmartEvaluateMeetingAnnexMapper smartEvaluateMeetingAnnexMapper;
	
	@Override
	@Transactional
	public void saveMain(SmartEvaluateMeeting smartEvaluateMeeting, List<SmartEvaluateMeetingPacpa> smartEvaluateMeetingPacpaList,List<SmartEvaluateMeetingAnnex> smartEvaluateMeetingAnnexList) {
		smartEvaluateMeetingMapper.insert(smartEvaluateMeeting);
		if(smartEvaluateMeetingPacpaList!=null && smartEvaluateMeetingPacpaList.size()>0) {
			for(SmartEvaluateMeetingPacpa entity:smartEvaluateMeetingPacpaList) {
				//外键设置
				entity.setParentId(smartEvaluateMeeting.getId());
				smartEvaluateMeetingPacpaMapper.insert(entity);
			}
		}
		if(smartEvaluateMeetingAnnexList!=null && smartEvaluateMeetingAnnexList.size()>0) {
			for(SmartEvaluateMeetingAnnex entity:smartEvaluateMeetingAnnexList) {
				//外键设置
				entity.setParentId(smartEvaluateMeeting.getId());
				smartEvaluateMeetingAnnexMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(SmartEvaluateMeeting smartEvaluateMeeting,List<SmartEvaluateMeetingPacpa> smartEvaluateMeetingPacpaList,List<SmartEvaluateMeetingAnnex> smartEvaluateMeetingAnnexList) {
		smartEvaluateMeetingMapper.updateById(smartEvaluateMeeting);
		
		//1.先删除子表数据
		smartEvaluateMeetingPacpaMapper.deleteByMainId(smartEvaluateMeeting.getId());
		smartEvaluateMeetingAnnexMapper.deleteByMainId(smartEvaluateMeeting.getId());
		
		//2.子表数据重新插入
		if(smartEvaluateMeetingPacpaList!=null && smartEvaluateMeetingPacpaList.size()>0) {
			for(SmartEvaluateMeetingPacpa entity:smartEvaluateMeetingPacpaList) {
				//外键设置
				entity.setParentId(smartEvaluateMeeting.getId());
				smartEvaluateMeetingPacpaMapper.insert(entity);
			}
		}
		if(smartEvaluateMeetingAnnexList!=null && smartEvaluateMeetingAnnexList.size()>0) {
			for(SmartEvaluateMeetingAnnex entity:smartEvaluateMeetingAnnexList) {
				//外键设置
				entity.setParentId(smartEvaluateMeeting.getId());
				smartEvaluateMeetingAnnexMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		smartEvaluateMeetingPacpaMapper.deleteByMainId(id);
		smartEvaluateMeetingAnnexMapper.deleteByMainId(id);
		smartEvaluateMeetingMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			smartEvaluateMeetingPacpaMapper.deleteByMainId(id.toString());
			smartEvaluateMeetingAnnexMapper.deleteByMainId(id.toString());
			smartEvaluateMeetingMapper.deleteById(id);
		}
	}
	
}
