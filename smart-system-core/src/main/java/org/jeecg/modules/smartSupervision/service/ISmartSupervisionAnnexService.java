package org.jeecg.modules.smartSupervision.service;

import org.jeecg.modules.smartSupervision.entity.SmartSupervisionAnnex;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 8项规定监督检查附件表
 * @Author: jeecg-boot
 * @Date:   2021-11-10
 * @Version: V1.0
 */
public interface ISmartSupervisionAnnexService extends IService<SmartSupervisionAnnex> {

	public List<SmartSupervisionAnnex> selectByMainId(String mainId);
}
