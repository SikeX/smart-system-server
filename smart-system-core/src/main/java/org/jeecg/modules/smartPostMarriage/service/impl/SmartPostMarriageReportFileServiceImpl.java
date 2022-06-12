package org.jeecg.modules.smartPostMarriage.service.impl;

import org.jeecg.modules.smartPostMarriage.entity.SmartPostMarriageReportFile;
import org.jeecg.modules.smartPostMarriage.mapper.SmartPostMarriageReportFileMapper;
import org.jeecg.modules.smartPostMarriage.service.ISmartPostMarriageReportFileService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 8项规定婚后报备宴请发票与附件表
 * @Author: jeecg-boot
 * @Date:   2021-11-10
 * @Version: V1.0
 */
@Service
public class SmartPostMarriageReportFileServiceImpl extends ServiceImpl<SmartPostMarriageReportFileMapper, SmartPostMarriageReportFile> implements ISmartPostMarriageReportFileService {
	
	@Autowired
	private SmartPostMarriageReportFileMapper smartPostMarriageReportFileMapper;
	
	@Override
	public List<SmartPostMarriageReportFile> selectByMainId(String mainId) {
		return smartPostMarriageReportFileMapper.selectByMainId(mainId);
	}
}
