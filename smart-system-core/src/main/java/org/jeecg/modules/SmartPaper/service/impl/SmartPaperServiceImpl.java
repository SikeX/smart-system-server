package org.jeecg.modules.SmartPaper.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.SmartPaper.entity.SmartPaper;
import org.jeecg.modules.SmartPaper.entity.SmartPeople;
import org.jeecg.modules.SmartPaper.entity.SmartTopic;
import org.jeecg.modules.SmartPaper.mapper.SmartPaperMapper;
import org.jeecg.modules.SmartPaper.mapper.SmartTopicMapper;
import org.jeecg.modules.SmartPaper.service.ISmartPaperService;
import org.jeecg.modules.SmartPaper.service.ISmartPeopleService;
import org.jeecg.modules.SmartPaper.vo.RandomPeople;
import org.jeecg.modules.SmartPaper.vo.SmartPaperPage;
import org.jeecg.modules.SmartPaper.vo.SmartTopicVo;
import org.jeecg.modules.SmartPaper.vo.SmartTriSurveyPage;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: 试卷表
 * @Author: jeecg-boot
 * @Date:   2021-11-21
 * @Version: V1.0
 */
@Service
@Transactional  //事务的注解
public class SmartPaperServiceImpl extends ServiceImpl<SmartPaperMapper, SmartPaper> implements ISmartPaperService {

