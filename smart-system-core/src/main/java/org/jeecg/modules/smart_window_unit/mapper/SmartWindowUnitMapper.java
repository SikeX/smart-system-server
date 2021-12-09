package org.jeecg.modules.smart_window_unit.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.smart_window_unit.entity.SmartWindowUnit;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 窗口单位
 * @Author: jeecg-boot
 * @Date:   2021-12-02
 * @Version: V1.0
 */
public interface SmartWindowUnitMapper extends BaseMapper<SmartWindowUnit> {
    /**
     * 根据部门Id查询部门Name
     *
     * @param id 部门id
     * @return 部门Name
     */
    String getDepartNameById(String id);
    /**
     * 根据人员Id查询人员Name
     *
     * @param id 人员id
     * @return 部门Name
     */
    String getUserNameById(String id);

}
