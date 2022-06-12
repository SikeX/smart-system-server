package org.jeecg.modules.SmartPaper.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.SmartPaper.entity.SmartExamInformation;
import org.jeecg.modules.SmartPaper.vo.SmartExamVo;

/**
 * @Description: 发布
 * @Author: zxh
 * @Date:   2021-11-25
 * @Version: V1.0
 */
public interface ISmartReleaseService extends IService<SmartExamInformation> {


    Result releaseExam(String paperId, SmartExamVo smartExamVO);
    Result releaseSurvey(String paperId, SmartExamVo smartExamVO);
}
