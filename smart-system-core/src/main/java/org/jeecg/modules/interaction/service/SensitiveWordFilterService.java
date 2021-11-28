package org.jeecg.modules.interaction.service;

import org.jeecg.modules.interaction.entity.SmartSensitiveWord;
import org.jeecg.modules.interaction.utils.SensitiveWordUtil;
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
@DependsOn("mybatisPlusSaasConfig")
public class SensitiveWordFilterService {

    @Resource
    private ISmartSensitiveWordService sensitiveWordService;

    // 初始化过滤器
    @PostConstruct
    public void init() {
        List<SmartSensitiveWord> sensitiveWords = sensitiveWordService.list();
        Set<String> sensitiveWordSet = new HashSet<>();
        for (SmartSensitiveWord sensitiveWord : sensitiveWords) {
            sensitiveWordSet.add(sensitiveWord.getWord());
        }
        SensitiveWordUtil.init(sensitiveWordSet);
    }
}
