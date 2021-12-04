package org.jeecg.modules.demo.test.controller;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.demo.test.entity.JeecgDemo;
import org.jeecg.modules.demo.test.service.IJeecgDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Description: 大屏预览入口
 * @Author: scott
 * @Date:2019-12-12
 * @Version:V1.0
 */
@Slf4j
@Controller
@RequestMapping("/test/bigScreen/templat")
public class BigScreenTemplatController extends JeecgController<JeecgDemo, IJeecgDemoService> {

    @Autowired
    private IJeecgDemoService jeecgDemoService;
    /**
     * @param modelAndView
     * @return
     */
    @RequestMapping("/html")
    public ModelAndView ftl(ModelAndView modelAndView) {
        modelAndView.setViewName("demo3");
        List<String> userList = new ArrayList<String>();
        userList.add("admin");
        userList.add("user1");
        userList.add("user2");
        log.info("--------------test--------------");
        modelAndView.addObject("userList", userList);
        return modelAndView;
    }

    /**
     * 生产销售监控模版
     * @param modelAndView
     * @return
     */
    @RequestMapping("/index1")
    public ModelAndView index1(ModelAndView modelAndView) {
        modelAndView.setViewName("/bigscreen/template1/index");
        return modelAndView;
    }

    /**
     * 智慧物流监控模版
     * @param modelAndView
     * @return
     */
    @RequestMapping("/index2")
    public ModelAndView index2(ModelAndView modelAndView) {
        modelAndView.setViewName("/bigscreen/template2/index");
        modelAndView.addObject("shenhe",0000000000);
        return modelAndView;
    }

    @RequestMapping(value = "/getHomeData")
    @ResponseBody
    public HashMap<String,Object> getHomeData() {
//        List<Object> hudong = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<>();
//        map.put("yushuZ", jeecgDemoService.getYushuZ());
//        map.put("xinnongZ",jeecgDemoService.getXinnongZ());
//        map.put("xinfaZ", jeecgDemoService.getXinfaZ());
//        map.put("taipingZ", jeecgDemoService.getTaipingZ());
//        map.put("seriesdata", seriesdata);
        return map;
    }

}
