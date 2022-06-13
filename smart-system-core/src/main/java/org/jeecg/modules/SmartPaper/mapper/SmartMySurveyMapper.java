package org.jeecg.modules.SmartPaper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.SmartPaper.entity.SmartExamInformation;

import org.jeecg.modules.SmartPaper.vo.SmartMySurveyVo;

import java.util.List;

/**
 * @Description: 考试信息表
 * @Author: jeecg-boot
 * @Date:   2021-11-23
 * @Version: V1.0
 */
public interface SmartMySurveyMapper extends BaseMapper<SmartMySurveyVo> {
    List<SmartMySurveyVo> getMyAllSurvey(Page<SmartMySurveyVo> page , @Param("userId") String userId,
                                         @Param("examName")String examName);
}
