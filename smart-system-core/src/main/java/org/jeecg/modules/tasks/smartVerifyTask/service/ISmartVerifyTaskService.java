package org.jeecg.modules.tasks.smartVerifyTask.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.tasks.smartVerifyTask.entity.SmartVerifyTask;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.tasks.smartVerifyTask.vo.SzydVerifyTaskListPage;
import org.jeecg.modules.tasks.smartVerifyTask.vo.VerifyTaskListPage;

import java.util.List;

/**
 * @Description: 审核任务表
 * @Author: jeecg-boot
 * @Date:   2021-11-04
 * @Version: V1.0
 */
public interface ISmartVerifyTaskService extends IService<SmartVerifyTask> {

    public Page<VerifyTaskListPage> getTaskList(Page<VerifyTaskListPage> page, List<String> typeList,
                                                VerifyTaskListPage verifyTaskListPage);

    public Page<VerifyTaskListPage> getNotPassList(Page<VerifyTaskListPage> page, List<String> typeList,
                                                VerifyTaskListPage verifyTaskListPage);

    IPage<SzydVerifyTaskListPage> getSzydTaskList(Page<SzydVerifyTaskListPage> page, List<String> typeList, SzydVerifyTaskListPage szydVerifyTaskListPage);

    IPage<SzydVerifyTaskListPage> getSzydNotPassList(Page<SzydVerifyTaskListPage> page, List<String> typeList, SzydVerifyTaskListPage szydVerifyTaskListPage);
}
