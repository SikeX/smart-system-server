package org.jeecg.modules.app.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.app.entity.Message;
import org.jeecg.modules.app.mapper.MessageMapper;
import org.jeecg.modules.app.model.MessageModel;
import org.jeecg.modules.app.service.IApiMessageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: 用户通告阅读标记表
 * @Author: jeecg-boot
 * @Date: 2019-02-21
 * @Version: V1.0
 */
@Service
public class IApiMessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IApiMessageService {

    @Resource
    private MessageMapper messageMapper;

    @Override
    public List<String> queryByUserId(String userId) {
        return messageMapper.queryByUserId(userId);
    }

    @Override
    public Page<MessageModel> getMyAnnouncementSendPage(Page<MessageModel> page,
                                                        MessageModel messageModel) {
        return page.setRecords(messageMapper.getMyAnnouncementSendList(page, messageModel));
    }

}
