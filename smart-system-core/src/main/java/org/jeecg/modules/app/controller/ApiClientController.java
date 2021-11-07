package org.jeecg.modules.app.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.UUIDGenerator;
import org.jeecg.modules.app.entity.AppUser;
import org.jeecg.modules.app.service.IApiClientService;
import org.jeecg.modules.app.service.impl.ApiClientServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

/**
 * @Description: 客户端的一些功能接口
 * @Author: CabbSir cabbsir@gmail.com
 * @Date: 2021-11-07
 * @Version: V1.0
 */
@Api(tags = "客户端 功能接口")
@RestController
@RequestMapping("/api/client")
@Slf4j
public class ApiClientController extends ApiBaseController {
    @Autowired
    private IApiClientService apiClientService;

    /**
     * 激活设备接口
     *
     * @param params client_ip android_id app_version mac sign brand model
     * @return
     */
    @ApiOperation(value = "客户端-激活账户接口", notes = "客户端-激活账户")
    @PostMapping(value = "/activate")
    public Result<?> activate(@RequestParam Map<String, String> params) {
        // 首先校验参数是否都存在
        String paramList = "clientIp|androidId|appVersion|mac|sign|brand|model";
        if (!super.checkParams(params, paramList)) {
            return Result.error("参数列表错误");
        }
        // 校验签名
        if (!super.checkSign(params)) {
            return Result.error("签名错误");
        }
        // 签名校验通过，先查询是否已经存在用户，根据androidId查询
        AppUser appUser = apiClientService.queryByAndroidId(params.get("androidId"));
        int now = (int) (System.currentTimeMillis() / 1000);
        int id;
        if (appUser != null) {
            // 更新字段
            id = appUser.getId();
            AppUser newData = new AppUser(id, params.get("androidId"), params.get("clientIp"), params.get("mac"),
                    params.get("appVersion"), params.get("brand"), params.get("model"), now,
                    ACCOUNT_ACTIVE_STATUS, now, now);
            apiClientService.updateById(newData);

        } else {
            appUser = new AppUser(params.get("androidId"), params.get("clientIp"), params.get("mac"),
                    params.get("appVersion"), params.get("brand"), params.get("model"), now,
                    ACCOUNT_ACTIVE_STATUS, now, now);
            appUser.setClientId(UUIDGenerator.generate());
            id = apiClientService.insert(appUser);
        }
        appUser = apiClientService.queryById(id);
        return Result.OK(appUser);
    }
}
