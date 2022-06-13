package org.jeecg.modules.smartPublicityEducation.service;

import org.jeecg.modules.smartPublicityEducation.entity.SmartPublicityEducationPeople;
import org.jeecg.modules.smartPublicityEducation.entity.SmartPublicityEducation;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 宣传教育
 * @Author: jeecg-boot
 * @Date:   2021-12-29
 * @Version: V1.0
 */
public interface ISmartPublicityEducationService extends IService<SmartPublicityEducation> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(SmartPublicityEducation smartPublicityEducation,List<SmartPublicityEducationPeople> smartPublicityEducationPeopleList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(SmartPublicityEducation smartPublicityEducation,List<SmartPublicityEducationPeople> smartPublicityEducationPeopleList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
