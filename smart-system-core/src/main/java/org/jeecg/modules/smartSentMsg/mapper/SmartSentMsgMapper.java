package org.jeecg.modules.smartSentMsg.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.smartSentMsg.entity.SmartSentMsg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 已发送的短信
 * @Author: jeecg-boot
 * @Date:   2021-12-03
 * @Version: V1.0
 */
public interface SmartSentMsgMapper extends BaseMapper<SmartSentMsg> {

    String getOrgId(String sendFrom);
}
