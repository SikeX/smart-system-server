package org.jeecg.modules.smartCreateAdvice.service;

import org.jeecg.modules.smartCreateAdvice.entity.SmartCreateAdviceAnnex;
import org.jeecg.modules.smartCreateAdvice.entity.SmartCreateAdvice;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 制发建议表
 * @Author: jeecg-boot
 * @Date:   2021-11-13
 * @Version: V1.0
 */
public interface ISmartCreateAdviceService extends IService<SmartCreateAdvice> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(SmartCreateAdvice smartCreateAdvice,List<SmartCreateAdviceAnnex> smartCreateAdviceAnnexList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(SmartCreateAdvice smartCreateAdvice,List<SmartCreateAdviceAnnex> smartCreateAdviceAnnexList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
