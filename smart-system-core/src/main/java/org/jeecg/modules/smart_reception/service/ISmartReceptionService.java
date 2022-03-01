package org.jeecg.modules.smart_reception.service;

import org.jeecg.modules.smart_reception.entity.Smart_8Visitor;
import org.jeecg.modules.smart_reception.entity.Smart_8Stay;
import org.jeecg.modules.smart_reception.entity.Smart_8Dining;
import org.jeecg.modules.smart_reception.entity.Smart_8List;
import org.jeecg.modules.smart_reception.entity.SmartReception;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 公务接待2.0
 * @Author: jeecg-boot
 * @Date:   2022-02-28
 * @Version: V1.0
 */
public interface ISmartReceptionService extends IService<SmartReception> {

	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);


}
