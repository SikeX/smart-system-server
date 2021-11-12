package org.jeecg.modules.smart_8regulations_for_reception.service;

import org.jeecg.modules.smart_8regulations_for_reception.entity.Smart_8regulationsForReceptionActivity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 八项规定公务接待公务活动项目
 * @Author: jeecg-boot
 * @Date:   2021-11-08
 * @Version: V1.0
 */
public interface ISmart_8regulationsForReceptionActivityService extends IService<Smart_8regulationsForReceptionActivity> {

	public List<Smart_8regulationsForReceptionActivity> selectByMainId(String mainId);
}
