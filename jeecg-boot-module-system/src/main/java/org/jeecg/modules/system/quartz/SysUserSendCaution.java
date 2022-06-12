package org.jeecg.modules.system.quartz;


import org.jeecg.common.api.dto.message.MessageDTO;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.system.vo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class SysUserSendCaution {

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysBaseAPI sysBaseAPI;

    //每月1号,上午8点15
    @Scheduled(cron = "0 30 8 L 3,6,9,12 ?")

    private void sendCaution() {
        //获取本月新增人员
        List<UserInfo> list = sysUserService.sendInformation();
        System.out.println("人员更新执行中.........................");
        if (list.size()>0){
            //获取单位负责人
            for(UserInfo p : list){
                p.setLeaders(sysUserService.getLeadersByOrgCode(p.getDepartCode()));
                //System.out.println(p);
            }
            for (int i = 0;i<list.size();i++){
                for(String leader : list.get(i).getLeaders()){
                    try {
                        MessageDTO messageDTO = new MessageDTO();
                        messageDTO.setTitle("人员变动提醒");
                        messageDTO.setContent("本季度新增人员："+list.get(i).getNames());
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
