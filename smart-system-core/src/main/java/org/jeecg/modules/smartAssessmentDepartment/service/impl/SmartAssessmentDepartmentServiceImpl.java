package org.jeecg.modules.smartAssessmentDepartment.service.impl;

import org.jeecg.common.system.vo.DictModel;
import org.jeecg.modules.smartAssessmentDepartment.entity.SmartAssessmentDepartment;
import org.jeecg.modules.smartAssessmentDepartment.mapper.SmartAssessmentDepartmentMapper;
import org.jeecg.modules.smartAssessmentDepartment.service.ISmartAssessmentDepartmentService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 负责评分的考核单位
 * @Author: jeecg-boot
 * @Date:   2022-03-01
 * @Version: V1.0
 */
@Service
public class SmartAssessmentDepartmentServiceImpl extends ServiceImpl<SmartAssessmentDepartmentMapper, SmartAssessmentDepartment> implements ISmartAssessmentDepartmentService {

    @Override
    public List<DictModel> getDictItems() {
        return baseMapper.getDictItems();
    }

    @Override
    public String getAssessmentDepartmentIdByDepartName(String departName) {
        return baseMapper.getAssessmentDepartmentIdByDepartName(departName);
    }
}
