package org.jeecg.modules.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.*;
import org.jeecg.modules.app.entity.AppUser;
import org.jeecg.modules.app.service.IApiClientService;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysTenant;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.model.SysLoginModel;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecg.modules.system.service.ISysTenantService;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    private ISysTenantService sysTenantService;
    @Autowired
    private ISysDictService sysDictService;

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
        appUser.setToken("");
        autoLogin();
        System.out.println(autoLogin());
        return Result.OK(appUser);
    }

    public Result<JSONObject> autoLogin(){
        Result<JSONObject> result = new Result<JSONObject>();
        String username = "client_common";
        //1. 校验用户是否有效
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername,username);
        SysUser sysUser = sysUserService.getOne(queryWrapper);

        //用户登录信息
        userInfo(sysUser, result);
        LoginUser loginUser = new LoginUser();
        BeanUtils.copyProperties(sysUser, loginUser);
        return result;
    }

    private Result<JSONObject> userInfo(SysUser sysUser, Result<JSONObject> result) {
        String syspassword = sysUser.getPassword();
        String username = sysUser.getUsername();
        // 获取用户部门信息
        JSONObject obj = new JSONObject();
        List<SysDepart> departs = sysDepartService.queryUserDeparts(sysUser.getId());
        obj.put("departs", departs);
        if (departs == null || departs.size() == 0) {
            obj.put("multi_depart", 0);
        } else if (departs.size() == 1) {
            sysUserService.updateUserDepart(username, departs.get(0).getOrgCode());
            obj.put("multi_depart", 1);
        } else {
            //查询当前是否有登录部门
            // update-begin--Author:wangshuai Date:20200805 for：如果用戶为选择部门，数据库为存在上一次登录部门，则取一条存进去
            SysUser sysUserById = sysUserService.getById(sysUser.getId());
            if(oConvertUtils.isEmpty(sysUserById.getOrgCode())){
                sysUserService.updateUserDepart(username, departs.get(0).getOrgCode());
            }
            // update-end--Author:wangshuai Date:20200805 for：如果用戶为选择部门，数据库为存在上一次登录部门，则取一条存进去
            obj.put("multi_depart", 2);
        }
        // update-begin--Author:sunjianlei Date:20210802 for：获取用户租户信息
        String tenantIds = sysUser.getRelTenantIds();
        if (oConvertUtils.isNotEmpty(tenantIds)) {
            List<String> tenantIdList = Arrays.asList(tenantIds.split(","));
            // 该方法仅查询有效的租户，如果返回0个就说明所有的租户均无效。
            List<SysTenant> tenantList = sysTenantService.queryEffectiveTenant(tenantIdList);
            if (tenantList.size() == 0) {
                result.error500("与该用户关联的租户均已被冻结，无法登录！");
                return result;
            } else {
                obj.put("tenantList", tenantList);
            }
        }
        // update-end--Author:sunjianlei Date:20210802 for：获取用户租户信息
        // 生成token
        String token = JwtUtil.sign(username, syspassword);
        // 设置token缓存有效时间
        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
        redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME * 2 / 1000);
        obj.put("token", token);
        obj.put("userInfo", sysUser);
        obj.put("sysAllDictItems", sysDictService.queryAllDictItems());
        result.setResult(obj);
        result.success("登录成功");
        return result;
    }
}
