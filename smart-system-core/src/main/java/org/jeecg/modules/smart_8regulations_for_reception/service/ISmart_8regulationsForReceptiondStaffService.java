package org.jeecg.modules.smart_8regulations_for_reception.service;

import org.jeecg.modules.smart_8regulations_for_reception.entity.Smart_8regulationsForReceptiondStaff;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 八项规定公务接待陪同人员信息
 * @Author: jeecg-boot
 * @Date:   2021-11-11
 * @Version: V1.0
 */
public interface ISmart_8regulationsForReceptiondStaffService extends IService<Smart_8regulationsForReceptiondStaff> {

	public List<Smart_8regulationsForReceptiondStaff> selectByMainId(String mainId);
}
