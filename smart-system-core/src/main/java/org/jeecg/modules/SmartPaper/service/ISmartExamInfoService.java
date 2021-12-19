package org.jeecg.modules.SmartPaper.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.SmartPaper.entity.SmartExamInformation;
import org.jeecg.modules.SmartPaper.vo.DeptExamRankVo;
import org.jeecg.modules.SmartPaper.vo.ExamPeopleScoreVo;

import java.util.List;

/**
 * @Description: 考试信息表
 * @Author: jeecg-boot
 * @Date:   2021-11-23
 * @Version: V1.0
 */
public interface ISmartExamInfoService extends IService<SmartExamInformation> {

    IPage<SmartExamInformation> getAllExam(Page<SmartExamInformation> page, String examName);
    /**
     *考试-单位排名
     *
     * @param examId
     * @return
     */
    IPage<DeptExamRankVo> deptExamRank(Page<DeptExamRankVo> page,String examId);
    /**
     *考试-个人排名
     *
     * @param examId
     * @return
     */
    IPage<ExamPeopleScoreVo> peopleExamRank(Page<ExamPeopleScoreVo> page, String examId);
}
