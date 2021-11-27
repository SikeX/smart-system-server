package org.jeecg.modules.SmartPaper.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.SmartPaper.entity.SmartExamPeople;
import org.jeecg.modules.SmartPaper.entity.SmartSubmit;
import org.jeecg.modules.SmartPaper.service.ISmartExamService;
import org.jeecg.modules.SmartPaper.vo.SmartSubmitExamVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
* @Description: 考试表
* @Author: jeecg-boot
* @Date:   2021-11-25
* @Version: V1.0
*/
@Api(tags="考试表")
@RestController
@RequestMapping("/SmartPaper/smartExam")
@Slf4j
public class SmartExamController extends JeecgController<SmartExamPeople, ISmartExamService> {
   @Autowired
   private ISmartExamService smartExamService;
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

}
