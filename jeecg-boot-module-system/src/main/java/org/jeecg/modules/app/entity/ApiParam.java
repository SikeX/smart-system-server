package org.jeecg.modules.app.entity;

public class ApiParam {
    private String clientIp = "";
    private String clientId = "";
    private String androidId = "";
    private String app_version = "";
    private String token = "";
    private String sign = "";
    private String mac = "";

    public ApiParam() {
    }

    public ApiParam(String clientIp, String clientId, String androidId, String app_version, String token, String sign, String mac) {
        this.clientIp = clientIp;
        this.clientId = clientId;
        this.androidId = androidId;
        this.app_version = app_version;
        this.token = token;
        this.sign = sign;
        this.mac = mac;
    }

    public ApiParam(String clientIp, String clientId, String androidId, String app_version, String sign, String mac) {
        this.clientIp = clientIp;
        this.clientId = clientId;
        this.androidId = androidId;
        this.app_version = app_version;
        this.sign = sign;
        this.mac = mac;
    }

    public ApiParam(String clientIp, String androidId, String app_version, String sign, String mac) {
        this.clientIp = clientIp;
        this.androidId = androidId;
        this.app_version = app_version;
        this.sign = sign;
        this.mac = mac;
    }

    @Override
    public String toString() {
        return "ApiParam{" +
                "clientIp='" + clientIp + '\'' +
                ", clientId='" + clientId + '\'' +
                ", androidId='" + androidId + '\'' +
                ", app_version='" + app_version + '\'' +
                ", token='" + token + '\'' +
                ", sign='" + sign + '\'' +
                ", mac='" + mac + '\'' +
                '}';
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
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

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
