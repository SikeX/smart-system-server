package org.jeecg.modules.smartPostMarriage.service;

import org.jeecg.modules.smartPostMarriage.entity.SmartPostMarriageReportFile;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 8项规定婚后报备宴请发票与附件表
 * @Author: jeecg-boot
 * @Date:   2021-11-05
 * @Version: V1.0
 */
public interface ISmartPostMarriageReportFileService extends IService<SmartPostMarriageReportFile> {

	public List<SmartPostMarriageReportFile> selectByMainId(String mainId);
}
