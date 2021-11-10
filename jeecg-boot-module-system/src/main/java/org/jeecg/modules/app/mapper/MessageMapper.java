package org.jeecg.modules.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.app.entity.Message;
import org.jeecg.modules.app.model.MessageModel;

import java.util.List;

/**
 * @Description: 用户通告阅读标记表
 * @Author: jeecg-boot
 * @Date: 2019-02-21
 * @Version: V1.0
 */
public interface MessageMapper extends BaseMapper<Message> {

    public List<String> queryByUserId(@Param("userId") String userId);

    /**
     * @param messageModel
     * @return
     * @功能：获取我的消息
     */
    public List<MessageModel> getMyAnnouncementSendList(Page<MessageModel> page, @Param("messageModel") MessageModel messageModel);

}
