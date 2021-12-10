package org.jeecg.modules.SmartPaper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.SmartPaper.entity.SmartExamInformation;

import java.util.List;

/**
 * @Description: 考试信息表
 * @Author: jeecg-boot
 * @Date:   2021-11-23
 * @Version: V1.0
 */
public interface SmartExamInformationMapper extends BaseMapper<SmartExamInformation> {

    List<SmartExamInformation> getAllExam(Page<SmartExamInformation> page, String examName);
}
