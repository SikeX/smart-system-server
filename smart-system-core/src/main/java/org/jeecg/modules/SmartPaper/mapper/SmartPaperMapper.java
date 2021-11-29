package org.jeecg.modules.SmartPaper.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.SmartPaper.entity.SmartPaper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.SmartPaper.entity.SmartTopic;
import org.jeecg.modules.SmartPaper.vo.SmartPaperPage;
import org.jeecg.modules.SmartPaper.vo.SmartTopicVo;

/**
 * @Description: 试卷表
 * @Author: jeecg-boot
 * @Date:   2021-11-21
 * @Version: V1.0
 */
@Mapper
public interface SmartPaperMapper extends BaseMapper<SmartPaper> {
    /**
     *
     * 根据试卷ID获取试卷信息
     *
     */
    SmartPaper getPaperById(String id);
    /**
     *
     * 根据试卷ID获取题目
     *
     */
    List<SmartTopicVo> getTopicListByPaperId(String id);
}
