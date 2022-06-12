package org.jeecg.modules.smartSentMsg.service;

import org.jeecg.modules.smartJob.entity.SysUser;
import org.jeecg.modules.smartSentMsg.entity.SmartSentMsg;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 已发送的短信
 * @Author: jeecg-boot
 * @Date:   2021-12-03
 * @Version: V1.0
 */
public interface ISmartSentMsgService extends IService<SmartSentMsg> {

    String getOrgId(String sendFrom);
}
