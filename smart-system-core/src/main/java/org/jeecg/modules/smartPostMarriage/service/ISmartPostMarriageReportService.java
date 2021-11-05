package org.jeecg.modules.smartPostMarriage.service;

import org.jeecg.modules.smartPostMarriage.entity.SmartPostMarriageReportFile;
import org.jeecg.modules.smartPostMarriage.entity.SmartPostMarriageReport;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 8项规定婚后报备表
 * @Author: jeecg-boot
 * @Date:   2021-11-05
 * @Version: V1.0
 */
public interface ISmartPostMarriageReportService extends IService<SmartPostMarriageReport> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(SmartPostMarriageReport smartPostMarriageReport,List<SmartPostMarriageReportFile> smartPostMarriageReportFileList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(SmartPostMarriageReport smartPostMarriageReport,List<SmartPostMarriageReportFile> smartPostMarriageReportFileList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
