package org.jeecg.modules.smart_8regulations_for_reception.service;

import org.jeecg.modules.smart_8regulations_for_reception.entity.*;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 八项规定公务接待
 * @Author: jeecg-boot
 * @Date:   2021-11-11
 * @Version: V1.0
 */
public interface ISmart_8regulationsForReceptionService extends IService<Smart_8regulationsForReception> {

	/**
	 * 添加一对多
	 *
	 */
	public void saveMain(Smart_8regulationsForReception smart_8regulationsForReception, List<Smart_8regulationsForReceptionStaff> smart_8regulationsForReceptionStaffList, List<Smart_8regulationsForReceptiondStaff> smart_8regulationsForReceptiondStaffList, List<Smart_8regulationsForReceptionActivity> smart_8regulationsForReceptionActivityList, List<Smart_8regulationsForReceptionAppendix> smart_8regulationsForReceptionAppendixList) ;

	/**
	 * 修改一对多
	 *
	 */
	public void updateMain(Smart_8regulationsForReception smart_8regulationsForReception,List<Smart_8regulationsForReceptionStaff> smart_8regulationsForReceptionStaffList,List<Smart_8regulationsForReceptiondStaff> smart_8regulationsForReceptiondStaffList,List<Smart_8regulationsForReceptionActivity> smart_8regulationsForReceptionActivityList,List<Smart_8regulationsForReceptionAppendix> smart_8regulationsForReceptionAppendixList);

	/**
	 * 删除一对多
	 */
	public void delMain (String id);

	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);

	/**
	 * 根据部门编码查询部门ID
	 *
	 * @param orgCode 部门编码
	 * @return 部门ID
	 */
	 String getDepartIdByOrgCode(String orgCode);
	/**
	 * 根据部门编码获所有子部门的ID
	 *
	 * @param orgCode 部门编码
	 * @return 子部门ID列表
	 */
	List<String> getChildrenIdByOrgCode(String orgCode);

}
