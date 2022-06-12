package org.jeecg.modules.SmartFirstFormPeople.service;

import org.jeecg.modules.SmartFirstFormPeople.entity.FirstFormInfo;
import org.jeecg.modules.SmartFirstFormPeople.entity.SmartFirstFormPeople;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.smartEvaluateList.entity.MonthCount;

import java.util.List;

/**
 * @Description: 执行第一种形态人员表
 * @Author: jeecg-boot
 * @Date:   2021-11-08
 * @Version: V1.0
 */
public interface ISmartFirstFormPeopleService extends IService<SmartFirstFormPeople> {

    List<MonthCount> statistics(String year,String departCode);

    List<FirstFormInfo> sendInformation();

    List<String> getLeadersByDepartId(List<String> departIds);
}
