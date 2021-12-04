package org.jeecg.modules.SmartPaper.service;

import org.jeecg.modules.SmartPaper.entity.SmartGradeNumber;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 成绩分布人数表
 * @Author: jeecg-boot
 * @Date:   2021-11-24
 * @Version: V1.0
 */
public interface ISmartGradeNumberService extends IService<SmartGradeNumber> {
    Integer excellentCount(double excellent_line);
    Integer goodCount(double good_line);
    Integer passCount();
    Integer failCount();
}
