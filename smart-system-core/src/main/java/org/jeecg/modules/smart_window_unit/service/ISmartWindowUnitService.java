package org.jeecg.modules.smart_window_unit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.smartReportingInformation.entity.SysRole;
import org.jeecg.modules.smart_window_people.entity.SmartWindowPeople;
import org.jeecg.modules.smart_window_unit.entity.SmartWindowUnit;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 窗口单位
 * @Author: jeecg-boot
 * @Date:   2021-12-02
 * @Version: V1.0
 */
public interface ISmartWindowUnitService extends IService<SmartWindowUnit> {

    String getDepartNameById(String id);

    String getUserNameById(String id);
    List<SysRole> getUser();

}
