package org.jeecg.modules.tasks.smartVerifyTask.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import org.jeecg.modules.tasks.smartVerifyTask.entity.SmartVerifyTask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 审核任务表
 * @Author: jeecg-boot
 * @Date:   2021-11-04
 * @Version: V1.0
 */

public interface SmartVerifyTaskMapper extends MPJBaseMapper<SmartVerifyTask> {
//    @Insert("INSERT INTO smart_verify_task(record_id, fill_depart) SELECT id, people_no from smart_premarital_filing");
}
