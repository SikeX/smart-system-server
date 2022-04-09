package org.jeecg.modules.SmartFaithless.entity;

import lombok.Data;
/**
 * @author zxh
 * @version 1.0
 * @description: 失信被执行人
 * @date 2022/2/28 15:53
 */
@Data
public class Faithless {
    private String realname;
    private String idcard;
    private String mobile;
}
