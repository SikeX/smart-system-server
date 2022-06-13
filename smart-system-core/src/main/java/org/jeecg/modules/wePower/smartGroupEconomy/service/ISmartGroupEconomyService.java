package org.jeecg.modules.wePower.smartGroupEconomy.service;

import org.jeecg.modules.wePower.smartGroupEconomy.entity.SmartGroupEconomy;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.Collection;

/**
 * @Description: 农村集体经济组织
 * @Author: jeecg-boot
 * @Date:   2022-03-06
 * @Version: V1.0
 */
public interface ISmartGroupEconomyService extends IService<SmartGroupEconomy> {

	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);


}
