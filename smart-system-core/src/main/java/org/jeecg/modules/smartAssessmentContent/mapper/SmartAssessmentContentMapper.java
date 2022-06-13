package org.jeecg.modules.smartAssessmentContent.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.smartAssessmentContent.entity.SmartAssessmentContent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 考核节点表
 * @Author: jeecg-boot
 * @Date:   2022-02-12
 * @Version: V1.0
 */
public interface SmartAssessmentContentMapper extends BaseMapper<SmartAssessmentContent> {

	/**
	 * 编辑节点状态
	 * @param id
	 * @param status
	 */
	void updateTreeNodeStatus(@Param("id") String id,@Param("status") String status);

}
