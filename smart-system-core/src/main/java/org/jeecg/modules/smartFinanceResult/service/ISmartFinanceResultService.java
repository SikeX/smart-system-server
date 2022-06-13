package org.jeecg.modules.smartFinanceResult.service;

import org.jeecg.modules.smartFinanceResult.entity.SmartFinanceAnnex;
import org.jeecg.modules.smartFinanceResult.entity.SmartFinanceResult;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 8项规定财物收支表
 * @Author: jeecg-boot
 * @Date:   2021-11-17
 * @Version: V1.0
 */
public interface ISmartFinanceResultService extends IService<SmartFinanceResult> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(SmartFinanceResult smartFinanceResult,List<SmartFinanceAnnex> smartFinanceAnnexList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(SmartFinanceResult smartFinanceResult,List<SmartFinanceAnnex> smartFinanceAnnexList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
