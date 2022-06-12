package org.jeecg.modules.smart_8regulations_for_reception.service.impl;

import org.jeecg.modules.smart_8regulations_for_reception.entity.Smart_8regulationsForReceptionStaff;
import org.jeecg.modules.smart_8regulations_for_reception.mapper.Smart_8regulationsForReceptionStaffMapper;
import org.jeecg.modules.smart_8regulations_for_reception.service.ISmart_8regulationsForReceptionStaffService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 八项规定公务接待人员信息
 * @Author: jeecg-boot
 * @Date:   2021-11-11
 * @Version: V1.0
 */
@Service
public class Smart_8regulationsForReceptionStaffServiceImpl extends ServiceImpl<Smart_8regulationsForReceptionStaffMapper, Smart_8regulationsForReceptionStaff> implements ISmart_8regulationsForReceptionStaffService {

	@Autowired
	private Smart_8regulationsForReceptionStaffMapper smart_8regulationsForReceptionStaffMapper;

	@Override
	public List<Smart_8regulationsForReceptionStaff> selectByMainId(String mainId) {
		return smart_8regulationsForReceptionStaffMapper.selectByMainId(mainId);
	}
}
