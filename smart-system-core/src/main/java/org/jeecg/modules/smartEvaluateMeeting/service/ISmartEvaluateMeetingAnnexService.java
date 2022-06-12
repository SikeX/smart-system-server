package org.jeecg.modules.smartEvaluateMeeting.service;

import org.jeecg.modules.smartEvaluateMeeting.entity.SmartEvaluateMeetingAnnex;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 述责述廉附件表
 * @Author: jeecg-boot
 * @Date:   2021-11-11
 * @Version: V1.0
 */
public interface ISmartEvaluateMeetingAnnexService extends IService<SmartEvaluateMeetingAnnex> {

	public List<SmartEvaluateMeetingAnnex> selectByMainId(String mainId);
}
