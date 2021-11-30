package org.jeecg.modules.SmartPaper.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.SmartPaper.entity.SmartExamInformation;
import org.jeecg.modules.SmartPaper.service.ISmartReleaseService;
import org.jeecg.modules.SmartPaper.vo.SmartExamVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
* @Description: 考试表
* @Author: jeecg-boot
* @Date:   2021-11-25
* @Version: V1.0
*/
@RestController
@RequestMapping("/SmartExam/smartRelease")
@Slf4j
public class SmartReleaseController extends JeecgController<SmartExamInformation, ISmartReleaseService> {
   @Autowired
   private ISmartReleaseService smartReleaseService;
   /**
    *
    * 发布考试
    *
    */
   @ApiOperation(value = "发布考试")
   @PostMapping(value = "/releaseExam/{paperId}" )
   public Result releaseExam(@PathVariable("paperId") String paperId,@RequestBody SmartExamVo smartExamVO){
      System.out.println("AAAAAAAAAAAAAAAAAAAAA");
      System.out.println(smartExamVO);
      Result res = smartReleaseService.releaseExam(paperId,smartExamVO);
      return res;
   }

}
