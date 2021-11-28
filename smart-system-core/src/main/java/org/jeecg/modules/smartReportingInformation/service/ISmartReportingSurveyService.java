package org.jeecg.modules.smartReportingInformation.service;

import org.jeecg.modules.smartReportingInformation.entity.SmartReportingSurvey;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 举报调查表
 * @Author: jeecg-boot
 * @Date:   2021-11-27
 * @Version: V1.0
 */
public interface ISmartReportingSurveyService extends IService<SmartReportingSurvey> {

	public List<SmartReportingSurvey> selectByMainId(String mainId);
}
