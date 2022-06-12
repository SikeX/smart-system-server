package org.jeecg.modules.smartJob.entity;

public class JobType{
    private static final String THEPART = "0"; //入党纪念日提醒
    private static final String PUNISH = "1"; //解除处分提醒
    private static final String CUSTOMIZED = "2";  //自定义任务
    private static final String POSTREMIND = "3"; //婚后报备提醒
    private static final String ONTHER = "4"; //其他系统提醒

    public static String getTHEPART() {
        return THEPART;
    }

    public static String getPUNISH() {
        return PUNISH;
    }

    public static String getONTHER() {
        return ONTHER;
    }
    public static String getPOSTREMIND() {
        return POSTREMIND;
    }

    public static String getCUSTOMIZED() {
        return CUSTOMIZED;
    }
}

