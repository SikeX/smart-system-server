package org.jeecg.modules.SmartPaper.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.SmartPaper.entity.SmartPeople;
import org.jeecg.modules.SmartPaper.vo.SmartSubmitExamVo;


public interface ISmartExamService extends IService<SmartPeople> {

    Result submitTestPaper(SmartSubmitExamVo smartSubmitExamVO);
}
