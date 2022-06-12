package org.jeecg.modules.smartFuneralReport.service.impl;

import org.jeecg.modules.smartFuneralReport.entity.SmartFuneralReport;
import org.jeecg.modules.smartFuneralReport.mapper.SmartFuneralReportMapper;
import org.jeecg.modules.smartFuneralReport.service.ISmartFuneralReportService;
import org.jeecg.modules.smartReportingInformation.entity.SmartJob;
import org.jeecg.modules.smartReportingInformation.entity.SmartReportingInformation;
import org.jeecg.modules.smartReportingInformation.entity.SysRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 丧事口头报备表
 * @Author: jeecg-boot
 * @Date:   2022-02-26
 * @Version: V1.0
 */


@Service
public class SmartFuneralReportServiceImpl extends ServiceImpl<SmartFuneralReportMapper, SmartFuneralReport> implements ISmartFuneralReportService {

    @Autowired
    private SmartFuneralReportMapper smartFuneralReportMapper;

    @Override
    public SmartJob getStatus() {
        return smartFuneralReportMapper.getStatus();
    }

    @Override
    public List<SmartFuneralReport> sendInformation() {

        return smartFuneralReportMapper.sendInformation();
    }

    @Override
    public String getRealnameById(String userId) {

        return smartFuneralReportMapper.getRealnameById(userId);
    }

    @Override
    public List<SysRole> getUser() {
        return smartFuneralReportMapper.getUser();
    }
}
