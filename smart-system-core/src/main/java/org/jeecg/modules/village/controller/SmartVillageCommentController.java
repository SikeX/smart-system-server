package org.jeecg.modules.village.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.village.entity.SmartVillageComment;
import org.jeecg.modules.village.service.ISmartVillageCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
* @Description: 村情互动-回复表
* @Author: jeecg-boot
* @Date:   2021-11-22
* @Version: V1.0
*/
@Api(tags="村情互动-回复表")
@RestController
@RequestMapping("/village/smartVillageComment")
@Slf4j
public class SmartVillageCommentController extends JeecgController<SmartVillageComment, ISmartVillageCommentService> {
   @Autowired
   private ISmartVillageCommentService smartVillageCommentService;

   /**
    * 分页列表查询
    *
    * @param smartVillageComment
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   @AutoLog(value = "村情互动-回复表-分页列表查询")
   @ApiOperation(value="村情互动-回复表-分页列表查询", notes="村情互动-回复表-分页列表查询")
   @GetMapping(value = "/list")
   public Result<?> queryPageList(SmartVillageComment smartVillageComment,
                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                  HttpServletRequest req) {
       QueryWrapper<SmartVillageComment> queryWrapper = QueryGenerator.initQueryWrapper(smartVillageComment, req.getParameterMap());
       Page<SmartVillageComment> page = new Page<SmartVillageComment>(pageNo, pageSize);
       IPage<SmartVillageComment> pageList = smartVillageCommentService.page(page, queryWrapper);
       return Result.OK(pageList);
   }

   /**
    *   添加
    *
    * @param smartVillageComment
    * @return
    */
   @AutoLog(value = "村情互动-回复表-添加")
   @ApiOperation(value="村情互动-回复表-添加", notes="村情互动-回复表-添加")
   @PostMapping(value = "/add")
   public Result<?> add(@RequestBody SmartVillageComment smartVillageComment) {
       smartVillageCommentService.save(smartVillageComment);
       return Result.OK("添加成功！");
   }

   /**
    *  编辑
    *
    * @param smartVillageComment
    * @return
    */
   @AutoLog(value = "村情互动-回复表-编辑")
   @ApiOperation(value="村情互动-回复表-编辑", notes="村情互动-回复表-编辑")
   @PutMapping(value = "/edit")
   public Result<?> edit(@RequestBody SmartVillageComment smartVillageComment) {
       smartVillageCommentService.updateById(smartVillageComment);
       return Result.OK("编辑成功!");
   }

   /**
    *   通过id删除
    *
    * @param id
    * @return
    */
   @AutoLog(value = "村情互动-回复表-通过id删除")
   @ApiOperation(value="村情互动-回复表-通过id删除", notes="村情互动-回复表-通过id删除")
   @DeleteMapping(value = "/delete")
   public Result<?> delete(@RequestParam(name="id",required=true) String id) {
       smartVillageCommentService.removeById(id);
       return Result.OK("删除成功!");
   }

   /**
    *  批量删除
    *
    * @param ids
    * @return
    */
   @AutoLog(value = "村情互动-回复表-批量删除")
   @ApiOperation(value="村情互动-回复表-批量删除", notes="村情互动-回复表-批量删除")
   @DeleteMapping(value = "/deleteBatch")
   public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
       this.smartVillageCommentService.removeByIds(Arrays.asList(ids.split(",")));
       return Result.OK("批量删除成功!");
   }

   /**
    * 通过id查询
    *
    * @param id
    * @return
    */
   @AutoLog(value = "村情互动-回复表-通过id查询")
   @ApiOperation(value="村情互动-回复表-通过id查询", notes="村情互动-回复表-通过id查询")
   @GetMapping(value = "/queryById")
   public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
       SmartVillageComment smartVillageComment = smartVillageCommentService.getById(id);
       if(smartVillageComment==null) {
           return Result.error("未找到对应数据");
       }
       return Result.OK(smartVillageComment);
   }

   /**
   * 导出excel
   *
   * @param request
   * @param smartVillageComment
   */
   @RequestMapping(value = "/exportXls")
   public ModelAndView exportXls(HttpServletRequest request, SmartVillageComment smartVillageComment) {
       return super.exportXls(request, smartVillageComment, SmartVillageComment.class, "村情互动-回复表");
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
       return super.importExcel(request, response, SmartVillageComment.class);
   }

}
