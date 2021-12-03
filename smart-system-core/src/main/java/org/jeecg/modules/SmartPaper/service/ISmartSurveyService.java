package org.jeecg.modules.SmartPaper.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.SmartPaper.entity.SmartPeople;
import org.jeecg.modules.SmartPaper.vo.SmartSubmitSurveyVo;


public interface ISmartSurveyService extends IService<SmartPeople> {
    Result submitTestSurvey(SmartSubmitSurveyVo smartSubmitSurveyVO);
}
