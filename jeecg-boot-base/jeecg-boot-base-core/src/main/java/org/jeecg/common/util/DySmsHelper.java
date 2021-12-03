package org.jeecg.common.util;

//import org.jeecg.common.util.smartSentMsg.service.ISmartSentMsgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Created on 17/6/7.
 * 短信API产品的DEMO程序,工程中包含了一个SmsDemo类，直接通过
 * 执行main函数即可体验短信产品API功能(只需要将AK替换成开通了云通信-短信产品功能的AK即可)
 * 工程依赖了2个jar包(存放在工程的libs目录下)
 * 1:aliyun-java-sdk-core.jar
 * 2:aliyun-java-sdk-dysmsapi.jar
 * <p>
 * 备注:Demo工程编码采用UTF-8
 * 国际短信发送请勿参照此DEMO
 */
@Component
public class DySmsHelper {

//    private static ISmartSentMsgService smartSentMsgService;
//    @Autowired
//    public void setSmartSentMsgService(ISmartSentMsgService smartSentMsgService){
//        DySmsHelper.smartSentMsgService = smartSentMsgService;
//    }

    private final static Logger logger = LoggerFactory.getLogger(DySmsHelper.class);

    //产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";

    // TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
    static String accessKeyId;
    static String accessKeySecret;

    public static void setAccessKeyId(String accessKeyId) {
        DySmsHelper.accessKeyId = accessKeyId;
    }

    public static void setAccessKeySecret(String accessKeySecret) {
        DySmsHelper.accessKeySecret = accessKeySecret;
    }

    public static String getAccessKeyId() {
        return accessKeyId;
    }

    public static String getAccessKeySecret() {
        return accessKeySecret;
    }

    //创蓝接口
    private static final String sendUrl = "http://smssh1.253.com/msg/v1/send/json";//API URL
    private static final Map<String, String> map = new HashMap<String, String>();

    static {
        map.put("account", "N7764651");//API账号
        map.put("password", "x5ipGcgoe");//API密码
    }


    //阿里云短信
//    public static boolean sendSms(String phone,JSONObject templateParamJson,DySmsEnum dySmsEnum) throws ClientException {
//    	//可自助调整超时时间
//        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
//        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
//
//        //update-begin-author：taoyan date:20200811 for:配置类数据获取
//        StaticConfig staticConfig = SpringContextUtils.getBean(StaticConfig.class);
//        setAccessKeyId(staticConfig.getAccessKeyId());
//        setAccessKeySecret(staticConfig.getAccessKeySecret());
//        //update-end-author：taoyan date:20200811 for:配置类数据获取
//
//        //初始化acsClient,暂不支持region化
//        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
//        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
//        IAcsClient acsClient = new DefaultAcsClient(profile);
//
//        //验证json参数
//        validateParam(templateParamJson,dySmsEnum);
//
//        //组装请求对象-具体描述见控制台-文档部分内容
//        SendSmsRequest request = new SendSmsRequest();
//        //必填:待发送手机号
//        request.setPhoneNumbers(phone);
//        //必填:短信签名-可在短信控制台中找到
//        request.setSignName(dySmsEnum.getSignName());
//        //必填:短信模板-可在短信控制台中找到
//        request.setTemplateCode(dySmsEnum.getTemplateCode());
//        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
//        request.setTemplateParam(templateParamJson.toJSONString());
//
//        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
//        //request.setSmsUpExtendCode("90997");
//
//        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
//        //request.setOutId("yourOutId");
//
//        boolean result = false;
//
//        //hint 此处可能会抛出异常，注意catch
//        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
//        logger.info("短信接口返回的数据----------------");
//        logger.info("{Code:" + sendSmsResponse.getCode()+",Message:" + sendSmsResponse.getMessage()+",RequestId:"+ sendSmsResponse.getRequestId()+",BizId:"+sendSmsResponse.getBizId()+"}");
//        if ("OK".equals(sendSmsResponse.getCode())) {
//            result = true;
//        }
//        return result;
//
//    }

    private static void validateParam(JSONObject templateParamJson, DySmsEnum dySmsEnum) {
        String keys = dySmsEnum.getKeys();
        String[] keyArr = keys.split(",");
        for (String item : keyArr) {
            if (!templateParamJson.containsKey(item)) {
                throw new RuntimeException("模板缺少参数：" + item);
            }
        }
    }


//    public static void main(String[] args) throws ClientException, InterruptedException {
//    	JSONObject obj = new JSONObject();
//    	obj.put("code", "1234");
//    	sendSms("13800138000", obj, DySmsEnum.FORGET_PASSWORD_TEMPLATE_CODE);
//    }


