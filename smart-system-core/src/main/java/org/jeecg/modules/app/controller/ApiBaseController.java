package org.jeecg.modules.app.controller;

import java.util.Map;

public class ApiBaseController {
    protected Boolean checkSign(Map<String, Object> params) {
        for (String key : params.keySet()) {
            System.out.print("Key = " + key + " ");
            System.out.println("Value = " + params.get(key));
        }
        return true;
    }
}
