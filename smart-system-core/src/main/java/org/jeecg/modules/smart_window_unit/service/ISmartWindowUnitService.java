package org.jeecg.modules.smart_window_unit.service;

import org.jeecg.modules.smart_window_unit.entity.SmartWindowUnit;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 窗口单位
 * @Author: jeecg-boot
 * @Date:   2021-12-02
 * @Version: V1.0
 */
public interface ISmartWindowUnitService extends IService<SmartWindowUnit> {

    String getDepartNameById(String id);

    String getUserNameById(String id);
}
