package org.jeecg.modules.smartTripleImportanceOneGreatness.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;


import org.jeecg.modules.smartTripleImportanceOneGreatness.entity.TypeCount;
import org.jeecg.modules.smartTripleImportanceOneGreatness.service.ISmartTriOneChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 三重一大统计
 * @Author: zxh
 * @Date:   2021-12-06
 * @Version: V1.0
 */
@RestController
@RequestMapping("/smartTriImpOneGre/chart")
@Slf4j
public class ChartController extends JeecgController<TypeCount, ISmartTriOneChartService> {
    @Autowired
    private ISmartTriOneChartService smartChartService;
    @AutoLog(value = "三重一大-按审核类型统计")
    @ApiOperation(value="三重一大-按审核类型统计", notes="三重一大-按审核类型统计")
    @GetMapping(value = "/countByVerifyStatus")
    public Result<?> countByVerifyStatus(@RequestParam (value="time") String time) {
        try{
            String year = "";
            String month = "";
            if(time != null && !time.isEmpty()){
                System.out.println(time);
                year = time.substring(0,4);
                month = time.substring(5,7);
                //System.out.println(year);
                //System.out.println(month);
            }
            List<TypeCount> list = smartChartService.countByVerifyStatus(year,month);
            List<TypeCount> typeList = smartChartService.getAllType();
            List<TypeCount> t0 = new ArrayList<>(typeList);
            List<TypeCount> t1 = new ArrayList<>(typeList);
            System.out.println(t0);
            for(int i=0;i<list.size();i++){
                String s = list.get(i).getStatu();
                String name = list.get(i).getKeyName();
                if(s.equals("0")){
                    for (int j = 0;j<t0.size();j++){
                        String name0 = t0.get(j).getKeyName();
                        if(name.equals(name0)){
                            t0.remove(j);
                        }
                    }
                    System.out.println(t0);
                }
                else {
                    for (int m = 0;m<t1.size();m++){
                        String name1 = t1.get(m).getKeyName();
                        if(name.equals(name1)){
                            t1.remove(m);
                        }
                    }
                }
            }
            //System.out.println(t0);
            if(t0.size()!= 0){
                for (TypeCount count : t0) {
                    TypeCount typeCount = new TypeCount();
                    typeCount.setKeyName(count.getKeyName());
                    typeCount.setStatu("0");
                    typeCount.setValue(0);
                    list.add(typeCount);
                }
            }
            if(t1.size()!= 0){
                for (TypeCount typeCount : t1) {
                    TypeCount typeCount1 = new TypeCount();
                    typeCount1.setKeyName(typeCount.getKeyName());
                    typeCount1.setStatu("1");
                    typeCount1.setValue(0);
                    list.add(typeCount1);
                }

            }
            System.out.println("####################");
            System.out.println(list);
            return Result.OK(list);
        }catch (Exception e){
            return Result.error("error");
        }

    }
    @AutoLog(value = "三重一大-按类型统计")
    @ApiOperation(value="三重一大-按类型统计", notes="三重一大-按类型统计")
    @GetMapping(value = "/countByType")
    public Result<?> countByType(@RequestParam (value="time") String time) {
        try{
            String year = "";
            String month = "";
            if(time != null && !time.isEmpty()){
                System.out.println(time);
                year = time.substring(0,4);
                month = time.substring(5,7);
                //System.out.println(year);
                //System.out.println(month);
            }
            List<TypeCount> list = smartChartService.countByType(year,month);
            List<TypeCount> typeList = smartChartService.getAllType();
            System.out.println("####################");
            System.out.println(list);
            return Result.OK(list);
        }catch (Exception e){
            return Result.error("error");
        }

    }
}
