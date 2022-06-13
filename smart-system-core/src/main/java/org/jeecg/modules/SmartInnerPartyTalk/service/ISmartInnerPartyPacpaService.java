package org.jeecg.modules.SmartInnerPartyTalk.service;

import org.jeecg.modules.SmartInnerPartyTalk.entity.SmartInnerPartyPacpa;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 党内谈话参与人表
 * @Author: jeecg-boot
 * @Date:   2021-11-10
 * @Version: V1.0
 */
public interface ISmartInnerPartyPacpaService extends IService<SmartInnerPartyPacpa> {

	public List<SmartInnerPartyPacpa> selectByMainId(String mainId);
}