    //创蓝智能云平台短信服务，发送验证码
    public static boolean sendSms(String mobile, JSONObject templateParamJson, DySmsEnum dySmsEnum) {

        String code = templateParamJson.get("code").toString();

        map.put("msg", "您好，您的验证码是" + code + "，十分钟内有效，请勿向任何人泄露。");//短信内容
        map.put("phone", mobile);//手机号
        // map.put("report","true");//是否需要状态报告
        // map.put("extend","123");//自定义扩展码
        JSONObject js = (JSONObject) JSONObject.toJSON(map);
        String reString = sendSmsByPost(sendUrl, js.toString());
        System.out.println(reString);

        //返回值
        JSONObject json = JSONObject.parseObject(reString);
        Map<String, Object> map1 = (Map<String, Object>) json;

        if (map1.get("code").equals("0")) {
            return true;
        } else {
            return false;
        }
    }

    //创蓝智能云平台短信服务，发送模板消息
    public static boolean sendSms(String sendFrom, String sendType, String tittle, String content, String receiver, String receiverPhone) {

        map.put("msg", content);//短信内容
        map.put("phone", receiverPhone);//手机号
        // map.put("report","true");//是否需要状态报告
        // map.put("extend","123");//自定义扩展码
        JSONObject js = (JSONObject) JSONObject.toJSON(map);
        String reString = sendSmsByPost(sendUrl, js.toString());
        System.out.println(reString);

        //返回值
        JSONObject json = JSONObject.parseObject(reString);
        Map<String, Object> map1 = (Map<String, Object>) json;

        if (map1.get("code").equals("0")) {
            return true;
        } else {
            return false;
        }




//        SysUser user = smartSentMsgService.getOrgCode(sendFrom);
//        System.out.println("org code" + user.getOrgCode());
//        SmartSentMsg smartSentMsg = new SmartSentMsg();
//        System.out.println(smartSentMsg.getOrgCode());
//        smartSentMsg.setOrgCode(code);
//        smartSentMsg.setSendFrom(sendFrom);
//        smartSentMsg.setContent(content);
//        smartSentMsg.setSendType(sendType);
//        smartSentMsg.setTittle(tittle);
//
        //单次发送最多999个号码
//        List<List<String>> phoneLists = getPhoneList(receiverPhone);
//        List<List<String>> personLists = getPersonList(receiver);
//        int len = phoneLists.size();
//
//        String regEx="[`~!@#$%^&*()+=|{}':;'\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。， 、？]";
//        boolean status = true;





//
//        for(int i = 0; i < len; i++){
//
//            String phones = phoneLists.get(i).toString().replaceAll(regEx, "");
//
//        map.put("msg", content);//短信内容
//        map.put("phone", receiverPhone);//手机号
//        // map.put("report","true");//是否需要状态报告
//        // map.put("extend","123");//自定义扩展码
//        JSONObject js = (JSONObject) JSONObject.toJSON(map);
//        String reString = sendSmsByPost(sendUrl, js.toString());
//        System.out.println(reString);

        //返回值
//        JSONObject json = JSONObject.parseObject(reString);
//        Map<String, Object> map1 = (Map<String, Object>) json;
//
//        if (map1.get("code").equals("0")) {
//            //将发成功的数据写入数据库
//            int len1 = phoneLists.get(i).size();
//            List<SmartSentMsg> insertList = new ArrayList<>();
//            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//            System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
//            for (int j = 0; j < len1; j++) {
//                smartSentMsg.setReceiverPhone(phoneLists.get(i).get(j));
//                smartSentMsg.setReceiver(personLists.get(i).get(j));
//                smartSentMsg.setSendTime(new Date());
//                insertList.add(smartSentMsg);
//            }
//            smartSentMsgService.getOrgCode(sendFrom);
//            status = true;
//        } else {
//            status = false;
//        }
//
//
//        return status;
//        return true;
    }

    private static String sendSmsByPost(String path, String postContent) {
        URL url = null;
        try {
            url = new URL(path);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.connect();
            OutputStream os = httpURLConnection.getOutputStream();
            os.write(postContent.getBytes("UTF-8"));
            os.flush();
            StringBuilder sb = new StringBuilder();
            int httpRspCode = httpURLConnection.getResponseCode();
            if (httpRspCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                return sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
