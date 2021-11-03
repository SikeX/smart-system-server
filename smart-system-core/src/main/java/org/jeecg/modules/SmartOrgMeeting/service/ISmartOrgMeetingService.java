package org.jeecg.modules.SmartOrgMeeting.service;

import org.jeecg.modules.SmartOrgMeeting.entity.SmartOrgMeetingPacpa;
import org.jeecg.modules.SmartOrgMeeting.entity.SmartOrgMeetingAnnex;
import org.jeecg.modules.SmartOrgMeeting.entity.SmartOrgMeeting;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 组织生活会
 * @Author: jeecg-boot
 * @Date:   2021-11-03
 * @Version: V1.0
 */
public interface ISmartOrgMeetingService extends IService<SmartOrgMeeting> {

	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);


}
