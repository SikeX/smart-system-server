package org.jeecg.modules.smart_video.client;


import org.jeecg.modules.smart_video.commonutils.R;
import org.jeecg.modules.smart_video.entity.UcenterMember;
//import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

//@FeignClient(name ="service-ucenter", fallback = UcenterFileDegradeFeignClient.class)
@Component
public interface UcenterClient {
//    @GetMapping("/educenter/member/getMemberInfotwo")
//    public UcenterMember getLoginInfotwo(@RequestParam("memberId") String memberId);
}
