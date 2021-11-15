package org.jeecg.modules.message.job;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.api.dto.message.MessageDTO;
import org.jeecg.common.constant.WebsocketConst;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.modules.message.entity.PersonInfo;
import org.jeecg.modules.message.entity.PunishPerson;
import org.jeecg.modules.message.entity.SmartTaskManage;
import org.jeecg.modules.message.service.ISysMessageTemplateService;
import org.jeecg.modules.message.websocket.WebSocket;
import org.jeecg.modules.system.service.ISysAnnouncementSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by lord on 21/10/31
 *
 */
@Component
public class TestJob {

    // private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Integer count0 = 1;

    @Autowired
    private ISysMessageTemplateService sysMessageTemplateService;

    @Autowired
    ISysBaseAPI iSysBaseAPI;

    @Resource
    private WebSocket webSocket;


    /**
     *
     * 只需在这里调用service即可。
     * 每隔十秒进行一次
     *
     */
//    @Scheduled(cron = "*/10 * * * * *")
//    public void reportCurrentTime() throws InterruptedException {
//        System.out.println("任务执行！！！" + "count = " + (count0++));
//    }

    //cron测试
    @Scheduled(cron = "0 51 13 * * ?")
    public void cronTest() throws InterruptedException {
        System.out.println("测试成功，每天13点51执行");
    }

    /**
     * 解除处分通知
     * 每天早上八点执行
     */
//    @Scheduled(cron = "0 0 8 * * ?")
    @Scheduled(cron = "0 0 20 * * ?")
    public void punishRe(){

        List<SmartTaskManage> smartTaskManage = sysMessageTemplateService.getTaskDetail();
        String status = smartTaskManage.get(1).getStatus();
        //发送类型
        String type = "1"; //smartTaskManage.get(1).getSendType();
        String content = smartTaskManage.get(1).getTemplateContent();

        if(status.equals("开启")){
            List<PunishPerson> punishPersonList = sysMessageTemplateService.getPunishList();
            System.out.println("解除处分通知");

            for(PunishPerson p : punishPersonList){
                if(type.equals("1")){
                    //调用短信接口
                    System.out.println(p.toString() + content);
                    System.out.println(smartTaskManage.get(0).getTaskName());
                }else{
                    //调用消息接口
                    System.out.println(p.toString() + content);

                    //系统消息
                    //MessageDTO messageDTO = new MessageDTO("admin", "lord1", "入党纪念日提醒", "入党纪念日快乐");
                    //iSysBaseAPI.sendSysAnnouncement(messageDTO);

                    //通知



                }
            }
        }
    }

    /**
     * 入党纪念日提醒
     * 每天早上八点执行
     */
//    @Scheduled(cron = "0 0 8 * * ?")
    @Scheduled(cron = "0 0 8 * * ?")
    public void anniversaryRe(){

        List<SmartTaskManage> smartTaskManage = sysMessageTemplateService.getTaskDetail();
        String status = smartTaskManage.get(0).getStatus();
        String type = "2"; //smartTaskManage.get(0).getSendType();
        String content = smartTaskManage.get(0).getTemplateContent();

        if(status.equals("开启")){
            List<PersonInfo> personInfoList = sysMessageTemplateService.getBirthList();
            System.out.println("入党纪念日通知");

            for(PersonInfo p : personInfoList){
                if(type.equals("1")){
                    //调用短信接口
                    System.out.println(p.toString() + content);
                    System.out.println(smartTaskManage.get(0).getTaskName());

                    MessageDTO messageDTO = new MessageDTO("admin", "lord1", "入党纪念日提醒", "入党纪念日快乐");

                    //iSysBaseAPI.sendSysAnnouncement(messageDTO);
                }else{
                    //调用app接口
                    System.out.println(p.toString() + content);

                    //调用消息接口
//                    String userId = "lord1"; //p.getUserName();
                    //String anntId = sysAnnouncement.getId();
                    //Date refDate = new Date();
//                    JSONObject obj = new JSONObject();
//                    obj.put(WebsocketConst.MSG_CMD, WebsocketConst.CMD_USER);
                    //obj.put(WebsocketConst.MSG_ID, sysAnnouncement.getId());
//                    obj.put(WebsocketConst.MSG_TXT, "入党纪念日通知");
//                    webSocket.sendMessage(userId, obj.toJSONString());

                }
            }
        }
    }
}
