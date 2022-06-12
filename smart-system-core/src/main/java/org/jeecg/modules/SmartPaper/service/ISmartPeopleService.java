package org.jeecg.modules.SmartPaper.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.SmartPaper.entity.SmartPeople;
import org.jeecg.modules.SmartPaper.vo.ExamPeopleScoreVo;
import org.jeecg.modules.SmartPaper.vo.RandomPeople;

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

    List<String> getAllVillageList();
    List<String> getAllHomeListByVillageId(String villageId,Integer selectedCount);

    RandomPeople getSelectedPeoByHomeCode(String homeCode);


    Page<RandomPeople> getTriPeoListByDptId(Page<RandomPeople> page, String paperId, String departId);

    Page<RandomPeople> getTriResultByEIdDId(Page<RandomPeople> page, String examId, String departId);

    List<RandomPeople> getTriGovPeoList(Integer selectedCount);
}
