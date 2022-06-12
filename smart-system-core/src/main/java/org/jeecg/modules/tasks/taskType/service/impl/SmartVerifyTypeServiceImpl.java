package org.jeecg.modules.tasks.taskType.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.smartSupervision.entity.SmartSupervision;
import org.jeecg.modules.tasks.taskType.entity.SmartVerifyType;
import org.jeecg.modules.tasks.taskType.mapper.SmartVerifyTypeMapper;
import org.jeecg.modules.tasks.taskType.service.ISmartVerifyTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Objects;

/**
 * @Description: 审核任务类型编码表
 * @Author: jeecg-boot
 * @Date:   2021-11-04
 * @Version: V1.0
 */
@Slf4j
@Service
public class SmartVerifyTypeServiceImpl extends ServiceImpl<SmartVerifyTypeMapper, SmartVerifyType> implements ISmartVerifyTypeService {

    @Autowired
    private SmartVerifyTypeMapper smartVerifyTypeMapper;

    @Override
    public Boolean getIsVerifyStatusByType(String type){
        QueryWrapper<SmartVerifyType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type_name",type);
//        SmartVerifyType smartVerifyType = new SmartVerifyType();
        String isVerify = smartVerifyTypeMapper.selectOne(queryWrapper).getIsVerify();
        log.info("审核状态"+isVerify);
        return !Objects.equals(isVerify, "0");
    }
}
