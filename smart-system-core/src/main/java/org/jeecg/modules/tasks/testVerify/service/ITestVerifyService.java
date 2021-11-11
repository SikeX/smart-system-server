package org.jeecg.modules.tasks.testVerify.service;

import org.jeecg.modules.tasks.smartVerifyTask.entity.SmartVerifyTask;
import org.jeecg.modules.tasks.testVerify.entity.TestVerify;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 审核测试
 * @Author: jeecg-boot
 * @Date:   2021-11-06
 * @Version: V1.0
 */
public interface ITestVerifyService extends IService<TestVerify> {
    public void saveOne(TestVerify testVerify, SmartVerifyTask smartVerifyTask);
}
