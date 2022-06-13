package org.jeecg.modules.smartAnswerInfo.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.smartAnswerInfo.entity.SmartAnswerInfo;
import org.jeecg.modules.smartAnswerInfo.entity.SmartDepartContentScore;
import org.jeecg.modules.smartAnswerInfo.mapper.SmartAnswerInfoMapper;
import org.jeecg.modules.smartAnswerInfo.service.ISmartAnswerInfoService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 答题信息表
 * @Author: jeecg-boot
 * @Date:   2022-02-21
 * @Version: V1.0
 */
@Service
public class SmartAnswerInfoServiceImpl extends ServiceImpl<SmartAnswerInfoMapper, SmartAnswerInfo> implements ISmartAnswerInfoService {

    @Override
    public List<SmartDepartContentScore> selectByMissionIdAndContentId(Page<SmartDepartContentScore> page, String missionId, String assContentId) {
        return baseMapper.selectByMissionIdAndContentId(page, missionId, assContentId);
    }

    @Override
    public void updateMissionStatus(String missionId, String missionStatus) {
        baseMapper.updateMissionStatus(missionId, missionStatus);
    }
}
