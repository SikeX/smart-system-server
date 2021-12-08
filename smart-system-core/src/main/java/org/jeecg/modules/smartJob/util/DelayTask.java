package org.jeecg.modules.smartJob.util;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import org.jeecg.common.util.DySmsHelper;
import org.jeecg.modules.smartJob.entity.SmartJob;
import org.jeecg.modules.smartJob.entity.SysUser;
import org.jeecg.modules.smartJob.service.ISmartJobService;
import org.jeecg.modules.smartSentMsg.entity.SmartSentMsg;
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
@Component
public class DelayTask {

    private static final String SMS = "1";  //短信
    private static final String SYS = "4";  //系统

    private static ISmartJobService smartJobService;


    @Autowired
    public void setSmartJobService(ISmartJobService smartJobService){

        DelayTask.smartJobService = smartJobService;
    }

    // 创建延迟任务实例
    private static HashedWheelTimer timer = new HashedWheelTimer(1, // 时间间隔
            TimeUnit.MINUTES,//时间单位
            64); // 时间轮中的槽数

    private static DelayTask delayTask = new DelayTask();

//    private List<Timeout> openList = new ArrayList<Timeout>();

    private Map<String, Timeout> openedMap = new HashMap<>();

    private DelayTask(){

    }

    public void addOpen(String name, Timeout task){

        openedMap.put(name, task);
    }

    public static DelayTask getInstance(){
        return delayTask;
    }

    /**
     * 添加延迟任务
     */
    public Timeout addTask(String jobBean, String from, String content ,long delay, String isToAll, String person, String sendType) {

        System.out.println("添加延迟任务 delay = " + delay);
        // 创建一个任务
        TimerTask task = new TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {
                System.out.println("执行延迟提醒任务" + " ，执行时间：" + LocalDateTime.now());

                //判断是否发给所有人
                if(isToAll.equals("0")){
                    //发给所有人
                    delayToAll(from, content, sendType);
                }else{
                    //发给指定用户
                    delayToSome(from, content, person, sendType);
                }

                //遍历openMap，删除当前任务bean
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
        System.out.println(openedMap.keySet());
        Timeout delTask = openedMap.remove(jobBean);

        //在map
        if(null != delTask){
            //取消任务
            System.out.println("任务取消：" + jobBean);
            return delTask.cancel();
        }else{
            //不在map
            return true;
        }
    }

    private void delayToSome(String from, String content, String person, String sendType) {
        //获取用户
        List<SysUser> users = ComputeTime.getUserInfo(person);

        //判断发送类型
        if(sendType.equals(SMS)){
            //将手机号拼接为String
//            int len = users.size();
//            String phones = "";


            //发送短信
            List<List<String>> infoList = ComputeTime.getNameAndPhone(users);
            System.out.println(infoList.get(0));
            System.out.println(infoList.get(1));
            int len = infoList.get(0).size();

//            String orgId = smartJobService.getOrgId("from");
//            System.out.println(orgId);
//
//            List<SmartSentMsg> list = ComputeTime.userToSMS(users, from, orgId, "0", "其他提醒", content);
//            DySmsHelper.sendSms(list);



            for(int i = 0; i < len; i++){
                DySmsHelper.sendSms(
//                        from,
//                        SMS,
//                        "其他提醒",
                        content,
//                        infoList.get(0).get(i),
                        infoList.get(1).get(i)
                );
                SmartSentMsg smartSentMsg = new SmartSentMsg();



//                smartJobService.saveSMS(smartSentMsg);
            }


        }else if(sendType.equals(SYS)){
            //发送系统消息

        }else{
            return;
        }


    }

    private void delayToAll(String from, String content, String sendType) {
        //获取用户

        List<SysUser> users = smartJobService.getAllUser();

        //判断发送类型
        if(sendType.equals(SMS)){
            //发送短信
            List<List<String>> infoList = ComputeTime.getNameAndPhone(users);
            System.out.println(infoList.get(0));
            System.out.println(infoList.get(1));
            int len = infoList.get(0).size();
            for(int i = 0; i < len; i++){
                DySmsHelper.sendSms(
                        from,
                        SMS,
                        "其他提醒",
                        content,
                        infoList.get(0).get(i),
                        infoList.get(1).get(i)
                );
            }

        }else if(sendType.equals(SYS)){
            //发送系统消息
        }else{
            return;
        }
    }

    //婚后报备十五日提醒
    public static Timeout addTask(String jobBean, String content, long delay, String person, String sendType) {

        System.out.println("添加婚后报备提醒 delay = " + delay);
        // 创建一个任务
        TimerTask task = new TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {
                System.out.println("执行延迟提醒任务" + " ，执行时间：" + LocalDateTime.now());

                //查询是否婚后报备
                //报备，结束任务

                //未报备，提醒管理员，结束任务

                //遍历openMap，删除当前任务bean
//                openedMap.remove(jobBean);

                //修改数据据job为null
                //修改数据库当前任务状态为结束
                smartJobService.updateStatus(jobBean);



            }
        };



        // 将任务添加到延迟队列中
        return timer.newTimeout(task, delay, TimeUnit.MINUTES);
    }

    public static boolean addPostMarray(String jobBean, String from, String content ,long delay, String person, String sendType){

        //添加任务
        addTask(jobBean, content , delay, person, sendType);

        //添加openMap


        return true;
    }
}
