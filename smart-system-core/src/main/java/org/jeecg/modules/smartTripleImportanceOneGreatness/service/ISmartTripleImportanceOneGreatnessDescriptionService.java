package org.jeecg.modules.smartTripleImportanceOneGreatness.service;

import org.jeecg.modules.smartTripleImportanceOneGreatness.entity.SmartTripleImportanceOneGreatnessDescription;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 三重一大附件表
 * @Author: jeecg-boot
 * @Date:   2021-11-12
 * @Version: V1.0
 */
public interface ISmartTripleImportanceOneGreatnessDescriptionService extends IService<SmartTripleImportanceOneGreatnessDescription> {

	public List<SmartTripleImportanceOneGreatnessDescription> selectByMainId(String mainId);
}
