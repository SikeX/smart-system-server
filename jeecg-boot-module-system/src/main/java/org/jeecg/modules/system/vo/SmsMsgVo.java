package org.jeecg.modules.system.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description:
 * @Author: sike
 * @Date: 2021/12/23 14:03
 * @Version: V1.0
 */
@Data
public class SmsMsgVo implements Serializable {
    private String content;

    private String msgType;

    private String userIds;

    private String departIds;

    private String peopleType;
}
