package org.jeecg.modules.SmartInnerPartyTalk.service.impl;

import org.jeecg.modules.SmartInnerPartyTalk.entity.SmartInnerPartyAnnex;
import org.jeecg.modules.SmartInnerPartyTalk.mapper.SmartInnerPartyAnnexMapper;
import org.jeecg.modules.SmartInnerPartyTalk.service.ISmartInnerPartyAnnexService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 党内谈话附件表
 * @Author: jeecg-boot
 * @Date:   2021-11-05
 * @Version: V1.0
 */
@Service
public class SmartInnerPartyAnnexServiceImpl extends ServiceImpl<SmartInnerPartyAnnexMapper, SmartInnerPartyAnnex> implements ISmartInnerPartyAnnexService {
	
	@Autowired
	private SmartInnerPartyAnnexMapper smartInnerPartyAnnexMapper;
	
	@Override
	public List<SmartInnerPartyAnnex> selectByMainId(String mainId) {
		return smartInnerPartyAnnexMapper.selectByMainId(mainId);
	}
}
