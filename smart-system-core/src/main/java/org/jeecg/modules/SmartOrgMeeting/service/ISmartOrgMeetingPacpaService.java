package org.jeecg.modules.SmartOrgMeeting.service;

import org.jeecg.modules.SmartOrgMeeting.entity.SmartOrgMeetingPacpa;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 组织生活会参会人员表
 * @Author: jeecg-boot
 * @Date:   2021-11-03
 * @Version: V1.0
 */
public interface ISmartOrgMeetingPacpaService extends IService<SmartOrgMeetingPacpa> {

	public List<SmartOrgMeetingPacpa> selectByMainId(String mainId);
}
