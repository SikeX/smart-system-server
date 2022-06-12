package org.jeecg.modules.smartPostMarriage.service.impl;

import org.jeecg.modules.smartJob.entity.SysUser;
import org.jeecg.modules.smartPostMarriage.entity.SmartPostMarriageReport;
import org.jeecg.modules.smartPostMarriage.entity.SmartPostMarriageReportFile;
import org.jeecg.modules.smartPostMarriage.mapper.SmartPostMarriageReportFileMapper;
import org.jeecg.modules.smartPostMarriage.mapper.SmartPostMarriageReportMapper;
import org.jeecg.modules.smartPostMarriage.service.ISmartPostMarriageReportService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 8项规定婚后报备表
 * @Author: jeecg-boot
 * @Date:   2021-11-10
 * @Version: V1.0
 */
@Service("SmartPostMarriageReportServiceImpl")
public class SmartPostMarriageReportServiceImpl extends ServiceImpl<SmartPostMarriageReportMapper, SmartPostMarriageReport> implements ISmartPostMarriageReportService {

	@Autowired
	private SmartPostMarriageReportMapper smartPostMarriageReportMapper;
	@Autowired
	private SmartPostMarriageReportFileMapper smartPostMarriageReportFileMapper;

	@Override
	@Transactional
	public void saveMain(SmartPostMarriageReport smartPostMarriageReport, List<SmartPostMarriageReportFile> smartPostMarriageReportFileList) {
		smartPostMarriageReportMapper.insert(smartPostMarriageReport);
		if(smartPostMarriageReportFileList!=null && smartPostMarriageReportFileList.size()>0) {
			for(SmartPostMarriageReportFile entity:smartPostMarriageReportFileList) {
				//外键设置
				entity.setMainTableId(smartPostMarriageReport.getId());
				smartPostMarriageReportFileMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(SmartPostMarriageReport smartPostMarriageReport,List<SmartPostMarriageReportFile> smartPostMarriageReportFileList) {
		smartPostMarriageReportMapper.updateById(smartPostMarriageReport);

		//1.先删除子表数据
		smartPostMarriageReportFileMapper.deleteByMainId(smartPostMarriageReport.getId());

		//2.子表数据重新插入
		if(smartPostMarriageReportFileList!=null && smartPostMarriageReportFileList.size()>0) {
			for(SmartPostMarriageReportFile entity:smartPostMarriageReportFileList) {
				//外键设置
				entity.setMainTableId(smartPostMarriageReport.getId());
				smartPostMarriageReportFileMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		smartPostMarriageReportFileMapper.deleteByMainId(id);
		smartPostMarriageReportMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			smartPostMarriageReportFileMapper.deleteByMainId(id.toString());
			smartPostMarriageReportMapper.deleteById(id);
		}
	}

	@Override
	public String getDepartIdByOrgCode(String orgCode) {
		return smartPostMarriageReportMapper.getDepartIdByOrgCode(orgCode);
	}

	@Override
	public List<String> getChildrenIdByOrgCode(String orgCode) {
		return smartPostMarriageReportMapper.getChildrenIdByOrgCode(orgCode);
	}

	@Override
	public SmartPostMarriageReport getByPreId(String preId) {
		return smartPostMarriageReportMapper.getByPreId(preId);
	}

	@Override
	public void editPreIsReport(String preId) {
		smartPostMarriageReportMapper.editPreIsReport(preId);
	}

	@Override
	public void setPreIsReport(String preId) {
		smartPostMarriageReportMapper.setPreIsReport(preId);
	}

	@Override
	public void setDelFlagByPreId(String preId) {
		smartPostMarriageReportMapper.setDelFlagByPreId(preId);
	}

	@Override
	public SysUser getSysUser(String id) {
		return smartPostMarriageReportMapper.getSysUser(id);
	}

}
