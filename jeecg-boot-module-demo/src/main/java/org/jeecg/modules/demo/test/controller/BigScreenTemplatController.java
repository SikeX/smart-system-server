package org.jeecg.modules.demo.test.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.demo.test.entity.Cloud;
import org.jeecg.modules.demo.test.entity.JeecgDemo;
import org.jeecg.modules.demo.test.entity.partyUser;
import org.jeecg.modules.demo.test.service.IJeecgDemoService;
import org.jeecg.modules.demo.test.service.IJeecgDemoServiceTwo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Description: 大屏预览入口
 * @Author: scott
 * @Date:2019-12-12
 * @Version:V1.0
 */
@Slf4j
@RestController
@RequestMapping("/test/bigScreen/templat")
public class BigScreenTemplatController extends JeecgController<JeecgDemo, IJeecgDemoService> {

    @Autowired
    private IJeecgDemoService jeecgDemoService;

    @Autowired
    private IJeecgDemoServiceTwo jeecgDemoServiceTwo;

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
        modelAndView.addObject("shenhe",jeecgDemoService.getShenhe());
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
        modelAndView.addObject("riqi",formatter.format(date));
        SimpleDateFormat formatteryear = new SimpleDateFormat("yyyy");
        int thisyear = Integer.parseInt(formatteryear.format(date).toString());
        modelAndView.addObject("thisyear",thisyear);
//        // 获取登录用户信息，可以用来查询用户ID
//        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        modelAndView.addObject("daishenhe",jeecgDemoService.getDaishenhe());
        modelAndView.addObject("yishenhe",jeecgDemoService.getYishenhe());

        return modelAndView;
    }

    @RequestMapping(value = "/getHomeData")
    @ResponseBody
    public HashMap<String,Object> getHomeData() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("yushuZ", jeecgDemoService.getYushuZ());
        map.put("xinnongZ",jeecgDemoService.getXinnongZ());
        map.put("xinfaZ", jeecgDemoService.getXinfaZ());
        map.put("taipingZ", jeecgDemoService.getTaipingZ());
        return map;
    }

    @RequestMapping(value = "/getChart1Data")
    @ResponseBody
    public HashMap<String,Object> getChart1Data() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("tongzhi", jeecgDemoService.getTongzhi());
        map.put("lianzheng",jeecgDemoService.getLianzheng());
        map.put("renwu", jeecgDemoService.getRenwu());
        return map;
    }

    @RequestMapping(value = "/getChart3Data")
    @ResponseBody
    public HashMap<String,Object> getChart3Data() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("tongzhiweidu", jeecgDemoService.getTongzhiweidu());
        map.put("tongzhiyidu", jeecgDemoService.getTongzhiyidu());
        map.put("lianzhengyidu", jeecgDemoService.getLianzhengyidu());
        map.put("lianzhengweidu", jeecgDemoService.getLianzhengweidu());
        map.put("renwutiao", jeecgDemoService.getRenwutiao());
        System.out.println("-----------------------------------------------------------------获取条形统计图数据");
        System.out.println(jeecgDemoService.getTongzhiweidu());
        System.out.println(jeecgDemoService.getTongzhiyidu());
        System.out.println(jeecgDemoService.getLianzhengyidu());
        System.out.println(jeecgDemoService.getLianzhengweidu());
        System.out.println(jeecgDemoService.getRenwutiao());
        return map;
    }

    @RequestMapping(value = "/getShenhe")
    @ResponseBody
    public HashMap<String,Object> getShenhe() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("daishenhe",jeecgDemoService.getDaishenhe());
        map.put("yishenhe",jeecgDemoService.getYishenhe());
        return map;
    }

    @RequestMapping(value = "/getShenhe2")
    @ResponseBody
    public HashMap<String,Object> getShenhe2() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("daiS",jeecgDemoService.getDaishenhe());
        map.put("yiS",jeecgDemoService.getYishenhe());
        return map;
    }

    @RequestMapping(value = "/getCloudData")
    @ResponseBody
    public HashMap<String,Object> getCloudData() {
        HashMap<String, Object> map = new HashMap<>();
        // 获取登录用户信息，可以用来查询用户ID
//        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
//        System.out.println(sysUser.getDepartId());
//        String username = JwtUtil.getUserNameByToken(req);
//        System.out.println(username);
        List<partyUser> userList = this.jeecgDemoService.getCloudData();
        List<Cloud> cloudList = new ArrayList<>();
        if(userList != null) {
            for (partyUser user : userList) {
                Cloud cloud = new Cloud();
                cloud.setName(user.getRealname());
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
                String partyYear = formatter.format(user.getJoinPartyDate());
                cloud.setValue(partyYear);
                cloudList.add(cloud);
            }
        }
        System.out.println("-----------------------------------------------------------------获取入党纪念日数据");
        System.out.println(userList);
        System.out.println(cloudList);
        map.put("data", cloudList);
        return map;
    }

    @RequestMapping(value = "/getDepartTree")
    @ResponseBody
    public HashMap<String,Object> getDepartTree() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("naturalData",jeecgDemoServiceTwo.getNaturalTree());
        map.put("workData",jeecgDemoServiceTwo.getWorkTree());
        return map;
    }
}
