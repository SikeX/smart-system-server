package org.jeecg.modules.interaction.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.common.util.ParamsUtil;
import org.jeecg.modules.interaction.domain.SmartVillageTopic;
import org.jeecg.modules.interaction.service.SmartVillageTopicService;
import org.jeecg.modules.smartSupervision.entity.SmartSupervision;
import org.jeecg.modules.smartSupervision.entity.SmartSupervisionAnnex;
import org.jeecg.modules.smartSupervision.vo.SmartSupervisionPage;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: sike
 * @Date: 2021/11/24 15:23
 * @Version: V1.0
 */
@Api(tags="八项规定监督检查表")
@RestController
@RequestMapping("/interaction")
@Slf4j
public class TopicController {

    @Autowired
    private SmartVillageTopicService smartVillageTopicService;
    /**
     * 分页列表查询
     *
     * @param smartSupervision
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "村情互动话题-分页列表查询")
    @ApiOperation(value="村情互动话题-分页列表查询", notes="村情互动话题-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SmartVillageTopic smartVillageTopic,
                                   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                   HttpServletRequest req) {
        Result<IPage<SmartVillageTopic>> result = new Result<IPage<SmartVillageTopic>>();

        LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getId();

        Page<SmartVillageTopic> pageList = new Page<SmartVillageTopic>(pageNo, pageSize);
        pageList = smartVillageTopicService.getTopicListPage(pageList, userId);
        result.setResult(pageList);
        result.setSuccess(true);
        return result;
    }

    /**
     *   添加
     *
     * @param smartVillageTopic
     * @return
     */
    @AutoLog(value = "村情互动话题-添加")
    @ApiOperation(value="村情互动话题-添加", notes="村情互动话题-添加")
    @Transactional
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SmartVillageTopic smartVillageTopic) {

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        String userId = sysUser.getId();
        smartVillageTopic.setUserId(userId);
        smartVillageTopic.setStatus(0);
        smartVillageTopicService.save(smartVillageTopic);

        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     *
     * @param smartSupervisionPage
     * @return
     */
//    @AutoLog(value = "八项规定监督检查表-编辑")
//    @ApiOperation(value="八项规定监督检查表-编辑", notes="八项规定监督检查表-编辑")
//    @PutMapping(value = "/edit")
//    public Result<?> edit(@RequestBody SmartSupervisionPage smartSupervisionPage) {
//        SmartSupervision smartSupervision = new SmartSupervision();
//        BeanUtils.copyProperties(smartSupervisionPage, smartSupervision);
//        SmartSupervision smartSupervisionEntity = smartSupervisionService.getById(smartSupervision.getId());
//        if(smartSupervisionEntity==null) {
//            return Result.error("未找到对应数据");
//        }
//        smartSupervision.setDepartId(null);
//        smartSupervision.setCreateTime(null);
//        smartSupervisionService.updateMain(smartSupervision, smartSupervisionPage.getSmartSupervisionAnnexList());
//        return Result.OK("编辑成功!");
//    }
//
//    /**
//     *   通过id删除
//     *
//     * @param id
//     * @return
//     */
//    @AutoLog(value = "八项规定监督检查表-通过id删除")
//    @ApiOperation(value="八项规定监督检查表-通过id删除", notes="八项规定监督检查表-通过id删除")
//    @DeleteMapping(value = "/delete")
//    public Result<?> delete(@RequestParam(name="id",required=true) String id) {
//        smartSupervisionService.delMain(id);
//        return Result.OK("删除成功!");
//    }
//
//    /**
//     *  批量删除
//     *
//     * @param ids
//     * @return
//     */
//    @AutoLog(value = "八项规定监督检查表-批量删除")
//    @ApiOperation(value="八项规定监督检查表-批量删除", notes="八项规定监督检查表-批量删除")
//    @DeleteMapping(value = "/deleteBatch")
//    public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
//        this.smartSupervisionService.delBatchMain(Arrays.asList(ids.split(",")));
//        return Result.OK("批量删除成功！");
//    }
//
//    /**
//     * 通过id查询
//     *
//     * @param id
//     * @return
//     */
//    @AutoLog(value = "八项规定监督检查表-通过id查询")
//    @ApiOperation(value="八项规定监督检查表-通过id查询", notes="八项规定监督检查表-通过id查询")
//    @GetMapping(value = "/queryById")
//    public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
//        SmartSupervision smartSupervision = smartSupervisionService.getById(id);
//        if(smartSupervision==null) {
//            return Result.error("未找到对应数据");
//        }
//        return Result.OK(smartSupervision);
//
//    }
//
//    /**
//     * 通过id查询
//     *
//     * @param id
//     * @return
//     */
//    @AutoLog(value = "8项规定监督检查附件表通过主表ID查询")
//    @ApiOperation(value="8项规定监督检查附件表主表ID查询", notes="8项规定监督检查附件表-通主表ID查询")
//    @GetMapping(value = "/querySmartSupervisionAnnexByMainId")
//    public Result<?> querySmartSupervisionAnnexListByMainId(@RequestParam(name="id",required=true) String id) {
//        List<SmartSupervisionAnnex> smartSupervisionAnnexList = smartSupervisionAnnexService.selectByMainId(id);
//        return Result.OK(smartSupervisionAnnexList);
//    }
}
