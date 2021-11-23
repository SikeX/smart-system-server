package org.jeecg.modules.smartOrgMeeting.service;

import org.jeecg.modules.smartOrgMeeting.entity.SmartOrgMeetingAnnex;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 组织生活会附件表
 * @Author: jeecg-boot
 * @Date:   2021-11-14
 * @Version: V1.0
 */
public interface ISmartOrgMeetingAnnexService extends IService<SmartOrgMeetingAnnex> {

	public List<SmartOrgMeetingAnnex> selectByMainId(String mainId);
}
