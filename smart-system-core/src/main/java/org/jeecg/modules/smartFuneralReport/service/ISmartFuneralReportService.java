package org.jeecg.modules.smartFuneralReport.service;

import org.jeecg.modules.smartFuneralReport.entity.SmartFuneralReport;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.smartReportingInformation.entity.SmartJob;
import org.jeecg.modules.smartReportingInformation.entity.SmartReportingInformation;
import org.jeecg.modules.smartReportingInformation.entity.SysRole;

import java.util.List;

/**
 * @Description: 丧事口头报备表
 * @Author: jeecg-boot
 * @Date:   2022-02-26
 * @Version: V1.0
 */
public interface ISmartFuneralReportService extends IService<SmartFuneralReport> {
    SmartJob getStatus();
    List<SmartFuneralReport> sendInformation();
    List<SysRole> getUser();
    String getRealnameById(String userId);

}
