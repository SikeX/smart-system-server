package org.jeecg.modules.smart_8regulations_for_reception.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.smart_8regulations_for_reception.entity.Smart_8regulationsForReceptionStaff;

import java.util.List;

/**
 * @Description: 八项规定公务接待人员信息
 * @Author: jeecg-boot
 * @Date:   2021-11-11
 * @Version: V1.0
 */
public interface ISmart_8regulationsForReceptionStaffService extends IService<Smart_8regulationsForReceptionStaff> {

	public List<Smart_8regulationsForReceptionStaff> selectByMainId(String mainId);
}
