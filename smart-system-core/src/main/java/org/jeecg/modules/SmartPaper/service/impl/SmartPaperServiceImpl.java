package org.jeecg.modules.SmartPaper.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.SmartPaper.entity.SmartPaper;
import org.jeecg.modules.SmartPaper.entity.SmartTopic;
import org.jeecg.modules.SmartPaper.mapper.SmartPaperMapper;
import org.jeecg.modules.SmartPaper.mapper.SmartTopicMapper;
import org.jeecg.modules.SmartPaper.service.ISmartPaperService;
import org.jeecg.modules.SmartPaper.vo.SmartPaperPage;
import org.jeecg.modules.SmartPaper.vo.SmartTopicVo;
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
    @Override
    public Result<?> insert(SmartPaperPage smartPaperPage) {
        try{
            SmartPaper smartPaper = new SmartPaper();
            BeanUtils.copyProperties(smartPaperPage,smartPaper);
            smartPaperMapper.insert(smartPaper);

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

            boolean isHas = false;
            for (int i=0; i<oldTopicIdList.size();i++){
                if((t.getId()).equals(oldTopicIdList.get(i))){
                    //System.out.println("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
                    //System.out.println("true");
                    isHas = true;
                    //存在则更新
                    UpdateWrapper<SmartTopic> topicId = new UpdateWrapper<>();
                    topicId.eq("id",t.getId());
                    smartTopicMapper.update(topic,topicId);
                    //移出旧的试卷id集合
                    oldTopicIdList.remove(i);
                    break;
                }
            }
            //不存在则添加
            if(isHas == false){
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

}
