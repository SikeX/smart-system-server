package org.jeecg.modules.SmartPaper.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.SmartPaper.entity.SmartExamPeople;
import org.jeecg.modules.SmartPaper.entity.SmartSubmit;
import org.jeecg.modules.SmartPaper.vo.SmartSubmitExamVo;

/**
 * @Description: 考试表
 * @Author: jeecg-boot
 * @Date:   2021-11-25
 * @Version: V1.0
 */
public interface ISmartExamService extends IService<SmartExamPeople> {

    Result submitTestPaper(SmartSubmitExamVo smartSubmitExamVO);
}
