package org.jeecg.modules.smart_reception.service;

import org.jeecg.modules.smartReportingInformation.entity.SysRole;
import org.jeecg.modules.smart_reception.entity.Smart_8Dining;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 用餐情况
 * @Author: jeecg-boot
 * @Date:   2022-02-28
 * @Version: V1.0
 */
public interface ISmart_8DiningService extends IService<Smart_8Dining> {

	public List<Smart_8Dining> selectByMainId(String mainId);

	List<Smart_8Dining> sendInformation();

	List<SysRole> getUser();

	String getDepartmentIdBymainId(String mainId);

	String getReceptionDepBymainId(String mainId);

	String getDepartmentNameById(String departmentId);

	String getUserNameByUsername(String username);

	String getMainIdById(String id);
}
