package org.jeecg.modules.smartJob.util;

import org.apache.commons.collections4.ListUtils;
import org.jeecg.modules.SmartPunishPeople.entity.SmartPunishPeople;
import org.jeecg.modules.smartJob.entity.SmartJob;
import org.jeecg.modules.smartJob.entity.SysUser;
import org.jeecg.modules.smartJob.service.ISmartJobService;
import org.jeecg.modules.smartSentMsg.entity.SmartSentMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: 计算执行时间
 * @author: lord
 * @date: 2021年11月30日 20:16
 */
@Component
public class ComputeTime {

    private static ISmartJobService smartJobService;

    public static boolean isFifteen(Date weddingTime) {

        Date now = new Date();
        long nowLong = now.getTime();
        long weddingLong = weddingTime.getTime();

        long delta = nowLong - weddingLong;
        delta /= ONE_MINUTE;
        delta /= 60;
        delta /= 24;

        if(delta > 15){
            return true;
        }else{
            return false;
        }
    }

    @Autowired
    public void setSmartJobService(ISmartJobService smartJobService){

        ComputeTime.smartJobService = smartJobService;
    }

    private static final long ONE_MINUTE = 60000L;  //一分钟
    private static final long ONE_DAY = 86400000L;     //一天

    //每日提醒任务，获取获取第一次执行时间
    public static long loopGetDelayMinutes(String hours){

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String[] s = hours.split(":");
        int hour = Integer.parseInt(s[0]);
        int minutes = Integer.parseInt(s[1]);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int nowDay = calendar.get(Calendar.DAY_OF_MONTH);
        int nowHour = calendar.get(Calendar.HOUR_OF_DAY);

        Date now = new Date();

        long delay = 0;
        Date delayDateD = null;


        long tem;
        String delayTime = year+ "-" + month + "-" + nowDay + " " + hours;
        try {
            delayDateD = df.parse(delayTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        delay = delayDateD.getTime() - now.getTime();

        if(delay < 0){
            //今天执行时间过了
            //执行时间加一天
            delayTime = year+ "-" + month + "-" + (nowDay + 1) + " " + hours;
            try {
                delayDateD = df.parse(delayTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            delay = delayDateD.getTime() - now.getTime();
            delay = delay / ONE_MINUTE;
            return delay;

        }else{

            //今天执行时间未过
            return delay / ONE_MINUTE;
        }
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

    //获取婚后报备提醒第一次执行时间
    public static long getPostDelayTime(Date weddingTime) {
        //婚礼日期三天后第一次提醒

        long time = weddingTime.getTime() - new Date().getTime();
        time = time / ONE_MINUTE;
        if(time < 0){
            //婚礼结束后才填报
            //加三天
//            time = time - 4320L;
            time /= 60 * 24;
            return -time;
        }else{
            //加三天
//            time = time + 4320L;
            time /= 60 * 24;
            return time;
        }
    }
    public static void setPostFirstTime(SmartJob smartJob, Date weddingTime) {

        long remindTime = weddingTime.getTime();
        //加三天
        remindTime += 259200000L;

        //早上八点提醒
        remindTime += 28800000L;

        Date newDate = new Date();
        newDate.setTime(remindTime);

        smartJob.setExecuteTimeDay(newDate);
        smartJob.setExecuteTimeHour("08:00:00");
    }

    public static long initGetPre(SmartJob s) {
        Date marrayDate = s.getExecuteTimeDay();
        Date now = new Date();
        long marrayDatel = marrayDate.getTime();
        long nowl = now.getTime();

        long delay = nowl - marrayDatel;

        long tem = 0;
        if(delay > 0){
            //婚礼已经举行,立即提醒一次，下次提醒三天后
            return 0;
        }else{
            //婚礼后三天执行
            delay /= ONE_MINUTE;

            //测试用
            delay /= 60;
            delay/= 24;

            return -delay + 3;
        }

    }

    public static boolean roundThree(Date weddingTime) {

        Date date = new Date();
        long wedding = weddingTime.getTime();
        long now = date.getTime();

        long delta = now - wedding;

        delta /= ONE_DAY;

        long round = delta % 3;

        if(round == 0 && delta >= 3){
            //提醒
            return true;
        }else{
            //不提醒
            return false;
        }
    }

    //入党纪念日获取电话和msgs
    public static Map<String, Object> getPhoneList(List<SysUser> sysUsers, String content, String sendType, String sendFrom, String tittle) {

        String orgId = smartJobService.getOrgId(sendFrom);

        //返回值
        Map<String, Object> map = new HashMap<>();

        //划分为每份999,短信一次提交最多发送10000个号码
        List<List<SysUser>> tem = ListUtils.partition(sysUsers, 999);

        List<String> temPhones = new ArrayList<>();
        List<List<SmartSentMsg>> temMsg = new ArrayList<>();

        //获取电话号码和smartSentMsg
        for(int i = 0; i < tem.size(); i++){
            String phones = null;
            List<SmartSentMsg> smartSentMsgs = new ArrayList<>();
            for(int j = 0; j < tem.get(i).size(); j++){
                if(null != tem.get(i).get(j).getPhone()){

                    SmartSentMsg smartSentMsg = getSmartSentMsg(
                            sendType,
                            sendFrom,
                            orgId,
                            tem.get(i).get(j).getRealname(),
                            tittle,
                            content
                    );

                    //收集手机号
                    if(null == phones){
                        phones = tem.get(i).get(j).getPhone();
                        smartSentMsg.setReceiverPhone(tem.get(i).get(j).getPhone());
                    }else{
                        phones += "," + tem.get(i).get(j).getPhone();
                        smartSentMsg.setReceiverPhone(tem.get(i).get(j).getPhone());
                    }

                    //收集smartSentMsg
                    smartSentMsgs.add(smartSentMsg);
                }
            }
            //暂存
            temPhones.add(phones);
            temMsg.add(smartSentMsgs);
        }

        map.put("phones", temPhones);
        map.put("Msgs", temMsg);

        return map;
    }

    //解除处分获取phone和Msgs
    public static Map<String, Object> getPunoshPhoneList(List<SmartPunishPeople> punishPeople, String content, String sendType, String sendFrom, String tittle) {

        String orgId = smartJobService.getOrgId(sendFrom);

        //返回值
        Map<String, Object> map = new HashMap<>();

        //划分为每份999,短信一次提交最多发送10000个号码
        List<List<SmartPunishPeople>> tem = ListUtils.partition(punishPeople, 999);

        List<String> temPhones = new ArrayList<>();
        List<List<SmartSentMsg>> temMsg = new ArrayList<>();

        //获取电话号码和smartSentMsg
        for(int i = 0; i < tem.size(); i++){
            String phones = null;
            List<SmartSentMsg> smartSentMsgs = new ArrayList<>();
            for(int j = 0; j < tem.get(i).size(); j++){
                if(null != tem.get(i).get(j).getPhone()){

                    SmartSentMsg smartSentMsg = getSmartSentMsg(
                            sendType,
                            sendFrom,
                            orgId,
                            tem.get(i).get(j).getPunishName(),
                            tittle,
                            content
                    );

                    //收集手机号
                    if(null == phones){
                        phones = tem.get(i).get(j).getPhone();
                        smartSentMsg.setReceiverPhone(tem.get(i).get(j).getPhone());
                    }else{
                        phones += "," + tem.get(i).get(j).getPhone();
                        smartSentMsg.setReceiverPhone(tem.get(i).get(j).getPhone());
                    }

                    //收集smartSentMsg
                    smartSentMsgs.add(smartSentMsg);
                }
            }
            //暂存
            temPhones.add(phones);
            temMsg.add(smartSentMsgs);
        }

        map.put("phones", temPhones);
        map.put("Msgs", temMsg);

        return map;
    }

    private static SmartSentMsg getSmartSentMsg(String sendType, String sendFrom, String orgId, String receiver, String tittle, String content){

        Date date = new Date();

        SmartSentMsg smartSentMsg = new SmartSentMsg();
        smartSentMsg.setSendType(sendType);
        smartSentMsg.setSendFrom(sendFrom);
        smartSentMsg.setSysOrgCode(orgId);
        smartSentMsg.setReceiver(receiver);
        smartSentMsg.setTittle(tittle);
        smartSentMsg.setSendTime(date);
        smartSentMsg.setContent(content);
        //默认成功，失败再改
        smartSentMsg.setStatus("0");

        return smartSentMsg;
    }

    //短信发送失败，修改状态
    public static void changeStatus(List<SmartSentMsg> smartSentMsgs) {
        for(SmartSentMsg s : smartSentMsgs){
            s.setStatus("1");
        }
    }

}
