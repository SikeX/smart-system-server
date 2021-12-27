package org.jeecg.modules.wePower.smartPublicityProject.service;

import org.jeecg.modules.wePower.smartPublicityProject.entity.SmartPublicityProjectVerify;
import org.jeecg.modules.wePower.smartPublicityProject.entity.SmartPublicityProject;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 项目管理
 * @Author: jeecg-boot
 * @Date:   2021-12-26
 * @Version: V1.0
 */
public interface ISmartPublicityProjectService extends IService<SmartPublicityProject> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(SmartPublicityProject smartPublicityProject,List<SmartPublicityProjectVerify> smartPublicityProjectVerifyList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(SmartPublicityProject smartPublicityProject,List<SmartPublicityProjectVerify> smartPublicityProjectVerifyList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
