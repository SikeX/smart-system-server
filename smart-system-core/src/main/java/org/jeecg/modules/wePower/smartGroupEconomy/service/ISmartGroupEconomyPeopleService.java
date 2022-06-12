package org.jeecg.modules.wePower.smartGroupEconomy.service;

import org.jeecg.modules.wePower.smartGroupEconomy.entity.SmartGroupEconomyPeople;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 集体经济组织人员
 * @Author: jeecg-boot
 * @Date:   2022-03-06
 * @Version: V1.0
 */
public interface ISmartGroupEconomyPeopleService extends IService<SmartGroupEconomyPeople> {

	public List<SmartGroupEconomyPeople> selectByMainId(String mainId);
}
