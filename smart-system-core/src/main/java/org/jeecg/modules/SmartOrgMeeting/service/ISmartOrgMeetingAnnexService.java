package org.jeecg.modules.SmartOrgMeeting.service;

import org.jeecg.modules.SmartOrgMeeting.entity.SmartOrgMeetingAnnex;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 组织生活会附件表
 * @Author: jeecg-boot
 * @Date:   2021-11-03
 * @Version: V1.0
 */
public interface ISmartOrgMeetingAnnexService extends IService<SmartOrgMeetingAnnex> {

	public List<SmartOrgMeetingAnnex> selectByMainId(String mainId);
}
