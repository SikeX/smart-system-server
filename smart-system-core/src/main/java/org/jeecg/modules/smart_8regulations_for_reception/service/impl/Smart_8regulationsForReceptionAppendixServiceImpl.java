package org.jeecg.modules.smart_8regulations_for_reception.service.impl;

import org.jeecg.modules.smart_8regulations_for_reception.entity.Smart_8regulationsForReceptionAppendix;
import org.jeecg.modules.smart_8regulations_for_reception.mapper.Smart_8regulationsForReceptionAppendixMapper;
import org.jeecg.modules.smart_8regulations_for_reception.service.ISmart_8regulationsForReceptionAppendixService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 八项规定公务接待信息附件表
 * @Author: jeecg-boot
 * @Date:   2021-11-11
 * @Version: V1.0
 */
@Service
public class Smart_8regulationsForReceptionAppendixServiceImpl extends ServiceImpl<Smart_8regulationsForReceptionAppendixMapper, Smart_8regulationsForReceptionAppendix> implements ISmart_8regulationsForReceptionAppendixService {
	
	@Autowired
	private Smart_8regulationsForReceptionAppendixMapper smart_8regulationsForReceptionAppendixMapper;
	
	@Override
	public List<Smart_8regulationsForReceptionAppendix> selectByMainId(String mainId) {
		return smart_8regulationsForReceptionAppendixMapper.selectByMainId(mainId);
	}
}
