package org.jeecg.modules.app.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.app.entity.Banner;
import org.jeecg.modules.app.service.IBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Description: banner的app接口
 * @Author: CabbSir cabbsir@gmail.com
 * @Date: 2021-11-06
 * @Version: V1.0
 */
@Api(tags = "app banner接口")
@RestController
@RequestMapping("/api/client")
@Slf4j
public class ApiBannerController extends ApiBaseController {
    @Autowired
    private IBannerService bannerService;

    /**
     * 分页列表查询
     * <p>
     * 没必要记录日志的接口
     *
     * @param banner
     * @param req
     * @return
     */
    @ApiOperation(value = "客户端-获取banner列表", notes = "客户端-获取banner列表")
    @GetMapping(value = "/banner")
    public Result<?> queryPageList(Banner banner, @RequestParam Map<String, String> params,
                                   HttpServletRequest req) {
        // 检查参数列表
        String paramList = "column|order|androidId|appVersion|mac|clientId|clientIp|sign";
        if (!super.checkParams(params, paramList)) {
            return Result.error("参数列表错误");
        }
        if (!super.checkSign(params)) {
            return Result.error("签名错误");
        }
        QueryWrapper<Banner> queryWrapper = QueryGenerator.initQueryWrapper(banner, req.getParameterMap());
        Page<Banner> page = new Page<>(1, 1000);
        IPage<Banner> pageList = bannerService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 这个接口不验证了
     * @param banner
     * @return
     */
    @ApiOperation(value = "小程序-获取banner列表", notes = "小程序-获取banner列表")
    @GetMapping(value = "/wx/banner")
    public Result<?> queryWxList(Banner banner, HttpServletRequest req) {
        QueryWrapper<Banner> queryWrapper = QueryGenerator.initQueryWrapper(banner, req.getParameterMap());
        Page<Banner> page = new Page<>(1, 1000);
        IPage<Banner> pageList = bannerService.page(page, queryWrapper);
        return Result.OK(pageList);
    }
}
