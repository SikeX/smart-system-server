package org.jeecg.modules.smart_8regulations_for_reception.service.impl;

import org.jeecg.modules.smart_8regulations_for_reception.entity.Smart_8regulationsForReceptionActivity;
import org.jeecg.modules.smart_8regulations_for_reception.mapper.Smart_8regulationsForReceptionActivityMapper;
import org.jeecg.modules.smart_8regulations_for_reception.service.ISmart_8regulationsForReceptionActivityService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 八项规定公务接待公务活动项目
 * @Author: jeecg-boot
 * @Date:   2021-11-11
 * @Version: V1.0
 */
@Service
public class Smart_8regulationsForReceptionActivityServiceImpl extends ServiceImpl<Smart_8regulationsForReceptionActivityMapper, Smart_8regulationsForReceptionActivity> implements ISmart_8regulationsForReceptionActivityService {
	
	@Autowired
	private Smart_8regulationsForReceptionActivityMapper smart_8regulationsForReceptionActivityMapper;
	
	@Override
	public List<Smart_8regulationsForReceptionActivity> selectByMainId(String mainId) {
		return smart_8regulationsForReceptionActivityMapper.selectByMainId(mainId);
	}
}
