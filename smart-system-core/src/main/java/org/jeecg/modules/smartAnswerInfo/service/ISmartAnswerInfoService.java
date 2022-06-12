package org.jeecg.modules.smartAnswerInfo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.smartAnswerInfo.entity.SmartAnswerInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.smartAnswerInfo.entity.SmartDepartContentScore;

import java.util.List;

/**
 * @Description: 答题信息表
 * @Author: jeecg-boot
 * @Date:   2022-02-21
 * @Version: V1.0
 */
public interface ISmartAnswerInfoService extends IService<SmartAnswerInfo> {
    List<SmartDepartContentScore> selectByMissionIdAndContentId(Page<SmartDepartContentScore> page, @Param("missionId") String missionId, @Param("assContentId") String assContentId);

    /**
     * 更新答题信息表中的任务状态
     *
     * @param missionId
     * @param missionStatus
     */
    void updateMissionStatus(String missionId, String missionStatus);
}
