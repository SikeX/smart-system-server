package org.jeecg.modules.wePower.publicity.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.modules.wePower.publicity.service.PublicityService;
import org.jeecg.modules.wePower.publicity.vo.PublicityQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:
 * @Author: sike
 * @Date: 2021/12/24 15:23
 * @Version: V1.0
 */
@Api(tags="信息公示")
@RestController
@RequestMapping("/publicity")
@Slf4j
public class PublicityController {

    @Autowired
    private PublicityService publicityService;
    @Autowired
    private ISysBaseAPI sysBaseAPI;


    /**
     * 获取查询参数
     *
     * @param req
     * @return
     */
    @AutoLog(value = "获取查询参数")
    @ApiOperation(value="获取查询参数", notes="获取查询参数")
    @GetMapping(value = "/getQuery")
    public Result<?> getQuery(HttpServletRequest req){
        PublicityQuery publicityQuery = new PublicityQuery();

        publicityQuery = publicityService.getQuery();

        return Result.OK(publicityQuery);
    }
}
