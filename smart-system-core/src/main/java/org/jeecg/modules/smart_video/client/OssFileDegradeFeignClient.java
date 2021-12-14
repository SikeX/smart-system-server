package org.jeecg.modules.smart_video.client;


import org.jeecg.modules.smart_video.commonutils.R;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OssFileDegradeFeignClient implements OssClient {
    @Override
    public R removeAlyWj(String url) {
        return R.error().message("删除文件time out");
    }

    @Override
    public R delete(List<String> WjList) {
        return R.error().message("删除多个文件time out");
    }
}
