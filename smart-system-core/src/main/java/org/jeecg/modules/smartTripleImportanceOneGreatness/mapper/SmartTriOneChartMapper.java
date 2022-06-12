package org.jeecg.modules.smartTripleImportanceOneGreatness.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.smartTripleImportanceOneGreatness.entity.TypeCount;

import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 三重一大统计
 * @Author: zxh
 * @Date:   2021-11-09
 * @Version: V1.0
 */
public interface SmartTriOneChartMapper extends BaseMapper<TypeCount> {

    List<TypeCount> countByVerifyStatus(@Param("year")String year,@Param("month")String month);

    List<TypeCount> getAllType();

    List<TypeCount> countByType(@Param("year")String year,@Param("month")String month);
}
