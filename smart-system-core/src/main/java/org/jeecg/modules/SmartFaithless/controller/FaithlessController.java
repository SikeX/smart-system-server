package org.jeecg.modules.SmartFaithless.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.SmartFaithless.entity.Faithless;
import org.jeecg.modules.SmartFaithless.entity.FaithlessPeople;
import org.jeecg.modules.SmartInnerPartyTalk.entity.SmartInnerPartyTalk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
* @Description: 失信被执行人
* @Author: zxh
* @Date:   2022-02-26
* @Version: V1.0
*/
@Api(tags="失信被执行人查询")
@RestController
@RequestMapping("/faithless")
@Slf4j
public class FaithlessController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${aliyun.appcode}")
    private String appcode;

    /**
     *
     *
     * @param
     * @return
     */
   @AutoLog(value = "失信被执行人查询")
   @ApiOperation(value="失信被执行人查询", notes="失信被执行人查询")
   @PostMapping(value = "/search")
   public Result<?> add(@RequestBody Faithless faithless) throws Exception{
       faithless.setMobile("15313999062");
       System.out.println("执行人:" + faithless);
       String host = "https://jumjokk.market.alicloudapi.com";
       String path = "/personal/disenforcement";
       String method = "POST";
       //Map<String, String> headers = new HashMap<String, String>();
       HttpHeaders headers = new HttpHeaders();
       //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
       headers.add("Authorization", "APPCODE " + appcode);
       //根据API的要求，定义相对应的Content-Type
       headers.add("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
       //headers.add("Content-Type", "application/json; charset=UTF-8");
       Map<String, String> querys = new HashMap<String, String>();
       Map<String, String> bodys = new HashMap<String, String>();
//       bodys.put("idcard", "idcard");
//       bodys.put("realname", "realname");

        try
        {
       // 设置请求参数,application/x-www-form-urlencoded
       MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
       postParameters.add("name", faithless.getRealname());
       postParameters.add("idcard_number", faithless.getIdcard());
       postParameters.add("mobile_number", faithless.getMobile());
       HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(postParameters, headers);
       System.out.println("httpEntity:"+httpEntity);
       JSONObject response = restTemplate.postForObject(host + path, httpEntity,JSONObject.class);
       System.out.println("带header的Post数据请求:" + response);


       List<FaithlessPeople> faithlessPeopleList = new ArrayList<>();

       Integer pageNo = 1;
       Integer pageSize =10;
       Page<FaithlessPeople> page = new Page<FaithlessPeople>(pageNo, pageSize);
       Integer code = response.getInteger("code");
       if(code == 200){
           JSONObject res = response.getJSONObject("data");
           Integer count = Integer.valueOf(res.getString("caseCount"));
           JSONArray jsonArray = res.getJSONArray("caseList");
           if(count != 0){
               for (int i = 0; i < jsonArray.size(); i++) {
                   JSONObject obj = jsonArray.getJSONObject(i);
                   FaithlessPeople p = new FaithlessPeople();
                   p.setIname(obj.getString("iname"));
                   p.setSexname(obj.getString("sexname"));
                   p.setAge(obj.getString("age"));
                   p.setCasecode(obj.getString("casecode"));
                   p.setGistcid(obj.getString("gistcid"));
                   p.setAreaname(obj.getString("areaname"));
                   p.setCourtname(obj.getString("courtname"));
                   p.setRegdate(obj.getString("regdate"));
                   p.setPublishdate(obj.getString("publishdate"));

                   faithlessPeopleList.add(p);

               }
           }
           page.setTotal(count);
           page.setRecords(faithlessPeopleList);
           System.out.println("page:" + page.getRecords());
           return Result.OK(page);
       }else if(code == 400){
           Result res = new Result();
           res.setCode(400);
           res.setMessage("信息有误!");
           res.setSuccess(false);
           return res;
       }
       return Result.error("error！");
        }catch(Exception e){
            return Result.error("系统错误！");
        }

   }

}
