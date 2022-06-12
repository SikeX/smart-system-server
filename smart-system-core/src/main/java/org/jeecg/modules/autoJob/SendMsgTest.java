package org.jeecg.modules.autoJob;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.common.api.dto.message.MessageDTO;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.util.DateUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Slf4j
public class SendMsgTest implements Job {

    @Autowired
    private ISysBaseAPI sysBaseAPI;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {


        log.info(String.format(" sike 发送系统通告测试 !  时间:" + DateUtils.getTimestamp()));

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setFromUser("sike");
        messageDTO.setToAll(true);
        messageDTO.setTitle("通知");
        messageDTO.setContent("通知测试from sike");

        sysBaseAPI.sendSysAnnouncement(messageDTO);


    }
}
