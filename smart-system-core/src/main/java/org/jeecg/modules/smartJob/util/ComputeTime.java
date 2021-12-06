package org.jeecg.modules.smartJob.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.common.util.entity.SmartSentMsg;
import org.jeecg.modules.smartJob.entity.SysUser;
import org.jeecg.modules.smartJob.service.ISmartJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Description: 计算执行时间
 * @author: lord
 * @date: 2021年11月30日 20:16
 */
@Component
public class ComputeTime {

    private static ISmartJobService smartJobService;

    public static List<SmartSentMsg> userToSMS(List<SysUser> users, String from, String orgId, String type, String tittle, String content) {

        Date date = new Date();

        int len = users.size();

        List<SmartSentMsg> list = new ArrayList<>();

        for(int i = 0; i < len; i++){
            SmartSentMsg smartSentMsg = new SmartSentMsg();
            smartSentMsg.setSendFrom(from);
            smartSentMsg.setTittle("其他提醒");
            smartSentMsg.setSysOrgCode(orgId);
            smartSentMsg.setContent(content);
            smartSentMsg.setSendTime(date);
            smartSentMsg.setReceiverPhone(users.get(i).getPhone());
            smartSentMsg.setReceiver(users.get(i).getRealname());

            list.add(smartSentMsg);
        }
        return list;
    }

    @Autowired
    public void setSmartJobService(ISmartJobService smartJobService){

        ComputeTime.smartJobService = smartJobService;
    }

    private static final long ONE_MINUTE = 60000L;

    public long computeDelay(Date day, Date hours){
        return 1L;
    }

    public long computeDelay(Date hours){
        return 1L;
    }

    //每日提醒任务，获取获取第一次执行时间
    public static long loopGetDelayMinutes(String hours){
        String[] s = hours.split(":");
        int hour = Integer.parseInt(s[0]);
        int minutes = Integer.parseInt(s[1]);
        System.out.println("hour = " + hour + " minute = " + minutes);

        Date date = new Date();
        int nowHour = date.getHours();
        int nowMinute = date.getMinutes();

        int delay = hour - nowHour;
        if(hour >= nowHour){
            //执行时间在当前小时之后
            delay = hour - nowHour;
        }else{
            delay = 24 - nowHour + hour;
        }

        if(minutes >= nowMinute){
            delay = delay * 60 + minutes - nowMinute;
        }else{
            delay = delay * 60 - nowMinute + minutes;
        }

        System.out.println("delay = " + delay);
        return delay;
    }

    //延迟任务获取执行时间
    public static long getDelayTime(Date day, String hours)  {

        if(null == day){
            day = new Date();
        }

        String[] s = hours.split(":");
        int hour = Integer.parseInt(s[0]);
        int minutes = Integer.parseInt(s[1]);

        Date now = new Date();

        //提醒时间点
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String[] days = df.format(day).split(" ");
        days = days[0].split("-");
        int delayYear = Integer.parseInt(days[0]);
        int delayMonth = Integer.parseInt(days[1]);
        int delayDay = Integer.parseInt(days[2]);
        String delayDateStr = "" + delayYear + "-" + delayMonth + "-" + delayDay + " " + hours;
        Date delayDateD = null;
        try {
            delayDateD = df.parse(delayDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //计算delay
        long delay = delayDateD.getTime() - now.getTime();
        delay = delay / ONE_MINUTE;

        return delay;
    }

    //获取指定人员列表
    public static List<SysUser> getUserInfo(String list){
        List<String> userids = Arrays.asList(list.split(","));
        List<SysUser> users = smartJobService.getUsers(userids);
        return users;
    }

    //获取用户名string和手机号String
    public static List<List<String>> getNameAndPhone(List<SysUser> users){
        int len = users.size();
        int count = 0;

        List<List<String>> userAndPhone = new ArrayList<>();
        List<String> user = new ArrayList<>();
        List<String> phones = new ArrayList<>();
        int c;

        if(len % 999 == 0){
            c = len / 999;
        }else{
            c = len / 999 + 1;
        }

        for(int i = 0; i < c; i++){
            String userTmp;
            String phonesTmp;
            if(null != users.get(count).getRealname()){
                userTmp = users.get(count).getRealname();
            }else{
                userTmp = "";
            }

            if(null != users.get(count).getPhone()){
                phonesTmp = users.get(count).getPhone();
            }else{
                phonesTmp = "";
            }

            count++;

            for(int j = 0; j < 998 && count < len; j++){
                userTmp += "," + users.get(count).getRealname();
                phonesTmp += "," + users.get(count).getPhone();
                count ++;
            }

            user.add(userTmp);
            phones.add(phonesTmp);
        }

        userAndPhone.add(user);
        userAndPhone.add(phones);

        return userAndPhone;
    }

}
