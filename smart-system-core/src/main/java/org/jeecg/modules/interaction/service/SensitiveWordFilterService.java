package org.jeecg.modules.interaction.service;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.interaction.entity.SmartSensitiveWord;
import org.jeecg.modules.interaction.utils.SensitiveWordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://yiiu.co
 */
@Component
@Slf4j
//@DependsOn("mybatisPlusSaasConfig")
public class SensitiveWordFilterService {

    @Autowired
    private ISmartSensitiveWordService smartSensitiveWordService;

    // 初始化过滤器
    public void init() {
        List<SmartSensitiveWord> sensitiveWords = smartSensitiveWordService.list();
        Set<String> sensitiveWordSet = new HashSet<>();
        for (SmartSensitiveWord sensitiveWord : sensitiveWords) {
            sensitiveWordSet.add(sensitiveWord.getWord());
        }
        log.info("什么"+String.valueOf(sensitiveWordSet));
        SensitiveWordUtil.init(sensitiveWordSet);
    }
}
