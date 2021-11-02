package org.jeecg.modules.smartEvaluateMeeting.service.impl;

import org.jeecg.modules.smartEvaluateMeeting.entity.SmartEvaluateMeetingAnnex;
import org.jeecg.modules.smartEvaluateMeeting.mapper.SmartEvaluateMeetingAnnexMapper;
import org.jeecg.modules.smartEvaluateMeeting.service.ISmartEvaluateMeetingAnnexService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 述责述廉附件表
 * @Author: jeecg-boot
 * @Date:   2021-11-02
 * @Version: V1.0
 */
@Service
public class SmartEvaluateMeetingAnnexServiceImpl extends ServiceImpl<SmartEvaluateMeetingAnnexMapper, SmartEvaluateMeetingAnnex> implements ISmartEvaluateMeetingAnnexService {
	
	@Autowired
	private SmartEvaluateMeetingAnnexMapper smartEvaluateMeetingAnnexMapper;
	
	@Override
	public List<SmartEvaluateMeetingAnnex> selectByMainId(String mainId) {
		return smartEvaluateMeetingAnnexMapper.selectByMainId(mainId);
	}
}
