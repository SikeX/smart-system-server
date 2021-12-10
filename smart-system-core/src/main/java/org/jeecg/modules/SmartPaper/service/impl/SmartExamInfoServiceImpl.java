package org.jeecg.modules.SmartPaper.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.SmartPaper.entity.SmartExamInformation;
import org.jeecg.modules.SmartPaper.mapper.SmartExamInformationMapper;
import org.jeecg.modules.SmartPaper.service.ISmartExamInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: 考试信息表
 * @Author: jeecg-boot
 * @Date:   2021-11-23
 * @Version: V1.0
 */
@Service
public class SmartExamInfoServiceImpl extends ServiceImpl<SmartExamInformationMapper, SmartExamInformation> implements ISmartExamInfoService {
    @Autowired
    private SmartExamInformationMapper smartExamInformationMapper;
    @Override
    public IPage<SmartExamInformation> getAllExam(Page<SmartExamInformation> page, String examName) {
        return page.setRecords(smartExamInformationMapper.getAllExam(page,examName));
    }
}
