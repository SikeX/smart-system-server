package org.jeecg.modules.smartAnswerAssContent.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.smartAnswerAssContent.entity.SmartAnswerAssContent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 答题考核节点表
 * @Author: jeecg-boot
 * @Date:   2022-02-21
 * @Version: V1.0
 */
public interface SmartAnswerAssContentMapper extends BaseMapper<SmartAnswerAssContent> {
    /**
     * 获取某个任务中某个考核要点所有被考核单位的得分情况
     *
     * @param missionId
     * @param assContentId
     * @return
     */
    List<SmartAnswerAssContent> listAllByAssContentIdAndMissionId(@Param("missionId") String missionId, @Param("assContentId") String assContentId);

    /**
     * 更新某个考核任务中某个考核要点所有被考核单位的最终成绩
     *
     * @param missionId
     * @param assContentId
     * @param scoreType
     */
    void updateFinalScore(@Param("missionId") String missionId, @Param("assContentId") String assContentId, @Param("scoreType") String scoreType);
}
