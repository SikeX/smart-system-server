package org.jeecg.modules.smart_8regulations_for_reception.service;

import org.jeecg.modules.smart_8regulations_for_reception.entity.Smart_8regulationsForReceptionAppendix;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 八项规定公务接待信息附件表
 * @Author: jeecg-boot
 * @Date:   2021-11-08
 * @Version: V1.0
 */
public interface ISmart_8regulationsForReceptionAppendixService extends IService<Smart_8regulationsForReceptionAppendix> {

	public List<Smart_8regulationsForReceptionAppendix> selectByMainId(String mainId);
}
