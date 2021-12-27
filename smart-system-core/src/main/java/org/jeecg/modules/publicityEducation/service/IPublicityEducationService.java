package org.jeecg.modules.publicityEducation.service;

import org.jeecg.modules.publicityEducation.entity.PublicityEducationPacpa;
import org.jeecg.modules.publicityEducation.entity.PublicityEducationAnnex;
import org.jeecg.modules.publicityEducation.entity.PublicityEducation;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 宣传教育主表
 * @Author: jeecg-boot
 * @Date:   2021-12-26
 * @Version: V1.0
 */
public interface IPublicityEducationService extends IService<PublicityEducation> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(PublicityEducation publicityEducation,List<PublicityEducationPacpa> publicityEducationPacpaList,List<PublicityEducationAnnex> publicityEducationAnnexList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(PublicityEducation publicityEducation,List<PublicityEducationPacpa> publicityEducationPacpaList,List<PublicityEducationAnnex> publicityEducationAnnexList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
