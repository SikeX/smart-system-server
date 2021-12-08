package org.jeecg.modules.smartJob.util;

import io.netty.util.Timeout;
import io.netty.util.concurrent.ScheduledFuture;
import org.jeecg.modules.smartJob.entity.JobType;
import org.jeecg.modules.smartJob.entity.SmartJob;
import org.jeecg.modules.smartJob.service.ISmartJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description: TODO
 * @author: lord
 * @date: 2021年12月03日 10:48
 */
@Component
public class TaskInit {

    private static ISmartJobService smartJobService;
    @Autowired
    public void setSmartJobService(ISmartJobService smartJobService){

        TaskInit.smartJobService = smartJobService;
    }

    static DelayTask delayTask = DelayTask.getInstance();
    static LoopTask loopTask = LoopTask.getInstance();

    public static boolean init(){
        //读数据库，筛选出状态为“开启”的任务
        List<SmartJob> list = smartJobService.initGetTasks();

        System.out.println("任务初始化中...");
        for(SmartJob s : list){
            initJob(s);
            System.out.println("任务：" + s.getJobName() + " 初始化完成");
        }
        System.out.println("初始化完成！");

        return true;
    }

    private static void initJob(SmartJob s) {
        if(s.getJobType().equals(JobType.getTHEPART())){

            //添加入党纪念日
            ScheduledFuture task = loopTask.addThePart(
                    s.getTemplateContent(),
                    ComputeTime.loopGetDelayMinutes(s.getExecuteTimeHour()),
                    s.getType()
            );
            loopTask.addOpen(s.getJobBean(), task);

        }else if(s.getJobType().equals(JobType.getPUNISH())){

            //添加解除处分
            ScheduledFuture task = loopTask.addPunish(
                    s.getTemplateContent(),
                    ComputeTime.loopGetDelayMinutes(s.getExecuteTimeHour()),
                    s.getType()
            );

            loopTask.addOpen(s.getJobBean(), task);
        }else if(s.getJobType().equals(JobType.getPOSTREMIND())){
            //婚后报备提醒
            ScheduledFuture task = loopTask.addLoop(
                    ComputeTime.loopGetDelayMinutes(s.getExecuteTimeHour()),
                    s.getTemplateContent()
            );
            loopTask.addOpen(s.getJobBean(), task);
        }else{
            //添加其他类型任务
            //检查是否需要每日提醒
            if(s.getIsLoop().equals("0")){
                //需要，开启loop任务
                ScheduledFuture task = loopTask.addLoop(
                        s.getCreateBy(),
                        s.getTemplateContent(),
                        ComputeTime.loopGetDelayMinutes(s.getExecuteTimeHour()),
                        s.getIsToAll(),
                        s.getToUser(),
                        s.getType()
                );

                loopTask.addOpen(s.getJobBean(), task);
            }else{
                //不需要，开启延迟任务
                Timeout task = delayTask.addTask(
                        s.getJobBean(),
                        s.getCreateBy(),
                        s.getTemplateContent(),
                        ComputeTime.getDelayTime(s.getExecuteTimeDay(),
                                s.getExecuteTimeHour()),
                        s.getIsToAll(),
                        s.getToUser(),
                        s.getType()
                );

                //记录
                delayTask.addOpen(s.getJobBean(), task);

            }
        }
    }
}
