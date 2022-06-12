package org.jeecg.modules.SmartPaper.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.SmartPaper.vo.SmartMyExamVo;
import org.jeecg.modules.SmartPaper.vo.SmartMySurveyVo;

public interface ISmartMySurveyService extends IService<SmartMySurveyVo> {
    Page<SmartMySurveyVo> getMyAllSurvey(Page<SmartMySurveyVo> page, String userId, String examName);
}
