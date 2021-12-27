package org.jeecg.modules.tasks.smartVerifyTask.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.tasks.smartVerifyTask.entity.SmartVerifyTask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.tasks.smartVerifyTask.vo.VerifyTaskListPage;

import java.util.List;

/**
 * @Description: 审核任务表
 * @Author: jeecg-boot
 * @Date:   2021-11-04
 * @Version: V1.0
 */

public interface SmartVerifyTaskMapper extends MPJBaseMapper<SmartVerifyTask> {
//    @Insert("INSERT INTO smart_verify_task(record_id, fill_depart) SELECT id, people_no from smart_premarital_filing")
    public List<VerifyTaskListPage> getTaskList(Page<VerifyTaskListPage> page, @Param("typeList") List<String> typeList,
                                         @Param("verifyTaskListPage") VerifyTaskListPage verifyTaskListPage);

    public List<VerifyTaskListPage> getNotPassList(Page<VerifyTaskListPage> page,
                                                 @Param("typeList") List<String> typeList,
                                                @Param("verifyTaskListPage") VerifyTaskListPage verifyTaskListPage);
}
