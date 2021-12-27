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

import java.util.List;

/**
 * @Description: 阳光评廉评价
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
    public Result<?> countByVerifyStatus() {
        try{
            List<TypeCount> list = smartChartService.countByVerifyStatus();
            return Result.OK(list);
        }catch (Exception e){
            return Result.error("error");
        }

    }
}
