package org.jeecg.modules.smartThreeMeetingOneLesson.service;

import org.jeecg.modules.smartThreeMeetingOneLesson.entity.SmartThreeMeetingOneLessonParticipants;
import org.jeecg.modules.smartThreeMeetingOneLesson.entity.SmartThreeMeetingOneLessonAnnex;
import org.jeecg.modules.smartThreeMeetingOneLesson.entity.SmartThreeMeetingOneLesson;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 三会一课
 * @Author: jeecg-boot
 * @Date:   2021-11-14
 * @Version: V1.0
 */
public interface ISmartThreeMeetingOneLessonService extends IService<SmartThreeMeetingOneLesson> {

	/**
	 * 添加一对多
	 *
	 */
	public void saveMain(SmartThreeMeetingOneLesson smartThreeMeetingOneLesson,List<SmartThreeMeetingOneLessonParticipants> smartThreeMeetingOneLessonParticipantsList,List<SmartThreeMeetingOneLessonAnnex> smartThreeMeetingOneLessonAnnexList) ;

	/**
	 * 修改一对多
	 *
	 */
	public void updateMain(SmartThreeMeetingOneLesson smartThreeMeetingOneLesson,List<SmartThreeMeetingOneLessonParticipants> smartThreeMeetingOneLessonParticipantsList,List<SmartThreeMeetingOneLessonAnnex> smartThreeMeetingOneLessonAnnexList);

	/**
	 * 删除一对多
	 */
	public void delMain (String id);

	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);

	/**
	 * 根据部门编码查询部门ID
	 *
	 * @param orgCode 部门编码
	 * @return 部门ID
	 */
	String getDepartIdByOrgCode(String orgCode);
	/**
	 * 根据部门编码获所有子部门的ID
	 *
	 * @param orgCode 部门编码
	 * @return 子部门ID列表
	 */
	List<String> getChildrenIdByOrgCode(String orgCode);
}