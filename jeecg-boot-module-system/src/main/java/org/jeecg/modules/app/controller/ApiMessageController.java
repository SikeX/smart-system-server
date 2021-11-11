package org.jeecg.modules.app.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.app.model.MessageModel;
import org.jeecg.modules.app.service.IApiMessageService;
import org.jeecg.modules.system.entity.SysAnnouncement;
import org.jeecg.modules.system.entity.SysAnnouncementSend;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysAnnouncementSendService;
import org.jeecg.modules.system.service.ISysAnnouncementService;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 消息提醒的app接口
 * @Author: CabbSir cabbsir@gmail.com
 * @Date: 2021-11-06
 * @Version: V1.0
 */
@Api(tags = "app 消息接口")
@RestController
@RequestMapping("/api/client")
@Slf4j
public class ApiMessageController extends ApiBaseController {
    @Autowired
    private IApiMessageService apiMessageService;
    @Autowired
    private ISysAnnouncementSendService sysAnnouncementSendService;
    @Autowired
    private ISysAnnouncementService sysAnnouncementService;
    @Autowired
    private ISysUserService sysUserService;

    /**
     * 功能 获取我的消息
     *
     * @param
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/messages")
    public Result<IPage<MessageModel>> showMessage(MessageModel announcementSendModel,
                                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Result<IPage<MessageModel>> result = new Result<>();
        // @TODO 这里暂定共用一个普通用户登录，上线之后如何更改再商讨
        String userId = "1457591503827329026"; // client用户当前id
        announcementSendModel.setUserId(userId);
        announcementSendModel.setPageNo((pageNo - 1) * pageSize);
        announcementSendModel.setPageSize(pageSize);
        Page<MessageModel> pageList = new Page<>(pageNo, pageSize);
        pageList = apiMessageService.getMyAnnouncementSendPage(pageList, announcementSendModel);
        result.setResult(pageList);
        result.setSuccess(true);
        return result;
    }


    /**
     * @return
     * @功能：补充用户数据，只返回未读消息数量
     */
    @RequestMapping(value = "/messages/sync", method = RequestMethod.GET)
    public Result<Map<String, Object>> asyncMessage(@RequestParam Map<String, String> params) {
        Result<Map<String, Object>> result = new Result<Map<String, Object>>();
        // 首先校验参数是否都存在
        String paramList = "clientIp|androidId|appVersion|mac|clientId|token|sign";
        if (!super.checkParams(params, paramList)) {
            log.info("参数列表错误");
            return null;
        }
        // 校验签名
        if (!super.checkSign(params)) {
            log.info("签名错误");
            return null;
        }
        // 这里直接使用传过来的id
        String userId = params.get("clientId");
        SysUser sysUser = sysUserService.queryById(userId);

        // 1.将系统消息补充到用户通告阅读标记表中
        LambdaQueryWrapper<SysAnnouncement> querySaWrapper = new LambdaQueryWrapper<SysAnnouncement>();
        querySaWrapper.eq(SysAnnouncement::getMsgType, CommonConstant.MSG_TYPE_ALL); // 全部人员
        querySaWrapper.eq(SysAnnouncement::getDelFlag, CommonConstant.DEL_FLAG_0.toString());  // 未删除
        querySaWrapper.eq(SysAnnouncement::getSendStatus, CommonConstant.HAS_SEND); //已发布
        querySaWrapper.ge(SysAnnouncement::getEndTime, sysUser.getCreateTime()); //新注册用户不看结束通知
//        update-begin--Author:liusq  Date:20210108 for：[JT-424] 【开源issue】bug处理--------------------
        querySaWrapper.notInSql(SysAnnouncement::getId, "select annt_id from sys_announcement_send where user_id='" + userId + "'");
        //update-begin--Author:liusq  Date:20210108  for： [JT-424] 【开源issue】bug处理--------------------
        log.info(querySaWrapper.toString() + "——————————————————————————————————————");
        List<SysAnnouncement> announcements = sysAnnouncementService.list(querySaWrapper);
        log.info(announcements.size() + "——————————————————————要补充的消息个数");
        if (announcements.size() > 0) {
            for (SysAnnouncement announcement : announcements) {
                //update-begin--Author:wangshuai  Date:20200803  for： 通知公告消息重复LOWCOD-759--------------------
                //因为websocket没有判断是否存在这个用户，要是判断会出现问题，故在此判断逻辑
                LambdaQueryWrapper<SysAnnouncementSend> query = new LambdaQueryWrapper<>();
                query.eq(SysAnnouncementSend::getAnntId, announcement.getId());
                query.eq(SysAnnouncementSend::getUserId, userId);
                SysAnnouncementSend one = sysAnnouncementSendService.getOne(query);
                if (null == one) {
                    SysAnnouncementSend announcementSend = new SysAnnouncementSend();
                    announcementSend.setAnntId(announcement.getId());
                    announcementSend.setUserId(userId);
                    announcementSend.setReadFlag(CommonConstant.NO_READ_FLAG);
                    sysAnnouncementSendService.save(announcementSend);
                }
                //update-end--Author:wangshuai  Date:20200803  for： 通知公告消息重复LOWCOD-759------------
            }
        }
        // 2.查询用户未读的系统消息
        Page<SysAnnouncement> anntMsgList = new Page<SysAnnouncement>(0, 5);
        anntMsgList = sysAnnouncementService.querySysCementPageByUserId(anntMsgList,userId,"1");//通知公告消息
        Page<SysAnnouncement> sysMsgList = new Page<SysAnnouncement>(0, 5);
        sysMsgList = sysAnnouncementService.querySysCementPageByUserId(sysMsgList,userId,"2");//系统消息
        Map<String, Object> sysMsgMap = new HashMap<String, Object>();
        // 不需要传过去具体的列表
//        sysMsgMap.put("sysMsgList", sysMsgList.getRecords());
        sysMsgMap.put("alertNum", sysMsgList.getTotal());
//        sysMsgMap.put("anntMsgList", anntMsgList.getRecords());
        sysMsgMap.put("commonNum", anntMsgList.getTotal());
        result.setSuccess(true);
        result.setResult(sysMsgMap);
        return result;
    }
}
