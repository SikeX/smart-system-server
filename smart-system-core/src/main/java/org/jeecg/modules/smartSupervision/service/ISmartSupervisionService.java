package org.jeecg.modules.smartSupervision.service;

import org.jeecg.modules.smartSupervision.entity.SmartSupervisionAnnex;
import org.jeecg.modules.smartSupervision.entity.SmartSupervision;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 八项规定监督检查表
 * @Author: jeecg-boot
 * @Date:   2021-11-10
 * @Version: V1.0
 */
public interface ISmartSupervisionService extends IService<SmartSupervision> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(SmartSupervision smartSupervision,List<SmartSupervisionAnnex> smartSupervisionAnnexList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(SmartSupervision smartSupervision,List<SmartSupervisionAnnex> smartSupervisionAnnexList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
