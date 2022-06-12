package org.jeecg.modules.smart_window_people.api.response;

/**
 * Created by Administrator on 2020/1/6.
 */
public enum StatusCode {

    Success(200,"成功"),
    Fail(-1,"失败"),

    InvalidParams(10010,"非法的参数！"),

    MailInfoSendDateTimeInvalidated(20000,"指定邮件为延迟发送时，其发送时间必填！"),

    ProductKillFail(30000,"商品抢购失败，请刷新浏览器再重试！"),

    PddPerTotalInvalid(40000,"指定的拼团人数应至少1个人！"),

    UserNameHasExist(5000,"该账号已存在！"),

    UserNamePasswordInvalid(5001,"账号密码不能为空！"),
    UserNamePasswordNotMatch(5002,"账号密码不匹配！"),

    CanNotRepeatSubmit(5003,"不允许重复提交，请稍后重试！"),

    ;

    private Integer code;
    private String msg;

    StatusCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
