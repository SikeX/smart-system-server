package org.jeecg.modules.SmartPaper.service;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.SmartPaper.entity.SmartPaper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.SmartPaper.vo.SmartPaperPage;

/**
 * @Description: 试卷表
 * @Author: jeecg-boot
 * @Date:   2021-11-21
 * @Version: V1.0
 */
public interface ISmartPaperService extends IService<SmartPaper> {
    //新建试卷
    Result insert(SmartPaperPage smartPaperPage);
    //获取试卷通过Id
    SmartPaperPage getPaperById(String id);

    Result updatePaperById(String id, SmartPaperPage smartPaperPage);

    Result deletePaper(String id);
}
