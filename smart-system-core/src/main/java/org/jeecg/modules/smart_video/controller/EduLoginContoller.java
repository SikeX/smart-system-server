package org.jeecg.modules.smart_video.controller;


import org.jeecg.modules.smart_video.commonutils.R;
import com.baomidou.mybatisplus.annotation.TableField;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/serviceedu/user")
@CrossOrigin
public class EduLoginContoller {

    @PostMapping("login")
    public R login(){

        return R.ok().data("token","admin");
    }

    @GetMapping("info")
    public R info(){
        return R.ok().data("roles","[admin]").data("name","admin").data("avatar",null);
    }
}
