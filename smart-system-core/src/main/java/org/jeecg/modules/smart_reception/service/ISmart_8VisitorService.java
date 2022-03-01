package org.jeecg.modules.smart_reception.service;

import org.jeecg.modules.smart_reception.entity.Smart_8Visitor;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 来访人员信息表
 * @Author: jeecg-boot
 * @Date:   2022-02-28
 * @Version: V1.0
 */
public interface ISmart_8VisitorService extends IService<Smart_8Visitor> {

	public List<Smart_8Visitor> selectByMainId(String mainId);
}
