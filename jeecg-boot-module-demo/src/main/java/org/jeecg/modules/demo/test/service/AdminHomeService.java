package org.jeecg.modules.demo.test.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;


/**
* @author sike
* @description 针对表【smart_village_topic】的数据库操作Service
* @createDate 2021-11-24 15:19:18
*/
public interface AdminHomeService extends IService<AdminHomeService> {

    public List<Object> getHudong();

}
