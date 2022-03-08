package org.jeecg.modules.smartAssessmentMission.service;

import org.jeecg.modules.smartAssessmentMission.entity.SmartAssessmentDepart;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 考核任务被考核单位
 * @Author: jeecg-boot
 * @Date:   2022-02-12
 * @Version: V1.0
 */
public interface ISmartAssessmentDepartService extends IService<SmartAssessmentDepart> {

	public List<SmartAssessmentDepart> selectByMainId(String mainId);
}
