package org.jeecg.modules.SmartPaper.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Joiner;
import org.jeecg.modules.SmartPaper.entity.SmartPeople;
import org.jeecg.modules.SmartPaper.mapper.SmartPeopleMapper;
import org.jeecg.modules.SmartPaper.service.ISmartPeopleService;
import org.jeecg.modules.SmartPaper.vo.ExamPeopleScoreVo;
import org.jeecg.modules.SmartPaper.vo.RandomPeople;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 考试参加人员表
 * @Author: jeecg-boot
 * @Date:   2021-11-24
 * @Version: V1.0
 */
@Service
public class SmartPeopleServiceImpl extends ServiceImpl<SmartPeopleMapper, SmartPeople> implements ISmartPeopleService {

    @Autowired
    private SmartPeopleMapper smartPeopleMapper;
    @Override
    public List<String> getMyExam(String userId) {
        List<String> examIds = smartPeopleMapper.getExamIdByuser(userId);
        return examIds;
    }
    @Override
    public String getMyExamString(String userId) {
        List<String>  examIds= smartPeopleMapper.getExamIdByuser(userId);
        return Joiner.on(",").join(examIds);
    }

    @Override
    public Page<ExamPeopleScoreVo> getScoreByExamId(Page<ExamPeopleScoreVo> page, String examId) {
        return page.setRecords(smartPeopleMapper.getScoreByExamId(page, examId));
    }

    @Override
    public List<String> getAllVillageList() {
        return  smartPeopleMapper.getAllVillageList();
    }
    @Override
    public List<String> getAllHomeListByVillageId(String villageId,Integer selectedCount) {
        return smartPeopleMapper.getAllHomeListByVillageId(villageId,selectedCount);
    }

    @Override
    public RandomPeople getSelectedPeoByHomeCode(String homeCode){
        return smartPeopleMapper.getSelectedPeoByHomeCode(homeCode);
    }

    @Override
    public Page<RandomPeople> getTriPeoListByDptId(Page<RandomPeople> page, String paperId, String departId) {
        return page.setRecords(smartPeopleMapper.getTriPeoListByDptId(page,paperId,departId));
    }

    @Override
    public Page<RandomPeople> getTriResultByEIdDId(Page<RandomPeople> page, String examId, String departId) {
        return page.setRecords(smartPeopleMapper.getTriResultByEIdDId(page, examId,departId));
    }

    @Override
    public List<RandomPeople> getTriGovPeoList(Integer selectedCount) {
        return smartPeopleMapper.getTriGovPeoList(selectedCount);
    }
}
