package org.jeecg.modules.SmartInnerPartyTalk.service;

import org.jeecg.modules.SmartInnerPartyTalk.entity.SmartInnerPartyPacpa;
import org.jeecg.modules.SmartInnerPartyTalk.entity.SmartInnerPartyAnnex;
import org.jeecg.modules.SmartInnerPartyTalk.entity.SmartInnerPartyTalk;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 党内谈话表
 * @Author: jeecg-boot
 * @Date:   2021-11-05
 * @Version: V1.0
 */
public interface ISmartInnerPartyTalkService extends IService<SmartInnerPartyTalk> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(SmartInnerPartyTalk smartInnerPartyTalk,List<SmartInnerPartyPacpa> smartInnerPartyPacpaList,List<SmartInnerPartyAnnex> smartInnerPartyAnnexList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(SmartInnerPartyTalk smartInnerPartyTalk,List<SmartInnerPartyPacpa> smartInnerPartyPacpaList,List<SmartInnerPartyAnnex> smartInnerPartyAnnexList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
