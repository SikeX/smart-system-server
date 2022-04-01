package org.jeecg.modules.faceRecognition.controller;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.faceRecognition.FaceGropTypeConstant;
import org.jeecg.modules.faceRecognition.vo.FaceVo;
import org.jeecg.modules.utils.FaceRecognitionUtil;
import org.jeecg.modules.utils.ImageUtils;
import org.jeecg.modules.utils.UrlUtil;
import org.jeecg.modules.wePower.smartGroupEconomy.entity.SmartGroupEconomyPeople;
import org.jeecg.modules.wePower.smartGroupEconomy.service.ISmartGroupEconomyPeopleService;
import org.jeecg.modules.wePower.smartVillageLead.entity.SmartVillageLead;
import org.jeecg.modules.wePower.smartVillageLead.service.ISmartVillageLeadService;
import org.jeecg.modules.wePower.smartVillageLead2.entity.SmartVillageLead2;
import org.jeecg.modules.wePower.smartVillageLead2.service.ISmartVillageLead2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @Description:
 * @Author: sike
 * @Date: 2022/3/25 22:39
 * @Version: V1.0
 */
@ApiModel(description = "人脸识别控制器")
@RestController
@RequestMapping("/faceRecognition")
@Slf4j
public class FaceRecognitionController {

    @Autowired
    private ISmartGroupEconomyPeopleService smartGroupEconomyPeopleService;

    @Autowired
    private ISmartVillageLeadService smartVillageLeadService;

    @Autowired
    private ISmartVillageLead2Service smartVillageLead2Service;

    @Value("${jeecg.fileBaseUrl}")
    private String fileBaseUrl;

    @GetMapping("/getFaceRecognition")
    public Result<?> getFaceRecognition(String imgPaths,
                                        @RequestParam(name = "groupId", required = false) String groupId) {

        String[] imgPathArr = imgPaths.split(",");

        List<FaceVo> faceList = new ArrayList<>();

        try {

            for (String imgPath : imgPathArr) {

                FaceRecognitionUtil faceRecognitionUtil = new FaceRecognitionUtil();

                String imgBase64 = ImageUtils.getBase64ByImgUrl(UrlUtil.urlEncodeChinese(fileBaseUrl + imgPath));


                JSONObject result = faceRecognitionUtil.searchFaces(imgBase64, groupId);

                JSONObject response = result.getJSONObject("result");

                if (result.getIntValue("error_code") != 0) {
                    log.info("1");
                    return Result.error(result.getString("error_msg"));
                }
                JSONArray faces = response.getJSONArray("face_list");

                if (groupId.equals(FaceGropTypeConstant.villageLead)) {

                    log.info("faces2:" + faces.toJSONString());

                    faces.forEach(item -> {

                        HashMap face = (HashMap) item;

                        FaceVo faceVo = new FaceVo();

                        log.info(String.valueOf(face.get("user_list")));
                        ArrayList<HashMap> userList = (ArrayList<HashMap>) face.get("user_list");

                        if (userList.isEmpty()) {
                            return;
                        }

                        String userId = userList.get(0).get("user_id").toString();

                        faceVo.setUserId(userId);
                        faceVo.setUserName(smartVillageLeadService.getById(userId).getName());
                        faceList.add(faceVo);
                    });
                } else if (groupId.equals(FaceGropTypeConstant.villageLead2)) {

                    log.info("faces3:" + faces.toJSONString());

                    faces.forEach(item -> {

                        HashMap face = (HashMap) item;

                        FaceVo faceVo = new FaceVo();

                        log.info(String.valueOf(face.get("user_list")));
                        ArrayList<HashMap> userList = (ArrayList<HashMap>) face.get("user_list");

                        if (userList.isEmpty()) {
                            return;
                        }

                        String userId = userList.get(0).get("user_id").toString();

                        faceVo.setUserId(userId);
                        faceVo.setUserName(smartVillageLead2Service.getById(userId).getName());
                        faceList.add(faceVo);
                    });
                } else if (groupId.equals(FaceGropTypeConstant.GroupEconomy)) {

                    log.info("faces:" + faces.toJSONString());

                    faces.forEach(item -> {

                        HashMap face = (HashMap) item;

                        FaceVo faceVo = new FaceVo();

                        log.info(String.valueOf(face.get("user_list")));
                        ArrayList<HashMap> userList = (ArrayList<HashMap>) face.get("user_list");

                        if (userList.isEmpty()) {
                            return;
                        }

                        String userId = userList.get(0).get("user_id").toString();

                        faceVo.setUserId(userId);
                        faceVo.setUserName(smartGroupEconomyPeopleService.getById(userId).getName());
                        faceList.add(faceVo);
                    });
                }
            }
            return Result.OK(faceList);
        } catch (RuntimeException e) {
            log.info("4");
            return Result.error(e.getMessage());
        }
    }

}
