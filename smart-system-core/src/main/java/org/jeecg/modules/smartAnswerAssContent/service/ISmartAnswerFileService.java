package org.jeecg.modules.smartAnswerAssContent.service;

import org.jeecg.modules.smartAnswerAssContent.entity.SmartAnswerFile;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 要点答题附件
 * @Author: jeecg-boot
 * @Date:   2022-02-21
 * @Version: V1.0
 */
public interface ISmartAnswerFileService extends IService<SmartAnswerFile> {

	public List<SmartAnswerFile> selectByMainId(String mainId);
}
