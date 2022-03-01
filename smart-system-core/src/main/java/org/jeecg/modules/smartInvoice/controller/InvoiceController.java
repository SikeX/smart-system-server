package org.jeecg.modules.smartInvoice.controller;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.smartInvoice.entity.Invoice;
import org.jeecg.modules.test.testAttached.entity.TestAttached;
import org.jeecg.modules.test.testAttached.entity.TestAttachedFile;
import org.jeecg.modules.test.testAttached.service.ITestAttachedFileService;
import org.jeecg.modules.test.testAttached.service.ITestAttachedService;
import org.jeecg.modules.test.testAttached.vo.TestAttachedPage;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
* @Description: 发票识别及验真
* @Author: sike
* @Date:   2022-02-26
* @Version: V1.0
*/
@Api(tags="附件测试主表")
@RestController
@RequestMapping("/invoice")
@Slf4j
public class InvoiceController {

    @Autowired
    private RestTemplate restTemplate;

   /**
    *   发票识别
    *
    * @param imgBase64
    * @return
    */
   @AutoLog(value = "发票识别")
   @ApiOperation(value="发票识别", notes="发票识别")
   @PostMapping(value = "/recognize")
   public Result<?> add(@RequestBody Invoice invoice) {
       log.info(invoice.getImg());
       String host = "https://ocrapi-invoice.taobao.com";
       String path = "/ocrservice/invoice";
       String method = "POST";
       String appcode = "1b638e4b0ead44c593727fffa06a82c8";
       HttpHeaders headers = new HttpHeaders();
       //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
       headers.add("Authorization", "APPCODE " + appcode);
       //根据API的要求，定义相对应的Content-Type
       headers.add("Content-Type", "application/json; charset=UTF-8");
       Map<String, String> querys = new HashMap<String, String>();
//       String body = "{\"img\": " + imgBase64 + "}";

       HttpEntity httpEntity = new HttpEntity<>(invoice, headers);
       JSONObject response = restTemplate.postForObject(host + path, httpEntity,JSONObject.class);
       System.out.println("带header的Post数据请求:" + response);


//       try {
//           /**
//            * 重要提示如下:
//            * HttpUtils请从
//            * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
//            * 下载
//            *
//            * 相应的依赖请参照
//            * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
//            */
//           HttpResponse response = HttpRequest.post(host + path)
//                   .body(body)
//                   .execute();
//           log.info(response.toString());
//           //获取response的body
////           System.out.println(EntityUtils.toString( .getEntity()));
//       } catch (Exception e) {
//           e.printStackTrace();
//       }
       return Result.OK("添加成功！");
   }

}
