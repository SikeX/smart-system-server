package org.jeecg.modules.smartJob.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.ScheduledFuture;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.SmartPunishPeople.entity.SmartPunishPeople;
import org.jeecg.modules.smartJob.entity.SmartJob;
import org.jeecg.modules.smartJob.service.ISmartJobService;
import org.jeecg.modules.smartJob.service.imp.SmartJobServiceImpl;
import org.jeecg.modules.smartPostMarriage.entity.SmartPostMarriageReport;
import org.jeecg.modules.smartPremaritalFiling.entity.SmartPremaritalFiling;
import org.jeecg.modules.smartPremaritalFiling.vo.SmartPremaritalFilingPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
    private static final int THREEDAY = 4320;

    private static final String SMS = "1";  //短信
    private static final String SYS = "4";  //系统

    private static ISmartJobService smartJobService;


    @Autowired
    public void setSmartJobService(ISmartJobService smartJobService){
        LoopTask.smartJobService = smartJobService;
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

        openedMap.put(name, task);
    }

    //添加定时任务
    public ScheduledFuture addLoop(String from, String content, long delay, String isToAll, String person, String sendType){

        System.out.println("添加定时任务 delay = " + delay);
        ScheduledFuture future = group.next().scheduleAtFixedRate(() -> {

            //逻辑
            System.out.println("其他类型任务" + " ，执行时间：" + LocalDateTime.now());
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
     * @param delay：第一次执行的时间
     * @return
     */
    public ScheduledFuture addLoop(long delay, String content){


        System.out.println("添加婚后提醒 delay = " + delay);
        ScheduledFuture future = group.next().scheduleAtFixedRate(() -> {

            System.out.println("婚后报备提醒执行" + " ，执行时间：" + LocalDateTime.now());

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

                SmartPostMarriageReport smartPostMarriageReport = smartJobService.selectByPreId(s.getId());

                if(null != smartPostMarriageReport){
                    //已报备
                    continue;
                }else{
                    //未报备
                    //判断是否超过15日
                    boolean isFifteen = ComputeTime.isFifteen(s.getWeddingTime());
                    if(isFifteen){
                        //超过

                        //通知管理员
                    }else{
                        //未超过
                        //判断今日是否提醒
                        boolean re = ComputeTime.roundThree(s.getWeddingTime());
                        if(re){
                            //提醒
                            DySmsHelper.sendSms(content, s.getContactNumber());
                        }
                    }
                }
            }

        }, delay, ONEDAY, TimeUnit.MINUTES);

        return future;
    }



    //入党纪念日提醒
    public ScheduledFuture addThePart(String content, long delay, String sendType){

        System.out.println("添加入党纪念日提醒 delay = " + delay);
        ScheduledFuture future = group.next().scheduleAtFixedRate(() -> {


            System.out.println("入党纪念日任务！执行！！ 日期：" + new Date());
            anniversaryRe(content, sendType);

        }, delay, ONEDAY, TimeUnit.MINUTES);

        return future;
    }

    //解处分提醒
    public ScheduledFuture addPunish(String content, long delay, String sendType){

        System.out.println("添加解除处分提醒 delay = " + delay);
        ScheduledFuture future = group.next().scheduleAtFixedRate(() -> {

            System.out.println("解除处分任务执行！执行！！ 日期：" + new Date());
            punishRe(content, sendType);

        }, delay, ONEDAY, TimeUnit.MINUTES);

        return future;
    }


    public boolean deleteJob(String jobBean) {

        //移除
        ScheduledFuture task = openedMap.remove(jobBean);
        if(null != task){
            //取消
            System.out.println("任务取消：" + jobBean);
            return task.cancel(true);
        }else{
            //任务不在执行中
            return true;
        }

    }


    private void punishRe(String content, String sendType){
        //查询今日解除处分人员
        List<SmartPunishPeople> list = smartJobService.getPunish();
        System.out.println("提醒内容为：" + content);

        //判断
        //查询任务信息
        if(sendType.equals(SMS)){
            //发送短信
            for(SmartPunishPeople p : list){
                System.out.println(p.toString());
                DySmsHelper.sendSms(
                        "admin",
                        SYS,
                        "解除处分提醒",
                        content,
                        p.getPunishName(),
                        p.getPhone()
                        );
            }
        }else if(sendType.equals(SYS)){
            //发送系统消息
        }else{
            return;
        }
    }

    private void anniversaryRe(String content, String sendType){

        //查询入党日期为今日的人员
        List<SysUser> sysUsers = smartJobService.getAnniversaryList();
        System.out.println("提醒内容为：" + content);

        //判断
        //查询任务信息
        if(sendType.equals(SMS)){
            //发送短信
            for(SysUser p : sysUsers){
                System.out.println(p.toString());
                DySmsHelper.sendSms(
                        "admin",
                        SMS,
                        "入党纪念日",
                        content,
                        p.getRealname(),
                        p.getPhone()
                );
            }
        }else if(sendType.equals(SYS)){
            //发送系统消息
        }else{
            return;
        }
    }

    private void loopToAll(String from, String content, String sendType){

        //获取人员列表
        List<SysUser> list = smartJobService.getAllUser();
        System.out.println("每日提醒，提醒内容：" + content);

        //判断发送类型
        if(sendType.equals(SMS)){
            //发送短信
            List<List<String>> infoList = ComputeTime.getNameAndPhone(list);
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
            //系统消息
        }else{
            return;
        }
    }

    private void loopToSome(String from, String content, String person, String sendType){

        //获取人员信息
        List<SysUser> list = ComputeTime.getUserInfo(person);
        System.out.println("每日提醒，提醒内容：" + content);

        //判断发送类型
        if(sendType.equals(SMS)){
            //发送短信
            List<List<String>> infoList = ComputeTime.getNameAndPhone(list);
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

//    public boolean addPostMarray(String peopleId, Date weddingTime, String jobBean){
//
//        SmartJob smartJob = new SmartJob();
//        smartJob.setJobBean(jobBean);
//
//        //部门管理人员，暂时写admin
//        smartJob.setUpdateBy("admin");
//
//        smartJob.setToUser(peopleId);
//        smartJob.setJobType("3");   //婚后报备提醒
//        smartJob.setIsLoop("0");
//        smartJob.setIsToAll("1");
//        smartJob.setType("1");
//        smartJob.setJobName("婚后报备提醒");
//        smartJob.setTemplateName("婚后报备提醒模板");
//        smartJob.setTemplateContent("Complex");
//
//        //获取delay
//        long delay = ComputeTime.getPostDelayTime(weddingTime);
//        //开启任务
//        ScheduledFuture future = addLoop(delay, peopleId, smartJob.getJobBean());
//        //添加任务到openMap
//        addOpen(smartJob.getJobBean(), future);
//
//        //设置时间
//        ComputeTime.setPostFirstTime(smartJob, weddingTime);
//
//        //保存job
//        smartJobService.save(smartJob);
//
//        return true;
//    }

//    //婚后报备编辑
//    public void editPostMarray(SmartPremaritalFiling smartPremaritalFiling) {
//        System.out.println("编辑！");
//        //取消之前的任务
//        deleteJob(smartPremaritalFiling.getId());
//
//        //重新添加
//        //从数据库查询任务
//        SmartJob smartJob = smartJobService.getByBean(smartPremaritalFiling.getId());
//
//        //更新任务信息
//        smartJob.setToUser(smartPremaritalFiling.getId());
//        smartJob.setJobStatus("开启");
//
//        //添加新的提醒
//        //获取delay
//        long delay = ComputeTime.getPostDelayTime(smartPremaritalFiling.getWeddingTime());
//        //开启任务
//        ScheduledFuture future = addLoop(
//                delay,
//                smartPremaritalFiling.getPeopleId(),
//                smartJob.getJobBean());
//        //添加任务到openMap
//        addOpen(smartJob.getJobBean(), future);
//
//        //设置时间
//        ComputeTime.setPostFirstTime(smartJob, smartPremaritalFiling.getWeddingTime());
//
//        //保存job
//        smartJobService.save(smartJob);
//    }
}
