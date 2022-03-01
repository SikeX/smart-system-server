package org.jeecg.modules.smartJob.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.ScheduledFuture;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.dto.message.MessageDTO;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.modules.SmartPunishPeople.entity.SmartPunishPeople;
import org.jeecg.modules.smartJob.entity.SmartJob;
import org.jeecg.modules.smartJob.service.ISmartJobService;
import org.jeecg.modules.smartJob.service.imp.SmartJobServiceImpl;
import org.jeecg.modules.smartPostMarriage.entity.SmartPostMarriageReport;
import org.jeecg.modules.smartPremaritalFiling.entity.SmartPremaritalFiling;
import org.jeecg.modules.smartPremaritalFiling.vo.SmartPremaritalFilingPage;
import org.jeecg.modules.smartSentMsg.entity.SmartSentMsg;
import org.jeecg.modules.smartSentMsg.service.ISmartSentMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.jeecg.common.util.DySmsHelper;
import org.jeecg.modules.smartJob.entity.SysUser;


/**
 * @Description: 循环定时任务
 * @author: lord
 * @date: 2021年11月30日 15:26
 */
@Slf4j
@Component
public class LoopTask {

    private static final int ONEDAY = 1440;

    private static final String SMS = "1";  //短信
    private static final String SYS = "4";  //系统

    private static ISmartJobService smartJobService;
    private static ISmartSentMsgService smartSentMsgService;
    private static ISysBaseAPI sysBaseAPI;

    @Autowired
    public void setSmartJobService(ISmartJobService smartJobService, ISmartSentMsgService smartSentMsgService, ISysBaseAPI sysBaseAPI){
        LoopTask.smartJobService = smartJobService;
        LoopTask.smartSentMsgService = smartSentMsgService;
        LoopTask.sysBaseAPI = sysBaseAPI;
    }

    //创建时间循环组
    public static final EventLoopGroup group = new DefaultEventLoopGroup(2);

    private static LoopTask loopTask = new LoopTask();

//    private List<ScheduledFuture> openList = new ArrayList<ScheduledFuture>();

    private Map<String, ScheduledFuture> openedMap = new HashMap<>();

    private LoopTask(){

    }

    public static LoopTask getInstance(){
        return loopTask;
    }

    public void addOpen(String name, ScheduledFuture task){

        log.info("\n添加任务:" + name);
        openedMap.put(name, task);
    }

    //添加定时任务
    public ScheduledFuture addLoop(String from, String content, long delay, String isToAll, String person, String sendType){

        log.info("\n添加定时任务 delay = " + delay);
        ScheduledFuture future = group.next().scheduleAtFixedRate(() -> {

            //逻辑
            log.info("\n其他类型定时任务" + " ，执行时间：" + LocalDateTime.now());
            if(isToAll.equals("0")){
                loopToAll(from, content, sendType);
            }else{
                loopToSome(from, content, person, sendType);
            }




        }, delay, ONEDAY, TimeUnit.MINUTES);

        return future;
    }

    /**
     * 添加婚后报备，每三日提醒
     * @param content：提醒的内容
     * @param delay：第一次执行距离现在的时间
     * @return
     */

    public ScheduledFuture addLoop(long delay, String content){

        log.info("\n添加婚后提醒 delay = " + delay);
        ScheduledFuture future = group.next().scheduleAtFixedRate(() -> {

            log.info("\n婚后报备提醒执行" + " ，执行时间：" + LocalDateTime.now());

            //逻辑
            //获取婚前报备表is_report为0的数据
            List<SmartPremaritalFiling> smartPremaritalFilings = smartJobService.selectNotReport();
            for(SmartPremaritalFiling s : smartPremaritalFilings){

                //判断婚礼是否已经行
                Date now = new Date();
                long wedding = s.getWeddingTime().getTime();
                long nowL = now.getTime();
                if((nowL - wedding) < 0){
                    //婚礼未进行
                    continue;
                }

                //根据婚前id查找婚后是否有记录
                SmartPostMarriageReport smartPostMarriageReport = smartJobService.selectByPreId(s.getId());

                if(null != smartPostMarriageReport){
                    //已报备
                    continue;
                }else{
                    //未报备
                    //判断是否超过15日
                    boolean isFifteen = ComputeTime.isFifteen(s.getWeddingTime());
                    if(isFifteen){
                        //超过，将婚前isReport字段更新为15，表示15天未填报，并发送系统消息提醒管理员
                        smartJobService.updatePreIsReport(s.getId());

                        //通知管理员
                    }else{
                        //未超过
                        //判断今日是否提醒
                        boolean isRemind = ComputeTime.roundThree(s.getWeddingTime());
                        if(isRemind){
                            //提醒

                            Date date = new Date();
                            String orgId = smartJobService.getOrgId("admin");

                            SmartSentMsg smartSentMsg = new SmartSentMsg();
                            smartSentMsg.setSendType("7");
                            smartSentMsg.setSendFrom("admin");
                            smartSentMsg.setSysOrgCode(orgId);
                            smartSentMsg.setReceiver(s.getPeopleName());
                            smartSentMsg.setTittle("婚后报备提醒");
                            smartSentMsg.setSendTime(date);
                            smartSentMsg.setContent(content);
                            smartSentMsg.setReceiverPhone(s.getContactNumber());

                            boolean isSuccess = DySmsHelper.sendSms(content, s.getContactNumber());

                            if(isSuccess){
                                smartSentMsg.setStatus("0");
                            }else{
                                smartSentMsg.setStatus("1");
                            }

                            //保存
                            smartSentMsgService.save(smartSentMsg);

                            SysUser toUser = smartJobService.getPeopleInfo(s.getPeopleId());
                            //发送站内信
                            MessageDTO messageDTO=new MessageDTO();
                            messageDTO.setTitle("婚后报备提醒");
                            messageDTO.setContent("您好，请于婚礼结束15日内填写婚后报备信息。");
                            messageDTO.setFromUser("admin");
                            messageDTO.setToUser(toUser.getUsername());
                            messageDTO.setCategory("1");
                            sysBaseAPI.sendSysAnnouncement(messageDTO);
                        }
                    }
                }
            }

        }, delay, ONEDAY, TimeUnit.MINUTES);

        return future;
    }


