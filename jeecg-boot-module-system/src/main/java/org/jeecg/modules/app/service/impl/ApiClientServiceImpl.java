package org.jeecg.modules.app.service.impl;

import org.jeecg.modules.app.entity.AppUser;
import org.jeecg.modules.app.entity.WXUser;
import org.jeecg.modules.app.mapper.AppUserMapper;
import org.jeecg.modules.app.mapper.WXUserMapper;
import org.jeecg.modules.app.service.IApiClientService;
import org.jeecg.modules.system.mapper.SysAnnouncementMapper;
import org.jeecg.modules.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApiClientServiceImpl implements IApiClientService {
    @Autowired
    private AppUserMapper appUserMapper;
    @Autowired
    private WXUserMapper wxUserMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysAnnouncementMapper sysAnnouncementMapper;

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

    @Override
    public WXUser queryWxUserByOpenId(String openId) {
        return wxUserMapper.queryByOpenId(openId);
    }

    @Override
    public int insertWxUser(WXUser wxUser) {
        return wxUserMapper.insert(wxUser);
    }

    @Override
    public void updateWxUserById(WXUser updateData) {
        wxUserMapper.updateById(updateData);
    }

    @Override
    public WXUser queryWxUserById(int id) {
        return wxUserMapper.queryById(id);
    }

    @Override
    public WXUser queryWxUserBySessionKey(String sessionKey) {
        return wxUserMapper.queryBySessionKey(sessionKey);
    }

    @Override
    public boolean updateWxUserPhoneById(int id, String sysUserId, String purePhoneNumber) {
        // 首先更新tb_wx_user的phone
        wxUserMapper.updatePhoneById(id, purePhoneNumber);
        // 更新sys_user
        sysUserMapper.updatePhoneById(sysUserId, purePhoneNumber);
        return true;
    }

    @Override
    public int queryUnreadMessageNumBySysUserId(String sysUserId) {
        return sysAnnouncementMapper.queryUnreadMessageNumBySysUserId(sysUserId);
    }
}
