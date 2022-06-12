package org.jeecg.modules.smartTripleImportanceOneGreatness.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.smartTripleImportanceOneGreatness.entity.TypeCount;


import java.util.List;

/**
 * @Description: 三重一大统计
 * @Author: zxh
 * @Date:   2021-12-06
 * @Version: V1.0
 */
public interface ISmartTriOneChartService extends IService<TypeCount> {

    List<TypeCount> countByVerifyStatus(String year,String month);

    List<TypeCount> getAllType();

    List<TypeCount> countByType(String year,String month);
}
