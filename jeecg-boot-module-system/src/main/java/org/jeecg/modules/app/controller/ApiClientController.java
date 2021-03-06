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
 * @Description: ?????????????????????????????????
 * @Author: CabbSir cabbsir@gmail.com
 * @Date: 2021-11-07
 * @Version: V1.0
 */
@Api(tags = "????????? ????????????")
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
     * ??????????????????
     *
     * @param params client_ip android_id app_version mac sign brand model
     * @return
     */
    @ApiOperation(value = "?????????-??????????????????", notes = "?????????-????????????")
    @PostMapping(value = "/activate")
    public Result<?> activate(@RequestParam Map<String, String> params) {
        // ?????????????????????????????????
        String paramList = "clientIp|androidId|appVersion|mac|sign|brand|model";
        if (!super.checkParams(params, paramList)) {
            return Result.error("??????????????????");
        }
        // ????????????
        if (!super.checkSign(params)) {
            return Result.error("????????????");
        }
        // ???????????????????????????????????????????????????????????????androidId??????
        AppUser appUser = apiClientService.queryByAndroidId(params.get("androidId"));
        int now = (int) (System.currentTimeMillis() / 1000);
        int id;
        if (appUser != null) {
            // ????????????
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
            // ?????????????????????????????????????????????
            sysUser = register(appUser, null, 1);
            if (sysUser == null) {
                log.error("??????????????????????????????????????????????????? " + appUser);
                return Result.OK(appUser); // ??????token???sys_user_id?????????????????????????????????????????????????????????????????????????????????
            }
            appUser.setSysUserId(sysUser.getId());
        } else {
            sysUser = sysUserService.queryById(appUser.getSysUserId());
        }
        // ????????????????????????token
        Result<?> loginResult = autoLogin(sysUser);
        if (loginResult.isSuccess()) {
            appUser.setToken(loginResult.getResult().toString());
        }
        return Result.OK(loginResult.getMessage(), appUser);
    }

    /**
     * ????????????????????? android_user_ + id
     * ?????????????????? 123456
     *
     * @param userType 1-???????????? 2-????????????
     */
    public SysUser register(AppUser appUser, WXUser wxUser, int userType) {
        // ?????????????????????????????????????????????
        try {
            SysUser sysUser = new SysUser();
            sysUser.setId(UUIDGenerator.generate());
            if (userType == 1) {
                sysUser.setUsername("android_user_" + appUser.getId());
                sysUser.setRealname("????????????_" + appUser.getId());
                sysUser.setClientId(appUser.getClientId());
            } else if (userType == 2) {
                sysUser.setUsername("weixin_user_" + wxUser.getId());
                sysUser.setRealname("????????????_" + wxUser.getId());
                sysUser.setClientId(wxUser.getWxOpenId());
            }
            sysUser.setPeopleType("4");
            sysUser.setCreateTime(new Date());//??????????????????
            sysUser.setUpdateTime(new Date());//??????????????????
            String salt = oConvertUtils.randomGen(8);
            sysUser.setSalt(salt);
            String passwordEncode = PasswordUtil.encrypt(sysUser.getUsername(), "123456", salt);
            sysUser.setPassword(passwordEncode);
            sysUser.setStatus(1);
            sysUser.setDelFlag(CommonConstant.DEL_FLAG_0);
            // ?????????????????????service ????????????
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
     * app???????????????????????????????????????????????????????????????????????????
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "?????????-????????????", notes = "?????????-??????")
    @PostMapping(value = "/login")
    public Result<?> login(@RequestParam Map<String, String> params) {
        // ?????????????????????????????????
        String paramList = "clientIp|androidId|appVersion|mac|sign|username|password";
        if (!super.checkParams(params, paramList)) {
            return Result.error("??????????????????");
        }
        // ????????????
        if (!super.checkSign(params)) {
            return Result.error("????????????");
        }
        String username = params.get("username");
        String password = params.get("password");

        //????????????????????????????????????androidId??????????????????????????????id???????????????username???????????????id?????????????????????????????????????????????id
        AppUser appUser = apiClientService.queryByAndroidId(params.get("androidId"));

        // ??????????????????????????????
        //1. ????????????????????????
        Result<JSONObject> result = new Result<>();
        SysUser sysUser = sysUserService.getUserByName(username);
        result = sysUserService.checkUserIsEffective(sysUser);
        if (!result.isSuccess()) {
            return result;
        }

        //2. ????????????????????????????????????
        String userpassword = PasswordUtil.encrypt(username, password, sysUser.getSalt());
        String syspassword = sysUser.getPassword();
        if (!syspassword.equals(userpassword)) {
            result.error500("????????????????????????");
            return result;
        }

        // ????????????????????????????????????????????????????????????
        if (appUser.getSysUserId() == null || !Objects.equals(appUser.getSysUserId(), sysUser.getId())) {
            appUser.setSysUserId(sysUser.getId());
            appUser.setMtime((int) System.currentTimeMillis());
            apiClientService.updateById(appUser);
        }

        JSONObject obj = new JSONObject();
        //??????????????????
        obj.put("userInfo", sysUser);

        // ??????token
        String token = JwtUtil.sign(username, syspassword);
        // ??????????????????
        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
        redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, 24 * 60 * 60);

        //token ??????
        obj.put("token", token);
        result.setResult(obj);
        result.setSuccess(true);
        result.setCode(200);
        baseCommonService.addLog("?????????: " + username + ",????????????[?????????]???", CommonConstant.LOG_TYPE_1, null);
        return result;
    }

    /**
     * app??????????????????
     *
     * @param sysUser
     * @return ????????????token
     */
    public Result<?> autoLogin(SysUser sysUser) {
        Result<JSONObject> result;
        String username = sysUser.getUsername();
        String password = sysUser.getPassword();

        //1. ????????????????????????
        SysUser databaseUser = sysUserService.getUserByName(username);
        result = sysUserService.checkUserIsEffective(databaseUser);
        if (!result.isSuccess()) {
            return result;
        }

        // ??????token
        String token = JwtUtil.sign(username, password);
        // 2021-11-28 ?????? ?????????????????????1h
        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
        redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, 60 * 60);

        //token ??????
        baseCommonService.addLog("?????????: " + username + ",????????????[?????????]???", CommonConstant.LOG_TYPE_1, null);
        return Result.OK(token);
    }

    /**
     * app?????????
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "?????????-????????????", notes = "?????????-??????")
    @PostMapping(value = "/logout")
    public Result<?> logout(@RequestParam Map<String, String> params) {
        // ?????????????????????????????????
        String paramList = "clientIp|androidId|appVersion|mac|sign|clientId|token";
        if (!super.checkParams(params, paramList)) {
            return Result.error("??????????????????");
        }
        // ????????????
        if (!super.checkSign(params)) {
            return Result.error("????????????");
        }
        //??????????????????
        String token = params.get("token").replace(CommonConstant.PREFIX_USER_TOKEN, "");
        if (oConvertUtils.isEmpty(token)) {
            return Result.error("??????????????????");
        }
        String username = JwtUtil.getUsername(token);
        LoginUser sysUser = sysBaseAPI.getUserByName(username);
        if (sysUser != null) {
            baseCommonService.addLog("?????????: " + sysUser.getRealname() + ",???????????????", CommonConstant.LOG_TYPE_1, null, sysUser);
            log.info(" ?????????:  " + sysUser.getRealname() + ",??????????????? ");
            //??????????????????Token??????
            redisUtil.del(CommonConstant.PREFIX_USER_TOKEN + token);
            //??????????????????Shiro????????????
            redisUtil.del(CommonConstant.PREFIX_USER_SHIRO_CACHE + sysUser.getId());
            //????????????????????????????????????????????????????????????sys:cache:user::<username>
            redisUtil.del(String.format("%s::%s", CacheConstant.SYS_USERS_CACHE, sysUser.getUsername()));
            //??????shiro???logout
            SecurityUtils.getSubject().logout();
            return Result.OK("??????????????????");
        } else {
            return Result.error("Token??????");
        }
    }


    /**
     * ??????????????????????????????app???
     *
     * @param params
     * @return
     */
    @GetMapping(value = "/info")
    public Result<?> userInfo(@RequestParam Map<String, String> params) {
        Result<IPage<AnnouncementSendModel>> result = new Result<>();
        // ?????????????????????????????????
        String paramList = "clientIp|androidId|appVersion|mac|sign|clientId|token";
        if (!super.checkParams(params, paramList)) {
            return Result.error("??????????????????");
        }
        // ????????????
        if (!super.checkSign(params)) {
            return Result.error("????????????");
        }
        // ???
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
        // ?????????phone number
        String jsonString = Wechat.decrypt(wechatConfig.getAppid(), encryptedData, sessionKey, iv);
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        // ??????sessionKey????????????
        WXUser wxUser = apiClientService.queryWxUserBySessionKey(sessionKey);
        wxUser.setPhone(jsonObject.getString("purePhoneNumber"));
        // ????????????????????????????????????phone??????????????????????????????????????????????????????
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
            return Result.error("????????????");
        }
    }

    @GetMapping(value = "/wxlogin")
    public Result<?> wxLogin(@RequestParam("code") String code) {
        // ??????code
        if (code == null || code.equals("")) {
            return Result.error("code??????");
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

        // ????????????????????????
        if (openId == null || session_key == null) {
            return Result.error("code??????");
        }

        // ?????????????????????????????????????????????????????????openId??????
        WXUser wxUser = apiClientService.queryWxUserByOpenId(openId);
        int now = (int) (System.currentTimeMillis() / 1000);
        int id;
        if (wxUser != null) {
            // ????????????
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
            // ?????????????????????????????????????????????
            sysUser = register(null, wxUser, 2);
            if (sysUser == null) {
                return Result.OK(wxUser); // ??????token???sys_user_id?????????????????????????????????????????????????????????????????????????????????
            }
            wxUser.setSysUserId(sysUser.getId());
        } else {
            sysUser = sysUserService.queryById(wxUser.getSysUserId());
        }
        wxUser.setPeopleType(sysUser.getPeopleType());
        // ????????????????????????token
        Result<?> loginResult = autoLogin(sysUser);
        if (loginResult.isSuccess()) {
            wxUser.setToken(loginResult.getResult().toString());
        }
        return Result.OK(loginResult.getMessage(), wxUser);
    }

    /**
     * ??????????????????????????????????????????????????????????????????????????????????????????
     *
     * @return
     * @Deprecate 2021-12-05 ?????????????????????????????????
     */
    @ApiOperation(value = "??????-????????????", notes = "??????????????????-??????")
    @PostMapping(value = "/wx/login")
    public Result<?> wxManualLogin(@RequestParam("username") String username,
                                   @RequestParam("password") String password,
                                   @RequestParam("openId") String openId) {
        //????????????????????????????????????openId??????????????????????????????id???????????????username???????????????id?????????????????????????????????????????????id
        WXUser wxUser = apiClientService.queryWxUserByOpenId(openId);

        // ??????????????????????????????
        //1. ????????????????????????
        Result<JSONObject> result = new Result<>();
        SysUser sysUser = sysUserService.getUserByName(username);
        wxUser.setPeopleType(sysUser.getPeopleType());
        result = sysUserService.checkUserIsEffective(sysUser);
        if (!result.isSuccess()) {
            return result;
        }

        //2. ????????????????????????????????????
        String userpassword = PasswordUtil.encrypt(username, password, sysUser.getSalt());
        String syspassword = sysUser.getPassword();
        if (!syspassword.equals(userpassword)) {
            result.error500("????????????????????????");
            return result;
        }

        // ????????????????????????????????????????????????????????????
        if (!Objects.equals(wxUser.getSysUserId(), sysUser.getId())) {
            // ????????????????????????????????????
            SysUser old = sysUserService.queryById(wxUser.getSysUserId());
            old.setPhone(null);
            sysUserService.updateById(old);
            wxUser.setSysUserId(sysUser.getId());
            wxUser.setPeopleType(sysUser.getPeopleType());
            wxUser.setMtime((int) System.currentTimeMillis());
            apiClientService.updateWxUserSysUserIdById(wxUser.getId(), sysUser.getId());
            // ?????????????????????????????????????????????sys_user???
            sysUser.setPhone(wxUser.getPhone());
            sysUserService.updateById(sysUser);
        }

        JSONObject obj = new JSONObject();
        //??????????????????
        obj.put("userInfo", wxUser);

        // ??????token
        String newToken = JwtUtil.sign(username, syspassword);
        // ??????????????????
        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + newToken, newToken);
        redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + newToken, 60 * 60);
        wxUser.setToken(newToken);
        //token ??????
        result.setResult(obj);
        result.setSuccess(true);
        result.setCode(200);
        baseCommonService.addLog("?????????: " + username + ",????????????[?????????]???", CommonConstant.LOG_TYPE_1, null);
        return result;
    }

    @GetMapping(value = "/user/info")
    @ApiOperation(value = "??????-????????????????????????", notes = "??????????????????-????????????????????????")
    public Result<?> wxGetUserInfo(@RequestParam("token") String token) {
        String username = JwtUtil.getUsername(token);
        SysUser user = sysUserService.getUserByName(username);
        return Result.OK(user);
    }

}
