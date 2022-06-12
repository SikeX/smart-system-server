package org.jeecg.modules.smartPostFuneralReport.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.interaction.mapper.SmartVillageCommentMapper;
import org.jeecg.modules.interaction.vo.CommentVo;
import org.jeecg.modules.smartPostFuneralReport.entity.SmartPostFuneralReport;
import org.jeecg.modules.smartPostFuneralReport.mapper.SmartPostFuneralReportMapper;
import org.jeecg.modules.smartPostFuneralReport.service.ISmartPostFuneralReportService;
import org.jeecg.modules.smartPostFuneralReport.vo.FuneralReport;
import org.jeecg.modules.smartPostMarriage.entity.SmartPostMarriageReport;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Description: 丧事事后报备表
 * @Author: jeecg-boot
 * @Date:   2022-02-26
 * @Version: V1.0
 */
@Service
public class SmartPostFuneralReportServiceImpl extends ServiceImpl<SmartPostFuneralReportMapper, SmartPostFuneralReport> implements ISmartPostFuneralReportService {
    @Resource
    private SmartPostFuneralReportMapper smartPostFuneralMapper;


    public String getDicText(String dictId,String itemValue){

        return smartPostFuneralMapper.getDicText(dictId,itemValue);
    }

    public String getDepByOrgCode(String orgCode){

        return smartPostFuneralMapper.getDepByOrgCode(orgCode);
    }

    public String getPostByCode(String code){

        return smartPostFuneralMapper.getPostByCode(code);
    }
    @Override
    public FuneralReport getFuneralReport(String id){

        return smartPostFuneralMapper.getFuneralReport(id);
    }
    @Override
    public List<FuneralReport> listByIds(List<String> ids){
       List<FuneralReport> reports = new ArrayList<>();
        for(String id:ids)
        {
            FuneralReport report = smartPostFuneralMapper.getFuneralReport(id);
            reports.add(report);
        }
        return reports;
    }

    public String getAge(Date birthDay){
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) { //出生日期晚于当前时间，无法计算
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);  //当前年份
        int monthNow = cal.get(Calendar.MONTH);  //当前月份
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH); //当前日期
        cal.setTime(birthDay);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        int age = yearNow - yearBirth;   //计算整岁数
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;}}//当前日期在生日之前，年龄减一
             else{
                age--;//当前月份在生日之前，年龄减一

            } }
        return Integer.toString(age); }
    @Override
    public SmartPostFuneralReport getByPreId(String preId) {
        return smartPostFuneralMapper.getByPreId(preId);
    }
}
