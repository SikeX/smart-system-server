package org.jeecg.modules.tasks.taskType.service;

import org.jeecg.modules.tasks.taskType.entity.SmartVerifyType;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 审核任务类型编码表
 * @Author: jeecg-boot
 * @Date:   2021-11-04
 * @Version: V1.0
 */
public interface ISmartVerifyTypeService extends IService<SmartVerifyType> {

    Boolean getIsVerifyStatusByType(String type);

}
