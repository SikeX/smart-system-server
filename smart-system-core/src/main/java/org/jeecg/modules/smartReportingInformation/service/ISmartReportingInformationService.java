package org.jeecg.modules.smartReportingInformation.service;

import org.jeecg.modules.smartReportingInformation.entity.SmartReportingSurvey;
import org.jeecg.modules.smartReportingInformation.entity.SmartReportingDescription;
import org.jeecg.modules.smartReportingInformation.entity.SmartReportingInformation;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 举报信息表
 * @Author: jeecg-boot
 * @Date:   2021-11-27
 * @Version: V1.0
 */
public interface ISmartReportingInformationService extends IService<SmartReportingInformation> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(SmartReportingInformation smartReportingInformation,List<SmartReportingSurvey> smartReportingSurveyList,List<SmartReportingDescription> smartReportingDescriptionList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(SmartReportingInformation smartReportingInformation,List<SmartReportingSurvey> smartReportingSurveyList,List<SmartReportingDescription> smartReportingDescriptionList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
