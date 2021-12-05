package org.jeecg.modules.SmartPaper.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.SmartPaper.entity.SmartPeople;

import javax.xml.crypto.Data;
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
}
