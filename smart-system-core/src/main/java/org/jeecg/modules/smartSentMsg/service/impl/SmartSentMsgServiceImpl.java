package org.jeecg.modules.smartSentMsg.service.impl;

import org.jeecg.modules.smartSentMsg.entity.SmartSentMsg;
import org.jeecg.modules.smartSentMsg.mapper.SmartSentMsgMapper;
import org.jeecg.modules.smartSentMsg.service.ISmartSentMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;

/**
 * @Description: 已发送的短信
 * @Author: jeecg-boot
 * @Date:   2021-12-03
 * @Version: V1.0
 */
@Service
public class SmartSentMsgServiceImpl extends ServiceImpl<SmartSentMsgMapper, SmartSentMsg> implements ISmartSentMsgService {

    @Resource
    SmartSentMsgMapper smartSentMsgMapper;

    @Override
    public String getOrgId(String sendFrom) {
        return smartSentMsgMapper.getOrgId(sendFrom);
    }
}
