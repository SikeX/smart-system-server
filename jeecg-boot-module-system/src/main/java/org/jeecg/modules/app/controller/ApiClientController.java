package org.jeecg.modules.app.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.minio.credentials.Jwt;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.CommonAPI;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.*;
import org.jeecg.modules.app.entity.AppUser;
import org.jeecg.modules.app.entity.WXUser;
import org.jeecg.modules.app.service.IApiClientService;
import org.jeecg.modules.app.util.Wechat;
import org.jeecg.modules.app.util.WechatConfig;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysTenant;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.model.AnnouncementSendModel;
import org.jeecg.modules.system.model.SysLoginModel;
import org.jeecg.modules.system.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;
import java.util.*;

/**
 * @Description: 改为微信小程序功能接口
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
    private ISysUserService sysUserService;
    @Autowired
    private RedisUtil redisUtil;
    @Resource
    private BaseCommonService baseCommonService;
    @Autowired
    private ISysBaseAPI sysBaseAPI;
    @Autowired
    private ISysAnnouncementSendService sysAnnouncementSendService;
    @Autowired
    private WechatConfig wechatConfig;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private CommonAPI commonAPI;

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
                    ACCOUNT_ACTIVE_STATUS, now, now, appUser.getSysUserId());
            apiClientService.updateById(newData);
        } else {
            appUser = new AppUser(params.get("androidId"), params.get("clientIp"), params.get("mac"),
                    params.get("appVersion"), params.get("brand"), params.get("model"), now,
                    ACCOUNT_ACTIVE_STATUS, now, now);
            appUser.setClientId(UUIDGenerator.generate());
            id = apiClientService.insert(appUser);
        }
        appUser = apiClientService.queryById(id);
        SysUser sysUser = null;
        if (appUser.getSysUserId() == null) {
            // 如果没有关联系统用户，那么注册
            sysUser = register(appUser, null, 1);
            if (sysUser == null) {
                log.error("注册新用户失败，客户端用户数据如下 " + appUser);
                return Result.OK(appUser); // 由于token和sys_user_id为空，客户端根据这个字段判断是否注册成功，并且返回提示
            }
            appUser.setSysUserId(sysUser.getId());
        } else {
            sysUser = sysUserService.queryById(appUser.getSysUserId());
        }
        // 自动登录并且返回token
        Result<?> loginResult = autoLogin(sysUser);
        if (loginResult.isSuccess()) {
            appUser.setToken(loginResult.getResult().toString());
        }
        return Result.OK(loginResult.getMessage(), appUser);
    }

    /**
     * 设置默认用户名 android_user_ + id
     * 设置默认密码 123456
     *
     * @param userType 1-安卓用户 2-微信用户
     */
    public SysUser register(AppUser appUser, WXUser wxUser, int userType) {
        // 激活成功之后自动在平台注册用户
        try {
            SysUser sysUser = new SysUser();
            sysUser.setId(UUIDGenerator.generate());
            if (userType == 1) {
                sysUser.setUsername("android_user_" + appUser.getId());
                sysUser.setRealname("安卓用户_" + appUser.getId());
                sysUser.setClientId(appUser.getClientId());
            } else if (userType == 2) {
                sysUser.setUsername("weixin_user_" + wxUser.getId());
                sysUser.setRealname("微信用户_" + wxUser.getId());
                sysUser.setClientId(wxUser.getWxOpenId());
            }
            sysUser.setPeopleType("4");
            sysUser.setCreateTime(new Date());//设置创建时间
            sysUser.setUpdateTime(new Date());//设置更新时间
            String salt = oConvertUtils.randomGen(8);
            sysUser.setSalt(salt);
            String passwordEncode = PasswordUtil.encrypt(sysUser.getUsername(), "123456", salt);
            sysUser.setPassword(passwordEncode);
            sysUser.setStatus(1);
            sysUser.setDelFlag(CommonConstant.DEL_FLAG_0);
            // 保存用户走一个service 保证事务
            if (userType == 1) {
                sysUserService.saveUserFromClient(sysUser, "mw7vfrjgbj2e8tdhaulnvz6e1oz4dgws", appUser.getId(), userType);
            } else if (userType == 2) {
                sysUserService.saveUserFromClient(sysUser, "mw7vfrjgbj2e8tdhaulnvz6e1oz4dgws", wxUser.getId(), userType);
            }
            return sysUser;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * app端输入用户名密码登录，同时更换绑定用户操作也在这里
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "客户端-登录接口", notes = "客户端-登录")
    @PostMapping(value = "/login")
    public Result<?> login(@RequestParam Map<String, String> params) {
        // 首先校验参数是否都存在
        String paramList = "clientIp|androidId|appVersion|mac|sign|username|password";
        if (!super.checkParams(params, paramList)) {
            return Result.error("参数列表错误");
        }
        // 校验签名
        if (!super.checkSign(params)) {
            return Result.error("签名错误");
        }
        String username = params.get("username");
        String password = params.get("password");

        //校验用户有效性，首先根据androidId查询出关联的平台用户id，然后根据username查平台用户id，相同直接登录，不同则修改关联id
        AppUser appUser = apiClientService.queryByAndroidId(params.get("androidId"));

        // 先验证是否能登陆成功
        //1. 校验用户是否有效
        Result<JSONObject> result = new Result<>();
        SysUser sysUser = sysUserService.getUserByName(username);
        result = sysUserService.checkUserIsEffective(sysUser);
        if (!result.isSuccess()) {
            return result;
        }

        //2. 校验用户名或密码是否正确
        String userpassword = PasswordUtil.encrypt(username, password, sysUser.getSalt());
        String syspassword = sysUser.getPassword();
        if (!syspassword.equals(userpassword)) {
            result.error500("用户名或密码错误");
            return result;
        }

        // 未关联或者关联用户不同，默认视作换绑账户
        if (appUser.getSysUserId() == null || !Objects.equals(appUser.getSysUserId(), sysUser.getId())) {
            appUser.setSysUserId(sysUser.getId());
            appUser.setMtime((int) System.currentTimeMillis());
            apiClientService.updateById(appUser);
        }

        JSONObject obj = new JSONObject();
        //用户登录信息
        obj.put("userInfo", sysUser);

        // 生成token
        String token = JwtUtil.sign(username, syspassword);
        // 设置超时时间
        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
        redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, 24 * 60 * 60);

        //token 信息
        obj.put("token", token);
        result.setResult(obj);
        result.setSuccess(true);
        result.setCode(200);
        baseCommonService.addLog("用户名: " + username + ",登录成功[移动端]！", CommonConstant.LOG_TYPE_1, null);
        return result;
    }

    /**
     * app进入自动登录
     *
     * @param sysUser
     * @return 返回值是token
     */
    public Result<?> autoLogin(SysUser sysUser) {
        Result<JSONObject> result;
        String username = sysUser.getUsername();
        String password = sysUser.getPassword();

        //1. 校验用户是否有效
        SysUser databaseUser = sysUserService.getUserByName(username);
        result = sysUserService.checkUserIsEffective(databaseUser);
        if (!result.isSuccess()) {
            return result;
        }

        // 生成token
        String token = JwtUtil.sign(username, password);
        // 2021-11-28 改为 设置超时时间为1h
        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
        redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, 60 * 60);

        //token 信息
        baseCommonService.addLog("用户名: " + username + ",登录成功[移动端]！", CommonConstant.LOG_TYPE_1, null);
        return Result.OK(token);
    }

    /**
     * app端登出
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "客户端-登出接口", notes = "客户端-登出")
    @PostMapping(value = "/logout")
    public Result<?> logout(@RequestParam Map<String, String> params) {
        // 首先校验参数是否都存在
        String paramList = "clientIp|androidId|appVersion|mac|sign|clientId|token";
        if (!super.checkParams(params, paramList)) {
            return Result.error("参数列表错误");
        }
        // 校验签名
        if (!super.checkSign(params)) {
            return Result.error("签名错误");
        }
        //用户退出逻辑
        String token = params.get("token").replace(CommonConstant.PREFIX_USER_TOKEN, "");
        if (oConvertUtils.isEmpty(token)) {
            return Result.error("退出登录失败");
        }
        String username = JwtUtil.getUsername(token);
        LoginUser sysUser = sysBaseAPI.getUserByName(username);
        if (sysUser != null) {
            baseCommonService.addLog("用户名: " + sysUser.getRealname() + ",退出成功！", CommonConstant.LOG_TYPE_1, null, sysUser);
            log.info(" 用户名:  " + sysUser.getRealname() + ",退出成功！ ");
            //清空用户登录Token缓存
            redisUtil.del(CommonConstant.PREFIX_USER_TOKEN + token);
            //清空用户登录Shiro权限缓存
            redisUtil.del(CommonConstant.PREFIX_USER_SHIRO_CACHE + sysUser.getId());
            //清空用户的缓存信息（包括部门信息），例如sys:cache:user::<username>
            redisUtil.del(String.format("%s::%s", CacheConstant.SYS_USERS_CACHE, sysUser.getUsername()));
            //调用shiro的logout
            SecurityUtils.getSubject().logout();
            return Result.OK("退出登录成功");
        } else {
            return Result.error("Token无效");
        }
    }


    /**
     * 获取用户信息，显示在app中
     *
     * @param params
     * @return
     */
    @GetMapping(value = "/info")
    public Result<?> userInfo(@RequestParam Map<String, String> params) {
        Result<IPage<AnnouncementSendModel>> result = new Result<>();
        // 首先校验参数是否都存在
        String paramList = "clientIp|androidId|appVersion|mac|sign|clientId|token";
        if (!super.checkParams(params, paramList)) {
            return Result.error("参数列表错误");
        }
        // 校验签名
        if (!super.checkSign(params)) {
            return Result.error("签名错误");
        }
        // 查
        SysUser sysUser = sysUserService.queryById(params.get("clientId"));
        AnnouncementSendModel announcementSendModel = new AnnouncementSendModel();
        announcementSendModel.setUserId(sysUser.getId());
        Page<AnnouncementSendModel> pageList = new Page<>(1, 1);
        String type = "msg";
        pageList = sysAnnouncementSendService.getMyAnnouncementSendPage(pageList, announcementSendModel, type);
        result.setResult(pageList);
        result.setSuccess(true);
        result.setMessage(sysUser.getUsername());
        return result;
    }

    @PostMapping(value = "/phone")
    public Result<?> parsePhoneNumber(@RequestParam("encryptedData") String encryptedData,
                                      @RequestParam("iv") String iv,
                                      @RequestParam("sessionKey") String sessionKey,
                                      @RequestParam("token") String token) {
        // 解析出phone number
        String jsonString = Wechat.decrypt(wechatConfig.getAppid(), encryptedData, sessionKey, iv);
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        // 根据sessionKey更新字段
        WXUser wxUser = apiClientService.queryWxUserBySessionKey(sessionKey);
        wxUser.setPhone(jsonObject.getString("purePhoneNumber"));
        // 查询系统中是否已经有这个phone的用户了，如果有直接绑定，没有再继续
        SysUser sysUser = sysUserService.queryByPhone(jsonObject.getString("purePhoneNumber"));
        if (sysUser != null) {
            apiClientService.updateWxUserSysUserIdById(wxUser.getId(), sysUser.getId());
            wxUser.setSysUserId(sysUser.getId());
            wxUser.setPeopleType(sysUser.getPeopleType());
            Result<?> loginResult = autoLogin(sysUser);
            if (loginResult.isSuccess()) {
                wxUser.setToken(loginResult.getResult().toString());
            }
        } else {
            wxUser.setToken(token);
            wxUser.setPeopleType("4");
        }
        if (apiClientService.updateWxUserPhoneById(wxUser.getId(), wxUser.getSysUserId(), jsonObject.getString("purePhoneNumber"))) {
            return Result.OK(wxUser);
        } else {
            return Result.error("更新错误");
        }
    }

    @GetMapping(value = "/wxlogin")
    public Result<?> wxLogin(@RequestParam("code") String code) {
        // 判断code
        if (code == null || code.equals("")) {
            return Result.error("code错误");
        }
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid={appid}&secret={secret}&js_code={code}&grant_type=authorization_code";
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("appid", wechatConfig.getAppid());
        requestMap.put("secret", wechatConfig.getSecret());
        requestMap.put("code", code);

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class, requestMap);
        JSONObject jsonObject = JSONObject.parseObject(responseEntity.getBody());
        String openId = jsonObject.getString("openid");
        String session_key = jsonObject.getString("session_key");

        // 判断登录是否成功
        if (openId == null || session_key == null) {
            return Result.error("code错误");
        }

        // 校验通过，先查询是否已经存在用户，根据openId查询
        WXUser wxUser = apiClientService.queryWxUserByOpenId(openId);
        int now = (int) (System.currentTimeMillis() / 1000);
        int id;
        if (wxUser != null) {
            // 更新字段
            id = wxUser.getId();
            WXUser updateData = new WXUser(id, session_key, now, now);
            apiClientService.updateWxUserById(updateData);
        } else {
            wxUser = new WXUser(openId, session_key, now, ACCOUNT_ACTIVE_STATUS, now, now, "4");
            apiClientService.insertWxUser(wxUser);
            id = apiClientService.queryWxUserByOpenId(openId).getId();
        }

        wxUser = apiClientService.queryWxUserById(id);
        SysUser sysUser = null;
        if (wxUser.getSysUserId() == null) {
            // 如果没有关联系统用户，那么注册
            sysUser = register(null, wxUser, 2);
            if (sysUser == null) {
                return Result.OK(wxUser); // 由于token和sys_user_id为空，客户端根据这个字段判断是否注册成功，并且返回提示
            }
            wxUser.setSysUserId(sysUser.getId());
        } else {
            sysUser = sysUserService.queryById(wxUser.getSysUserId());
        }
        wxUser.setPeopleType(sysUser.getPeopleType());
        // 自动登录并且返回token
        Result<?> loginResult = autoLogin(sysUser);
        if (loginResult.isSuccess()) {
            wxUser.setToken(loginResult.getResult().toString());
        }
        return Result.OK(loginResult.getMessage(), wxUser);
    }

    /**
     * 微信小程序端输入用户名密码登录，同时更换绑定用户操作也在这里
     *
     * @return
     * @Deprecate 2021-12-05 弃用这个方法，不会调用
     */
    @ApiOperation(value = "微信-登录接口", notes = "微信小程序端-登录")
    @PostMapping(value = "/wx/login")
    public Result<?> wxManualLogin(@RequestParam("username") String username,
                                   @RequestParam("password") String password,
                                   @RequestParam("openId") String openId) {
        //校验用户有效性，首先根据openId查询出关联的平台用户id，然后根据username查平台用户id，相同直接登录，不同则修改关联id
        WXUser wxUser = apiClientService.queryWxUserByOpenId(openId);

        // 先验证是否能登陆成功
        //1. 校验用户是否有效
        Result<JSONObject> result = new Result<>();
        SysUser sysUser = sysUserService.getUserByName(username);
        wxUser.setPeopleType(sysUser.getPeopleType());
        result = sysUserService.checkUserIsEffective(sysUser);
        if (!result.isSuccess()) {
            return result;
        }

        //2. 校验用户名或密码是否正确
        String userpassword = PasswordUtil.encrypt(username, password, sysUser.getSalt());
        String syspassword = sysUser.getPassword();
        if (!syspassword.equals(userpassword)) {
            result.error500("用户名或密码错误");
            return result;
        }

        // 未关联或者关联用户不同，默认视作换绑账户
        if (!Objects.equals(wxUser.getSysUserId(), sysUser.getId())) {
            // 先将之前的手机号取消绑定
            SysUser old = sysUserService.queryById(wxUser.getSysUserId());
            old.setPhone(null);
            sysUserService.updateById(old);
            wxUser.setSysUserId(sysUser.getId());
            wxUser.setPeopleType(sysUser.getPeopleType());
            wxUser.setMtime((int) System.currentTimeMillis());
            apiClientService.updateWxUserSysUserIdById(wxUser.getId(), sysUser.getId());
            // 此时一定已经有手机号了，更新到sys_user表
            sysUser.setPhone(wxUser.getPhone());
            sysUserService.updateById(sysUser);
        }

        JSONObject obj = new JSONObject();
        //用户登录信息
        obj.put("userInfo", wxUser);

        // 生成token
        String newToken = JwtUtil.sign(username, syspassword);
        // 设置超时时间
        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + newToken, newToken);
        redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + newToken, 60 * 60);
        wxUser.setToken(newToken);
        //token 信息
        result.setResult(obj);
        result.setSuccess(true);
        result.setCode(200);
        baseCommonService.addLog("用户名: " + username + ",登录成功[移动端]！", CommonConstant.LOG_TYPE_1, null);
        return result;
    }

    @GetMapping(value = "/user/info")
    @ApiOperation(value = "微信-获取平台用户信息", notes = "微信小程序端-获取用户详细信息")
    public Result<?> wxGetUserInfo(@RequestParam("token") String token) {
        String username = JwtUtil.getUsername(token);
        SysUser user = sysUserService.getUserByName(username);
        return Result.OK(user);
    }

}
