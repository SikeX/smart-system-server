package org.jeecg.modules.SmartPaper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
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
public interface SmartExamInformationMapper extends BaseMapper<SmartExamInformation> {

    List<SmartExamInformation> getAllExam(Page<SmartExamInformation> page,@Param("examName") String examName);
    /**
     *考试-单位排名
     *
     * @param examId
     * @return
     */
    List<DeptExamRankVo> deptExamRank(Page<DeptExamRankVo> page,@Param("examId")String examId);
    /**
     *考试-个人排名
     *
     * @param examId
     * @return
     */
    List<ExamPeopleScoreVo> peopleExamRank(Page<ExamPeopleScoreVo> page, @Param("examId")String examId);
}
