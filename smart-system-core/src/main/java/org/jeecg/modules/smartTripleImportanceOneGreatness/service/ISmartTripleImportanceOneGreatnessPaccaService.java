package org.jeecg.modules.smartTripleImportanceOneGreatness.service;

import org.jeecg.modules.smartTripleImportanceOneGreatness.entity.SmartTripleImportanceOneGreatnessPacca;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 三重一大参会人员表
 * @Author: jeecg-boot
 * @Date:   2021-11-13
 * @Version: V1.0
 */
public interface ISmartTripleImportanceOneGreatnessPaccaService extends IService<SmartTripleImportanceOneGreatnessPacca> {

	public List<SmartTripleImportanceOneGreatnessPacca> selectByMainId(String mainId);
}
