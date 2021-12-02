package org.jeecg.modules.smartReportingInformation.service.impl;

import org.jeecg.modules.smartReportingInformation.entity.SmartReportingDescription;
import org.jeecg.modules.smartReportingInformation.mapper.SmartReportingDescriptionMapper;
import org.jeecg.modules.smartReportingInformation.service.ISmartReportingDescriptionService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 举报附件表
 * @Author: jeecg-boot
 * @Date:   2021-11-29
 * @Version: V1.0
 */
@Service
public class SmartReportingDescriptionServiceImpl extends ServiceImpl<SmartReportingDescriptionMapper, SmartReportingDescription> implements ISmartReportingDescriptionService {
	
	@Autowired
	private SmartReportingDescriptionMapper smartReportingDescriptionMapper;
	
	@Override
	public List<SmartReportingDescription> selectByMainId(String mainId) {
		return smartReportingDescriptionMapper.selectByMainId(mainId);
	}
}
