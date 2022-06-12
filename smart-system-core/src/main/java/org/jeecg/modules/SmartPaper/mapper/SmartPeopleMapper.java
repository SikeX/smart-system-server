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
    String getNameById(@Param("userId")String userId);
    //获取用户考试ID
    List<String> getExamIdByuser(@Param("userId")String userId);
    //获取成绩
    Integer getGrade(@Param("userId")String userId, @Param("examId")String examId);
    //更新成绩
    void updateGrade(@Param("userId")String userId, @Param("examId")String examId, @Param("grade")int grade, @Param("date") Date date);

    List<ExamPeopleScoreVo> getScoreByExamId(Page<ExamPeopleScoreVo> page, @Param("examId") String examId);

    List<String> getAllVillageList();

    List<String> getAllHomeListByVillageId(@Param("villageId")String villageId,@Param("selectedCount")Integer selectedCount);

    RandomPeople getSelectedPeoByHomeCode(@Param("homeCode") String homeCode);

    List<RandomPeople> getTriPeoListByDptId(Page<RandomPeople> page, @Param("paperId")String paperId, @Param("departId")String departId);

    String selectByUIdEId(@Param("examId")String examId, @Param("userId")String userId);

    int updateSumSur(@Param("examId")String examId, @Param("userId")String userId,
                     @Param("grade")int grade, @Param("isFinish")String isFinish, @Param("inquirerId")String inquirerId,
                     @Param("satisfaction")String satisfaction,@Param("isReport") String isReport);

    List<RandomPeople> getTriResultByEIdDId(Page<RandomPeople> page, @Param("examId")String examId, @Param("departId")String departId);

    List<RandomPeople> getTriGovPeoList(Integer selectedCount);
}
