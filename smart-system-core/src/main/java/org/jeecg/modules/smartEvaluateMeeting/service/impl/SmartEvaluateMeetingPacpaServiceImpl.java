package org.jeecg.modules.smartEvaluateMeeting.service.impl;

import org.jeecg.modules.smartEvaluateMeeting.entity.SmartEvaluateMeetingPacpa;
import org.jeecg.modules.smartEvaluateMeeting.mapper.SmartEvaluateMeetingPacpaMapper;
import org.jeecg.modules.smartEvaluateMeeting.service.ISmartEvaluateMeetingPacpaService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 述责述廉参与人表
 * @Author: jeecg-boot
 * @Date:   2021-11-03
 * @Version: V1.0
 */
@Service
public class SmartEvaluateMeetingPacpaServiceImpl extends ServiceImpl<SmartEvaluateMeetingPacpaMapper, SmartEvaluateMeetingPacpa> implements ISmartEvaluateMeetingPacpaService {
	
	@Autowired
	private SmartEvaluateMeetingPacpaMapper smartEvaluateMeetingPacpaMapper;
	
	@Override
	public List<SmartEvaluateMeetingPacpa> selectByMainId(String mainId) {
		return smartEvaluateMeetingPacpaMapper.selectByMainId(mainId);
	}
}
