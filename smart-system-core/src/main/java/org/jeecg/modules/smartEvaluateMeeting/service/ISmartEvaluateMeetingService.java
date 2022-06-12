package org.jeecg.modules.smartEvaluateMeeting.service;

import org.jeecg.modules.smartEvaluateMeeting.entity.SmartEvaluateMeetingPacpa;
import org.jeecg.modules.smartEvaluateMeeting.entity.SmartEvaluateMeetingAnnex;
import org.jeecg.modules.smartEvaluateMeeting.entity.SmartEvaluateMeeting;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 述责述廉表
 * @Author: jeecg-boot
 * @Date:   2021-11-11
 * @Version: V1.0
 */
public interface ISmartEvaluateMeetingService extends IService<SmartEvaluateMeeting> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(SmartEvaluateMeeting smartEvaluateMeeting,List<SmartEvaluateMeetingPacpa> smartEvaluateMeetingPacpaList,List<SmartEvaluateMeetingAnnex> smartEvaluateMeetingAnnexList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(SmartEvaluateMeeting smartEvaluateMeeting,List<SmartEvaluateMeetingPacpa> smartEvaluateMeetingPacpaList,List<SmartEvaluateMeetingAnnex> smartEvaluateMeetingAnnexList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
