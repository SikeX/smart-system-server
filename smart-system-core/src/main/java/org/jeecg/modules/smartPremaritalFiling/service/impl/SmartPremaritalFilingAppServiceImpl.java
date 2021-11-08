package org.jeecg.modules.smartPremaritalFiling.service.impl;

import org.jeecg.modules.smartPremaritalFiling.entity.SmartPremaritalFilingApp;
import org.jeecg.modules.smartPremaritalFiling.mapper.SmartPremaritalFilingAppMapper;
import org.jeecg.modules.smartPremaritalFiling.service.ISmartPremaritalFilingAppService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 8项规定婚前报备表附表
 * @Author: jeecg-boot
 * @Date:   2021-11-08
 * @Version: V1.0
 */
@Service
public class SmartPremaritalFilingAppServiceImpl extends ServiceImpl<SmartPremaritalFilingAppMapper, SmartPremaritalFilingApp> implements ISmartPremaritalFilingAppService {
	
	@Autowired
	private SmartPremaritalFilingAppMapper smartPremaritalFilingAppMapper;
	
	@Override
	public List<SmartPremaritalFilingApp> selectByMainId(String mainId) {
		return smartPremaritalFilingAppMapper.selectByMainId(mainId);
	}
}
