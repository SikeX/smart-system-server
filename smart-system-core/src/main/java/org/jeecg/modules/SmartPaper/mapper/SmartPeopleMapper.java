package org.jeecg.modules.SmartPaper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.SmartPaper.entity.SmartPeople;
import org.jeecg.modules.SmartPaper.vo.ExamPeopleScoreVo;
import org.jeecg.modules.SmartPaper.vo.RandomPeople;

import java.util.Date;
import java.util.List;

/**
 * @Description: 考试参加人员表
 * @Author: jeecg-boot
 * @Date:   2021-11-24
 * @Version: V1.0
 */
public interface SmartPeopleMapper extends BaseMapper<SmartPeople> {
    //通过Id获取姓名
    String getNameById(String userId);
    //获取用户考试ID
    List<String> getExamIdByuser(String userId);
    //获取成绩
    Integer getGrade(String userId, String examId);
    //更新成绩
    void updateGrade(String userId, String examId, int grade, Date date);

    List<ExamPeopleScoreVo> getScoreByExamId(Page<ExamPeopleScoreVo> page, @Param("examId") String examId);

    List<String> getAllVillageList();

    List<String> getAllHomeListByVillageId(String villageId,Integer selectedCount);

    RandomPeople getSelectedPeoByHomeCode(String homeCode);

    List<RandomPeople> getTriPeoListByDptId(Page<RandomPeople> page, String paperId, String departId);

    String selectByUIdEId(String examId, String userId);

    int updateSumSur(String examId, String userId, int grade, String isFinish, String inquirerId, String satisfaction, String isReport);

    List<RandomPeople> getTriResultByEIdDId(Page<RandomPeople> page, String examId, String departId);
}
