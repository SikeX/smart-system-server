package org.jeecg.modules.app.service;

import org.jeecg.modules.app.entity.AppUser;

public interface IApiClientService {
    int insert(AppUser appUser);

    AppUser queryById(int id);

    AppUser queryByAndroidId(String androidId);

    int updateById(AppUser newData);
}
