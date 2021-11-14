package org.jeecg.modules.app.entity;

public class AppUser {
    private int id;
    private String clientId;
    private String androidId;
    private String lastLoginIp;
    private String mac;
    private String appVersion;
    private String brand;
    private String phoneModel;
    private int lastLoginTime;
    private String sysUserId;
    private String phone;
    private int status;
    private int ctime;
    private int mtime;
    private String token;

    public AppUser(int id, String clientId, String androidId, String lastLoginIp, String mac, String appVersion, String brand, String phoneModel, int lastLoginTime, String sysUserId, String phone, int status, int ctime, int mtime, String token) {
        this.id = id;
        this.clientId = clientId;
        this.androidId = androidId;
        this.lastLoginIp = lastLoginIp;
        this.mac = mac;
        this.appVersion = appVersion;
        this.brand = brand;
        this.phoneModel = phoneModel;
        this.lastLoginTime = lastLoginTime;
        this.sysUserId = sysUserId;
        this.phone = phone;
        this.status = status;
        this.ctime = ctime;
        this.mtime = mtime;
        this.token = token;
    }

    public AppUser(int id, String androidId, String lastLoginIp, String mac, String appVersion, String brand, String phoneModel, int lastLoginTime, int status, int ctime, int mtime) {
        this.id = id;
        this.androidId = androidId;
        this.lastLoginIp = lastLoginIp;
        this.mac = mac;
        this.appVersion = appVersion;
        this.brand = brand;
        this.phoneModel = phoneModel;
        this.lastLoginTime = lastLoginTime;
        this.status = status;
        this.ctime = ctime;
        this.mtime = mtime;
    }

    public AppUser(String androidId, String lastLoginIp, String mac, String appVersion, String brand, String phoneModel, int lastLoginTime, int status, int ctime, int mtime) {
        this.androidId = androidId;
        this.lastLoginIp = lastLoginIp;
        this.mac = mac;
        this.appVersion = appVersion;
        this.brand = brand;
        this.phoneModel = phoneModel;
        this.lastLoginTime = lastLoginTime;
        this.status = status;
        this.ctime = ctime;
        this.mtime = mtime;
    }

    public AppUser() {
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "id=" + id +
                ", clientId='" + clientId + '\'' +
                ", androidId='" + androidId + '\'' +
                ", lastLoginIp='" + lastLoginIp + '\'' +
                ", mac='" + mac + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", brand='" + brand + '\'' +
                ", phoneModel='" + phoneModel + '\'' +
                ", lastLoginTime=" + lastLoginTime +
                ", sysUserId='" + sysUserId + '\'' +
                ", phone='" + phone + '\'' +
                ", status=" + status +
                ", ctime=" + ctime +
                ", mtime=" + mtime +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
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
