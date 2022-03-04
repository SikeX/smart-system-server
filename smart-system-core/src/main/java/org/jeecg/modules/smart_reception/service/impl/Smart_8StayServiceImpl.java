package org.jeecg.modules.smart_reception.service.impl;

import org.jeecg.modules.smart_reception.entity.Smart_8Stay;
import org.jeecg.modules.smart_reception.mapper.Smart_8StayMapper;
import org.jeecg.modules.smart_reception.service.ISmart_8StayService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 住宿信息
 * @Author: jeecg-boot
 * @Date:   2022-02-28
 * @Version: V1.0
 */
@Service
public class Smart_8StayServiceImpl extends ServiceImpl<Smart_8StayMapper, Smart_8Stay> implements ISmart_8StayService {
	
	@Autowired
	private Smart_8StayMapper smart_8StayMapper;
	
	@Override
	public List<Smart_8Stay> selectByMainId(String mainId) {
		return smart_8StayMapper.selectByMainId(mainId);
	}
}
