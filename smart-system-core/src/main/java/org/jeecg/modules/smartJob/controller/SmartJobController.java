package org.jeecg.modules.smartJob.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.netty.util.Timeout;
import io.netty.util.concurrent.ScheduledFuture;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.smartJob.entity.JobType;
import org.jeecg.modules.smartJob.entity.SmartJob;
import org.jeecg.modules.smartJob.service.ISmartJobService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.smartJob.service.imp.SmartJobServiceImpl;
import org.jeecg.modules.smartJob.util.ComputeTime;
import org.jeecg.modules.smartJob.util.DelayTask;
import org.jeecg.modules.smartJob.util.LoopTask;
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
 * @Description: TODO
 * @author: lord
 * @date: 2021年11月30日 16:01
 */

@Api(tags="定时任务信息表")
@RestController
@RequestMapping("/smartJob/smartJob")
@Slf4j
public class SmartJobController extends JeecgController<SmartJob, ISmartJobService> {

    @Autowired
    ISmartJobService smartJobService;

    DelayTask delayTask = DelayTask.getInstance();
    LoopTask loopTask = LoopTask.getInstance();

    /**
     * 分页列表查询
     *
     * @param smartJob
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "定时任务信息表-分页列表查询")
    @ApiOperation(value="定时任务信息表-分页列表查询", notes="定时任务信息表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SmartJob smartJob,
                                   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<SmartJob> queryWrapper = QueryGenerator.initQueryWrapper(smartJob, req.getParameterMap());
        Page<SmartJob> page = new Page<SmartJob>(pageNo, pageSize);
        IPage<SmartJob> pageList = smartJobService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     *   添加
     *
     * @param smartJob
     * @return
     */
    @AutoLog(value = "定时任务信息表-添加")
    @ApiOperation(value="定时任务信息表-添加", notes="定时任务信息表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SmartJob smartJob) {
        System.out.println(smartJob.toString());

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        boolean isExist = smartJobService.openJob(smartJob, sysUser.getUsername());
        if(isExist){
            smartJobService.save(smartJob);
            return Result.OK("添加成功！");
        }else {
            return Result.error("任务已存在，请勿重复添加");
        }

    }

    /**
     *  编辑
     *
     * @param smartJob
     * @return
     */
    @AutoLog(value = "定时任务信息表-编辑")
    @ApiOperation(value="定时任务信息表-编辑", notes="定时任务信息表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SmartJob smartJob) {

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        smartJobService.edit(smartJob, sysUser.getUsername());

        //更新状态
        smartJob.setJobStatus("开启");
        smartJobService.updateById(smartJob);
        return Result.OK("编辑成功!");
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "定时任务信息表-通过id删除")
    @ApiOperation(value="定时任务信息表-通过id删除", notes="定时任务信息表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name="id",required=true) String id) {
        //删除
        SmartJob smartJob = smartJobService.getById(id);
        //判断是delay还是loop
        if(smartJob.getIsLoop().equals("0")){
            //loop
            //从map取下job并取消任务
            loopTask.deleteJob(smartJob.getJobBean());

            smartJobService.removeById(id);
            return Result.OK("删除成功!");
        }else{
            //delay
            //从map取下job并取消任务
            delayTask.deleteTask(smartJob.getJobBean());

            smartJobService.removeById(id);
            return Result.OK("删除成功!");

        }
    }

    /**
     *  批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "定时任务信息表-批量删除")
    @ApiOperation(value="定时任务信息表-批量删除", notes="定时任务信息表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        this.smartJobService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "定时任务信息表-通过id查询")
    @ApiOperation(value="定时任务信息表-通过id查询", notes="定时任务信息表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
        SmartJob smartJob = smartJobService.getById(id);
        if(smartJob==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(smartJob);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param smartJob
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartJob smartJob) {
        return super.exportXls(request, smartJob, SmartJob.class, "定时任务信息表");
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
        return super.importExcel(request, response, SmartJob.class);
    }
}
