package org.jeecg.modules.smartJob.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.netty.util.Timeout;
import io.netty.util.concurrent.ScheduledFuture;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.SmartPunishPeople.entity.SmartPunishPeople;
import org.jeecg.modules.SmartPunishPeople.mapper.SmartPunishPeopleMapper;
import org.jeecg.modules.smartJob.entity.JobType;
import org.jeecg.modules.smartJob.entity.SmartJob;
import org.jeecg.modules.smartJob.entity.SysUser;
import org.jeecg.modules.smartJob.mapper.SmartJobMapper;
import org.jeecg.modules.smartJob.service.ISmartJobService;
import org.jeecg.modules.smartJob.util.ComputeTime;
import org.jeecg.modules.smartJob.util.DelayTask;
import org.jeecg.modules.smartJob.util.LoopTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Description: TODO
 * @author: lord
 * @date: 2021年11月30日 16:05
 */
@Service
@Slf4j
public class SmartJobServiceImpl extends ServiceImpl<SmartJobMapper, SmartJob> implements ISmartJobService {

    DelayTask delayTask = DelayTask.getInstance();
    LoopTask loopTask = LoopTask.getInstance();

    @Resource
    SmartJobMapper smartJobMapper;

    @Resource
    SmartPunishPeopleMapper smartPunishPeopleMapper;

    @Override
    public boolean edit(SmartJob smartJob, String sendFrom) {

        //查询jobBean
//        QueryWrapper<SmartJob> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("id", smartJob.getId());
//        SmartJob smartJob1 = getOne(queryWrapper);

        //从map删除
        //判断类型
        if(smartJob.getIsLoop().equals("0")){
            //loop
            loopTask.deleteJob(smartJob.getJobBean());
        }else{
            //delay
            delayTask.deleteTask(smartJob.getJobBean());
        }



        //重新添加
        return openJob(smartJob, sendFrom);

    }

    @Override
    public boolean openJob(SmartJob smartJob, String sendFrom) {

        smartJob.setJobBean();
        if(smartJob.getJobType().equals(JobType.getTHEPART())){
            //检查入党纪念日任务是否开启
            QueryWrapper<SmartJob> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("job_type", smartJob.getJobType());
            SmartJob smartJob1 = getOne(queryWrapper);

            if(smartJob1 == null){
                //未开启，添加loop任务，
                ScheduledFuture task = loopTask.addThePart(
                        smartJob.getTemplateContent(),
                        ComputeTime.loopGetDelayMinutes(smartJob.getExecuteTimeHour()),
                        smartJob.getType()
                        );

                loopTask.addOpen(smartJob.getJobBean(), task);
                return true;
            }else{
                //开启，返回已开启信息
                return false;
            }
        }else if(smartJob.getJobType().equals(JobType.getPUNISH())){
            //检查解除处分任务是否开启
            QueryWrapper<SmartJob> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("job_type", smartJob.getJobType());
            SmartJob smartJob1 = getOne(queryWrapper);
            if(smartJob1 == null){
                //未开启，添加loop任务，
                ScheduledFuture task = loopTask.addPunish(
                        smartJob.getTemplateContent(),
                        ComputeTime.loopGetDelayMinutes(smartJob.getExecuteTimeHour()),
                        smartJob.getType()
                        );

                loopTask.addOpen(smartJob.getJobBean(), task);
                return true;
            }else{
                //开启，返回已开启信息
                return false;
            }
        }else{
            //添加其他类型任务
            //检查是否需要每日提醒
            if(smartJob.getIsLoop().equals("0")){
                //需要，开启loop任务
                ScheduledFuture task = loopTask.addLoop(
                        sendFrom,
                        smartJob.getTemplateContent(),
                        ComputeTime.loopGetDelayMinutes(smartJob.getExecuteTimeHour()),
                        smartJob.getIsToAll(),
                        smartJob.getToUser(),
                        smartJob.getType()
                        );

                loopTask.addOpen(smartJob.getJobBean(), task);
                return true;
            }else{
                //不需要，开启延迟任务
                Timeout task = delayTask.addTask(
                        smartJob.getJobBean(),
                        sendFrom,
                        smartJob.getTemplateContent(),
                        ComputeTime.getDelayTime(smartJob.getExecuteTimeDay(),
                                smartJob.getExecuteTimeHour()),
                        smartJob.getIsToAll(),
                        smartJob.getToUser(),
                        smartJob.getType()
                );

                //记录
                delayTask.addOpen(smartJob.getJobBean(), task);



                return true;
            }
        }
    }

    @Override
    public List<SmartPunishPeople> getPunish() {
        return smartJobMapper.getPunish();
    }

    @Override
    public List<SysUser> getAnniversaryList() {
        return smartJobMapper.getAnniversaryList();
    }

    @Override
    public List<SysUser> getAllUser() {
        return smartJobMapper.getAllUser();
    }

    @Override
    public List<SysUser> selectBatchIds(String ids) {
        return null;
    }

    @Override
    public List<SysUser> getUsers(List<String> userids) {
        return smartJobMapper.getUsers(userids);
    }

    @Override
    public void updateStatus(String jobBean) {
        //修改数据据job为null
        //修改数据库当前任务状态为结束
        QueryWrapper<SmartJob> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("job_bean", jobBean);
        SmartJob smartJob = getOne(queryWrapper);

        smartJob.setJobStatus("结束");
        smartJob.setJobBean("null");

        if(updateById(smartJob)){
            System.out.println("延迟任务执行成功，状态更新成功");
        }
    }

    @Override
    public List<SmartJob> initGetTasks() {
        return smartJobMapper.initGetTasks();
    }

    @Override
    public String getOrgId(String from) {
        return smartJobMapper.getOrgId(from);
    }


}
