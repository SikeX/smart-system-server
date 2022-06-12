package org.jeecg.modules.smart_window_people.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.smart_window_people.entity.SmartWindowPeople;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 窗口人员管理
 * @Author: jeecg-boot
 * @Date:   2021-12-02
 * @Version: V1.0
 */
public interface SmartWindowPeopleMapper extends BaseMapper<SmartWindowPeople> {
    /**
     * 根据People表departmentId查询Unit表Pid
     *
     * @param id People表departmentId
     * @return Unit表Pid
     */
    String getPidByDepartmentId(String id);

    /**
     * 根据People表departmentId查询Unit表Pid
     *
     * @param id People表departmentId
     * @return Unit表Name
     */
    String getDepartmentNameByDepartmentId(String id);
    /**
     * 根据部门Id查询部门Name
     *
     * @param id 部门id
     * @return 部门Name
     */
    String getDepartNameById(String id);
}
