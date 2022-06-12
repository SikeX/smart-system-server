package org.jeecg.modules.smart_data_sheet.service;

import org.jeecg.modules.smart_data_sheet.entity.SmartDataSheetFile;
import org.jeecg.modules.smart_data_sheet.entity.SmartDataSheet;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 资料库
 * @Author: jeecg-boot
 * @Date:   2021-11-11
 * @Version: V1.0
 */
public interface ISmartDataSheetService extends IService<SmartDataSheet> {

	/**
	 * 根据部门编码查询部门ID
	 *
	 * @param orgCode 部门编码
	 * @return 部门ID
	 */
	String getDepartIdByOrgCode(String orgCode);
	/**
	 * 根据部门编码获所有子部门的ID
	 *
	 * @param orgCode 部门编码
	 * @return 子部门ID列表
	 */
	List<String> getChildrenIdByOrgCode(String orgCode);

	/**
	 * 添加一对多
	 *
	 */
	public void saveMain(SmartDataSheet smartDataSheet,List<SmartDataSheetFile> smartDataSheetFileList) ;

	/**
	 * 修改一对多
	 *
	 */
	public void updateMain(SmartDataSheet smartDataSheet,List<SmartDataSheetFile> smartDataSheetFileList);

	/**
	 * 删除一对多
	 */
	public void delMain (String id);

	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);

}
