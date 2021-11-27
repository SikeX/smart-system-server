package org.jeecg.modules.app.entity;

public class WXUser {
    private int id;
    private String wxOpenId;
    private String wxSessionKey;
    private int lastLoginTime;
    private String sysUserId;
    private String phone;
    private int status;
    private int ctime;
    private int mtime;
    private String token;

    public WXUser() {
    }

    public WXUser(int id, String wxOpenId, String wxSessionKey, int lastLoginTime, String sysUserId, String phone, int status, int ctime, int mtime, String token) {
        this.id = id;
        this.wxOpenId = wxOpenId;
        this.wxSessionKey = wxSessionKey;
        this.lastLoginTime = lastLoginTime;
        this.sysUserId = sysUserId;
        this.phone = phone;
        this.status = status;
        this.ctime = ctime;
        this.mtime = mtime;
        this.token = token;
    }

    public WXUser(int id, String wxSessionKey, int lastLoginTime, int mtime) {
        this.id = id;
        this.wxSessionKey = wxSessionKey;
        this.lastLoginTime = lastLoginTime;
        this.mtime = mtime;
    }

    public WXUser(String wxOpenId, String wxSessionKey, int lastLoginTime, int status, int ctime, int mtime) {
        this.wxOpenId = wxOpenId;
        this.wxSessionKey = wxSessionKey;
        this.lastLoginTime = lastLoginTime;
        this.status = status;
        this.ctime = ctime;
        this.mtime = mtime;
    }

    @Override
    public String toString() {
        return "WXUser{" +
                "id=" + id +
                ", wxOpenId='" + wxOpenId + '\'' +
                ", wxSessionKey='" + wxSessionKey + '\'' +
                ", lastLoginTime=" + lastLoginTime +
                ", sysUserId='" + sysUserId + '\'' +
                ", phone='" + phone + '\'' +
                ", status=" + status +
                ", ctime=" + ctime +
                ", mtime=" + mtime +
                ", token='" + token + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }

    public String getWxSessionKey() {
        return wxSessionKey;
    }

    public void setWxSessionKey(String wxSessionKey) {
        this.wxSessionKey = wxSessionKey;
    }

    public int getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(int lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(String sysUserId) {
        this.sysUserId = sysUserId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCtime() {
        return ctime;
    }

    public void setCtime(int ctime) {
        this.ctime = ctime;
    }

    public int getMtime() {
        return mtime;
    }

    public void setMtime(int mtime) {
        this.mtime = mtime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
