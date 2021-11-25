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
import org.jeecg.modules.village.entity.SmartVillageTopic;
import org.jeecg.modules.village.service.ISmartVillageTopicService;
import org.jeecg.modules.village.entity.SmartVillageComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
* @Description: 村情互动-村民提问表
* @Author: jeecg-boot
* @Date:   2021-11-22
* @Version: V1.0
*/
@Api(tags="村情互动-村民提问表")
@RestController
@RequestMapping("/village/smartVillageTopic")
@Slf4j
public class SmartVillageTopicController extends JeecgController<SmartVillageTopic, ISmartVillageTopicService> {
   @Autowired
   private ISmartVillageTopicService smartVillageTopicService;

    /**
     * 获取问题列表
     *
     * @return
     */
    @AutoLog(value = "村情互动-村民提问表-查询")
    @ApiOperation(value="村情互动-村民提问表-查询", notes="村情互动-村民提问表-查询")
    @GetMapping(value = "/queryTopicList")
    public Result<?> queryTopicList() {
        List<SmartVillageTopic> topicList = new ArrayList<>();
        topicList = smartVillageTopicService.queryTopicList();
        System.out.println("被调用了");
        return Result.OK(topicList);
    }

    /**
     * 获取问题列表
     *
     * @param topicId
     * @return
     */

    @GetMapping(value = "/queryCommentList")
    public Result<?> queryCommentList(@RequestParam(name="topicId") String topicId) {
        List<SmartVillageComment> commentList = new ArrayList<>();
        commentList = smartVillageTopicService.queryCommentList(topicId);
        return Result.OK(commentList);
    }



   /**
    * 分页列表查询
    *
    * @param smartVillageTopic
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   @AutoLog(value = "村情互动-村民提问表-分页列表查询")
   @ApiOperation(value="村情互动-村民提问表-分页列表查询", notes="村情互动-村民提问表-分页列表查询")
   @GetMapping(value = "/list")
   public Result<?> queryPageList(SmartVillageTopic smartVillageTopic,
                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                  HttpServletRequest req) {
       QueryWrapper<SmartVillageTopic> queryWrapper = QueryGenerator.initQueryWrapper(smartVillageTopic, req.getParameterMap());
       Page<SmartVillageTopic> page = new Page<SmartVillageTopic>(pageNo, pageSize);
       IPage<SmartVillageTopic> pageList = smartVillageTopicService.page(page, queryWrapper);
       return Result.OK(pageList);
   }

   /**
    *   添加
    *
    * @param smartVillageTopic
    * @return
    */
   @AutoLog(value = "村情互动-村民提问表-添加")
   @ApiOperation(value="村情互动-村民提问表-添加", notes="村情互动-村民提问表-添加")
   @PostMapping(value = "/add")
   public Result<?> add(@RequestBody SmartVillageTopic smartVillageTopic) {
       smartVillageTopicService.save(smartVillageTopic);
       return Result.OK("添加成功！");
   }

   /**
    *  编辑
    *
    * @param smartVillageTopic
    * @return
    */
   @AutoLog(value = "村情互动-村民提问表-编辑")
   @ApiOperation(value="村情互动-村民提问表-编辑", notes="村情互动-村民提问表-编辑")
   @PutMapping(value = "/edit")
   public Result<?> edit(@RequestBody SmartVillageTopic smartVillageTopic) {
       smartVillageTopicService.updateById(smartVillageTopic);
       return Result.OK("编辑成功!");
   }

   /**
    *   通过id删除
    *
    * @param id
    * @return
    */
   @AutoLog(value = "村情互动-村民提问表-通过id删除")
   @ApiOperation(value="村情互动-村民提问表-通过id删除", notes="村情互动-村民提问表-通过id删除")
   @DeleteMapping(value = "/delete")
   public Result<?> delete(@RequestParam(name="id",required=true) String id) {
       smartVillageTopicService.removeById(id);
       return Result.OK("删除成功!");
   }

   /**
    *  批量删除
    *
    * @param ids
    * @return
    */
   @AutoLog(value = "村情互动-村民提问表-批量删除")
   @ApiOperation(value="村情互动-村民提问表-批量删除", notes="村情互动-村民提问表-批量删除")
   @DeleteMapping(value = "/deleteBatch")
   public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
       this.smartVillageTopicService.removeByIds(Arrays.asList(ids.split(",")));
       return Result.OK("批量删除成功!");
   }

   /**
    * 通过id查询
    *
    * @param id
    * @return
    */
   @AutoLog(value = "村情互动-村民提问表-通过id查询")
   @ApiOperation(value="村情互动-村民提问表-通过id查询", notes="村情互动-村民提问表-通过id查询")
   @GetMapping(value = "/queryById")
   public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
       SmartVillageTopic smartVillageTopic = smartVillageTopicService.getById(id);
       if(smartVillageTopic==null) {
           return Result.error("未找到对应数据");
       }
       return Result.OK(smartVillageTopic);
   }

   /**
   * 导出excel
   *
   * @param request
   * @param smartVillageTopic
   */
   @RequestMapping(value = "/exportXls")
   public ModelAndView exportXls(HttpServletRequest request, SmartVillageTopic smartVillageTopic) {
       return super.exportXls(request, smartVillageTopic, SmartVillageTopic.class, "村情互动-村民提问表");
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
       return super.importExcel(request, response, SmartVillageTopic.class);
   }

}
