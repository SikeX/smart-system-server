package org.jeecg.modules.SmartPunishPeople.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.common.api.dto.message.MessageDTO;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.modules.SmartPunishPeople.entity.SmartPunishPeople;
import org.jeecg.modules.SmartPunishPeople.entity.punishInfo;
import org.jeecg.modules.SmartPunishPeople.service.ISmartPunishPeopleService;
import org.jeecg.modules.smartJob.entity.SysUser;
import org.jeecg.modules.smartReportingInformation.entity.SysRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;


@Component
public class PunishSendCaution {

    @Autowired
    private ISmartPunishPeopleService smartPunishPeopleService;
    @Autowired
    private ISysBaseAPI sysBaseAPI;

    //每月1号,上午8点
    @Scheduled(cron = "0 0 8 1 * ?")

    private void sendCaution() {
        //获取本月处分人员
        List<punishInfo> list = smartPunishPeopleService.sendInformation();
        System.out.println("处分人员更新执行中.........................");
        if (list.size()>0){
            //获取单位负责人
            for(punishInfo p : list){
                List<String> departIds = Arrays.asList((p.getDepartCode()).split(","));
                //System.out.println(departIds);
                p.setLeaders(smartPunishPeopleService.getLeadersByDepartId(departIds));
                //System.out.println(p);
            }
            for (int i = 0;i<list.size();i++){
                for(String leader : list.get(i).getLeaders()){
                    try {
                        MessageDTO messageDTO = new MessageDTO();
                        messageDTO.setTitle("处分人员变动提醒");
                        messageDTO.setContent(list.get(i).getPunishers() + "于上个月受处分。");
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
