package org.jeecg.modules.SmartPaper.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.SmartPaper.entity.SmartExamInformation;
import org.jeecg.modules.SmartPaper.mapper.SmartExamInformationMapper;
import org.jeecg.modules.SmartPaper.service.ISmartExamInfoService;
import org.jeecg.modules.SmartPaper.vo.DeptExamRankVo;
import org.jeecg.modules.SmartPaper.vo.ExamPeopleScoreVo;
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
public class SmartExamInfoServiceImpl extends ServiceImpl<SmartExamInformationMapper, SmartExamInformation> implements ISmartExamInfoService {
    @Autowired
    private SmartExamInformationMapper smartExamInformationMapper;
    @Override
    public IPage<SmartExamInformation> getAllExam(Page<SmartExamInformation> page, String examName) {
        return page.setRecords(smartExamInformationMapper.getAllExam(page,examName));
    }
    /**
     *考试-单位排名
     *
     * @param examId
     * @return
     */
    @Override
    public IPage<DeptExamRankVo> deptExamRank(Page<DeptExamRankVo> page,String examId) {
        return page.setRecords(smartExamInformationMapper.deptExamRank(page,examId));
    }
    /**
     *考试-个人排名
     *
     * @param examId
     * @return
     */
    @Override
    public IPage<ExamPeopleScoreVo> peopleExamRank(Page<ExamPeopleScoreVo> page, String examId) {
        return page.setRecords(smartExamInformationMapper.peopleExamRank(page,examId));
    }
}
