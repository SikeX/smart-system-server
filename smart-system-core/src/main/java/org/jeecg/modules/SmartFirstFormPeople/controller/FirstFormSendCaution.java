package org.jeecg.modules.SmartFirstFormPeople.controller;


import org.jeecg.common.api.dto.message.MessageDTO;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.modules.SmartFirstFormPeople.entity.FirstFormInfo;
import org.jeecg.modules.SmartFirstFormPeople.service.ISmartFirstFormPeopleService;
import org.jeecg.modules.SmartPunishPeople.entity.punishInfo;
import org.jeecg.modules.SmartPunishPeople.service.ISmartPunishPeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;


@Component
public class FirstFormSendCaution {

    @Autowired
    private ISmartFirstFormPeopleService smartFirstFormPeopleService;
    @Autowired
    private ISysBaseAPI sysBaseAPI;

    //每月1号,上午8点15
    @Scheduled(cron = "0 15 8 1 * ?")

    private void sendCaution() {
        //获取本月处分人员
        List<FirstFormInfo> list = smartFirstFormPeopleService.sendInformation();
        System.out.println("执行第一种形态更新执行中.........................");
        if (list.size()>0){
            //获取单位负责人
            for(FirstFormInfo p : list){
                List<String> departIds = Arrays.asList((p.getDepartCode()).split(","));
                p.setLeaders(smartFirstFormPeopleService.getLeadersByDepartId(departIds));
                //System.out.println(p);
            }
            for (int i = 0;i<list.size();i++){
                for(String leader : list.get(i).getLeaders()){
                    try {
                        MessageDTO messageDTO = new MessageDTO();
                        messageDTO.setTitle("执行第一种形态人员变动提醒");
                        messageDTO.setContent(list.get(i).getInterviewees() + "于上个月执行第一种形态。");
                        messageDTO.setFromUser("admin");
                        messageDTO.setToUser(leader);
                        messageDTO.setCategory("1");

                        sysBaseAPI.sendSysAnnouncement(messageDTO);

                    } catch (NullPointerException e) {

                    }
                }
            }
        }

    }
}
