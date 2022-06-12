package org.jeecg.modules.SmartPaper.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.SmartPaper.mapper.SmartMySurveyMapper;
import org.jeecg.modules.SmartPaper.service.ISmartMySurveyService;
import org.jeecg.modules.SmartPaper.vo.SmartMySurveyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: 考试信息表
 * @Author: jeecg-boot
 * @Date:   2021-11-23
 * @Version: V1.0
 */
@Service
public class SmartMySurveyServiceImpl extends ServiceImpl<SmartMySurveyMapper, SmartMySurveyVo> implements ISmartMySurveyService {

    @Autowired
    private SmartMySurveyMapper smartMySurveyMapper;

    @Override
    public Page<SmartMySurveyVo> getMyAllSurvey(Page<SmartMySurveyVo> page, String userId, String examName) {
        return page.setRecords(smartMySurveyMapper.getMyAllSurvey(page,userId,examName));
    }
}
