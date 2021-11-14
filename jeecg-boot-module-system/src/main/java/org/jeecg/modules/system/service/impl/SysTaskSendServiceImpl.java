package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.system.entity.SysAnnouncementSend;
import org.jeecg.modules.system.mapper.SysAnnouncementSendMapper;
import org.jeecg.modules.system.mapper.SysTaskSendMapper;
import org.jeecg.modules.system.model.AnnouncementSendModel;
import org.jeecg.modules.system.service.ISysAnnouncementSendService;
import org.jeecg.modules.system.service.ISysTaskSendService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: 用户通告阅读标记表
 * @Author: jeecg-boot
 * @Date:  2019-02-21
 * @Version: V1.0
 */
@Service
public class SysTaskSendServiceImpl extends ServiceImpl<SysTaskSendMapper, SysAnnouncementSend> implements ISysTaskSendService {

	@Resource
	private SysTaskSendMapper sysTaskSendMapper;
	
	@Override
	public List<String> queryByUserId(String userId) {
		return sysTaskSendMapper.queryByUserId(userId);
	}

	@Override
	public Page<AnnouncementSendModel> getMyAnnouncementSendPage(Page<AnnouncementSendModel> page,
			AnnouncementSendModel announcementSendModel) {
		 return page.setRecords(sysTaskSendMapper.getMyAnnouncementSendList(page, announcementSendModel));
	}

}
