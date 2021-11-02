package org.jeecg.modules.smartInnerParty.service;

import org.jeecg.modules.smartInnerParty.entity.SmartInnerPartyAnnex;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 党内谈话附件表
 * @Author: jeecg-boot
 * @Date:   2021-11-02
 * @Version: V1.0
 */
public interface ISmartInnerPartyAnnexService extends IService<SmartInnerPartyAnnex> {

	public List<SmartInnerPartyAnnex> selectByMainId(String mainId);
}
