package org.jeecg.modules.smartPublicityEducation.service;

import org.jeecg.modules.smartPublicityEducation.entity.SmartPublicityEducationPeople;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 宣传教育参会人员
 * @Author: jeecg-boot
 * @Date:   2021-12-29
 * @Version: V1.0
 */
public interface ISmartPublicityEducationPeopleService extends IService<SmartPublicityEducationPeople> {

	public List<SmartPublicityEducationPeople> selectByMainId(String mainId);
}
