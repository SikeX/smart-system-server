package org.jeecg.modules.app.controller;

import org.jeecg.modules.app.util.Encrypt;

import java.util.Map;
import java.util.TreeMap;

public class ApiBaseController {
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
}
