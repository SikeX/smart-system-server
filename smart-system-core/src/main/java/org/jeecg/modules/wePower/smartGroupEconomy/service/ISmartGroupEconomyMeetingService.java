package org.jeecg.modules.wePower.smartGroupEconomy.service;

import org.jeecg.modules.wePower.smartGroupEconomy.entity.SmartGroupEconomyMeeting;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 集体经济组织会议
 * @Author: jeecg-boot
 * @Date:   2022-03-06
 * @Version: V1.0
 */
public interface ISmartGroupEconomyMeetingService extends IService<SmartGroupEconomyMeeting> {

	public List<SmartGroupEconomyMeeting> selectByMainId(String mainId);
}
