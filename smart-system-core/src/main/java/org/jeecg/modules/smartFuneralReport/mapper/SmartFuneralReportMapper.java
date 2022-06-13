package org.jeecg.modules.smartFuneralReport.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.smartFuneralReport.entity.SmartFuneralReport;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.smartReportingInformation.entity.SmartJob;
import org.jeecg.modules.smartReportingInformation.entity.SmartReportingInformation;
import org.jeecg.modules.smartReportingInformation.entity.SysRole;

/**
 * @Description: 丧事口头报备表
 * @Author: jeecg-boot
 * @Date:   2022-02-26
 * @Version: V1.0
 */
public interface SmartFuneralReportMapper extends BaseMapper<SmartFuneralReport> {
    SmartJob getStatus();
    List<SmartFuneralReport> sendInformation();
    String getRealnameById(String userId);
    List<SysRole> getUser();
}
