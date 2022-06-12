package org.jeecg.modules.SmartFirstFormPeople.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.SmartFirstFormPeople.entity.FirstFormInfo;
import org.jeecg.modules.SmartFirstFormPeople.entity.SmartFirstFormPeople;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.smartEvaluateList.entity.MonthCount;

/**
 * @Description: 执行第一种形态人员表
 * @Author: jeecg-boot
 * @Date:   2021-11-08
 * @Version: V1.0
 */
public interface SmartFirstFormPeopleMapper extends BaseMapper<SmartFirstFormPeople> {
    List<FirstFormInfo> sendInformation();

    List<String> getLeadersByDepartId(@Param("departIds") List<String> departIds);

    List<MonthCount> statistics(@Param("year") String year, @Param("departCode") String departCode);
}