    @Autowired
    private SmartPaperMapper smartPaperMapper;
    @Autowired
    private SmartTopicMapper smartTopicMapper;
    @Autowired
    private ISmartPeopleService smartPeopleService;
    @Override
    public Result<?> insert(SmartPaperPage smartPaperPage) {
        try{
            SmartPaper smartPaper = new SmartPaper();
            BeanUtils.copyProperties(smartPaperPage,smartPaper);
            smartPaperMapper.insert(smartPaper);
            String paperId = smartPaper.getId();

            SmartTopic t = new SmartTopic();
            for (SmartTopicVo topic:smartPaperPage.getSmartTopicVoList()){
                BeanUtils.copyProperties(topic,t);
                t.setPaperId(smartPaper.getId());
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println(t);
                smartTopicMapper.insert(t);
            }

            return Result.OK("success");
        }catch (Exception e){
            //强制手动事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.error(100,e.toString());
        }
    }
    //通过ID获取试卷
    @Override
    public SmartPaperPage getPaperById(String id) {
        SmartPaper smartPaper = smartPaperMapper.getPaperById(id);
        List<SmartTopicVo> smartTopicVoList = smartPaperMapper.getTopicListByPaperId(id);
        SmartPaperPage smartPaperPage = new SmartPaperPage();
        BeanUtils.copyProperties(smartPaper,smartPaperPage);
        smartPaperPage.setSmartTopicVoList(smartTopicVoList);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println(smartPaperPage);
        return smartPaperPage;
    }
    //编辑试卷
    @Override
    public Result updatePaperById(String id, SmartPaperPage smartPaperPage) {
        try{
        //获取原试卷_题目记录
        List<SmartTopicVo> smartTopicVoList = smartPaperMapper.getTopicListByPaperId(id);
        //获取旧的试题id集合
        List<String> oldTopicIdList = new ArrayList<String>();
        for (SmartTopicVo tt:smartTopicVoList){
            oldTopicIdList.add(tt.getId());
        }
        //判断更新后的题目是否存在  存在则更新   不存在则添加  剩下没有匹配到的则删除记录
        for (SmartTopicVo t:smartPaperPage.getSmartTopicVoList()){

            SmartTopic topic = new SmartTopic();
            BeanUtils.copyProperties(t,topic);
            //题目ID不为空，为旧题目
            if(t.getId() != null && !(t.getId()).isEmpty() ){
                for (int i=0; i<oldTopicIdList.size();i++){
                    if((t.getId()).equals(oldTopicIdList.get(i))){
                        System.out.println("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
                        System.out.println("true");
                        //存在则更新
                        UpdateWrapper<SmartTopic> topicId = new UpdateWrapper<>();
                        topicId.eq("id",t.getId());
                        smartTopicMapper.update(topic,topicId);
                        //移出旧的试卷id集合
                        oldTopicIdList.remove(i);
                        break;
                    }
                }
            }//不存在则添加
            else{
                topic.setPaperId(id);
                smartTopicMapper.insert(topic);
            }
        }
        //剩下没有匹配到的则删除
        for (String oldT_id:oldTopicIdList){
            smartTopicMapper.deleteById(oldT_id);
        }

        //修改试卷信息
        SmartPaper smartPaper = new SmartPaper();
        BeanUtils.copyProperties(smartPaperPage,smartPaper);
        UpdateWrapper<SmartPaper> paperId = new UpdateWrapper<>();
            paperId.eq("id",id);
        smartPaperMapper.update(smartPaper,paperId);
        return Result.OK();
    }catch (Exception e){
        //强制手动事务回滚
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return Result.error(100,e.toString());
    }
    }
    //删除试卷
    @Override
    public Result deletePaper(String id) {
        try{
            smartPaperMapper.deleteById(id);
            //获取试卷_题目记录
            List<SmartTopicVo> smartTopicVoList = smartPaperMapper.getTopicListByPaperId(id);
            //删除试题
            for (int i=0; i<smartTopicVoList.size();i++){
                SmartTopicVo smartTopicVo = smartTopicVoList.get(i);
                smartTopicMapper.deleteById(smartTopicVo.getId());
            }
            return Result.OK();
        }catch (Exception e){
            return Result.error(e.toString());
        }
    }

    @Override
    public Result insertTriSurvey(SmartTriSurveyPage smartTriSurveyPage) {
        try{
            SmartPaper smartPaper = new SmartPaper();
            BeanUtils.copyProperties(smartTriSurveyPage,smartPaper);
            smartPaperMapper.insert(smartPaper);
            String paperId = smartPaper.getId();
            Integer selectedCount = smartTriSurveyPage.getSelectedCount();
            System.out.println(selectedCount);
            //插入试卷及题目信息
            SmartTopic t = new SmartTopic();
            for (SmartTopicVo topic:smartTriSurveyPage.getSmartTopicVoList()){
                BeanUtils.copyProperties(topic,t);
                t.setPaperId(smartPaper.getId());
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println(t);
                smartTopicMapper.insert(t);
            }

            //随机选人
            Page<RandomPeople> randomPeoplePage = new Page<RandomPeople>();
            List<RandomPeople> randomPeopleList = new ArrayList<RandomPeople>();
            //在每个村的总家庭数范围内，随机选择对应家庭数量,每个家庭选择一个人
            List<String> allVillageList = smartPeopleService.getAllVillageList();
            for(int i = 0;i<allVillageList.size();i++){
                String villageId = allVillageList.get(i);
                List<String> selectedHomeList = smartPeopleService.getAllHomeListByVillageId(villageId,selectedCount);
                for(int j = 0;j<selectedHomeList.size();j++){
                    String homeCode = selectedHomeList.get(j);
                    RandomPeople randomPeople = smartPeopleService.getSelectedPeoByHomeCode(homeCode);
                    randomPeopleList.add(randomPeople);
                }
            }
            System.out.println(randomPeopleList);
            for(int i = 0;i<randomPeopleList.size();i++){
                String personsId = randomPeopleList.get(i).getUserId();
                SmartPeople smartPeople = new SmartPeople();
                smartPeople.setExamId(paperId);
                smartPeople.setPersonId(personsId);
                smartPeople.setIsFinish("0");
                smartPeopleService.save(smartPeople);
            }
            randomPeoplePage.setRecords(randomPeopleList);

            return Result.OK("success");
        }catch (Exception e){
            //强制手动事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.error(100,e.toString());
        }
    }

    @Override
    public Page<RandomPeople> getTriPeoList(Page<RandomPeople> page,String paperId) {
        return page.setRecords(smartPaperMapper.getTriPeoList(page,paperId));
    }

}