    //入党纪念日提醒
    public ScheduledFuture addThePart(String content, long delay, String sendType){

        log.info("\n添加入党纪念日提醒 delay = " + delay);
        ScheduledFuture future = group.next().scheduleAtFixedRate(() -> {


            log.info("\n入党纪念日任务！执行！！ 日期：" + new Date());
            anniversaryRe(content, sendType);
            Set<String> keySet = openedMap.keySet();
            for(String s : keySet){
                ScheduledFuture future1 = openedMap.get(s);
                System.out.println(future1.getDelay(TimeUnit.MINUTES));
            }

        }, delay, ONEDAY, TimeUnit.MINUTES);

        return future;
    }

    //解处分提醒
    public ScheduledFuture addPunish(String content, long delay, String sendType){

        log.info("\n添加解除处分提醒 delay = " + delay);
        ScheduledFuture future = group.next().scheduleAtFixedRate(() -> {

            log.info("\n解除处分任务执行！执行！！ 日期：" + new Date());
            punishRe(content, sendType);

        }, delay, ONEDAY, TimeUnit.MINUTES);

        future.getDelay(TimeUnit.MINUTES);

        return future;
    }


    public boolean deleteJob(String jobBean) {

        log.info("loopTask openedMap keySet: " + openedMap.keySet());

        //移除
        ScheduledFuture task = openedMap.remove(jobBean);
        if(null != task){
            //取消
            log.info("\n任务取消：" + jobBean);
            return task.cancel(true);
        }else{
            //任务不在执行中
            log.info("\n任务取消失败：" + jobBean + " 任务不存在！");
            return true;
        }

    }

    private void punishRe(String content, String sendType){

        String msgType = "5"; //短信类型
        String sendFrom = "admin";  //发送人
        String tittle = "解除处分通知";  //title

        //查询今日解除处分人员
        List<SmartPunishPeople> list = smartJobService.getPunish();

        //将电话号码划分为每份最多999个
        Map<String, Object> map = ComputeTime.getPunoshPhoneList(list, content, msgType, sendFrom, tittle);
        List<String> temPhones = (List<String>)map.get("phones");
        List<List<SmartSentMsg>> temMsg = (List<List<SmartSentMsg>>)map.get("Msgs");
        int len = temPhones.size();

        //判断
        //查询任务信息
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
//            for(SmartPunishPeople s : list){
//                MessageDTO messageDTO=new MessageDTO();
//                messageDTO.setTitle("其他");
//                messageDTO.setContent(content);
//                messageDTO.setFromUser(from);
//                messageDTO.setToUser(s.getUsername());
//                messageDTO.setCategory("1");
//                sysBaseAPI.sendSysAnnouncement(messageDTO);
//            }
        }else{
            return;
        }
    }

    private void anniversaryRe(String content, String sendType){
        String msgType = "4"; //短信类型
        String sendFrom = "admin";  //发送人
        String tittle = "入党纪念日提醒";  //title

        //查询入党日期为今日的人员
        List<SysUser> sysUsers = smartJobService.getAnniversaryList();

        //将电话号码划分为每份最多999个
        Map<String, Object> map = ComputeTime.getPhoneList(sysUsers, content, msgType, sendFrom, tittle);
        List<String> temPhones = (List<String>)map.get("phones");
        List<List<SmartSentMsg>> temMsg = (List<List<SmartSentMsg>>)map.get("Msgs");
        int len = temPhones.size();

        //判断
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
            for(SysUser s : sysUsers){
                MessageDTO messageDTO=new MessageDTO();
                messageDTO.setTitle("入党纪念日提醒");
                messageDTO.setContent(content);
                messageDTO.setFromUser("admin");
                messageDTO.setToUser(s.getUsername());
                messageDTO.setCategory("1");
                sysBaseAPI.sendSysAnnouncement(messageDTO);
            }
        }else{
            return;
        }
    }

    private void loopToAll(String from, String content, String sendType){

        String msgType = "7"; //短信类型
        String sendFrom = from;  //发送人
        String tittle = "其他";  //title

        //获取人员列表
        List<SysUser> list = smartJobService.getAllUser();

        //将电话号码划分为每份最多999个
        Map<String, Object> map = ComputeTime.getPhoneList(list, content, msgType, sendFrom, tittle);
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
            //系统消息
            //发送站内信
            for(SysUser s : list){
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

    private void loopToSome(String from, String content, String person, String sendType){

        String msgType = "7"; //短信类型
        String sendFrom = from;  //发送人
        String tittle = "其他";  //title

        //获取人员信息
        List<SysUser> list = ComputeTime.getUserInfo(person);

        //将电话号码划分为每份最多999个
        Map<String, Object> map = ComputeTime.getPhoneList(list, content, msgType, sendFrom, tittle);
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
            for(SysUser s : list){
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
