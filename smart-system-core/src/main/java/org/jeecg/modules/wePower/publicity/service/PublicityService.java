package org.jeecg.modules.wePower.publicity.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.wePower.publicity.vo.PublicityQuery;

/**
* @author sike
* @description 获取信息公示查询接口
* @createDate 2021-12-24 17:17:53
*/
public interface PublicityService extends IService<PublicityQuery> {

    /**
     * 获取信息公示查询接口
     * @return
     */
    public PublicityQuery getQuery();

}
