package org.jeecg.modules.smart_reception.controller;


import org.jeecg.common.api.dto.message.MessageDTO;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.modules.smartReportingInformation.entity.SmartJob;
import org.jeecg.modules.smartReportingInformation.entity.SysRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;



import org.jeecg.modules.smart_reception.entity.SmartReception;
import org.jeecg.modules.smart_reception.entity.Smart_8Dining;
import org.jeecg.modules.smart_reception.service.ISmart_8DiningService;
import org.jeecg.modules.smart_reception.service.ISmartReceptionService;

import java.util.Date;
import java.util.List;


@Component
public class ReceptionSendCaution {

    private static final long ONE_DAY=86400000L;//每天的毫秒数
    @Autowired
    private ISmart_8DiningService smart_8DiningService;
    private ISmartReceptionService smartReceptionService;

    @Autowired
    private ISysBaseAPI sysBaseAPI;

    @Scheduled(cron = "0 20 20 * * ?")

    private void sendCaution() {//给管理员发送提醒

        //发送消息
        List<Smart_8Dining> send = smart_8DiningService.sendInformation();
        for (Smart_8Dining s : send) {
            Integer num = s.getNum();
            Integer numr = s.getNumR();
            String crateBy = null;
            if(s.getUpdateBy() != null){
                crateBy = smart_8DiningService.getUserNameByUsername(s.getUpdateBy());
            }
            else {
                crateBy = smart_8DiningService.getUserNameByUsername(s.getCreateBy());
            }
            Date time = null;
            if(s.getUpdateTime() != null){
                time = s.getUpdateTime();
            }
            else {
                time = s.getCreateTime();
            }
            String departmentId = smart_8DiningService.getDepartmentIdBymainId(s.getMainId());
            String departmentName = smart_8DiningService.getDepartmentNameById(departmentId);

            String receptionName = smart_8DiningService.getReceptionDepBymainId(s.getMainId());

            String stage = s.getName();
            List<SysRole> role=smart_8DiningService.getUser();
            for (SysRole r :role){
                try{
                    MessageDTO messageDTO=new MessageDTO();
                    messageDTO.setTitle("公务接待用餐情况预警");
                    messageDTO.setContent(departmentName + crateBy +"于"+ time + "上传的用于接待" + receptionName +
                            "的用餐情况信息异常。具体信息为：用餐地点："+ stage +"，用餐总人数：" + num +"人，陪同用餐人数：" + numr + "人。");
                    messageDTO.setFromUser("admin");
                    messageDTO.setToUser(r.getUsername());
                    messageDTO.setCategory("1");

                    sysBaseAPI.sendSysAnnouncement(messageDTO);

                }catch (NullPointerException e){
                }
            }

        }

    }
}
