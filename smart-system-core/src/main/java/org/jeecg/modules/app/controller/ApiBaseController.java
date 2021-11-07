package org.jeecg.modules.app.controller;

import org.jeecg.modules.app.util.Encrypt;

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
