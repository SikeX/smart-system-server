package org.jeecg.modules.SmartPaper.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.dto.message.MessageDTO;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
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

   @Autowired
   private ISysBaseAPI sysBaseAPI;
   /**
    *
    * 发布考试
    *
    */
   @ApiOperation(value = "发布考试")
   @PostMapping(value = "/releaseExam/{paperId}")
   public Result releaseExam(@PathVariable("paperId") String paperId,@RequestBody SmartExamVo smartExamVO){
      //System.out.println("AAAAAAAAAAAAAAAAAAAAA");
      //System.out.println(smartExamVO);
      Result res = smartReleaseService.releaseExam(paperId,smartExamVO);
      //发送系统消息提醒
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
      MessageDTO messageDTO = new MessageDTO();
      messageDTO.setTitle("考试提醒");
      messageDTO.setContent("您有新的考试，请在规定时间内完成考试！");
      messageDTO.setFromUser(sysUser.getRealname());
      messageDTO.setToUser(smartExamVO.getUsers());
      messageDTO.setCategory("3");

      sysBaseAPI.sendSysAnnouncementById(messageDTO);
      return res;
   }
   /**
    *
    * 发布考试
    *
    */
   @ApiOperation(value = "发布调查问卷")
   @PostMapping(value = "/releaseSurvey/{paperId}")
   public Result releaseSurvey(@PathVariable("paperId") String paperId,@RequestBody SmartExamVo smartExamVO){
      //System.out.println("AAAAAAAAAAAAAAAAAAAAA");
      //System.out.println(smartExamVO);
      Result res = smartReleaseService.releaseSurvey(paperId,smartExamVO);
      //发送系统消息提醒
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
      MessageDTO messageDTO = new MessageDTO();
      messageDTO.setTitle("调查问卷提醒");
      messageDTO.setContent("您有新的调查问卷，请在规定时间内完成调查问卷！");
      messageDTO.setFromUser(sysUser.getRealname());
      messageDTO.setToUser(smartExamVO.getUsers());
      messageDTO.setCategory("3");

      sysBaseAPI.sendSysAnnouncementById(messageDTO);
      return res;
   }

}
