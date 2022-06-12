package org.jeecg.modules.smartAnswerAssContent.service.impl;

import org.jeecg.modules.smartAnswerAssContent.entity.SmartAnswerAssScore;
import org.jeecg.modules.smartAnswerAssContent.mapper.SmartAnswerAssScoreMapper;
import org.jeecg.modules.smartAnswerAssContent.service.ISmartAnswerAssScoreService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 答题评分表
 * @Author: jeecg-boot
 * @Date:   2022-02-21
 * @Version: V1.0
 */
@Service
public class SmartAnswerAssScoreServiceImpl extends ServiceImpl<SmartAnswerAssScoreMapper, SmartAnswerAssScore> implements ISmartAnswerAssScoreService {
	
	@Autowired
	private SmartAnswerAssScoreMapper smartAnswerAssScoreMapper;
	
	@Override
	public List<SmartAnswerAssScore> selectByMainId(String mainId) {
		return smartAnswerAssScoreMapper.selectByMainId(mainId);
	}
}
