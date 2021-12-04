//package org.jeecg.config;
//
//import lombok.Data;
//import org.jeecg.common.util.DySmsHelper;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @Description: 创蓝短信服务配置类
// * @author: lord
// * @date: 2021年12月04日 14:45
// */
//
//@Configuration
//public class SMSConfig {
//
//    @Value("${chuanglan.config.sendUrl}")
//    private String sendUrl;
//    @Value("${chuanglan.config.account}")
//    private String account;
//    @Value("${chuanglan.config.password}")
//    private String password;
//
//    @Bean
//    public void initDySmsHelper(){
//        DySmsHelper.setSendUrl(sendUrl);
//        DySmsHelper.setAccount(account);
//        DySmsHelper.setPassword(password);
//    }
//}
