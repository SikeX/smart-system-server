package org.jeecg.modules.SmartPaper.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.SmartPaper.entity.SmartExamInformation;

/**
 * @Description: 考试信息表
 * @Author: jeecg-boot
 * @Date:   2021-11-23
 * @Version: V1.0
 */
public interface ISmartExamInfoService extends IService<SmartExamInformation> {

    IPage<SmartExamInformation> getAllExam(Page<SmartExamInformation> page, String examName);
}
