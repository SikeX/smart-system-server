package org.jeecg.modules.SmartPaper.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.SmartPaper.entity.SmartPaper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.SmartPaper.vo.RandomPeople;
import org.jeecg.modules.SmartPaper.vo.SmartPaperPage;
import org.jeecg.modules.SmartPaper.vo.SmartTriSurveyPage;

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

    Result insertTriSurvey(SmartTriSurveyPage smartTriSurveyPage);

    Page<RandomPeople> getTriPeoList(Page<RandomPeople> page,String paperId,String paperType);

    Result insertTriGovSurvey(SmartTriSurveyPage smartTriSurveyPage);

    Page<RandomPeople> getTriPeoGovList(Page<RandomPeople> pageList, String paperId, String paperType);
}
