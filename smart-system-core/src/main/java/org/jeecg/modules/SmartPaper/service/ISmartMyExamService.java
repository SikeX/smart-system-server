package org.jeecg.modules.SmartPaper.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.SmartPaper.entity.SmartExamInformation;
import org.jeecg.modules.SmartPaper.vo.SmartMyExamVo;

import java.util.List;

/**
 * @Description: 考试信息表
 * @Author: jeecg-boot
 * @Date:   2021-11-23
 * @Version: V1.0
 */
public interface ISmartMyExamService extends IService<SmartMyExamVo> {
    Page<SmartMyExamVo> getMyAllExam(Page<SmartMyExamVo> page,String userId,String examName);
}
