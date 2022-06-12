package org.jeecg.modules.smartAnswerAssContent.service.impl;

import org.jeecg.modules.smartAnswerAssContent.entity.SmartAnswerFile;
import org.jeecg.modules.smartAnswerAssContent.mapper.SmartAnswerFileMapper;
import org.jeecg.modules.smartAnswerAssContent.service.ISmartAnswerFileService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 要点答题附件
 * @Author: jeecg-boot
 * @Date:   2022-02-21
 * @Version: V1.0
 */
@Service
public class SmartAnswerFileServiceImpl extends ServiceImpl<SmartAnswerFileMapper, SmartAnswerFile> implements ISmartAnswerFileService {
	
	@Autowired
	private SmartAnswerFileMapper smartAnswerFileMapper;
	
	@Override
	public List<SmartAnswerFile> selectByMainId(String mainId) {
		return smartAnswerFileMapper.selectByMainId(mainId);
	}
}
