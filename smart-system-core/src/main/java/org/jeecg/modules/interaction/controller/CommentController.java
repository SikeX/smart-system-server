package org.jeecg.modules.interaction.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.checkerframework.checker.units.qual.A;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.interaction.domain.SmartVillageComment;
import org.jeecg.modules.interaction.domain.SmartVillageTopic;
import org.jeecg.modules.interaction.service.SensitiveWordFilterService;
import org.jeecg.modules.interaction.service.SmartVillageCommentService;
import org.jeecg.modules.interaction.utils.SensitiveWordUtil;
import org.jeecg.modules.interaction.vo.CommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:
 * @Author: sike
 * @Date: 2021/11/26 11:57
 * @Version: V1.0
 */
@Api(tags="村情互动回答")
@RestController
@RequestMapping("/interaction/comment")
@Slf4j
public class CommentController {

    @Autowired
    private SmartVillageCommentService smartVillageCommentService;

    @Autowired
    private SensitiveWordFilterService sensitiveWordFilterService;


    /**
     * 分页列表查询
     *
     * @param smartVillageComment
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "村情互动回答-分页列表查询")
    @ApiOperation(value="村情互动回答-分页列表查询", notes="村情互动回答-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SmartVillageComment smartVillageComment,
                                   @RequestParam(name="topicId") String topicId,
                                   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                   HttpServletRequest req) {
        Result<IPage<SmartVillageComment>> result = new Result<IPage<SmartVillageComment>>();

        Page<SmartVillageComment> page = new Page<>(pageNo, pageSize);

        QueryWrapper<SmartVillageComment> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("topic_id", topicId).eq("status","1").orderByDesc("create_time");

        IPage<SmartVillageComment> pageList = smartVillageCommentService.page(page,queryWrapper);

        result.setResult(pageList);
        result.setSuccess(true);
        return result;
    }

    /**
     * 查询待审核回答
     *
     * @param smartVillageComment
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "村情互动回答-分页列表查询")
    @ApiOperation(value="村情互动回答-分页列表查询", notes="村情互动回答-分页列表查询")
    @GetMapping(value = "/verifyCommentList")
    public Result<?> getVerifyCommentList(CommentVo commentVo,
                                          @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                          @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                          HttpServletRequest req) {
        Result<IPage<CommentVo>> result = new Result<IPage<CommentVo>>();

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        Page<CommentVo> page = new Page<>(pageNo, pageSize);

        IPage<CommentVo> pageList = smartVillageCommentService.getCommentListPage(page,sysUser.getId());

        result.setResult(pageList);
        result.setSuccess(true);
        return result;
    }

    /**
     *   添加
     *
     * @param smartVillageComment
     * @return
     */
    @AutoLog(value = "村情互动回答-添加")
    @ApiOperation(value="村情互动回答-添加", notes="村情互动回答-添加")
    @Transactional
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SmartVillageComment smartVillageComment) {

        sensitiveWordFilterService.init();

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        String userId = sysUser.getId();
        smartVillageComment.setStatus("0");
        smartVillageComment.setUserId(userId);
        //对敏感词进行过滤
        smartVillageComment.setContent(SensitiveWordUtil.replaceSensitiveWord(smartVillageComment.getContent(),"*",
                SensitiveWordUtil.MinMatchType));
        smartVillageCommentService.save(smartVillageComment);

        return Result.OK("添加成功！");
    }


    @AutoLog(value = "八项规定监督检查表-编辑")
    @ApiOperation(value="八项规定监督检查表-编辑", notes="八项规定监督检查表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody CommentVo commentVo) {
        SmartVillageComment smartVillageComment = new SmartVillageComment();
        smartVillageComment.setId(commentVo.getId());
        smartVillageComment.setStatus(commentVo.getStatus());

        smartVillageCommentService.updateById(smartVillageComment);

        return Result.OK("编辑成功!");
    }

}
