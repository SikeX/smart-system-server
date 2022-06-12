package org.jeecg.modules.SmartPaper.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.SmartPaper.entity.SmartPeople;
import org.jeecg.modules.SmartPaper.entity.SmartSubmit;
import org.jeecg.modules.SmartPaper.entity.SmartTopic;
import org.jeecg.modules.SmartPaper.mapper.SmartPeopleMapper;
import org.jeecg.modules.SmartPaper.mapper.SmartSubmitMapper;
import org.jeecg.modules.SmartPaper.mapper.SmartTopicMapper;
import org.jeecg.modules.SmartPaper.service.ISmartSurveyService;
import org.jeecg.modules.SmartPaper.vo.SmartSubmitExamVo;
import org.jeecg.modules.SmartPaper.vo.SmartSubmitSurveyVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional  //事务的注解
public class SmartSurveyServiceImpl extends ServiceImpl
        <SmartPeopleMapper, SmartPeople> implements ISmartSurveyService {
    @Autowired
    private SmartTopicMapper smartTopicMapper;
    @Autowired
    private SmartPeopleMapper smartPeopleMapper;
    @Autowired
    private SmartSubmitMapper smartSubmitMapper;
    /**
     *
     * 试卷提交,自动批改
     *
     *
     */
    @Override
    public Result submitTestSurvey(SmartSubmitSurveyVo smartSubmitSurveyVO) {
        try{
            System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSS");
            System.out.println(smartSubmitSurveyVO);
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            SmartPeople smartPeople = new SmartPeople();
            BeanUtils.copyProperties(smartSubmitSurveyVO,smartPeople);
            List<SmartSubmit> smartSubmitList = smartSubmitSurveyVO.getSmartSubmitList();
            String paperId = (smartSubmitList.get(0)).getPaperId();
            LambdaQueryWrapper<SmartTopic> queryWrapper = new LambdaQueryWrapper();
            queryWrapper.eq(SmartTopic::getPaperId,paperId);
            List<SmartTopic> smartTopicList  = smartTopicMapper.selectList(queryWrapper);
            int grade = 0;
            String userId = sysUser.getId();
            String examId = smartPeople.getExamId();
            //提交时间
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println("当前时间：" + sdf.format(date));
            smartPeople.setSubmitTime(date);

            //判断是否评分
            Integer isMark = smartSubmitSurveyVO.getIsMark();
            if(isMark == 1){
                //总分
                for (int i=0;i<smartSubmitList.size();i++){
                    SmartSubmit smartSubmit = new SmartSubmit();
                    BeanUtils.copyProperties(smartSubmitList.get(i),smartSubmit);
                    SmartTopic topic = smartTopicList.get(i);
                    smartSubmit.setQuestionId(topic.getId());
                    smartSubmit.setUserId(sysUser.getId());
                    smartSubmitMapper.insert(smartSubmit);
                    /*判断回答是否正确，单选及判断*/
                    if(smartSubmit.getType().equals("0") || smartSubmit.getType().equals("2")){
                        if(topic.getCorrectAnswer().equals(smartSubmit.getSubmitAnswer())){
                            //计算总分
                            grade = grade + topic.getScore();
                        }
                    }
                    //多选题
                    else if(smartSubmit.getType().equals("1")){
                        String[] correctList = topic.getCorrectAnswer().split("\n");
                        String[] submitList = smartSubmit.getSubmitAnswer().split("\n");
                        List<String> list = new ArrayList<String>();
                        //倒序赋值，list.remove删除时序号会前移
                        for(int m= submitList.length-1;m>=0;m--){
                            list.add(submitList[m]);
                        }
                        System.out.println("CCCCCCCCCCCCCCCCCCCCCCCCCC");
                        System.out.println(correctList[0]);
                        //与正确答案选项个数不一样
                        if(correctList.length !=submitList.length){

                        }
                        //与正确答案选项个数一样
                        else {
                            //循环判断答案（提交答案顺序可能与正确答案不一致）
                            for(int j=submitList.length-1;j>=0;j--){
                                for(int k= 0;k<correctList.length;k++){
                                    if(submitList[j].equals(correctList[k])){
                                        list.remove(0);
                                    }
                                    else {
                                    }
                                }
                            }
                            //list为空，正确
                            if (list.size() == 0){
                                grade = grade + topic.getScore();
                            }else {

                            }

                        }
                    }

                    //填空题
                    else if(smartSubmit.getType().equals("3")) {
                        String[] correctAnswerArr = topic.getCorrectAnswer().split("\n");
                        String[] submitAnswerArr = smartSubmit.getSubmitAnswer().split("\n");
                        double topicGrade = 0;
                        for(int n=0;n<correctAnswerArr.length;n++){
                            System.out.println("CCCCCCCCCCCCCCCCCCCCCCCCCC");
                            System.out.println((double)topic.getScore() / correctAnswerArr.length);
                            if(correctAnswerArr[n].equals(submitAnswerArr[n])){
                                topicGrade = topicGrade +((double) topic.getScore() / correctAnswerArr.length);
                            }
                        }
                        grade = grade + (int) topicGrade;
                    }
                    //简答题
                    else if(smartSubmit.getType().equals("4")){
                        String[] correctAnswerArr = topic.getCorrectAnswer().split("\n");
                        double topicGrade = 0;
                        for (String ca : correctAnswerArr) {
                            if(smartSubmit.getSubmitAnswer().contains(ca)){
                                topicGrade = topicGrade +  ((double)topic.getScore() / correctAnswerArr.length);
                            }
                        }
                        grade = grade + (int) topicGrade;

                    }

                }
                //记录最高成绩
                Integer oldGrade =  smartPeopleMapper.getGrade(userId,examId);
                System.out.println("oldGradeoldGradeoldGradeoldGradeoldGradeoldGradeoldGradeoldGrade");
                System.out.println(oldGrade);
                //String stringGrade = String.valueOf(oldGrade);
                //成绩不为空，记录最高成绩
                if(oldGrade != -1){
                    if(oldGrade<grade){
                        smartPeopleMapper.updateGrade(userId,examId,grade,date);
                    }else {

                    }
                }
                //成绩为空，插入成绩
                else{
                    smartPeopleMapper.updateGrade(userId,examId,grade,date);
                }
            }else {
                for (int i=0;i<smartSubmitList.size();i++){
                    SmartSubmit smartSubmit = new SmartSubmit();
                    BeanUtils.copyProperties(smartSubmitList.get(i),smartSubmit);
                    SmartTopic topic = smartTopicList.get(i);
                    smartSubmit.setQuestionId(topic.getId());
                    smartSubmit.setUserId(sysUser.getId());
                    smartSubmitMapper.insert(smartSubmit);
                }
                QueryWrapper<SmartPeople> updateWrapper = new QueryWrapper<SmartPeople>();
                updateWrapper
                        .eq("exam_id",examId)
                        .eq("person_id", userId);
                //无分问卷，成绩为空代表未参与（若空数据库转换成绩为-1），0表示已参与
                smartPeople.setExamGrade(0);
                smartPeopleMapper.update(smartPeople,updateWrapper);
            }
            return Result.OK(grade);
        }catch (Exception e){
            //强制手动事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.error("请求失败",e.toString());
        }
    }

    @Override
    public Result submitTriPreSurvey(SmartSubmitSurveyVo smartSubmitSurveyVO) {
        try{
            System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSS");
            System.out.println(smartSubmitSurveyVO);
            /**调查人*/
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            SmartPeople smartPeople = new SmartPeople();

            BeanUtils.copyProperties(smartSubmitSurveyVO,smartPeople);
            List<SmartSubmit> smartSubmitList = smartSubmitSurveyVO.getSmartSubmitList();
            String paperId = (smartSubmitList.get(0)).getPaperId();
            LambdaQueryWrapper<SmartTopic> queryWrapper = new LambdaQueryWrapper();
            queryWrapper.eq(SmartTopic::getPaperId,paperId);
            List<SmartTopic> smartTopicList  = smartTopicMapper.selectList(queryWrapper);
            int grade = 0;
            //被访人ID
            String userId = smartSubmitSurveyVO.getPersonId();
            //调查人ID
            String inquirerId = sysUser.getId();
            String examId = smartPeople.getExamId();
            String  satisfaction = smartSubmitSurveyVO.getSatisfaction();
            String isReport = smartSubmitSurveyVO.getIsReport();
            String isFinish = smartPeopleMapper.selectByUIdEId(examId,userId);
            System.out.println("isFinishisFinishisFinishisFinishisFinishisFinish");
            System.out.println("isFinish:"+isFinish);
            //提交时间
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println("当前时间：" + sdf.format(date));
            smartPeople.setSubmitTime(date);

            QueryWrapper<SmartPeople> updateWrapper = new QueryWrapper<SmartPeople>();
            updateWrapper
                    .eq("exam_id",examId)
                    .eq("person_id", userId);

            //判断是否评分
            Integer isMark = smartSubmitSurveyVO.getIsMark();
            if(isMark == 1){
                //总分
                for (int i=0;i<smartSubmitList.size();i++){
                    SmartSubmit smartSubmit = new SmartSubmit();
                    BeanUtils.copyProperties(smartSubmitList.get(i),smartSubmit);
                    SmartTopic topic = smartTopicList.get(i);
                    smartSubmit.setQuestionId(topic.getId());
                    smartSubmit.setUserId(userId);
                    smartSubmitMapper.insert(smartSubmit);
                    /*判断回答是否正确，单选及判断*/
                    if(smartSubmit.getType().equals("0") || smartSubmit.getType().equals("2")){
                        if(topic.getCorrectAnswer().equals(smartSubmit.getSubmitAnswer())){
                            //计算总分
                            grade = grade + topic.getScore();
                        }
                    }
                    //多选题
                    else if(smartSubmit.getType().equals("1")){
                        String[] correctList = topic.getCorrectAnswer().split("\n");
                        String[] submitList = smartSubmit.getSubmitAnswer().split("\n");
                        List<String> list = new ArrayList<String>();
                        //倒序赋值，list.remove删除时序号会前移
                        for(int m= submitList.length-1;m>=0;m--){
                            list.add(submitList[m]);
                        }
                        System.out.println("CCCCCCCCCCCCCCCCCCCCCCCCCC");
                        System.out.println(correctList[0]);
                        //与正确答案选项个数不一样
                        if(correctList.length !=submitList.length){

                        }
                        //与正确答案选项个数一样
                        else {
                            //循环判断答案（提交答案顺序可能与正确答案不一致）
                            for(int j=submitList.length-1;j>=0;j--){
                                for(int k= 0;k<correctList.length;k++){
                                    if(submitList[j].equals(correctList[k])){
                                        list.remove(0);
                                    }
                                    else {
                                    }
                                }
                            }
                            //list为空，正确
                            if (list.size() == 0){
                                grade = grade + topic.getScore();
                            }else {

                            }

                        }
                    }

                    //填空题
                    else if(smartSubmit.getType().equals("3")) {
                        String[] correctAnswerArr = topic.getCorrectAnswer().split("\n");
                        String[] submitAnswerArr = smartSubmit.getSubmitAnswer().split("\n");
                        double topicGrade = 0;
                        for(int n=0;n<correctAnswerArr.length;n++){
                            System.out.println("CCCCCCCCCCCCCCCCCCCCCCCCCC");
                            System.out.println((double)topic.getScore() / correctAnswerArr.length);
                            if(correctAnswerArr[n].equals(submitAnswerArr[n])){
                                topicGrade = topicGrade +((double) topic.getScore() / correctAnswerArr.length);
                            }
                        }
                        grade = grade + (int) topicGrade;
                    }
                    //简答题
                    else if(smartSubmit.getType().equals("4")){
                        String[] correctAnswerArr = topic.getCorrectAnswer().split("\n");
                        double topicGrade = 0;
                        for (String ca : correctAnswerArr) {
                            if(smartSubmit.getSubmitAnswer().contains(ca)){
                                topicGrade = topicGrade +  ((double)topic.getScore() / correctAnswerArr.length);
                            }
                        }
                        grade = grade + (int) topicGrade;

                    }

                }

                if(isFinish.equals("0")){
                    smartPeople.setExamGrade(grade);
                    smartPeople.setInquirerId(inquirerId);
                    smartPeople.setIsFinish("1");
                    smartPeople.setSatisfaction(satisfaction);
                    smartPeople.setIsReport(isReport);
                    smartPeopleMapper.update(smartPeople,updateWrapper);
//                    isFinish ="1";
//                    smartPeopleMapper.updateSumSur(examId,userId,grade,isFinish,inquirerId,satisfaction,isReport);
                }
            }else {
                for (int i=0;i<smartSubmitList.size();i++){
                    SmartSubmit smartSubmit = new SmartSubmit();
                    BeanUtils.copyProperties(smartSubmitList.get(i),smartSubmit);
                    SmartTopic topic = smartTopicList.get(i);
                    smartSubmit.setQuestionId(topic.getId());
                    smartSubmit.setUserId(userId);
                    System.out.println("smartSubmit---"+smartSubmit);
                    int t = smartSubmitMapper.insert(smartSubmit);
                    System.out.println("insert---"+t);
                }
                if(isFinish.equals("0")){
                    //无分问卷，成绩为空代表未参与（若空数据库转换成绩为-1），0表示已参与
                    smartPeople.setExamGrade(0);
                    smartPeople.setIsFinish("1");
                    smartPeople.setInquirerId(inquirerId);
                    smartPeople.setSatisfaction(satisfaction);
                    smartPeople.setIsReport(isReport);
                    smartPeopleMapper.update(smartPeople,updateWrapper);
//                    grade = 0;
//                    isFinish ="1";
//                    int r = smartPeopleMapper.updateSumSur(examId,userId,grade,isFinish,inquirerId,satisfaction,isReport);
//                    System.out.println(r);
                }

            }
            return Result.OK(grade);
        }catch (Exception e){
            //强制手动事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.error("请求失败",e.toString());
        }
    }


}
