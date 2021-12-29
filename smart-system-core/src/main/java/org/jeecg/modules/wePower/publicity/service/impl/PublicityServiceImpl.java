package org.jeecg.modules.wePower.publicity.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.modules.wePower.publicity.service.PublicityService;
import org.jeecg.modules.wePower.publicity.mapper.PublicityMapper;
import org.jeecg.modules.wePower.publicity.vo.PublicityCommon;
import org.jeecg.modules.wePower.publicity.vo.PublicityQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
* @author sike
* @description 针对表【smart_create_advice】的数据库操作Service实现
* @createDate 2021-12-24 17:17:53
*/
@Slf4j
@Service
public class PublicityServiceImpl extends ServiceImpl<PublicityMapper, PublicityQuery>
    implements PublicityService {

    @Autowired
    private ISysBaseAPI sysBaseAPI;


    @Override
    public PublicityQuery getQuery(){
        PublicityQuery publicityQuery = new PublicityQuery();
        // 获取区域
        List<PublicityCommon> locationQueryList = getLocationQuery("A13A04");
        PublicityCommon location = new PublicityCommon();
        location.setValue("0");
        location.setLabel("不限");
        locationQueryList.add(location);
        publicityQuery.setLocation(locationQueryList);
        // 获取部门
        List<PublicityCommon> departQueryList = new ArrayList<>();
        sysBaseAPI.getAllBusDepart().forEach((item) -> {
            PublicityCommon depart = new PublicityCommon();
            depart.setValue(item.getOrgCode());
            depart.setLabel(item.getDepartName());
            departQueryList.add(depart);
        });
        PublicityCommon depart = new PublicityCommon();
        depart.setValue("0");
        depart.setLabel("不限");
        departQueryList.add(depart);
        publicityQuery.setDepartment(departQueryList);
        // 获取信息类型
        List<String> typeList = new ArrayList<>();
        typeList.add("领导班子");
        typeList.add("党务村务公开");
        typeList.add("小微权利");
        typeList.add("惠民补贴");
        typeList.add("项目管理");
        typeList.add("资产资源");
//        typeList.add("政策法规");
//        typeList.add("举报投诉");
        List<PublicityCommon> typeQueryList = new ArrayList<>();
        AtomicInteger count = new AtomicInteger(1);
        typeList.forEach((item) -> {
            PublicityCommon type = new PublicityCommon();
            type.setValue(String.valueOf(count.get()));
            type.setLabel(item);
            typeQueryList.add(type);
            count.getAndIncrement();
        });
        PublicityCommon type = new PublicityCommon();
        type.setLabel("不限");
        type.setValue("0");
        typeQueryList.add(type);
        publicityQuery.setType(typeQueryList);
        // 获取年份查询条件
        //获取当前日期的年份
        Calendar calendar = Calendar.getInstance();
        int yearNow = calendar.get(Calendar.YEAR);
        int leastYear = 2020;
        //存放年份的list
        List<Integer> dateList = new ArrayList<Integer>();
        for (int i = yearNow; i >= leastYear  ; i--) {
            dateList.add(i);
        }
        List<PublicityCommon> yearQueryList = new ArrayList<>();
        dateList.forEach((item) -> {
            PublicityCommon year = new PublicityCommon();
            year.setLabel(String.valueOf(item));
            year.setValue(String.valueOf(item));
            yearQueryList.add(year);
        });
        PublicityCommon year = new PublicityCommon();
        year.setLabel("不限");
        year.setValue("0");
        yearQueryList.add(year);
        publicityQuery.setYear(yearQueryList);

        return publicityQuery;

    }

    public List<PublicityCommon> getLocationQuery(String orgCode) {
        List<PublicityCommon> locationQueryList = new ArrayList<>();
        sysBaseAPI.getChildrenDepart(orgCode).forEach((item) -> {
            PublicityCommon location = new PublicityCommon();
            location.setLabel(item.getDepartName());
            location.setValue(item.getId());
            location.setChildren(getLocationQuery(item.getOrgCode()));
            locationQueryList.add(location);
        });
        return locationQueryList;
    }

}




