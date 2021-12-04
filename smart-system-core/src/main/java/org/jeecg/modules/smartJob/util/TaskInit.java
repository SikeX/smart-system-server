package org.jeecg.modules.smartJob.util;

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

    public static boolean init(){
        //读数据库，筛选出状态为“开启”的任务
        List<SmartJob> list = smartJobService.initGetTasks();

        System.out.println("任务初始化中...");
        for(SmartJob s : list){
            smartJobService.openJob(s, s.getCreateBy());
            System.out.println("任务：" + s.getJobName() + " 初始化完成");
        }
        System.out.println("初始化完成！");

        //开启任务
//        smartJobService.openJob();

        return true;
    }
}
