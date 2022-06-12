package org.jeecg.modules.smart_8regulations_for_reception.service.impl;

import org.jeecg.modules.smart_8regulations_for_reception.entity.Smart_8regulationsForReceptiondStaff;
import org.jeecg.modules.smart_8regulations_for_reception.mapper.Smart_8regulationsForReceptiondStaffMapper;
import org.jeecg.modules.smart_8regulations_for_reception.service.ISmart_8regulationsForReceptiondStaffService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 八项规定公务接待陪同人员信息
 * @Author: jeecg-boot
 * @Date:   2021-11-11
 * @Version: V1.0
 */
@Service
public class Smart_8regulationsForReceptiondStaffServiceImpl extends ServiceImpl<Smart_8regulationsForReceptiondStaffMapper, Smart_8regulationsForReceptiondStaff> implements ISmart_8regulationsForReceptiondStaffService {
	
	@Autowired
	private Smart_8regulationsForReceptiondStaffMapper smart_8regulationsForReceptiondStaffMapper;
	
	@Override
	public List<Smart_8regulationsForReceptiondStaff> selectByMainId(String mainId) {
		return smart_8regulationsForReceptiondStaffMapper.selectByMainId(mainId);
	}
}
