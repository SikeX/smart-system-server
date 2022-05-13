package org.jeecg.modules.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description:
 * @Author: sike
 * @Date: 2022/3/11 15:29
 * @Version: V1.0
 */
public class UrlUtil {
    public static String urlEncodeChinese(String url) {
        try {
            Matcher matcher = Pattern.compile("[\\u4e00-\\u9fa5]").matcher(url);
            String tmp = "";
            while (matcher.find()) {
                tmp = matcher.group();
                url = url.replaceAll(tmp, URLEncoder.encode(tmp, "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url.replace(" ","%20");
    }
}
