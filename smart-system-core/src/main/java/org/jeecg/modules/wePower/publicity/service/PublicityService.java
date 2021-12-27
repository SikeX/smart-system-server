package org.jeecg.modules.wePower.publicity.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.wePower.publicity.vo.PublicityQuery;

/**
* @author sike
* @description 针对表【smart_create_advice】的数据库操作Service
* @createDate 2021-12-24 17:17:53
*/
public interface PublicityService extends IService<PublicityQuery> {

    public PublicityQuery getQuery();

}
