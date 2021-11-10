package org.jeecg.modules.app.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.app.entity.Message;
import org.jeecg.modules.app.model.MessageModel;

import java.util.List;

/**
 * @Description: 用户通告阅读标记表
 * @Author: jeecg-boot
 * @Date: 2019-02-21
 * @Version: V1.0
 */
public interface IApiMessageService extends IService<Message> {

    public List<String> queryByUserId(String userId);

    /**
     * @param messageModel
     * @return
     * @功能：获取我的消息
     */
    public Page<MessageModel> getMyAnnouncementSendPage(Page<MessageModel> page, MessageModel messageModel);

}
