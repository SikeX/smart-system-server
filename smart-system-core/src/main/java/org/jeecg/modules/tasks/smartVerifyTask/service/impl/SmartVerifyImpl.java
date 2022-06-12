package org.jeecg.modules.tasks.smartVerifyTask.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.dto.message.MessageDTO;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.system.vo.SysDepartModel;
import org.jeecg.modules.tasks.smartVerifyDetail.entity.SmartVerifyDetail;
import org.jeecg.modules.tasks.smartVerifyDetail.service.ISmartVerifyDetailService;
import org.jeecg.modules.tasks.smartVerifyTask.entity.SmartVerifyTask;
import org.jeecg.modules.tasks.smartVerifyTask.mapper.SmartVerifyTaskMapper;
import org.jeecg.modules.tasks.smartVerifyTask.service.SmartVerify;
import org.jeecg.modules.tasks.taskType.entity.SmartVerifyType;
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
    public Integer getFlowStatusById(String id){
        QueryWrapper<SmartVerifyTask> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("flow_no",id);
        return smartVerifyTaskMapper.selectOne(queryWrapper).getFlowStatus();

    }

    @Override
    public void addVerifyRecord(String id, String verifyType ) {
        SmartVerifyTask smartVerifyTask = new SmartVerifyTask();
        // 获取用户信息
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        String userId = sysUser.getId();
        log.info("当前用户id：" + userId);

        String departType = sysBaseAPI.getDepTypeByUserId(userId);
        String userDepartId = sysBaseAPI.getDepartIdsByOrgCode(sysUser.getOrgCode());
        smartVerifyTask.setFillPerson(sysUser.getRealname());
        smartVerifyTask.setFillDepart(userDepartId);
        smartVerifyTask.setTaskType(verifyType);
        smartVerifyTask.setFlowNo(id);
        smartVerifyTask.setDepartType(departType);

        // 获取登录用户业务父部门id
        String parentId = sysBaseAPI.getParentIdByUserId(userId);
        if(StrUtil.isBlank(parentId)){
            smartVerifyTask.setFlowStatus(1);
            smartVerifyTaskMapper.insert(smartVerifyTask);
            return;
        }
        smartVerifyTask.setFlowStatus(2);

        smartVerifyTaskMapper.insert(smartVerifyTask);

        String first = parentId;
        log.info(first);

        if(StrUtil.isEmpty(first)){
            String[] userIdList = new String[1];
            userIdList[0] = userId;
            sysBaseAPI.sendWebSocketMsg(userIdList,"申请已通过");
            return;
        }
        // 如果是教育局则绕开
        else if(first.equals("e389cf6bc9a54fd58f2e74f09d8f1b1f")){
            first = sysBaseAPI.getParentDepIdByDepartId("e389cf6bc9a54fd58f2e74f09d8f1b1f");
        }
        String second = sysBaseAPI.getParentDepIdByDepartId(first);

        if(StrUtil.isEmpty(second)){
            String[] userIdList = new String[1];
            userIdList[0] = userId;
            sysBaseAPI.sendWebSocketMsg(userIdList,"申请已通过");
            return;
        }

//        QueryWrapper queryWrapper = new QueryWrapper();
//        queryWrapper.eq("type_name",verifyType);
//        SmartVerifyType smartVerifyType = smartVerifyTypeService.getOne(queryWrapper);

        SmartVerifyDetail smartVerifyDetail1 = new SmartVerifyDetail();
        SmartVerifyDetail smartVerifyDetail2 = new SmartVerifyDetail();

        smartVerifyDetail1.setFlowNo(smartVerifyTask.getFlowNo());
        smartVerifyDetail1.setAuditDepart(first);
        // 第一审核人为待我审核
        smartVerifyDetail1.setAuditStatus(2);

        smartVerifyDetail2.setFlowNo(smartVerifyTask.getFlowNo());
        smartVerifyDetail2.setAuditDepart(second);
        // 第二审核人为待审核中
        smartVerifyDetail2.setAuditStatus(1);

        smartVerifyDetailService.save(smartVerifyDetail1);
        smartVerifyDetailService.save(smartVerifyDetail2);
    }
}
