package org.jeecg.modules.smartAssessmentDepartment.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.modules.smartAssessmentDepartment.entity.SmartAssessmentDepartment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 负责评分的考核单位
 * @Author: jeecg-boot
 * @Date:   2022-03-01
 * @Version: V1.0
 */
public interface SmartAssessmentDepartmentMapper extends BaseMapper<SmartAssessmentDepartment> {
    /**
     * 获取评分考核单位字典,将text变为单位名称
     *
     * @return
     */
    List<DictModel> getDictItems();

    /**
     * 根据系统单位名称获取评分考核单位的ID
     *
     * @param departName 系统单位名称
     * @return
     */
    String getAssessmentDepartmentIdByDepartName(@Param("departName") String departName);
}
