package org.jeecg.modules.smart_reception.service.impl;

import org.jeecg.modules.smart_reception.entity.Smart_8List;
import org.jeecg.modules.smart_reception.mapper.Smart_8ListMapper;
import org.jeecg.modules.smart_reception.service.ISmart_8ListService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 接待清单
 * @Author: jeecg-boot
 * @Date:   2022-02-28
 * @Version: V1.0
 */
@Service
public class Smart_8ListServiceImpl extends ServiceImpl<Smart_8ListMapper, Smart_8List> implements ISmart_8ListService {
	
	@Autowired
	private Smart_8ListMapper smart_8ListMapper;
	
	@Override
	public List<Smart_8List> selectByMainId(String mainId) {
		return smart_8ListMapper.selectByMainId(mainId);
	}
}
