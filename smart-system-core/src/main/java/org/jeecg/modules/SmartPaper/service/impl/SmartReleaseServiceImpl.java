package org.jeecg.modules.SmartPaper.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.SmartPaper.entity.SmartExamInformation;

import org.jeecg.modules.SmartPaper.mapper.SmartPaperMapper;
import org.jeecg.modules.SmartPaper.mapper.SmartReleaseMapper;
import org.jeecg.modules.SmartPaper.service.ISmartReleaseService;
import org.jeecg.modules.SmartPaper.vo.SmartExamVo;
import org.jeecg.modules.SmartPaper.mapper.SmartExamInformationMapper;
import org.jeecg.modules.SmartPaper.entity.SmartPeople;
import org.jeecg.modules.SmartPaper.mapper.SmartPeopleMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: 发布
 * @Author: zxh
 * @Date:   2021-11-25
 * @Version: V1.0
 */
@Service
@Transactional  //事务的注解
public class SmartReleaseServiceImpl extends ServiceImpl<SmartReleaseMapper, SmartExamInformation> implements ISmartReleaseService {

    @Autowired
    private SmartExamInformationMapper smartExamInformationMapper;
    @Autowired
    private SmartPeopleMapper smartPeopleMapper;
    @Autowired
    private SmartPaperMapper smartPaperMapper;
    /**
     *
     * 试卷发布
     *
     *
     */
    @Override
    public Result releaseExam(String paperId,SmartExamVo smartExamVO) {
        SmartExamInformation smartExamInfo = new SmartExamInformation();
        BeanUtils.copyProperties(smartExamVO,smartExamInfo);
        String user = smartExamVO.getUsers();
        System.out.println("AAAAAAAAAAAAAAAAAAAAA");
        System.out.println(user);
        String[] users = user.split(",");
        System.out.println(users[0]);
        smartExamInfo.setPaperId(paperId);
        String paperName = (smartPaperMapper.getPaperById(paperId)).getPaperName();
        smartExamInfo.setExamName(paperName);
        //更新试卷状态
        smartPaperMapper.updateStatus(paperId);
        int count = smartExamInformationMapper.insert(smartExamInfo);
        if(count == 1){
            String examId = smartExamInfo.getId();
            for(int i =0;i<users.length;i++){
                SmartPeople smartPeople = new SmartPeople();
                smartPeople.setExamId(examId);
                String userId = users[i];
                String name = smartPeopleMapper.getNameById(userId);
                smartPeople.setPersonId(users[i]);
                smartPeople.setPersonName(name);
                smartPeopleMapper.insert(smartPeople);
            }
            return Result.OK();
        }
        else {
            return Result.error("");
        }

    }

}
