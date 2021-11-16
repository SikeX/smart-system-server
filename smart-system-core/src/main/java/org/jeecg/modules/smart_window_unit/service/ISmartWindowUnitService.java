package org.jeecg.modules.smart_window_unit.service;

import org.jeecg.modules.smart_window_unit.entity.SmartWindowUnit;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.exception.JeecgBootException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.List;

/**
 * @Description: 窗口单位
 * @Author: jeecg-boot
 * @Date:   2021-11-11
 * @Version: V1.0
 */
public interface ISmartWindowUnitService extends IService<SmartWindowUnit> {

	/**根节点父ID的值*/
	public static final String ROOT_PID_VALUE = "0";

	/**树节点有子节点状态值*/
	public static final String HASCHILD = "1";

	/**树节点无子节点状态值*/
	public static final String NOCHILD = "0";

	/**新增节点*/
	void addSmartWindowUnit(SmartWindowUnit smartWindowUnit);

	/**修改节点*/
	void updateSmartWindowUnit(SmartWindowUnit smartWindowUnit) throws JeecgBootException;

	/**删除节点*/
	void deleteSmartWindowUnit(String id) throws JeecgBootException;

	/**查询所有数据，无分页*/
    List<SmartWindowUnit> queryTreeListNoPage(QueryWrapper<SmartWindowUnit> queryWrapper);

}
