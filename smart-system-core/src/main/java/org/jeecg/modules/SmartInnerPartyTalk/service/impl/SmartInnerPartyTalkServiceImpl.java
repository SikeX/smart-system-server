package org.jeecg.modules.SmartInnerPartyTalk.service.impl;

import org.jeecg.modules.SmartInnerPartyTalk.entity.SmartInnerPartyTalk;
import org.jeecg.modules.SmartInnerPartyTalk.entity.SmartInnerPartyPacpa;
import org.jeecg.modules.SmartInnerPartyTalk.entity.SmartInnerPartyAnnex;
import org.jeecg.modules.SmartInnerPartyTalk.mapper.SmartInnerPartyPacpaMapper;
import org.jeecg.modules.SmartInnerPartyTalk.mapper.SmartInnerPartyAnnexMapper;
import org.jeecg.modules.SmartInnerPartyTalk.mapper.SmartInnerPartyTalkMapper;
import org.jeecg.modules.SmartInnerPartyTalk.service.ISmartInnerPartyTalkService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 党内谈话表
 * @Author: jeecg-boot
 * @Date:   2021-11-10
 * @Version: V1.0
 */
@Service
public class SmartInnerPartyTalkServiceImpl extends ServiceImpl<SmartInnerPartyTalkMapper, SmartInnerPartyTalk> implements ISmartInnerPartyTalkService {

	@Autowired
	private SmartInnerPartyTalkMapper smartInnerPartyTalkMapper;
	@Autowired
	private SmartInnerPartyPacpaMapper smartInnerPartyPacpaMapper;
	@Autowired
	private SmartInnerPartyAnnexMapper smartInnerPartyAnnexMapper;
	
	@Override
	@Transactional
	public void saveMain(SmartInnerPartyTalk smartInnerPartyTalk, List<SmartInnerPartyPacpa> smartInnerPartyPacpaList,List<SmartInnerPartyAnnex> smartInnerPartyAnnexList) {
		smartInnerPartyTalkMapper.insert(smartInnerPartyTalk);
		if(smartInnerPartyPacpaList!=null && smartInnerPartyPacpaList.size()>0) {
			for(SmartInnerPartyPacpa entity:smartInnerPartyPacpaList) {
				//外键设置
				entity.setMainId(smartInnerPartyTalk.getId());
				smartInnerPartyPacpaMapper.insert(entity);
			}
		}
		if(smartInnerPartyAnnexList!=null && smartInnerPartyAnnexList.size()>0) {
			for(SmartInnerPartyAnnex entity:smartInnerPartyAnnexList) {
				//外键设置
				entity.setMainId(smartInnerPartyTalk.getId());
				smartInnerPartyAnnexMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(SmartInnerPartyTalk smartInnerPartyTalk,List<SmartInnerPartyPacpa> smartInnerPartyPacpaList,List<SmartInnerPartyAnnex> smartInnerPartyAnnexList) {
		smartInnerPartyTalkMapper.updateById(smartInnerPartyTalk);
		
		//1.先删除子表数据
		smartInnerPartyPacpaMapper.deleteByMainId(smartInnerPartyTalk.getId());
		smartInnerPartyAnnexMapper.deleteByMainId(smartInnerPartyTalk.getId());
		
		//2.子表数据重新插入
		if(smartInnerPartyPacpaList!=null && smartInnerPartyPacpaList.size()>0) {
			for(SmartInnerPartyPacpa entity:smartInnerPartyPacpaList) {
				//外键设置
				entity.setMainId(smartInnerPartyTalk.getId());
				smartInnerPartyPacpaMapper.insert(entity);
			}
		}
		if(smartInnerPartyAnnexList!=null && smartInnerPartyAnnexList.size()>0) {
			for(SmartInnerPartyAnnex entity:smartInnerPartyAnnexList) {
				//外键设置
				entity.setMainId(smartInnerPartyTalk.getId());
				smartInnerPartyAnnexMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		smartInnerPartyPacpaMapper.deleteByMainId(id);
		smartInnerPartyAnnexMapper.deleteByMainId(id);
		smartInnerPartyTalkMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			smartInnerPartyPacpaMapper.deleteByMainId(id.toString());
			smartInnerPartyAnnexMapper.deleteByMainId(id.toString());
			smartInnerPartyTalkMapper.deleteById(id);
		}
	}

}
