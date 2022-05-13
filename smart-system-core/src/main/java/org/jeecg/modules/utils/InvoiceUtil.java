package org.jeecg.modules.utils;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.PmsUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: sike
 * @Date: 2022/5/9 0:20
 * @Version: V1.0
 */
@RestController
@Slf4j
public class InvoiceUtil {

    private static String appcode;

    @Value("${aliyun.appcode}")
    public void setAppcode(String appcode) {
        InvoiceUtil.appcode = appcode;
    }

    private static RestTemplate restTemplate;

    @Resource
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public static JSONObject recognize(String imgBase64) {
        String host = "http://dgfp.market.alicloudapi.com/ocrservice/invoice";

        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Type", "application/json; charset=UTF-8");

        headers.add("Authorization", "APPCODE " + appcode);
        log.info(appcode);


        String url = host;

        Map<String, String> bodyMap = new HashMap<>();

        bodyMap.put("img", imgBase64);

        HttpEntity httpEntity = new HttpEntity<>(bodyMap,headers);

        JSONObject response = restTemplate.postForObject(url, httpEntity, JSONObject.class);
        log.info(String.valueOf(response));

        return response;

    }

    public static JSONObject verificate(String fpdm, String fphm, String kprq, String noTaxAmount, String checkCode) {
        String host = "https://fapiao.market.alicloudapi.com/v2/invoice/query";

        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

        headers.add("Authorization", "APPCODE " + appcode);

        log.info(appcode);


        String url = host;

        MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap();

        bodyMap.add("fpdm", fpdm);
        bodyMap.add("fphm", fphm);
        bodyMap.add("kprq", kprq);
        bodyMap.add("noTaxAmount", noTaxAmount);
        bodyMap.add("checkCode", checkCode);

        HttpEntity httpEntity = new HttpEntity<>(bodyMap,headers);

        JSONObject response = restTemplate.postForObject(url, httpEntity, JSONObject.class);
        log.info(String.valueOf(response));

        return response;

    }

}
