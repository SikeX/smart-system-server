package org.jeecg.modules.test.testDict.service.impl;

import org.jeecg.modules.test.testDict.mapper.TestDictMapper;
import org.jeecg.modules.test.testDict.service.ITestDictService;
import org.jeecg.modules.test.testDict.entity.TestDict;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 字典测试
 * @Author: jeecg-boot
 * @Date:   2021-11-15
 * @Version: V1.0
 */
@Service
public class TestDictServiceImpl extends ServiceImpl<TestDictMapper, TestDict> implements ITestDictService {

}
