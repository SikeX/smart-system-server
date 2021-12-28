package org.jeecg.modules.SmartPaper.mapper;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.SmartPaper.entity.SmartPaper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.SmartPaper.entity.SmartTopic;
import org.jeecg.modules.SmartPaper.vo.RandomPeople;
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
    SmartPaper getPaperById(@Param("id") String id);
    /**
     *
     * 根据试卷ID获取题目
     *
     */
    List<SmartTopicVo> getTopicListByPaperId(@Param("id")String id);
    /**
     *
     * 根据试卷ID更新试卷状态
     *
     */
    void updateStatus(@Param("paperId")String paperId);

    List<RandomPeople> getTriPeoList(Page<RandomPeople> page,@Param("paperId")String paperId);
}
