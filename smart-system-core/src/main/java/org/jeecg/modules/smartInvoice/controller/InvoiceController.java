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
import org.jeecg.modules.smartInvoice.vo.InvoiceResult;
import org.jeecg.modules.test.testAttached.entity.TestAttached;
import org.jeecg.modules.test.testAttached.entity.TestAttachedFile;
import org.jeecg.modules.test.testAttached.service.ITestAttachedFileService;
import org.jeecg.modules.test.testAttached.service.ITestAttachedService;
import org.jeecg.modules.test.testAttached.vo.TestAttachedPage;
import org.jeecg.modules.utils.ImageUtils;
import org.jeecg.modules.utils.InvoiceUtil;
import org.jeecg.modules.utils.UrlUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${jeecg.fileBaseUrl}")
    private String fileBaseUrl;

    /**
     * 发票识别及验真
     * @param invoice
     * @return
     */
   @AutoLog(value = "发票识别")
   @ApiOperation(value="发票识别", notes="发票识别")
   @PostMapping(value = "/recognize")
   public Result<?> add(@RequestBody Invoice invoice) {

       String imgPath = invoice.getImg();

       String imgBase64 = ImageUtils.getBase64ByImgUrl(UrlUtil.urlEncodeChinese( fileBaseUrl + imgPath));

       InvoiceResult invoiceResult = new InvoiceResult();

       JSONObject recgnizeObject = new JSONObject();

       try {
           JSONObject response = InvoiceUtil.recognize(imgBase64);
           recgnizeObject.put("success", true);
           recgnizeObject.put("data", response);
           invoiceResult.setInvoiceData(recgnizeObject);
           invoiceResult = this.verify(response, invoiceResult);
           return Result.ok(invoiceResult);
       } catch (Exception e) {
           String errorMessage = e.getMessage();
           errorMessage = errorMessage.substring(errorMessage.indexOf("{"), errorMessage.lastIndexOf("}") + 1);
           JSONObject errorJson = JSONObject.parseObject(errorMessage);
           recgnizeObject.put("success", false);
           recgnizeObject.put("data", errorJson.getString("error_msg"));
           invoiceResult.setInvoiceData(recgnizeObject);
           return Result.ok(invoiceResult);
       }

   }

   public InvoiceResult verify(JSONObject response, InvoiceResult invoiceResult) {
       JSONObject verifyObject = new JSONObject();
       try {
           String fpdm = response.getJSONObject("data").getString("发票代码");
           String fphm = response.getJSONObject("data").getString("发票号码");
           String kprq = response.getJSONObject("data").getString("开票日期");
           String noTaxAmount = response.getJSONObject("data").getString("不含税金额");
           String checkCode = response.getJSONObject("data").getString("校验码");

           JSONObject verificateResult = InvoiceUtil.verificate(fpdm, fphm, kprq, noTaxAmount, checkCode);
           verifyObject.put("success", true);
           verifyObject.put("data", verificateResult);
           invoiceResult.setVerificationData(verifyObject);
           return invoiceResult;
       } catch (Exception e) {
           String errorMessage = e.getMessage();
           errorMessage = errorMessage.substring(errorMessage.indexOf("{"), errorMessage.lastIndexOf("}") + 1);
           JSONObject errorJson = JSONObject.parseObject(errorMessage);
           verifyObject.put("success", false);
           verifyObject.put("data", errorJson.getString("message"));
           invoiceResult.setVerificationData(verifyObject);
           return invoiceResult;
       }
   }


}
