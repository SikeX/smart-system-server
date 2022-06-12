package org.jeecg.modules.smartDemocraticLifeMeeting.service;

import org.jeecg.modules.smartDemocraticLifeMeeting.entity.SmartDemocraticLifePeople;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 民主生活参会人员表
 * @Author: jeecg-boot
 * @Date:   2022-02-26
 * @Version: V1.0
 */
public interface ISmartDemocraticLifePeopleService extends IService<SmartDemocraticLifePeople> {

	public List<SmartDemocraticLifePeople> selectByMainId(String mainId);
}
