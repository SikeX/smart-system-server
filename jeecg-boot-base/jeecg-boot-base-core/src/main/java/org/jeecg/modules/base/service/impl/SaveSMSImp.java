package org.jeecg.modules.base.service.impl;

import org.jeecg.modules.base.mapper.SaveSMSMapper;
import org.jeecg.modules.base.service.SaveSMS;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @Description: TODO
* @author: lord
* @date: 2021年12月04日 11:15
*/

@Service
public class SaveSMSImp implements SaveSMS{

    @Resource
    SaveSMSMapper saveSMSMapper;


    @Override
    public String selectId(String name) {
        return saveSMSMapper.selectOrgId(name);
    }
}
