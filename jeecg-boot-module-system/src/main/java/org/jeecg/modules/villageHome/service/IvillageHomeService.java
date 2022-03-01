package org.jeecg.modules.villageHome.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.villageHome.entity.villageHome;

/**
 * @Description: 乡镇户口表
 * @Author: jeecg-boot
 * @Date:   2021-12-24
 * @Version: V1.0
 */
public interface IvillageHomeService extends IService<villageHome> {
    public String getHostByHomeCode(String homeCode);
}
