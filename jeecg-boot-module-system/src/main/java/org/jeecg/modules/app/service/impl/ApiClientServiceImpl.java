package org.jeecg.modules.app.service.impl;

import org.jeecg.modules.app.entity.AppUser;
import org.jeecg.modules.app.mapper.AppUserMapper;
import org.jeecg.modules.app.service.IApiClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApiClientServiceImpl implements IApiClientService {
    @Autowired
    private AppUserMapper appUserMapper;

    @Override
    public int insert(AppUser appUser) {
        return appUserMapper.insert(appUser);
    }

    @Override
    public AppUser queryById(int id) {
        return appUserMapper.queryById(id);
    }

    @Override
    public AppUser queryByAndroidId(String androidId) {
        return appUserMapper.queryByAndroidId(androidId);
    }

    @Override
    public int updateById(AppUser newData) {
        return appUserMapper.updateById(newData);
    }
}
