package org.jeecg.modules.smart_video.controller;

import org.jeecg.modules.smart_video.commonutils.JwtUtils;
import org.jeecg.modules.smart_video.commonutils.R;
//import com.atguigu.educenter.entity.UcenterMember;
import org.jeecg.modules.smart_video.client.UcenterClient;
import org.jeecg.modules.smart_video.entity.EduComment;
import org.jeecg.modules.smart_video.service.EduCommentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.jeecg.modules.smart_video.client.UcenterClient;
import org.jeecg.modules.smart_video.commonutils.JwtUtils;
import org.jeecg.modules.smart_video.commonutils.R;
import org.jeecg.modules.smart_video.entity.EduComment;
import org.jeecg.modules.smart_video.entity.UcenterMember;
import org.jeecg.modules.smart_video.service.EduCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-10-03
 */
@RestController
@RequestMapping("/serviceedu/edu-comment")
@CrossOrigin("*")
public class EduCommentController {
    @Autowired
    private EduCommentService eduCommentService;
    @Autowired
    private UcenterClient ucenterClient;


    //根据课程id查询评论列表
    @ApiOperation(value = "评论分页列表")
    @GetMapping("{page}/{limit}")
    public R index(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,

            @ApiParam(name = "courseId", value = "查询对象", required = false)
                      String courseId) {
        Page<EduComment> pageParam = new Page<>(page, limit);
        QueryWrapper<EduComment> wrapper = new QueryWrapper<>();

        wrapper.eq("course_id",courseId);
        wrapper.eq("is_deleted",1);

        wrapper.orderByDesc("gmt_create");

        eduCommentService.page(pageParam,wrapper);

        List<EduComment> commentList = pageParam.getRecords();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("items", commentList);
        map.put("current", pageParam.getCurrent());  //当前页
        map.put("pages", pageParam.getPages()); //总页数
        map.put("size", pageParam.getSize());  //每页记录数
        map.put("total", pageParam.getTotal());  //总记录数
        map.put("hasNext", pageParam.hasNext());  //是都有下一页
        map.put("hasPrevious", pageParam.hasPrevious());   //是否有上一页
        //返回分页所有数据
        return R.ok().data(map);
    }

//    @ApiOperation(value = "添加评论")
//    @PostMapping("auth/save")
//    public R save(@RequestBody EduComment eduComment, HttpServletRequest request) {
//        String memberId = JwtUtils.getMemberIdByJwtToken(request);
//        //System.out.println("用户id："+memberId);
//        if(StringUtils.isEmpty(memberId)) {
//            return R.error().code(28004).message("请登录");
//        }
//
//        UcenterMember member = ucenterClient.getLoginInfotwo(memberId);
//
//        System.out.println(member.getNickname());
//
//        eduComment.setNickname(member.getNickname());
//
//        eduCommentService.save(eduComment);
//        return R.ok();
//    }

    //分页查询课程学习次数
    //current 当前页
    //limit  每页显示的数
    @GetMapping("Comment/{current}/{limit}")
    @ApiOperation(value = "分页查询")
    public R pageListComment(@PathVariable long current,
                             @PathVariable long limit){
        //创建page对象
        Page<EduComment> eduCommentPage = new Page<>(current,limit);
        //调用方法实现分页
        eduCommentService.page(eduCommentPage,null);

        long total = eduCommentPage.getTotal(); //总记录数
        List<EduComment> records = eduCommentPage.getRecords();

        return R.ok().data("total",total).data("rows",records);

    }

    @ApiOperation(value = "根据ID进行逻辑删除")
    @DeleteMapping("{id}")
    public R removeById(@PathVariable String id) {

        boolean flag = eduCommentService.removeById(id);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @ApiOperation(value = "根据ID进行审核评论")
    @PostMapping("set/{id}")
    public R setById(@PathVariable String id) {

        EduComment educomment = eduCommentService.getById(id);
        educomment.setIsDeleted("1");
        boolean flag = eduCommentService.updateById(educomment);
        if(flag) {
            return R.ok();
        }else{return R.error();}
    }


}

