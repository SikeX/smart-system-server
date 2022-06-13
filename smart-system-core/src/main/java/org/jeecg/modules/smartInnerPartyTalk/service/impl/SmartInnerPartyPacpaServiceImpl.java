package org.jeecg.modules.smartInnerPartyTalk.service.impl;

import org.jeecg.modules.smartInnerPartyTalk.entity.SmartInnerPartyPacpa;
import org.jeecg.modules.smartInnerPartyTalk.mapper.SmartInnerPartyPacpaMapper;
import org.jeecg.modules.smartInnerPartyTalk.service.ISmartInnerPartyPacpaService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 党内谈话参与人表
 * @Author: jeecg-boot
 * @Date:   2021-11-01
 * @Version: V1.0
 */
@Service
public class SmartInnerPartyPacpaServiceImpl extends ServiceImpl<SmartInnerPartyPacpaMapper, SmartInnerPartyPacpa> implements ISmartInnerPartyPacpaService {
	
	@Autowired
	private SmartInnerPartyPacpaMapper smartInnerPartyPacpaMapper;
	
	@Override
	public List<SmartInnerPartyPacpa> selectByMainId(String mainId) {
		return smartInnerPartyPacpaMapper.selectByMainId(mainId);
	}
}
