package org.jeecg.modules.smartDemocraticLifeMeeting.service;

import org.jeecg.modules.smartDemocraticLifeMeeting.entity.SmartDemocraticLifeEnclosure;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 民主生活会附件表
 * @Author: jeecg-boot
 * @Date:   2021-11-03
 * @Version: V1.0
 */
public interface ISmartDemocraticLifeEnclosureService extends IService<SmartDemocraticLifeEnclosure> {

	public List<SmartDemocraticLifeEnclosure> selectByMainId(String mainId);
}
