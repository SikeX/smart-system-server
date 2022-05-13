package org.jeecg.modules.villageHome.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.villageHome.entity.villageHome;
import org.jeecg.modules.villageHome.mapper.vHomeMapper;
import org.jeecg.modules.villageHome.mapper.villageHomeMapper;
import org.jeecg.modules.villageHome.service.IvHomeService;
import org.jeecg.modules.villageHome.service.IvillageHomeService;
import org.jeecg.modules.villageHome.vo.vHome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: 乡镇户口表
 * @Author: jeecg-boot
 * @Date:   2021-12-24
 * @Version: V1.0
 */
@Service
public class vHomeServiceImpl extends ServiceImpl<vHomeMapper, vHome> implements IvHomeService {

}
