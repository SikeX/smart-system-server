package org.jeecg.modules.smart_8regulations_for_reception.service;

import org.jeecg.modules.smart_8regulations_for_reception.entity.Smart_8regulationsForReceptionStaff;
import org.jeecg.modules.smart_8regulations_for_reception.entity.Smart_8regulationsForReceptiondStaff;
import org.jeecg.modules.smart_8regulations_for_reception.entity.Smart_8regulationsForReceptionActivity;
import org.jeecg.modules.smart_8regulations_for_reception.entity.Smart_8regulationsForReceptionAppendix;
import org.jeecg.modules.smart_8regulations_for_reception.entity.Smart_8regulationsForReception;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 八项规定公务接待
 * @Author: jeecg-boot
 * @Date:   2021-11-08
 * @Version: V1.0
 */
public interface ISmart_8regulationsForReceptionService extends IService<Smart_8regulationsForReception> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(Smart_8regulationsForReception smart_8regulationsForReception,List<Smart_8regulationsForReceptionStaff> smart_8regulationsForReceptionStaffList,List<Smart_8regulationsForReceptiondStaff> smart_8regulationsForReceptiondStaffList,List<Smart_8regulationsForReceptionActivity> smart_8regulationsForReceptionActivityList,List<Smart_8regulationsForReceptionAppendix> smart_8regulationsForReceptionAppendixList) ;
	
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
	
}
