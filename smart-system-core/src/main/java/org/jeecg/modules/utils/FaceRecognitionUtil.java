package org.jeecg.modules.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: sike
 * @Date: 2022/3/10 0:20
 * @Version: V1.0
 */
@RestController
@Slf4j
public class FaceRecognitionUtil {

    private String apiKey = "tzWA3CjcTSoTSGRsFyln4ARB";

    private String apiSecret = "fxCwK0FKG0QEWSp45OrDRMzxG5TgCa7z";

    private static RestTemplate restTemplate;

    @Resource
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    /**
     * 获取百度智能云api token
     *
     * @return
     */
    public JSONObject getToken() {
        String host = "https://aip.baidubce.com/oauth/2.0/token?";
        String getAccessTokenUrl = host
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=" + apiKey
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + apiSecret;

        JSONObject response = restTemplate.getForObject(getAccessTokenUrl,JSONObject.class);


        return response;
    }


    /**
     * 人脸注册模块
     *
     * @param imgBase64
     * @param groupId
     * @param originName
     * @return
     */
    public JSONObject registerFace(String imgBase64, String groupId, String originName) {

        String name = PinyinUtil.getPinyin(originName, "_");

        String host = "https://aip.baidubce.com/rest/2.0/face/v3/faceset/user/add?";

        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Type", "application/json; charset=UTF-8");

        String access_token = getToken().getString("access_token");

        if (StrUtil.hasEmpty(access_token)) {
            throw new RuntimeException("unable to get token");
        } else {

            String url = host + "access_token=" + access_token;

            Map<String, String> bodyMap = new HashMap<>();

            bodyMap.put("image", imgBase64);
            bodyMap.put("image_type", "BASE64");
            bodyMap.put("group_id", groupId);
            bodyMap.put("user_id", name);


            HttpEntity httpEntity = new HttpEntity<>(bodyMap, headers);


            JSONObject response = restTemplate.postForObject(url, httpEntity, JSONObject.class );

            return response;

        }

    }

    public JSONObject searchFaces(String imgBase64, String groupId) {


        String host = "https://aip.baidubce.com/rest/2.0/face/v3/search?";

        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Type", "application/json; charset=UTF-8");

        String access_token = getToken().getString("access_token");

        if (StrUtil.hasEmpty(access_token)) {
            throw new RuntimeException("unable to get token");
        } else {

            String url = host + "access_token=" + access_token;

            Map<String, String> bodyMap = new HashMap<>();

            bodyMap.put("image", imgBase64);
            bodyMap.put("image_type", "BASE64");
            bodyMap.put("group_id", groupId);


            HttpEntity httpEntity = new HttpEntity<>(bodyMap, headers);


            JSONObject response = restTemplate.postForObject(url, httpEntity, JSONObject.class );

            return response;

        }
    }

}
