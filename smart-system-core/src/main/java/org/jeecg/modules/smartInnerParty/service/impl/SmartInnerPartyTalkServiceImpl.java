package org.jeecg.modules.smartInnerParty.service.impl;

import org.jeecg.modules.smartInnerParty.entity.SmartInnerPartyTalk;
import org.jeecg.modules.smartInnerParty.entity.SmartInnerPartyAnnex;
import org.jeecg.modules.smartInnerParty.entity.SmartInnerPartyPacpa;
import org.jeecg.modules.smartInnerParty.mapper.SmartInnerPartyAnnexMapper;
import org.jeecg.modules.smartInnerParty.mapper.SmartInnerPartyPacpaMapper;
import org.jeecg.modules.smartInnerParty.mapper.SmartInnerPartyTalkMapper;
import org.jeecg.modules.smartInnerParty.service.ISmartInnerPartyTalkService;
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
 * @Date:   2021-11-02
 * @Version: V1.0
 */
@Service
public class SmartInnerPartyTalkServiceImpl extends ServiceImpl<SmartInnerPartyTalkMapper, SmartInnerPartyTalk> implements ISmartInnerPartyTalkService {

	@Autowired
	private SmartInnerPartyTalkMapper smartInnerPartyTalkMapper;
	@Autowired
	private SmartInnerPartyAnnexMapper smartInnerPartyAnnexMapper;
	@Autowired
	private SmartInnerPartyPacpaMapper smartInnerPartyPacpaMapper;
	
	@Override
	@Transactional
	public void saveMain(SmartInnerPartyTalk smartInnerPartyTalk, List<SmartInnerPartyAnnex> smartInnerPartyAnnexList,List<SmartInnerPartyPacpa> smartInnerPartyPacpaList) {
		smartInnerPartyTalkMapper.insert(smartInnerPartyTalk);
		if(smartInnerPartyAnnexList!=null && smartInnerPartyAnnexList.size()>0) {
			for(SmartInnerPartyAnnex entity:smartInnerPartyAnnexList) {
				//外键设置
				entity.setInnerPartyId(smartInnerPartyTalk.getId());
				smartInnerPartyAnnexMapper.insert(entity);
			}
		}
		if(smartInnerPartyPacpaList!=null && smartInnerPartyPacpaList.size()>0) {
			for(SmartInnerPartyPacpa entity:smartInnerPartyPacpaList) {
				//外键设置
				entity.setInnerPartyId(smartInnerPartyTalk.getId());
				smartInnerPartyPacpaMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(SmartInnerPartyTalk smartInnerPartyTalk,List<SmartInnerPartyAnnex> smartInnerPartyAnnexList,List<SmartInnerPartyPacpa> smartInnerPartyPacpaList) {
		smartInnerPartyTalkMapper.updateById(smartInnerPartyTalk);
		
		//1.先删除子表数据
		smartInnerPartyAnnexMapper.deleteByMainId(smartInnerPartyTalk.getId());
		smartInnerPartyPacpaMapper.deleteByMainId(smartInnerPartyTalk.getId());
		
		//2.子表数据重新插入
		if(smartInnerPartyAnnexList!=null && smartInnerPartyAnnexList.size()>0) {
			for(SmartInnerPartyAnnex entity:smartInnerPartyAnnexList) {
				//外键设置
				entity.setInnerPartyId(smartInnerPartyTalk.getId());
				smartInnerPartyAnnexMapper.insert(entity);
			}
		}
		if(smartInnerPartyPacpaList!=null && smartInnerPartyPacpaList.size()>0) {
			for(SmartInnerPartyPacpa entity:smartInnerPartyPacpaList) {
				//外键设置
				entity.setInnerPartyId(smartInnerPartyTalk.getId());
				smartInnerPartyPacpaMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		smartInnerPartyAnnexMapper.deleteByMainId(id);
		smartInnerPartyPacpaMapper.deleteByMainId(id);
		smartInnerPartyTalkMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			smartInnerPartyAnnexMapper.deleteByMainId(id.toString());
			smartInnerPartyPacpaMapper.deleteByMainId(id.toString());
			smartInnerPartyTalkMapper.deleteById(id);
		}
	}
	
}
