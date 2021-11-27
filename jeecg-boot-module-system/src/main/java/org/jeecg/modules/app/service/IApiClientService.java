package org.jeecg.modules.app.service;

import org.jeecg.modules.app.entity.AppUser;
import org.jeecg.modules.app.entity.WXUser;

public interface IApiClientService {
    int insert(AppUser appUser);

    AppUser queryById(int id);

    AppUser queryByAndroidId(String androidId);

    int updateById(AppUser newData);

    WXUser queryWxUserByOpenId(String openId);

    int insertWxUser(WXUser wxUser);

    void updateWxUserById(WXUser updateData);

    WXUser queryWxUserById(int id);

    WXUser queryWxUserBySessionKey(String sessionKey);

    boolean updateWxUserPhoneById(int id, String sysUserId, String purePhoneNumber);
}
