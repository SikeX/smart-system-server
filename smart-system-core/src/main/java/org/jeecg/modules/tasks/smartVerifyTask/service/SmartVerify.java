package org.jeecg.modules.tasks.smartVerifyTask.service;

import org.apache.poi.ss.formula.functions.T;

/**
 * @Description:
 * @Author: sike
 * @Date: 2021/11/10 11:51
 * @Version: V1.0
 */
public interface SmartVerify {

    Integer getFlowStatusById(String id);

    void addVerifyRecord(String id, String verifyType);
}
