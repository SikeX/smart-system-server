package org.jeecg.modules.smartAnswerAssContent.service;

import org.jeecg.modules.smartAnswerAssContent.entity.SmartAnswerAssScore;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 答题评分表
 * @Author: jeecg-boot
 * @Date:   2022-02-21
 * @Version: V1.0
 */
public interface ISmartAnswerAssScoreService extends IService<SmartAnswerAssScore> {

	public List<SmartAnswerAssScore> selectByMainId(String mainId);
}
