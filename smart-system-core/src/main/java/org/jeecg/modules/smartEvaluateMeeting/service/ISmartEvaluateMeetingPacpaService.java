package org.jeecg.modules.smartEvaluateMeeting.service;

import org.jeecg.modules.smartEvaluateMeeting.entity.SmartEvaluateMeetingPacpa;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 述责述廉参与人表
 * @Author: jeecg-boot
 * @Date:   2021-11-02
 * @Version: V1.0
 */
public interface ISmartEvaluateMeetingPacpaService extends IService<SmartEvaluateMeetingPacpa> {

	public List<SmartEvaluateMeetingPacpa> selectByMainId(String mainId);
}
