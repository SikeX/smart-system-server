package org.jeecg.modules.tasks.smartVerifyTask.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.dto.message.MessageDTO;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.tasks.smartVerifyDetail.entity.SmartVerifyDetail;
import org.jeecg.modules.tasks.smartVerifyDetail.service.ISmartVerifyDetailService;
import org.jeecg.modules.tasks.smartVerifyTask.entity.SmartVerifyTask;
import org.jeecg.modules.tasks.smartVerifyTask.mapper.SmartVerifyTaskMapper;
import org.jeecg.modules.tasks.smartVerifyTask.service.SmartVerify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: sike
 * @Date: 2021/11/10 11:53
 * @Version: V1.0
 */

@Slf4j
@Service
public class SmartVerifyImpl implements SmartVerify {

    @Autowired
    private SmartVerifyTaskMapper smartVerifyTaskMapper;

    @Autowired
    private ISysBaseAPI sysBaseAPI;

    @Autowired ISmartVerifyDetailService smartVerifyDetailService;

    @Override
    public void addVerifyRecord(String id, String verifyType ) {
        SmartVerifyTask smartVerifyTask = new SmartVerifyTask();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userDepartId = sysBaseAPI.getDepartIdsByOrgCode(sysUser.getOrgCode());
        smartVerifyTask.setFillPerson(sysUser.getRealname());
        smartVerifyTask.setFillDepart(userDepartId);
        smartVerifyTask.setTaskType(verifyType);
        smartVerifyTask.setFlowNo(id);
        smartVerifyTask.setFlowStatus(2);
        smartVerifyTaskMapper.insert(smartVerifyTask);


        log.info(sysBaseAPI.getParentDepartId(userDepartId).getText());
        String first = sysBaseAPI.getParentDepartId(userDepartId).getText();
        if(first == "75e4cd3cea8843718c58c24e8857679f"){
            first = sysBaseAPI.getParentDepartId("75e4cd3cea8843718c58c24e8857679f").getText();
        }
        String second = sysBaseAPI.getParentDepartId(first).getText();

//        QueryWrapper queryWrapper = new QueryWrapper();
//        queryWrapper.eq("type_name",verifyType);
//        SmartVerifyType smartVerifyType = smartVerifyTypeService.getOne(queryWrapper);

        SmartVerifyDetail smartVerifyDetail1 = new SmartVerifyDetail();
        SmartVerifyDetail smartVerifyDetail2 = new SmartVerifyDetail();

        smartVerifyDetail1.setFlowNo(smartVerifyTask.getFlowNo());
        smartVerifyDetail1.setAuditDepart(first);
        // 第一审核人为待我审核
        smartVerifyDetail1.setAuditDepart("2");

        smartVerifyDetail2.setFlowNo(smartVerifyTask.getFlowNo());
        smartVerifyDetail2.setAuditDepart(second);
        // 第二审核人为待审核中
        smartVerifyDetail2.setAuditDepart("1");

        smartVerifyDetailService.save(smartVerifyDetail1);
        smartVerifyDetailService.save(smartVerifyDetail2);
    }
}
