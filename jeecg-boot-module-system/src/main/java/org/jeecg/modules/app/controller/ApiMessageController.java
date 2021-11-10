package org.jeecg.modules.app.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.app.model.MessageModel;
import org.jeecg.modules.app.service.IApiMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
