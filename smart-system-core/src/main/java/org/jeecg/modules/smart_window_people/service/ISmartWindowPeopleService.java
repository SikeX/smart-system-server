package org.jeecg.modules.smart_window_people.service;

import org.jeecg.modules.smart_window_people.entity.SmartWindowPeople;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 窗口人员管理
 * @Author: jeecg-boot
 * @Date:   2021-12-02
 * @Version: V1.0
 */
public interface ISmartWindowPeopleService extends IService<SmartWindowPeople> {

    String getPidByDepartmentId(String id);
    String getDepartmentNameByDepartmentId(String id);
    String getDepartNameById(String id);

}
