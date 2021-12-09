package org.jeecg.modules.smart_data_sheet_new.service.impl;

import org.jeecg.modules.smart_data_sheet.mapper.SmartDataSheetFileMapper;
import org.jeecg.modules.smart_data_sheet.mapper.SmartDataSheetMapper;
import org.jeecg.modules.smart_data_sheet_new.entity.SmartDataSheetNew;
import org.jeecg.modules.smart_data_sheet_new.mapper.SmartDataSheetNewMapper;
import org.jeecg.modules.smart_data_sheet_new.service.ISmartDataSheetNewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 资料库
 * @Author: jeecg-boot
 * @Date:   2021-12-07
 * @Version: V1.0
 */
@Service
public class SmartDataSheetNewServiceImpl extends ServiceImpl<SmartDataSheetNewMapper, SmartDataSheetNew> implements ISmartDataSheetNewService {
    @Autowired
    private SmartDataSheetNewMapper smartDataSheetNewMapper;

    @Override
    public String getDepartNameById(String id) {
        return smartDataSheetNewMapper.getDepartNameById(id);
    }

    @Override
    public String getDepartIdByOrgCode(String orgCode) {
        return smartDataSheetNewMapper.getDepartIdByOrgCode(orgCode);
    }

    @Override
    public String getUserNameById(String id){
        return  smartDataSheetNewMapper.getUserNameById(id);
    }

    @Override
    public List<String> getChildrenIdByOrgCode(String orgCode) {
        return smartDataSheetNewMapper.getChildrenIdByOrgCode(orgCode);
    }
}
