package org.jeecg.modules.tasks.smartVerifyTask.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.tasks.smartVerifyTask.entity.SmartVerifyTask;
import org.jeecg.modules.tasks.smartVerifyTask.mapper.SmartVerifyTaskMapper;
import org.jeecg.modules.tasks.smartVerifyTask.service.ISmartVerifyTaskService;
import org.jeecg.modules.tasks.smartVerifyTask.vo.SzydVerifyTaskListPage;
import org.jeecg.modules.tasks.smartVerifyTask.vo.VerifyTaskListPage;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: 审核任务表
 * @Author: jeecg-boot
 * @Date:   2021-11-04
 * @Version: V1.0
 */
@Service
public class SmartVerifyTaskServiceImpl extends ServiceImpl<SmartVerifyTaskMapper, SmartVerifyTask> implements ISmartVerifyTaskService {


    @Resource
    SmartVerifyTaskMapper smartVerifyTaskMapper;

    @Override
    public Page<VerifyTaskListPage> getTaskList(Page<VerifyTaskListPage> page, List<String> typeList,
                                                VerifyTaskListPage verifyTaskListPage){
        return page.setRecords(smartVerifyTaskMapper.getTaskList(page, typeList, verifyTaskListPage));
    }

    @Override
    public Page<VerifyTaskListPage> getNotPassList(Page<VerifyTaskListPage> page, List<String> typeList,
                                                   VerifyTaskListPage verifyTaskListPage){
        return page.setRecords(smartVerifyTaskMapper.getNotPassList(page, typeList, verifyTaskListPage));
    }

    @Override
    public IPage<SzydVerifyTaskListPage> getSzydTaskList(Page<SzydVerifyTaskListPage> page, List<String> typeList, SzydVerifyTaskListPage szydVerifyTaskListPage) {
        return page.setRecords(smartVerifyTaskMapper.getSzydTaskList(page, typeList, szydVerifyTaskListPage));
    }

    @Override
    public IPage<SzydVerifyTaskListPage> getSzydNotPassList(Page<SzydVerifyTaskListPage> page, List<String> typeList, SzydVerifyTaskListPage szydVerifyTaskListPage) {
        return page.setRecords(smartVerifyTaskMapper.getSzydNotPassList(page, typeList, szydVerifyTaskListPage));
    }

}
