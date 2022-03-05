package org.jeecg.modules.smartAssessmentDepartment.service;

import org.jeecg.common.system.vo.DictModel;
import org.jeecg.modules.smartAssessmentDepartment.entity.SmartAssessmentDepartment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 负责评分的考核单位
 * @Author: jeecg-boot
 * @Date:   2022-03-01
 * @Version: V1.0
 */
public interface ISmartAssessmentDepartmentService extends IService<SmartAssessmentDepartment> {
    List<DictModel> getDictItems();
}
