package org.jeecg.modules.smartTripleImportanceOneGreatness.service;

import org.jeecg.modules.smartTripleImportanceOneGreatness.entity.SmartTripleImportanceOneGreatnessDecription;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 三重一大附件表
 * @Author: jeecg-boot
 * @Date:   2021-11-10
 * @Version: V1.0
 */
public interface ISmartTripleImportanceOneGreatnessDecriptionService extends IService<SmartTripleImportanceOneGreatnessDecription> {

	public List<SmartTripleImportanceOneGreatnessDecription> selectByMainId(String mainId);
}
