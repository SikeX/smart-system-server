package org.jeecg.modules.smartAssessmentContent.service;

import org.jeecg.modules.smartAssessmentContent.entity.SmartAssessmentContent;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.exception.JeecgBootException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.List;

/**
 * @Description: 考核节点表
 * @Author: jeecg-boot
 * @Date:   2022-02-12
 * @Version: V1.0
 */
public interface ISmartAssessmentContentService extends IService<SmartAssessmentContent> {

	/**根节点父ID的值*/
	public static final String ROOT_PID_VALUE = "0";
	
	/**树节点有子节点状态值*/
	public static final String HASCHILD = "1";
	
	/**树节点无子节点状态值*/
	public static final String NOCHILD = "0";

	/**新增节点*/
	void addSmartAssessmentContent(SmartAssessmentContent smartAssessmentContent);
	
	/**修改节点*/
	void updateSmartAssessmentContent(SmartAssessmentContent smartAssessmentContent) throws JeecgBootException;
	
	/**删除节点*/
	void deleteSmartAssessmentContent(String id) throws JeecgBootException;

	/**查询所有数据，无分页*/
    List<SmartAssessmentContent> queryTreeListNoPage(QueryWrapper<SmartAssessmentContent> queryWrapper);

}
