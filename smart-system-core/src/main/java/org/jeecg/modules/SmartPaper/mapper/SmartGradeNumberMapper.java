package org.jeecg.modules.SmartPaper.mapper;

import org.jeecg.modules.SmartPaper.entity.SmartGradeNumber;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 成绩分布人数表
 * @Author: jeecg-boot
 * @Date:   2021-11-24
 * @Version: V1.0
 */
public interface SmartGradeNumberMapper extends BaseMapper<SmartGradeNumber> {

    Integer excellentCount(double excellent_line);//优秀总数量
    Integer goodCount(double good_line);//良好总数量
    Integer passCount();//及格总数量
    Integer failCount();//不及格总数量
}
