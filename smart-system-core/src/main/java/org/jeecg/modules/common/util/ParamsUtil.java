package org.jeecg.modules.common.util;

import java.util.*;

public class ParamsUtil {
    /**
     * 获取请求参数中的高级查询条件
     *
     * @param map 请求参数
     * @return
     */
    public static List<String> getSuperQueryParams(Map map) {
        ArrayList<String> list = new ArrayList<>();
        // 遍历
        for (Object o : map.entrySet()) {
            Map.Entry element = (Map.Entry) o;
            // key值
            Object strKey = element.getKey();
            // value,数组形式
            String[] value = (String[]) element.getValue();
            if ("superQueryParams".equals(strKey)) {
                for (int i = 0; i < value.length; i++) {
                    // 如果为[],则直接跳出循环
                    if ("%5B%5D".equals(value[i])) {
                        break;
                    }
                    // 不要用addAll，为空的时候会出错
                    list.add(value[i]);
                }
                return list;
            }
        }
        return list;
    }
}
