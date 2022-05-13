package org.jeecg.modules.smartFuneralReport.controller;


import org.jeecg.common.api.dto.message.MessageDTO;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.modules.smartFuneralReport.entity.SmartFuneralReport;
import org.jeecg.modules.smartFuneralReport.service.ISmartFuneralReportService;
import org.jeecg.modules.smartReportingInformation.entity.SmartJob;
import org.jeecg.modules.smartReportingInformation.entity.SysRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;


@Component
public class FuneralSendCaution {

    private static final long ONE_DAY=86400000L;//每天的毫秒数
    @Autowired
    private ISmartFuneralReportService smartFuneralReportService;
    @Autowired
    private ISysBaseAPI sysBaseAPI;

    @Scheduled(cron = "0 10 * * * ?")

    private void sendCaution() {//给管理员发送提醒
        //获取项目的状态（开始或关闭）
        SmartJob jobstatus=
                smartFuneralReportService.getStatus();
        if(jobstatus.equals("1")){
                return;
        }
        else{
            //发送消息
                List<SmartFuneralReport> send =
                        smartFuneralReportService.sendInformation();
                for (SmartFuneralReport s : send) {
//                    String status = s.getProcessingResult();
//                    if(status.equals("未受理")){
                        long now =new Date().getTime();//当前时间
                        long report = s.getReportTime().getTime();//口头报备填写时间
                        long time=now - report;
                        time/= ONE_DAY;
                        if(time<15){
                            continue;
                        }
                        else{
                            //获取用户角色
                            List<SysRole> role=smartFuneralReportService.getUser();
                            String userId = s.getPeopleId();
                            String peopleName = "人员";
                            String realname = smartFuneralReportService.getRealnameById(userId);
                            if(realname != null && !realname.equals(""))
                            {peopleName = realname;}
                            for(SysRole r:role){
                                try{
                                    MessageDTO messageDTO=new MessageDTO();
                                    messageDTO.setTitle("丧事事后报备信息提醒");
                                    messageDTO.setContent(peopleName+"的丧事事后报备已超过15日未提交");
                                    messageDTO.setFromUser("admin");
                                    messageDTO.setToUser(r.getUsername());
                                    messageDTO.setCategory("1");

                                    sysBaseAPI.sendSysAnnouncement(messageDTO);

                                }catch (NullPointerException e){

                                }
                            }
//                            break;
                        }

                }
            }
    }
}
