package org.jeecg.modules.smartTripleImportanceOneGreatness.service;

import org.jeecg.modules.smartTripleImportanceOneGreatness.entity.SmartTripleImportanceOneGreatnessPacca;
import org.jeecg.modules.smartTripleImportanceOneGreatness.entity.SmartTripleImportanceOneGreatness;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 三重一大表
 * @Author: jeecg-boot
 * @Date:   2022-02-27
 * @Version: V1.0
 */
public interface ISmartTripleImportanceOneGreatnessService extends IService<SmartTripleImportanceOneGreatness> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(SmartTripleImportanceOneGreatness smartTripleImportanceOneGreatness,List<SmartTripleImportanceOneGreatnessPacca> smartTripleImportanceOneGreatnessPaccaList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(SmartTripleImportanceOneGreatness smartTripleImportanceOneGreatness,List<SmartTripleImportanceOneGreatnessPacca> smartTripleImportanceOneGreatnessPaccaList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
