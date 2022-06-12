package org.jeecg.modules.smartDemocraticLifeMeeting.service;

import org.jeecg.modules.smartDemocraticLifeMeeting.entity.SmartDemocraticLifePeople;
import org.jeecg.modules.smartDemocraticLifeMeeting.entity.SmartDemocraticLifeMeeting;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 民主生活会表
 * @Author: jeecg-boot
 * @Date:   2022-02-26
 * @Version: V1.0
 */
public interface ISmartDemocraticLifeMeetingService extends IService<SmartDemocraticLifeMeeting> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(SmartDemocraticLifeMeeting smartDemocraticLifeMeeting,List<SmartDemocraticLifePeople> smartDemocraticLifePeopleList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(SmartDemocraticLifeMeeting smartDemocraticLifeMeeting,List<SmartDemocraticLifePeople> smartDemocraticLifePeopleList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
