package org.jeecg.modules.smartReportingInformation.controller;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.dto.message.MessageDTO;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.smartJob.entity.SysUser;
import org.jeecg.modules.smartReportingInformation.entity.SmartReportingInformation;
import org.jeecg.modules.smartReportingInformation.entity.SysRole;
import org.jeecg.modules.smartReportingInformation.service.ISmartReportingInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;


@Component
public class Send {

    private static final long ONE_DAY=86400000L;//每天的毫秒数
    @Autowired
    private ISmartReportingInformationService smartReportingInformationService;
    @Autowired
    private ISysBaseAPI sysBaseAPI;

    @Scheduled(cron = "0 0 11 * * ?")

    private void send() {
        List<SmartReportingInformation> send =
                smartReportingInformationService.sendInformation();
        for (SmartReportingInformation s : send) {
           String status = s.getProcessingResult();
           if(status.equals("未受理")){
               long now=new Date().getTime();//当前时间
               long report = s.getReportingTime().getTime();//举报时间
               long time=report-now;
               time/= ONE_DAY;
               if(time>3){
                   continue;
               }
               else{
//                   LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
//                   String username=sysUser.getUsername();
                   List<SysRole> role=smartReportingInformationService.getUser();
                   //role.get(0).getRoleCode();
                   for(SysRole r:role){
                       try{
                           MessageDTO messageDTO=new MessageDTO();
                           messageDTO.setTitle("举报信息提醒");
                           messageDTO.setContent("您有举报信息还未处理");
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

    }
}
