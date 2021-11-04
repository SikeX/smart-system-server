package org.jeecg.modules.smartPremaritalFiling.service;

import org.jeecg.modules.smartPremaritalFiling.entity.SmartPremaritalFilingApp;
import org.jeecg.modules.smartPremaritalFiling.entity.SmartPremaritalFiling;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 8项规定婚前报备表
 * @Author: jeecg-boot
 * @Date:   2021-11-04
 * @Version: V1.0
 */
public interface ISmartPremaritalFilingService extends IService<SmartPremaritalFiling> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(SmartPremaritalFiling smartPremaritalFiling,List<SmartPremaritalFilingApp> smartPremaritalFilingAppList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(SmartPremaritalFiling smartPremaritalFiling,List<SmartPremaritalFilingApp> smartPremaritalFilingAppList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
