package org.jeecg.modules.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.MD5Util;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.app.util.Encrypt;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysTenant;
import org.jeecg.modules.system.entity.SysUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ApiBaseController {
    protected static final int ACCOUNT_ACTIVE_STATUS = 1;
    protected static final int ACCOUNT_INACTIVE_STATUS = 2;

    protected Boolean checkSign(Map<String, String> params) {
        TreeMap<String, String> orderedParams = new TreeMap<>(params);
        String sign = orderedParams.get("sign");
        orderedParams.remove("sign");
        StringBuilder mySign = new StringBuilder();
        for (String key : orderedParams.keySet()) {
            mySign.append(key).append("=").append(orderedParams.get(key));
        }
        return Encrypt.MD5("6clnOgAvp5HulYNTOma5" +
                Encrypt.SHA256(mySign + "6clnOgAvp5HulYNTOma5")).equals(sign);
    }

    protected Boolean checkParams(Map<String, String> params, String paramList) {
        // paramList中是所有需要的参数列表，用|分割，因此只需验证map中是否存在key即可
        String [] keyArr = paramList.split("\\|");
        for (String s : keyArr) {
            if (params.get(s) == null) {
                return false;
            }
        }
        return true;
    }
}
