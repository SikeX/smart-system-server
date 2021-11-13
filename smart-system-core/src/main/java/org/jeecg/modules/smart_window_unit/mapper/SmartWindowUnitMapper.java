package org.jeecg.modules.smart_window_unit.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.smart_window_unit.entity.SmartWindowUnit;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 窗口单位
 * @Author: jeecg-boot
 * @Date:   2021-11-11
 * @Version: V1.0
 */
public interface SmartWindowUnitMapper extends BaseMapper<SmartWindowUnit> {

	/**
	 * 编辑节点状态
	 * @param id
	 * @param status
	 */
	void updateTreeNodeStatus(@Param("id") String id,@Param("status") String status);

}
