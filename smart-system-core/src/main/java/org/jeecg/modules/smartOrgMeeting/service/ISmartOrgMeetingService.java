package org.jeecg.modules.smartOrgMeeting.service;

import org.jeecg.modules.smartOrgMeeting.entity.SmartOrgMeetingPacpa;
import org.jeecg.modules.smartOrgMeeting.entity.SmartOrgMeeting;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 组织生活会
 * @Author: jeecg-boot
 * @Date:   2022-02-26
 * @Version: V1.0
 */
public interface ISmartOrgMeetingService extends IService<SmartOrgMeeting> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(SmartOrgMeeting smartOrgMeeting,List<SmartOrgMeetingPacpa> smartOrgMeetingPacpaList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(SmartOrgMeeting smartOrgMeeting,List<SmartOrgMeetingPacpa> smartOrgMeetingPacpaList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
