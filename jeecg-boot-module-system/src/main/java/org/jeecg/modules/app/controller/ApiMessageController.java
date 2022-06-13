package org.jeecg.modules.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.WebsocketConst;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.app.service.IApiClientService;
import org.jeecg.modules.message.websocket.WebSocket;
import org.jeecg.modules.system.entity.SysAnnouncement;
import org.jeecg.modules.system.entity.SysAnnouncementSend;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.model.AnnouncementSendModel;
import org.jeecg.modules.system.service.ISysAnnouncementSendService;
import org.jeecg.modules.system.service.ISysAnnouncementService;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.jeecg.common.constant.CommonConstant.ANNOUNCEMENT_SEND_STATUS_1;

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
    private ISysAnnouncementSendService sysAnnouncementSendService;
    @Autowired
    private ISysAnnouncementService sysAnnouncementService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private IApiClientService apiClientService;
    @Resource
    private WebSocket webSocket;

    /**
     * 功能 获取我的消息
     *
     * @param params
     * @return
     */
    @GetMapping(value = "/messages")
    public Result<IPage<AnnouncementSendModel>> showMessage(@RequestParam Map<String, String> params) {
        Result<IPage<AnnouncementSendModel>> result = new Result<>();

        // 首先校验参数是否都存在
        String paramList = "clientIp|androidId|appVersion|mac|clientId|token|_t|column|order|field|pageNo|pageSize|sign";
        if (!super.checkParams(params, paramList)) {
            log.info("参数列表错误");
            return null;
        }
        // 校验签名
        if (!super.checkSign(params)) {
            log.info("签名错误");
            return null;
        }

        // 用自己的用户登录, 直接根据clientId查询
        String userId = params.get("clientId");
        log.info("————————————获取到的clientId是： " + userId);
        int pageNo = Integer.parseInt(params.get("pageNo"));
        int pageSize = Integer.parseInt(params.get("pageSize"));
        AnnouncementSendModel announcementSendModel = new AnnouncementSendModel();
        announcementSendModel.setUserId(userId);
        announcementSendModel.setPageNo((pageNo - 1) * pageSize);
        announcementSendModel.setPageSize(pageSize);
        Page<AnnouncementSendModel> pageList = new Page<>(pageNo, pageSize);
        String type = "msg";
        pageList = sysAnnouncementSendService.getMyAnnouncementSendPage(pageList, announcementSendModel, type);
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
        Result<Map<String, Object>> result = new Result<>();
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
        LambdaQueryWrapper<SysAnnouncement> querySaWrapper = new LambdaQueryWrapper<>();
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
        Page<SysAnnouncement> anntMsgList = new Page<>(0, 5);
        anntMsgList = sysAnnouncementService.querySysCementPageByUserId(anntMsgList, userId, "1");//通知公告消息
        Page<SysAnnouncement> sysMsgList = new Page<>(0, 5);
        sysMsgList = sysAnnouncementService.querySysCementPageByUserId(sysMsgList, userId, "2");//系统消息
        Map<String, Object> sysMsgMap = new HashMap<>();
        // 不需要传过去具体的列表
//        sysMsgMap.put("sysMsgList", sysMsgList.getRecords());
        sysMsgMap.put("alertNum", sysMsgList.getTotal());
//        sysMsgMap.put("anntMsgList", anntMsgList.getRecords());
        sysMsgMap.put("commonNum", anntMsgList.getTotal());
        result.setSuccess(true);
        result.setResult(sysMsgMap);
        return result;
    }

    /**
     * 更新用户系统消息阅读状态
     *
     * @param params
     * @return
     */
    @PostMapping(value = "/messages/read")
    public Result<SysAnnouncementSend> editMessageReadStatusByAnnId(@RequestParam Map<String, String> params) {
        Result<SysAnnouncementSend> result = new Result<>();
        // 首先校验参数是否都存在
        String paramList = "clientIp|androidId|appVersion|mac|clientId|token|sign|anntId";
        if (!super.checkParams(params, paramList)) {
            log.info("参数列表错误");
            return null;
        }
        // 校验签名
        if (!super.checkSign(params)) {
            log.info("签名错误");
            return null;
        }
        String anntId = params.get("anntId");
        // 直接使用传过来的id
        String userId = params.get("clientId");
        LambdaUpdateWrapper<SysAnnouncementSend> updateWrapper = new UpdateWrapper().lambda();
        updateWrapper.set(SysAnnouncementSend::getReadFlag, CommonConstant.HAS_READ_FLAG);
        updateWrapper.set(SysAnnouncementSend::getReadTime, new Date());
        updateWrapper.last("where annt_id ='" + anntId + "' and user_id ='" + userId + "'");
        SysAnnouncementSend announcementSend = new SysAnnouncementSend();
        sysAnnouncementSendService.update(announcementSend, updateWrapper);
        result.setSuccess(true);

        // 进行前端会进行的同步消息，目前不知道用处，后期研究
        syncNotice(anntId);
        return result;
    }


    /**
     * 同步消息
     *
     * @param anntId
     * @return
     * @link SysAnnouncementController/syncNotic()
     */
    public Result<SysAnnouncement> syncNotice(String anntId) {
        Result<SysAnnouncement> result = new Result<>();
        JSONObject obj = new JSONObject();
        if (StringUtils.isNotBlank(anntId)) {
            SysAnnouncement sysAnnouncement = sysAnnouncementService.getById(anntId);
            if (sysAnnouncement == null) {
                result.error500("未找到对应实体");
            } else {
                if (sysAnnouncement.getMsgType().equals(CommonConstant.MSG_TYPE_ALL)) {
                    obj.put(WebsocketConst.MSG_CMD, WebsocketConst.CMD_TOPIC);
                    obj.put(WebsocketConst.MSG_ID, sysAnnouncement.getId());
                    obj.put(WebsocketConst.MSG_TXT, sysAnnouncement.getTitile());
                    obj.put("type", "2");
                    webSocket.sendMessage(obj.toJSONString());
                } else {
                    // 2.插入用户通告阅读标记表记录
                    String userId = sysAnnouncement.getUserIds();
                    if (oConvertUtils.isNotEmpty(userId)) {
                        String[] userIds = userId.substring(0, (userId.length() - 1)).split(",");
                        obj.put(WebsocketConst.MSG_CMD, WebsocketConst.CMD_USER);
                        obj.put(WebsocketConst.MSG_ID, sysAnnouncement.getId());
                        obj.put(WebsocketConst.MSG_TXT, sysAnnouncement.getTitile());
                        obj.put("type", "2");
                        webSocket.sendMessage(userIds, obj.toJSONString());
                    }
                }
            }
        } else {
            obj.put(WebsocketConst.MSG_CMD, WebsocketConst.CMD_TOPIC);
            obj.put(WebsocketConst.MSG_TXT, "批量设置已读");
            webSocket.sendMessage(obj.toJSONString());
        }
        return result;
    }

    /**
     * 通告查看详情页面 返回web页面
     *
     * @param anntId
     * @return
     */
    @GetMapping("/message/detail/{anntId}")
    public ModelAndView showContent(ModelAndView modelAndView, @PathVariable("anntId") String anntId) {
        SysAnnouncement announcement = sysAnnouncementService.getById(anntId);
        if (announcement != null) {
            // 这里在我的消息中只能查看已发布的
            if (ANNOUNCEMENT_SEND_STATUS_1.equals(announcement.getSendStatus())) {
                modelAndView.addObject("data", announcement);
                modelAndView.setViewName("announcement/showContent");
                return modelAndView;
            }
        }
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        return modelAndView;
    }

    /**
     * 功能 获取我的消息 微信小程序端
     */
    @GetMapping(value = "/wx/messages")
    public Result<IPage<AnnouncementSendModel>> showWxMessage(@RequestParam("sysid") String sysUserId,
                                                              @RequestParam("token") String token,
                                                              @RequestParam("pageNo") int pageNo,
                                                              @RequestParam("pageSize") int pageSize) {
        Result<IPage<AnnouncementSendModel>> result = new Result<>();
        // 校验token
        if (JwtUtil.getUsername(token) == null) {
            return null;
        }

        // 用自己的用户登录, 直接根据clientId查询
        log.info("————————————获取到的uid是： " + sysUserId);
        AnnouncementSendModel announcementSendModel = new AnnouncementSendModel();
        announcementSendModel.setUserId(sysUserId);
        announcementSendModel.setPageNo((pageNo - 1) * pageSize);
        announcementSendModel.setPageSize(pageSize);
        Page<AnnouncementSendModel> pageList = new Page<>(pageNo, pageSize);
        String type = "msg";
        pageList = sysAnnouncementSendService.getMyAnnouncementSendPage(pageList, announcementSendModel, type);
        result.setResult(pageList);
        result.setSuccess(true);
        return result;
    }

    /**
     * 更新用户系统消息阅读状态
     * @return
     */
    @PostMapping(value = "/wx/messages/read")
    public Result<SysAnnouncementSend> editMessageReadStatusByAnnIdWx(@RequestParam("sysid") String sysUserId,
                                                                      @RequestParam("token") String token,
                                                                      @RequestParam("anntId") String anntId) {
        Result<SysAnnouncementSend> result = new Result<>();
        // 校验token
        if (JwtUtil.getUsername(token) == null) {
            return null;
        }
        LambdaUpdateWrapper<SysAnnouncementSend> updateWrapper = new UpdateWrapper().lambda();
        updateWrapper.set(SysAnnouncementSend::getReadFlag, CommonConstant.HAS_READ_FLAG);
        updateWrapper.set(SysAnnouncementSend::getReadTime, new Date());
        updateWrapper.last("where annt_id ='" + anntId + "' and user_id ='" + sysUserId + "'");
        SysAnnouncementSend announcementSend = new SysAnnouncementSend();
        sysAnnouncementSendService.update(announcementSend, updateWrapper);
        result.setSuccess(true);

        // 进行前端会进行的同步消息，目前不知道用处，后期研究
        syncNotice(anntId);
        return result;
    }

    /**
     * @return
     * @功能：补充用户数据，只返回未读消息数量
     */
    @GetMapping(value = "/wx/messages/sync")
    public Result<Map<String, Object>> asyncWxMessage(@RequestParam("sysid") String sysUserId,
                                                      @RequestParam("token") String token) {
        Result<Map<String, Object>> result = new Result<>();
        // 校验token
        if (JwtUtil.getUsername(token) == null) {
            return null;
        }
        // 这里直接使用传过来的id
        SysUser sysUser = sysUserService.queryById(sysUserId);

        // 1.将系统消息补充到用户通告阅读标记表中
        LambdaQueryWrapper<SysAnnouncement> querySaWrapper = new LambdaQueryWrapper<>();
        querySaWrapper.eq(SysAnnouncement::getMsgType, CommonConstant.MSG_TYPE_ALL); // 全部人员
        querySaWrapper.eq(SysAnnouncement::getDelFlag, CommonConstant.DEL_FLAG_0.toString());  // 未删除
        querySaWrapper.eq(SysAnnouncement::getSendStatus, CommonConstant.HAS_SEND); //已发布
        querySaWrapper.ge(SysAnnouncement::getEndTime, sysUser.getCreateTime()); //新注册用户不看结束通知
//        update-begin--Author:liusq  Date:20210108 for：[JT-424] 【开源issue】bug处理--------------------
        querySaWrapper.notInSql(SysAnnouncement::getId, "select annt_id from sys_announcement_send where user_id='" + sysUserId + "'");
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
                query.eq(SysAnnouncementSend::getUserId, sysUserId);
                SysAnnouncementSend one = sysAnnouncementSendService.getOne(query);
                if (null == one) {
                    SysAnnouncementSend announcementSend = new SysAnnouncementSend();
                    announcementSend.setAnntId(announcement.getId());
                    announcementSend.setUserId(sysUserId);
                    announcementSend.setReadFlag(CommonConstant.NO_READ_FLAG);
                    sysAnnouncementSendService.save(announcementSend);
                }
                //update-end--Author:wangshuai  Date:20200803  for： 通知公告消息重复LOWCOD-759------------
            }
        }
        // 2.查询用户未读的系统消息
        int num = apiClientService.queryUnreadMessageNumBySysUserId(sysUserId);
        Map<String, Object> sysMsgMap = new HashMap<>();
        // 不需要传过去具体的列表
        sysMsgMap.put("nums", num);
        result.setSuccess(true);
        result.setResult(sysMsgMap);
        return result;
    }
}
