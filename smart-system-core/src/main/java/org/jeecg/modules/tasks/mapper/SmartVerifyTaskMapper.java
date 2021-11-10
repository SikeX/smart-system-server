package org.jeecg.modules.tasks.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.tasks.entity.SmartVerifyTask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 审核任务表
 * @Author: jeecg-boot
 * @Date:   2021-11-04
 * @Version: V1.0
 */
public interface SmartVerifyTaskMapper extends BaseMapper<SmartVerifyTask> {
}
