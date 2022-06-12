package org.jeecg.modules.smart_reception.mapper;

import java.util.List;

import org.jeecg.modules.smartFuneralReport.entity.SmartFuneralReport;
import org.jeecg.modules.smartReportingInformation.entity.SysRole;
import org.jeecg.modules.smart_reception.entity.Smart_8Dining;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.smart_reception.entity.Smart_8Visitor;

/**
 * @Description: 用餐情况
 * @Author: jeecg-boot
 * @Date:   2022-02-28
 * @Version: V1.0
 */
public interface Smart_8DiningMapper extends BaseMapper<Smart_8Dining> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<Smart_8Dining> selectByMainId(@Param("mainId") String mainId);

	List<Smart_8Dining> sendInformation();

	List<SysRole> getUser();

	String getDepartmentIdBymainId(String mainId);

	String getDepartmentNameById(String departmentId);

	String getReceptionDepBymainId(String mainId);

	String getUserNameByUsername(String username);

	String getMainIdById(String id);
}
