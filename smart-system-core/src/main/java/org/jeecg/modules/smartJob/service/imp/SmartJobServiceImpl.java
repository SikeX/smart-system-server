package org.jeecg.modules.smartJob.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.netty.util.Timeout;
import io.netty.util.concurrent.ScheduledFuture;
import lombok.extern.slf4j.Slf4j;
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
import org.jeecg.modules.smartPostMarriage.entity.SmartPostMarriageReport;
import org.jeecg.modules.smartPremaritalFiling.entity.SmartPremaritalFiling;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
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

        //重新添加
        if(smartJob.getJobType().equals(JobType.getTHEPART())){

            //检查入党纪念日任务和当前编辑任务id是否相同
            QueryWrapper<SmartJob> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("job_type", smartJob.getJobType());
            SmartJob smartJob1 = getOne(queryWrapper);

            if(null != smartJob1 && smartJob1.getId().equals(smartJob.getId())){

                //删除以前的job
                if(smartJob.getIsLoop().equals("0")){
                    //loop
                    loopTask.deleteJob(smartJob.getJobBean());
                }else{
                    //delay
                    delayTask.deleteTask(smartJob.getJobBean());
                }

                smartJob.setJobBean();

                //添加入党纪念日
                ScheduledFuture task = loopTask.addThePart(
                        smartJob.getTemplateContent(),
                        ComputeTime.loopGetDelayMinutes(smartJob.getExecuteTimeHour()),
                        smartJob.getType()
                );
                loopTask.addOpen(smartJob.getJobBean(), task);
                return true;
            }else if(null == smartJob1){

                //删除以前的job
                if(smartJob.getIsLoop().equals("0")){
                    //loop
                    loopTask.deleteJob(smartJob.getJobBean());
                }else{
                    //delay
                    delayTask.deleteTask(smartJob.getJobBean());
                }

                smartJob.setJobBean();

                //添加入党纪念日，修改
                ScheduledFuture task = loopTask.addThePart(
                        smartJob.getTemplateContent(),
                        ComputeTime.loopGetDelayMinutes(smartJob.getExecuteTimeHour()),
                        smartJob.getType()
                );
                loopTask.addOpen(smartJob.getJobBean(), task);
                return true;
            }else{
                return false;
            }

        }else if(smartJob.getJobType().equals(JobType.getPUNISH())){

            //检查解除处分任务是否开启
            QueryWrapper<SmartJob> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("job_type", smartJob.getJobType());
            SmartJob smartJob1 = getOne(queryWrapper);

            if(null != smartJob1 && smartJob.getId().equals(smartJob1.getId())){

                //删除以前的job
                if(smartJob.getIsLoop().equals("0")){
                    //loop
                    loopTask.deleteJob(smartJob.getJobBean());
                }else{
                    //delay
                    delayTask.deleteTask(smartJob.getJobBean());
                }

                smartJob.setJobBean();

                //添加解除处分,修改
                ScheduledFuture task = loopTask.addPunish(
                        smartJob.getTemplateContent(),
                        ComputeTime.loopGetDelayMinutes(smartJob.getExecuteTimeHour()),
                        smartJob.getType()
                );

                loopTask.addOpen(smartJob.getJobBean(), task);
                return true;
            }else if(null == smartJob1){

                //删除以前的job
                if(smartJob.getIsLoop().equals("0")){
                    //loop
                    loopTask.deleteJob(smartJob.getJobBean());
                }else{
                    //delay
                    delayTask.deleteTask(smartJob.getJobBean());
                }

                smartJob.setJobBean();

                //添加
                //添加解除处分,修改
                ScheduledFuture task = loopTask.addPunish(
                        smartJob.getTemplateContent(),
                        ComputeTime.loopGetDelayMinutes(smartJob.getExecuteTimeHour()),
                        smartJob.getType()
                );

                loopTask.addOpen(smartJob.getJobBean(), task);
                return true;
            }else{
                return false;
            }
        }else if(smartJob.getJobType().equals(JobType.getPOSTREMIND())){

            //婚后报备提醒
            QueryWrapper<SmartJob> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("job_type", smartJob.getJobType());
            SmartJob smartJob1 = getOne(queryWrapper);

            if(null != smartJob1 & smartJob.getId().equals(smartJob1.getId())){

                //删除以前的job
                if(smartJob.getIsLoop().equals("0")){
                    //loop
                    loopTask.deleteJob(smartJob.getJobBean());
                }else{
                    //delay
                    delayTask.deleteTask(smartJob.getJobBean());
                }

                smartJob.setJobBean();

                //婚后报备提醒,修改
                ScheduledFuture task = loopTask.addLoop(
                        ComputeTime.loopGetDelayMinutes(smartJob.getExecuteTimeHour()),
                        smartJob.getTemplateContent()
                );
                loopTask.addOpen(smartJob.getJobBean(), task);
                return true;
            }else if(null == smartJob1){

                //删除以前的job
                if(smartJob.getIsLoop().equals("0")){
                    //loop
                    loopTask.deleteJob(smartJob.getJobBean());
                }else{
                    //delay
                    delayTask.deleteTask(smartJob.getJobBean());
                }

                smartJob.setJobBean();

                //添加
                //婚后报备提醒
                ScheduledFuture task = loopTask.addLoop(
                        ComputeTime.loopGetDelayMinutes(smartJob.getExecuteTimeHour()),
                        smartJob.getTemplateContent()
                );
                loopTask.addOpen(smartJob.getJobBean(), task);
                return true;
            }else{
                return false;
            }
        }else if(smartJob.getJobType().equals(JobType.getCUSTOMIZED())){
            //添加其他类型任务
            //检查是否需要每日提醒
            if(smartJob.getIsLoop().equals("0")){

                //删除以前的job
                if(smartJob.getIsLoop().equals("0")){
                    //loop
                    loopTask.deleteJob(smartJob.getJobBean());
                }else{
                    //delay
                    delayTask.deleteTask(smartJob.getJobBean());
                }

                smartJob.setJobBean();

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

                //删除以前的job
                if(smartJob.getIsLoop().equals("0")){
                    //loop
                    loopTask.deleteJob(smartJob.getJobBean());
                }else{
                    //delay
                    delayTask.deleteTask(smartJob.getJobBean());
                }

                smartJob.setJobBean();

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
        }else{
            return true;
        }
    }

    @Override
    public boolean openJob(SmartJob smartJob, String sendFrom) {

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
        }else if(smartJob.getJobType().equals(JobType.getPOSTREMIND())){
            //婚后报备提醒
            QueryWrapper<SmartJob> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("job_type", smartJob.getJobType());
            SmartJob smartJob1 = getOne(queryWrapper);
            if(null == smartJob1){
                //未添加
                ScheduledFuture task = loopTask.addLoop(
                        ComputeTime.loopGetDelayMinutes(smartJob.getExecuteTimeHour()),
                        smartJob.getTemplateContent()
                );
                loopTask.addOpen(smartJob.getJobBean(), task);
                return true;
            }else{
                //已开启
                return false;
            }
        }else if(smartJob.getJobType().equals(JobType.getCUSTOMIZED())){
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
        }else{
            return true;
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
        //修改数据库当前任务状态为结束
        QueryWrapper<SmartJob> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("job_bean", jobBean);
        SmartJob smartJob = getOne(queryWrapper);

        smartJob.setJobStatus("1");

        if(updateById(smartJob)){
            log.info("延迟任务执行成功，状态更新成功! job = " + smartJob.getJobBean());
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

    @Override
    public boolean init(SmartJob s, String createBy) {
        //重新添加
        if(s.getJobType().equals(JobType.getTHEPART())){

            //添加入党纪念日
            ScheduledFuture task = loopTask.addThePart(
                    s.getTemplateContent(),
                    ComputeTime.loopGetDelayMinutes(s.getExecuteTimeHour()),
                    s.getType()
            );
            loopTask.addOpen(s.getJobBean(), task);
            return true;

        }else if(s.getJobType().equals(JobType.getPUNISH())){

            //添加解除处分
            ScheduledFuture task = loopTask.addPunish(
                    s.getTemplateContent(),
                    ComputeTime.loopGetDelayMinutes(s.getExecuteTimeHour()),
                    s.getType()
            );

            loopTask.addOpen(s.getJobBean(), task);
            return true;
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
                return true;
            }else{
                //不需要，开启延迟任务
                //判断任务时间是否已过
                long time = ComputeTime.getDelayTime(s.getExecuteTimeDay(), s.getExecuteTimeHour());
                if(time < 0){
                    //任务时间已过，更状态为已完成
                    s.setJobStatus("1");
                }else{
                    Timeout task = delayTask.addTask(
                            s.getJobBean(),
                            s.getCreateBy(),
                            s.getTemplateContent(),
                            time,
                            s.getIsToAll(),
                            s.getToUser(),
                            s.getType()
                    );
                    //记录
                    delayTask.addOpen(s.getJobBean(), task);
                }

                return true;
            }
        }
    }

    @Override
    //根据人员id查询婚后报备表
    public SmartPremaritalFiling getPreMarrayInfo(String person) {

        return smartJobMapper.getPreMarrayInfo(person);
    }

    @Override
    //判断是否报备
    public boolean selectPostByName(String peopleId) {
        SmartPostMarriageReport smartPostMarriageReport = smartJobMapper.selectPostByName(peopleId);
        if(null != smartPostMarriageReport){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public SmartPremaritalFiling getPreById(String jobBean) {
        return smartJobMapper.getPreById(jobBean);
    }

    @Override
    public boolean isReport(String preId) {
        SmartPostMarriageReport smartPostMarriageReport = smartJobMapper.selectByPreId(preId);

        if(null != smartPostMarriageReport){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public SmartJob getByBean(String id) {
        return smartJobMapper.getByBean(id);
    }

    @Override
    public List<SmartPremaritalFiling> selectNotReport() {
        return smartJobMapper.selectNotReport();
    }

    @Override
    public SmartPostMarriageReport selectByPreId(String id) {
        return smartJobMapper.selectByPreId(id);
    }

    @Override
    public void changeJobStatus(SmartJob smartJob) {
        if(smartJob.getJobType().equals(JobType.getTHEPART())){
            //检查入党纪念日任务是否开启
            QueryWrapper<SmartJob> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("job_type", smartJob.getJobType());
            SmartJob smartJob1 = getOne(queryWrapper);

            if(smartJob.getId().equals(smartJob1.getId())){
                //未开启，添加loop任务，
                ScheduledFuture task = loopTask.addThePart(
                        smartJob.getTemplateContent(),
                        ComputeTime.loopGetDelayMinutes(smartJob.getExecuteTimeHour()),
                        smartJob.getType()
                );

                loopTask.addOpen(smartJob.getJobBean(), task);
            }
        }else if(smartJob.getJobType().equals(JobType.getPUNISH())){
            //检查解除处分任务是否开启
            QueryWrapper<SmartJob> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("job_type", smartJob.getJobType());
            SmartJob smartJob1 = getOne(queryWrapper);
            if(smartJob.getId().equals(smartJob1.getId())){
                //未开启，添加loop任务，
                ScheduledFuture task = loopTask.addPunish(
                        smartJob.getTemplateContent(),
                        ComputeTime.loopGetDelayMinutes(smartJob.getExecuteTimeHour()),
                        smartJob.getType()
                );

                loopTask.addOpen(smartJob.getJobBean(), task);
            }else{
                //开启
            }
        }else if(smartJob.getJobType().equals(JobType.getPOSTREMIND())){
            //婚后报备提醒
            QueryWrapper<SmartJob> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("job_type", smartJob.getJobType());
            SmartJob smartJob1 = getOne(queryWrapper);
            if(smartJob.getId().equals(smartJob1.getId())){
                //未添加
                ScheduledFuture task = loopTask.addLoop(
                        ComputeTime.loopGetDelayMinutes(smartJob.getExecuteTimeHour()),
                        smartJob.getTemplateContent()
                );
                loopTask.addOpen(smartJob.getJobBean(), task);
            }else{
                //已开启
            }
        }else if(smartJob.getJobType().equals(JobType.getCUSTOMIZED())){
            //添加自定义类型任务
            //检查是否需要每日提醒
            if(smartJob.getIsLoop().equals("0")){
                //需要，开启loop任务
                ScheduledFuture task = loopTask.addLoop(
                        smartJob.getCreateBy(),
                        smartJob.getTemplateContent(),
                        ComputeTime.loopGetDelayMinutes(smartJob.getExecuteTimeHour()),
                        smartJob.getIsToAll(),
                        smartJob.getToUser(),
                        smartJob.getType()
                );

                loopTask.addOpen(smartJob.getJobBean(), task);
            }else{
                //不需要，开启延迟任务
                Timeout task = delayTask.addTask(
                        smartJob.getJobBean(),
                        smartJob.getCreateBy(),
                        smartJob.getTemplateContent(),
                        ComputeTime.getDelayTime(smartJob.getExecuteTimeDay(),
                                smartJob.getExecuteTimeHour()),
                        smartJob.getIsToAll(),
                        smartJob.getToUser(),
                        smartJob.getType()
                );

                //记录
                delayTask.addOpen(smartJob.getJobBean(), task);
            }
        }
    }

    @Override
    public SysUser getPeopleInfo(String peopleId) {
        return smartJobMapper.getPeopleInfo(peopleId);
    }

    @Override
    public void updatePreIsReport(String id) {
        smartJobMapper.updatePreIsReport(id);
    }
}
