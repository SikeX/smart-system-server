package org.jeecg.modules.SmartPaper.controller;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;



import lombok.extern.slf4j.Slf4j;


import org.jeecg.modules.SmartPaper.entity.SmartPeople;
import org.jeecg.modules.SmartPaper.service.ISmartPeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 考试参加人员表
 * @Author: jeecg-boot
 * @Date:   2021-11-24
 * @Version: V1.0
 */
@Api(tags="考试参加人员表")
@RestController
@RequestMapping("/SmartPaper/smartPeople")
@Slf4j
public class SmartPeopleController extends JeecgController<SmartPeople, ISmartPeopleService> {
    @Autowired
    private ISmartPeopleService smartPeopleService;

    /**
     * 分页列表查询
     *
     * @param smartPeople
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "考试参加人员表-分页列表查询")
    @ApiOperation(value="考试参加人员表-分页列表查询", notes="考试参加人员表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SmartPeople smartPeople,
                                   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<SmartPeople> queryWrapper = QueryGenerator.initQueryWrapper(smartPeople, req.getParameterMap());
        Page<SmartPeople> page = new Page<SmartPeople>(pageNo, pageSize);
        IPage<SmartPeople> pageList = smartPeopleService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     *   添加
     *
     * @param smartPeople
     * @return
     */
    @AutoLog(value = "考试参加人员表-添加")
    @ApiOperation(value="考试参加人员表-添加", notes="考试参加人员表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SmartPeople smartPeople) {
        smartPeopleService.save(smartPeople);
        return Result.OK("添加成功！");
    }



    /**
     *  编辑
     *
     * @param smartPeople
     * @return
     */
    @AutoLog(value = "考试参加人员表-编辑")
    @ApiOperation(value="考试参加人员表-编辑", notes="考试参加人员表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SmartPeople smartPeople) {
        smartPeopleService.updateById(smartPeople);
        return Result.OK("编辑成功!");
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "考试参加人员表-通过id删除")
    @ApiOperation(value="考试参加人员表-通过id删除", notes="考试参加人员表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name="id",required=true) String id) {
        smartPeopleService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     *  批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "考试参加人员表-批量删除")
    @ApiOperation(value="考试参加人员表-批量删除", notes="考试参加人员表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        this.smartPeopleService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "考试参加人员表-通过id查询")
    @ApiOperation(value="考试参加人员表-通过id查询", notes="考试参加人员表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
        SmartPeople smartPeople = smartPeopleService.getById(id);
        if(smartPeople==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(smartPeople);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param smartPeople
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartPeople smartPeople) {
        return super.exportXls(request, smartPeople, SmartPeople.class, "考试参加人员表");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, SmartPeople.class);
    }

}
