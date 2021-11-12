package org.jeecg.modules.smart_data_sheet.service.impl;

import org.jeecg.modules.smart_data_sheet.entity.SmartDataSheet;
import org.jeecg.modules.smart_data_sheet.entity.SmartDataSheetFile;
import org.jeecg.modules.smart_data_sheet.mapper.SmartDataSheetFileMapper;
import org.jeecg.modules.smart_data_sheet.mapper.SmartDataSheetMapper;
import org.jeecg.modules.smart_data_sheet.service.ISmartDataSheetService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 资料库
 * @Author: jeecg-boot
 * @Date:   2021-11-11
 * @Version: V1.0
 */
@Service
public class SmartDataSheetServiceImpl extends ServiceImpl<SmartDataSheetMapper, SmartDataSheet> implements ISmartDataSheetService {

	@Autowired
	private SmartDataSheetMapper smartDataSheetMapper;
	@Autowired
	private SmartDataSheetFileMapper smartDataSheetFileMapper;

	@Override
	public String getDepartIdByOrgCode(String orgCode) {
		return smartDataSheetMapper.getDepartIdByOrgCode(orgCode);
	}

	@Override
	public List<String> getChildrenIdByOrgCode(String orgCode) {
		return smartDataSheetMapper.getChildrenIdByOrgCode(orgCode);
	}


	@Override
	@Transactional
	public void saveMain(SmartDataSheet smartDataSheet, List<SmartDataSheetFile> smartDataSheetFileList) {
		smartDataSheetMapper.insert(smartDataSheet);
		if(smartDataSheetFileList!=null && smartDataSheetFileList.size()>0) {
			for(SmartDataSheetFile entity:smartDataSheetFileList) {
				//外键设置
				entity.setMainId(smartDataSheet.getId());
				smartDataSheetFileMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(SmartDataSheet smartDataSheet,List<SmartDataSheetFile> smartDataSheetFileList) {
		smartDataSheetMapper.updateById(smartDataSheet);

		//1.先删除子表数据
		smartDataSheetFileMapper.deleteByMainId(smartDataSheet.getId());

		//2.子表数据重新插入
		if(smartDataSheetFileList!=null && smartDataSheetFileList.size()>0) {
			for(SmartDataSheetFile entity:smartDataSheetFileList) {
				//外键设置
				entity.setMainId(smartDataSheet.getId());
				smartDataSheetFileMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		smartDataSheetFileMapper.deleteByMainId(id);
		smartDataSheetMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			smartDataSheetFileMapper.deleteByMainId(id.toString());
			smartDataSheetMapper.deleteById(id);
		}
	}

}
