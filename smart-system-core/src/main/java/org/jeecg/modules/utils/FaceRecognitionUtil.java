package org.jeecg.modules.utils;

import cn.hutool.core.util.RandomUtil;
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
import java.util.Random;

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
     * @param userId
     * @return
     */
    public JSONObject registerFace(String imgBase64, String groupId, String userId) {

//        String name = PinyinUtil.getPinyin(originName, "_");

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
            bodyMap.put("user_id", userId);


            HttpEntity httpEntity = new HttpEntity<>(bodyMap, headers);


            JSONObject response = restTemplate.postForObject(url, httpEntity, JSONObject.class );

            return response;

        }

    }

    /**
     * 更新人脸信息
     *
     * @param imgBase64
     * @param groupId
     * @param userId
     * @return
     */
    public JSONObject updateFace(String imgBase64, String groupId, String userId) {

//        String name = PinyinUtil.getPinyin(userId, "_");

        String host = "https://aip.baidubce.com/rest/2.0/face/v3/faceset/user/update?";

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
            bodyMap.put("user_id", userId);

            HttpEntity httpEntity = new HttpEntity<>(bodyMap, headers);


            JSONObject response = restTemplate.postForObject(url, httpEntity, JSONObject.class );

            return response;
        }
    }

    /**
     * 人脸搜索模块
     *
     * @param imgBase64
     * @param groupId
     * @return
     */
    public JSONObject searchFaces(String imgBase64, String groupId) {


        String host = "https://aip.baidubce.com/rest/2.0/face/v3/multi-search?";

        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Type", "application/json; charset=UTF-8");

        String access_token = getToken().getString("access_token");

        if (StrUtil.hasEmpty(access_token)) {
            throw new RuntimeException("unable to get token");
        } else {

            String url = host + "access_token=" + access_token;

            Map<String, Object> bodyMap = new HashMap<>();

            bodyMap.put("image", imgBase64);
            bodyMap.put("image_type", "BASE64");
            bodyMap.put("group_id_list", groupId);
            bodyMap.put("max_face_num", 10);
            bodyMap.put("max_user_num", 10);


            HttpEntity httpEntity = new HttpEntity<>(bodyMap, headers);


            JSONObject response = restTemplate.postForObject(url, httpEntity, JSONObject.class );

            return response;

        }
    }

    /**
     * 用户组创建
     *
     * @param groupId
     * @return
     */
    public void createUserGroup(String groupId) {

        String host = "https://aip.baidubce.com/rest/2.0/face/v3/faceset/group/add?";

        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Type", "application/json; charset=UTF-8");

        String access_token = getToken().getString("access_token");

        if (StrUtil.hasEmpty(access_token)) {
            throw new RuntimeException("unable to get token");
        } else {

            String url = host + "access_token=" + access_token;

            Map<String, String> bodyMap = new HashMap<>();

            bodyMap.put("group_id", groupId);

            HttpEntity httpEntity = new HttpEntity<>(bodyMap, headers);

            JSONObject response = restTemplate.postForObject(url, httpEntity, JSONObject.class);

        }
    }

    public JSONObject deleteUserGroup(String groupId) {

        String host = "https://aip.baidubce.com/rest/2.0/face/v3/faceset/group/delete?";

        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Type", "application/json; charset=UTF-8");

        String access_token = getToken().getString("access_token");

        if (StrUtil.hasEmpty(access_token)) {
            throw new RuntimeException("unable to get token");
        } else {

            String url = host + "access_token=" + access_token;

            Map<String, String> bodyMap = new HashMap<>();

            bodyMap.put("group_id", groupId);

            HttpEntity httpEntity = new HttpEntity<>(bodyMap, headers);

            JSONObject response = restTemplate.postForObject(url, httpEntity, JSONObject.class);

            return response;
        }
    }

    /**
     * 删除用户
     *
     * @param groupId
     * @param userName
     * @param faceToken
     *
     * @return
     */
    public JSONObject deleteUser(String groupId, String userName, String faceToken) {

        String userId = PinyinUtil.getPinyin(userName, "_");

        String host = "https://aip.baidubce.com/rest/2.0/face/v3/faceset/face/delete?";

        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Type", "application/json; charset=UTF-8");

        String access_token = getToken().getString("access_token");

        if (StrUtil.hasEmpty(access_token)) {
            throw new RuntimeException("unable to get token");
        } else {

            String url = host + "access_token=" + access_token;

            Map<String, Object> bodyMap = new HashMap<>();

            bodyMap.put("log_id", RandomUtil.randomInt(6,10));
            bodyMap.put("group_id", groupId);
            bodyMap.put("user_id", userId);
            bodyMap.put("face_token", faceToken);

            HttpEntity httpEntity = new HttpEntity<>(bodyMap, headers);

            JSONObject response = restTemplate.postForObject(url, httpEntity, JSONObject.class);

            return response;
        }
    }

}
