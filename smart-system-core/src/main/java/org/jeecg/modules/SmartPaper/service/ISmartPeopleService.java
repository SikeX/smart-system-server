package org.jeecg.modules.SmartPaper.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.SmartPaper.entity.SmartPeople;
import org.jeecg.modules.SmartPaper.vo.ExamPeopleScoreVo;

import java.util.List;

/**
 * @Description: 考试参加人员表
 * @Author: jeecg-boot
 * @Date:   2021-11-24
 * @Version: V1.0
 */
public interface ISmartPeopleService extends IService<SmartPeople> {

    List<String> getMyExam(String userId);
    String getMyExamString(String userId);

    Page<ExamPeopleScoreVo> getScoreByExamId(Page<ExamPeopleScoreVo> page, String examId);
}
