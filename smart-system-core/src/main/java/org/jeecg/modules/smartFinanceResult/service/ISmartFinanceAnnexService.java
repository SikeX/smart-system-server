package org.jeecg.modules.smartFinanceResult.service;

import org.jeecg.modules.smartFinanceResult.entity.SmartFinanceAnnex;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 8项规定财物收支附件
 * @Author: jeecg-boot
 * @Date:   2021-11-17
 * @Version: V1.0
 */
public interface ISmartFinanceAnnexService extends IService<SmartFinanceAnnex> {

	public List<SmartFinanceAnnex> selectByMainId(String mainId);
}
