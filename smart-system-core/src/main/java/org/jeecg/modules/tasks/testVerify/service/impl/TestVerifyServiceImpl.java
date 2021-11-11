package org.jeecg.modules.tasks.testVerify.service.impl;

import org.jeecg.modules.tasks.testVerify.entity.TestVerify;
import org.jeecg.modules.tasks.testVerify.mapper.TestVerifyMapper;
import org.jeecg.modules.tasks.testVerify.service.ITestVerifyService;
import org.jeecg.modules.tasks.smartVerifyTask.entity.SmartVerifyTask;
import org.jeecg.modules.tasks.smartVerifyTask.mapper.SmartVerifyTaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 审核测试
 * @Author: jeecg-boot
 * @Date:   2021-11-06
 * @Version: V1.0
 */
@Service
public class TestVerifyServiceImpl extends ServiceImpl<TestVerifyMapper, TestVerify> implements ITestVerifyService {

    @Autowired
    TestVerifyMapper testVerifyMapper;

    @Autowired
    SmartVerifyTaskMapper smartVerifyTaskMapper;


    @Override
    @Transactional
    public void saveOne(TestVerify testVerify, SmartVerifyTask smartVerifyTask){
        testVerifyMapper.insert(testVerify);
        smartVerifyTask.setTaskType("婚前");
        smartVerifyTask.setSysOrgCode(testVerify.getId());
        smartVerifyTaskMapper.insert(smartVerifyTask);
    }
}
