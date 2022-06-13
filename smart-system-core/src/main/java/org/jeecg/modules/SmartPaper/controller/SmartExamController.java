package org.jeecg.modules.SmartPaper.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.SmartPaper.entity.SmartPeople;
import org.jeecg.modules.SmartPaper.service.ISmartExamService;
import org.jeecg.modules.SmartPaper.service.ISmartSurveyService;
import org.jeecg.modules.SmartPaper.vo.SmartSubmitExamVo;
import org.jeecg.modules.SmartPaper.vo.SmartSubmitSurveyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
* @Description: 考试表
* @Author: jeecg-boot
* @Date:   2021-11-25
* @Version: V1.0
*/
@Api(tags="考试表")
@RestController
@RequestMapping("/SmartPaper/smartSubmit")
@Slf4j
public class SmartExamController extends JeecgController<SmartPeople, ISmartExamService> {
   @Autowired
   private ISmartExamService smartExamService;
   @Autowired
   private ISmartSurveyService smartSurveyService;
   
   /**
    *
    * 提交试卷
    *
    */
   @ApiOperation(value = "提交试卷")
   @PostMapping(value = "/submitTestPaper" )
   public Result submitTestPaper(@RequestBody SmartSubmitExamVo smartSubmitExamVO){
      System.out.println(smartSubmitExamVO);
      Result res = smartExamService.submitTestPaper(smartSubmitExamVO);
      return res;
   }

   /**
    *
    * 提交调查问卷
    *
    */
   @ApiOperation(value = "提交调查问卷")
   @PostMapping(value = "/submitTestSurvey" )
   public Result submitTestSurvey(@RequestBody SmartSubmitSurveyVo smartSubmitSurveyVO){
      //System.out.println(smartSubmitSurveyVO);
      Result res = smartSurveyService.submitTestSurvey(smartSubmitSurveyVO);
      return res;
   }

   /**
    *
    * 提交三员+调查问卷
    *
    */
   @ApiOperation(value = "提交三员+调查问卷")
   @PostMapping(value = "/submitTriPreSurvey" )
   public Result submitTriPreSurvey(@RequestBody SmartSubmitSurveyVo smartSubmitSurveyVO){
      System.out.println(smartSubmitSurveyVO);
      Result res = smartSurveyService.submitTriPreSurvey(smartSubmitSurveyVO);
      return res;
   }
}
