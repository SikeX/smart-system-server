package org.jeecg.modules.smartReportingInformation.service;

import org.jeecg.modules.smartReportingInformation.entity.SmartReportingDescription;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 举报附件表
 * @Author: jeecg-boot
 * @Date:   2021-11-29
 * @Version: V1.0
 */
public interface ISmartReportingDescriptionService extends IService<SmartReportingDescription> {

	public List<SmartReportingDescription> selectByMainId(String mainId);
}
