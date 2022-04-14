package org.jeecg.modules.smart_8_escorted_meal.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.smart_8_escorted_meal.entity.Smart_8EscortedMeal;
import org.jeecg.modules.smart_8_escorted_meal.service.ISmart_8EscortedMealService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.smart_reception.service.ISmart_8DiningService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 陪同用餐人员表
 * @Author: jeecg-boot
 * @Date: 2022-02-28
 * @Version: V1.0
 */
@Api(tags = "陪同用餐人员表")
@RestController
@RequestMapping("/smart_8_escorted_meal/smart_8EscortedMeal")
@Slf4j
public class Smart_8EscortedMealController extends JeecgController<Smart_8EscortedMeal, ISmart_8EscortedMealService> {
    @Autowired
    private ISmart_8EscortedMealService smart_8EscortedMealService;
    private ISmart_8DiningService smart_8DiningService;

    /**
     * 分页列表查询
     *
     * @param smart_8EscortedMeal
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "陪同用餐人员表-分页列表查询")
    @ApiOperation(value = "陪同用餐人员表-分页列表查询", notes = "陪同用餐人员表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(Smart_8EscortedMeal smart_8EscortedMeal,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<Smart_8EscortedMeal> queryWrapper = QueryGenerator.initQueryWrapper(smart_8EscortedMeal, req.getParameterMap());
        Page<Smart_8EscortedMeal> page = new Page<Smart_8EscortedMeal>(pageNo, pageSize);
        IPage<Smart_8EscortedMeal> pageList = smart_8EscortedMealService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    @AutoLog(value = "陪同用餐人员表-分页列表查询")
    @ApiOperation(value = "陪同用餐人员表-分页列表查询", notes = "陪同用餐人员表-分页列表查询")
    @GetMapping(value = "/getListByMainId")
    public Result<?> getListByMainId(Smart_8EscortedMeal smart_8EscortedMeal,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                     @RequestParam(name = "mainId") String mainId,
                                     HttpServletRequest req) {
        QueryWrapper<Smart_8EscortedMeal> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("main_id", mainId);
        Page<Smart_8EscortedMeal> page = new Page<Smart_8EscortedMeal>(pageNo, pageSize);
        IPage<Smart_8EscortedMeal> pageList = smart_8EscortedMealService.page(page, queryWrapper);
        return Result.OK(pageList);
    }


    /**
     * 添加
     *
     * @param smart_8EscortedMeal
     * @return
     */
    @AutoLog(value = "陪同用餐人员表-添加")
    @ApiOperation(value = "陪同用餐人员表-添加", notes = "陪同用餐人员表-添加")
    @PostMapping(value = "/addByMainId")
    public Result<?> add(@RequestBody Smart_8EscortedMeal smart_8EscortedMeal) {
        smart_8EscortedMealService.save(smart_8EscortedMeal);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param smart_8EscortedMeal
     * @return
     */
    @AutoLog(value = "陪同用餐人员表-编辑")
    @ApiOperation(value = "陪同用餐人员表-编辑", notes = "陪同用餐人员表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody Smart_8EscortedMeal smart_8EscortedMeal) {
        smart_8EscortedMealService.updateById(smart_8EscortedMeal);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "陪同用餐人员表-通过id删除")
    @ApiOperation(value = "陪同用餐人员表-通过id删除", notes = "陪同用餐人员表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        smart_8EscortedMealService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "陪同用餐人员表-批量删除")
    @ApiOperation(value = "陪同用餐人员表-批量删除", notes = "陪同用餐人员表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.smart_8EscortedMealService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "陪同用餐人员表-通过id查询")
    @ApiOperation(value = "陪同用餐人员表-通过id查询", notes = "陪同用餐人员表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        Smart_8EscortedMeal smart_8EscortedMeal = smart_8EscortedMealService.getById(id);
        if (smart_8EscortedMeal == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(smart_8EscortedMeal);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param smart_8EscortedMeal
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Smart_8EscortedMeal smart_8EscortedMeal) {
        return super.exportXls(request, smart_8EscortedMeal, Smart_8EscortedMeal.class, "陪同用餐人员表");
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
        return super.importExcel(request, response, Smart_8EscortedMeal.class);
    }

}
