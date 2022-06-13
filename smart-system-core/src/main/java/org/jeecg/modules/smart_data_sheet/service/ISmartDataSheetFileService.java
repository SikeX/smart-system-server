package org.jeecg.modules.smart_data_sheet.service;

import org.jeecg.modules.smart_data_sheet.entity.SmartDataSheetFile;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 资料库文件
 * @Author: jeecg-boot
 * @Date:   2021-11-11
 * @Version: V1.0
 */
public interface ISmartDataSheetFileService extends IService<SmartDataSheetFile> {

	public List<SmartDataSheetFile> selectByMainId(String mainId);
}
