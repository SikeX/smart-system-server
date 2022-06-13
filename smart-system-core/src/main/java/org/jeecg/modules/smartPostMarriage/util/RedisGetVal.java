package org.jeecg.modules.smartPostMarriage.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 从redis获取dict
 */
@Slf4j
@Component
public class RedisGetVal {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public String getValue(String dictCode, String data) {
        String keyString = String.format("sys:cache:dict::%s:%s", dictCode, data);

        if (stringRedisTemplate.hasKey(keyString)) {
            String value = stringRedisTemplate.opsForValue().get(keyString);
            value = value.replaceAll("\"","");
            log.info("key: " + keyString + ", value: " + value);
            return value;
        }
        return data;
    }
}
