package org.jeecg.modules.SmartFinanceResult.service;

import org.jeecg.modules.SmartFinanceResult.entity.SmartFinanceAnnex;
import org.jeecg.modules.SmartFinanceResult.entity.SmartFinanceResult;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 8项规定财物收支表
 * @Author: jeecg-boot
 * @Date:   2021-11-03
 * @Version: V1.0
 */
public interface ISmartFinanceResultService extends IService<SmartFinanceResult> {

	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);


}
