package org.jeecg.modules.smartReportingInformation.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.smartReportingInformation.entity.SmartJob;
import org.jeecg.modules.smartReportingInformation.entity.SmartReportingInformation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.smartReportingInformation.entity.SysRole;

/**
 * @Description: 举报信息表
 * @Author: jeecg-boot
 * @Date:   2021-11-29
 * @Version: V1.0
 */
public interface SmartReportingInformationMapper extends BaseMapper<SmartReportingInformation> {

    List<SmartReportingInformation> sendInformation();
    List<SysRole> getUser();
    SmartJob getStatus();
}
