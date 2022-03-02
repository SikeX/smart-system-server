package org.jeecg.modules.smartAssessmentMission.service;

import org.jeecg.modules.smartAssessmentMission.entity.SmartAssessmentDepart;
import org.jeecg.modules.smartAssessmentMission.entity.SmartAssessmentMission;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 考核任务表
 * @Author: jeecg-boot
 * @Date:   2022-02-12
 * @Version: V1.0
 */
public interface ISmartAssessmentMissionService extends IService<SmartAssessmentMission> {

	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);


}
