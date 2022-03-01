package org.jeecg.modules.smart_reception.service.impl;

import org.jeecg.modules.smart_reception.entity.Smart_8Visitor;
import org.jeecg.modules.smart_reception.mapper.Smart_8VisitorMapper;
import org.jeecg.modules.smart_reception.service.ISmart_8VisitorService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 来访人员信息表
 * @Author: jeecg-boot
 * @Date:   2022-02-28
 * @Version: V1.0
 */
@Service
public class Smart_8VisitorServiceImpl extends ServiceImpl<Smart_8VisitorMapper, Smart_8Visitor> implements ISmart_8VisitorService {
	
	@Autowired
	private Smart_8VisitorMapper smart_8VisitorMapper;
	
	@Override
	public List<Smart_8Visitor> selectByMainId(String mainId) {
		return smart_8VisitorMapper.selectByMainId(mainId);
	}
}
