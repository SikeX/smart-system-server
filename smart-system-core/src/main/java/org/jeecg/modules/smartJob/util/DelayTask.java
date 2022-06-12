package org.jeecg.modules.smartJob.util;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.dto.message.MessageDTO;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.util.DySmsHelper;
import org.jeecg.modules.smartJob.entity.SmartJob;
import org.jeecg.modules.smartJob.entity.SysUser;
import org.jeecg.modules.smartJob.service.ISmartJobService;
import org.jeecg.modules.smartSentMsg.entity.SmartSentMsg;
import org.jeecg.modules.smartSentMsg.service.ISmartSentMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 延迟任务
 * @author: lord
 * @date: 2021年11月30日 14:35
 */
@Slf4j
@Component
public class DelayTask {

    private static final String SMS = "1";  //短信
    private static final String SYS = "4";  //系统

    private static ISmartJobService smartJobService;
    private static ISmartSentMsgService smartSentMsgService;
    private static ISysBaseAPI sysBaseAPI;

    @Autowired
    public void setSmartJobService(ISmartJobService smartJobService, ISmartSentMsgService smartSentMsgService, ISysBaseAPI sysBaseAPI){

        DelayTask.smartJobService = smartJobService;

        DelayTask.smartSentMsgService = smartSentMsgService;

        DelayTask.sysBaseAPI = sysBaseAPI;
    }

    // 创建延迟任务实例
    private static HashedWheelTimer timer = new HashedWheelTimer(1, // 时间间隔
            TimeUnit.MINUTES,//时间单位
            64); // 时间轮中的槽数

    private static DelayTask delayTask = new DelayTask();

    private Map<String, Timeout> openedMap = new HashMap<>();

    private DelayTask(){

    }

    public void addOpen(String name, Timeout task){

        log.info("\n添加任务:" + name);
        openedMap.put(name, task);
    }

    public static DelayTask getInstance(){
        return delayTask;
    }

    /**
     * 添加延迟任务
     */
    public Timeout addTask(String jobBean, String from, String content ,long delay, String isToAll, String person, String sendType) {

        log.info("\n添加延迟任务 delay = " + delay + " jobBean:" + jobBean);
        // 创建一个任务
        TimerTask task = new TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {
                log.info("\n执行延迟提醒任务, jobBean：" + jobBean + " ，执行时间：" + LocalDateTime.now());

                //判断是否发给所有人
                if(isToAll.equals("0")){
                    //发给所有人
                    delayToAll(from, content, sendType);
                }else{
                    //发给指定用户
                    delayToSome(from, content, person, sendType);
                }

                //执行结束，移除线程信息
                //遍历openMap，删除当前任务
                openedMap.remove(jobBean);

                //修改数据据job为null
                //修改数据库当前任务状态为结束
                smartJobService.updateStatus(jobBean);

            }
        };

        // 将任务添加到延迟队列中
        return timer.newTimeout(task, delay, TimeUnit.MINUTES);
    }

    public boolean deleteTask(String jobBean){
        //关闭任务
        log.info("delayTask openedMap keySet: " + openedMap.keySet());
        Timeout delTask = openedMap.remove(jobBean);

        //在map
        if(null != delTask){
            //取消任务
            log.info("\n任务取消：" + jobBean);
            return delTask.cancel();
        }else{
            //不在map
            log.info("\n任务取消失败：" + jobBean + " 任务不存在或已取消！");
            return true;
        }
    }

    private void delayToSome(String from, String content, String person, String sendType) {

        String msgType = "7"; //短信类型
        String sendFrom = from;  //发送人
        String tittle = "其他";  //title

        //获取用户
        List<SysUser> users = ComputeTime.getUserInfo(person);
        //将电话号码划分为每份最多999个
        Map<String, Object> map = ComputeTime.getPhoneList(users, content, msgType, sendFrom, tittle);
        List<String> temPhones = (List<String>)map.get("phones");
        List<List<SmartSentMsg>> temMsg = (List<List<SmartSentMsg>>)map.get("Msgs");
        int len = temPhones.size();

        //判断发送类型
        if(sendType.equals(SMS)){

            //发送短信
            for(int i =0; i < len; i++){
                boolean isSuccess = DySmsHelper.sendSms(content, temPhones.get(i));
                if(isSuccess){
                    //保存短息
                    smartSentMsgService.saveBatch(temMsg.get(i), 999);
                }else{
                    //修改状态并保存
                    ComputeTime.changeStatus(temMsg.get(i));
                    smartSentMsgService.saveBatch(temMsg.get(i), 999);
                }
            }

        }else if(sendType.equals(SYS)){
            //发送系统消息
            //发送站内信
            for(SysUser s : users){
                MessageDTO messageDTO=new MessageDTO();
                messageDTO.setTitle("其他");
                messageDTO.setContent(content);
                messageDTO.setFromUser(from);
                messageDTO.setToUser(s.getUsername());
                messageDTO.setCategory("1");
                sysBaseAPI.sendSysAnnouncement(messageDTO);
            }

        }else{
            return;
        }
    }

    private void delayToAll(String from, String content, String sendType) {

        String msgType = "7"; //短信类型
        String sendFrom = from;  //发送人
        String tittle = "其他";  //title

        //获取用户
        List<SysUser> users = smartJobService.getAllUser();
        //将电话号码划分为每份最多999个
        Map<String, Object> map = ComputeTime.getPhoneList(users, content, msgType, sendFrom, tittle);
        List<String> temPhones = (List<String>)map.get("phones");
        List<List<SmartSentMsg>> temMsg = (List<List<SmartSentMsg>>)map.get("Msgs");
        int len = temPhones.size();

        //判断发送类型
        if(sendType.equals(SMS)){
            //发送短信
            for(int i =0; i < len; i++){
                boolean isSuccess = DySmsHelper.sendSms(content, temPhones.get(i));
                if(isSuccess){
                    //保存短息
                    smartSentMsgService.saveBatch(temMsg.get(i), 999);
                }else{
                    //修改状态并保存
                    ComputeTime.changeStatus(temMsg.get(i));
                    smartSentMsgService.saveBatch(temMsg.get(i), 999);
                }
            }

        }else if(sendType.equals(SYS)){
            //发送系统消息
            //发送站内信
            for(SysUser s : users){
                MessageDTO messageDTO=new MessageDTO();
                messageDTO.setTitle("其他");
                messageDTO.setContent(content);
                messageDTO.setFromUser(from);
                messageDTO.setToUser(s.getUsername());
                messageDTO.setCategory("1");
                sysBaseAPI.sendSysAnnouncement(messageDTO);
            }
        }else{
            return;
        }
    }
}
