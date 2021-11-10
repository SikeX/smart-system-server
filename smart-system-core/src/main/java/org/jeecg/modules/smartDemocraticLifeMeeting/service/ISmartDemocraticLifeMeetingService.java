package org.jeecg.modules.smartDemocraticLifeMeeting.service;

import org.jeecg.modules.smartDemocraticLifeMeeting.entity.SmartDemocraticLifePeople;
import org.jeecg.modules.smartDemocraticLifeMeeting.entity.SmartDemocraticLifeEnclosure;
import org.jeecg.modules.smartDemocraticLifeMeeting.entity.SmartDemocraticLifeMeeting;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 民主生活会表
 * @Author: jeecg-boot
 * @Date:   2021-11-03
 * @Version: V1.0
 */
public interface ISmartDemocraticLifeMeetingService extends IService<SmartDemocraticLifeMeeting> {

	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);


}
