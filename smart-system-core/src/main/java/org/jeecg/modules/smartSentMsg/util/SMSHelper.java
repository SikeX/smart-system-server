package org.jeecg.modules.smartSentMsg.util;

import org.apache.commons.collections4.ListUtils;
import org.jeecg.modules.smartJob.entity.SysUser;
import org.jeecg.modules.smartJob.service.ISmartJobService;
import org.jeecg.modules.smartJob.util.ComputeTime;
import org.jeecg.modules.smartSentMsg.entity.SmartSentMsg;
import org.jeecg.modules.smartSentMsg.service.ISmartSentMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: TODO
 * @author: lord
 * @date: 2021年12月03日 17:03
 */
@Component
public class SMSHelper {

    private static ISmartSentMsgService smartSentMsgService;

    @Autowired
    public void setSmartJobService(ISmartSentMsgService smartSentMsgService){

        SMSHelper.smartSentMsgService = smartSentMsgService;
    }

    public static boolean saveSMS(String sendFrom, String sendType, String tittle, String content, String receiver, String receiverPhone){

        String orgId = smartSentMsgService.getOrgId(sendFrom);
        System.out.println(orgId);
        SmartSentMsg smartSentMsg = new SmartSentMsg();
        smartSentMsg.setContent(content);
        smartSentMsg.setSendFrom(sendFrom);
        smartSentMsg.setTittle(tittle);
        smartSentMsg.setSysOrgCode(orgId);

        List<String> names = Arrays.asList(receiver.split(","));
        List<String> phone = Arrays.asList(receiverPhone.split(","));
        int len =phone.size();

        List<SmartSentMsg> list = new ArrayList<>();
        String phones = "";

        for(int i = 0; i < len; i++){
            smartSentMsg.setReceiver(names.get(i));
            smartSentMsg.setReceiverPhone(phone.get(i));
            phones += phone.get(i);
        }









        return true;
    }

    private static List<List<String>> getPhoneList(String phones) {
        List<String> tem = Arrays.asList(phones.split(","));
        return ListUtils.partition(tem, 999);
    }

    private static List<List<String>> getPersonList(String person) {
        List<String> list = Arrays.asList(person.split(","));
        return ListUtils.partition(list, 999);
    }
}
