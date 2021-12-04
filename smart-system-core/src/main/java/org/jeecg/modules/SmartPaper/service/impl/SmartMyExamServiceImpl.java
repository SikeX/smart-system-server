package org.jeecg.modules.SmartPaper.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.SmartPaper.mapper.SmartMyExamMapper;
import org.jeecg.modules.SmartPaper.service.ISmartMyExamService;
import org.jeecg.modules.SmartPaper.vo.SmartMyExamVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 考试信息表
 * @Author: jeecg-boot
 * @Date:   2021-11-23
 * @Version: V1.0
 */
@Service
public class SmartMyExamServiceImpl extends ServiceImpl<SmartMyExamMapper, SmartMyExamVo> implements ISmartMyExamService {

    @Autowired
    private SmartMyExamMapper smartMyExamMapper;
    @Override
    public Page<SmartMyExamVo> getMyAllExam(Page<SmartMyExamVo> page,String userId,String examName) {
        return page.setRecords(smartMyExamMapper.getMyAllExam(page,userId,examName));
    }
}
