package org.jeecg.modules.smartCreateAdvice.service;

import org.jeecg.modules.smartCreateAdvice.entity.SmartCreateAdviceAnnex;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 制发建议附件表
 * @Author: jeecg-boot
 * @Date:   2021-11-13
 * @Version: V1.0
 */
public interface ISmartCreateAdviceAnnexService extends IService<SmartCreateAdviceAnnex> {

	public List<SmartCreateAdviceAnnex> selectByMainId(String mainId);
}
