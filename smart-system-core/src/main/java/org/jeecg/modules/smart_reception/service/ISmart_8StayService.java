package org.jeecg.modules.smart_reception.service;

import org.jeecg.modules.smart_reception.entity.Smart_8Stay;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 住宿信息
 * @Author: jeecg-boot
 * @Date:   2022-02-28
 * @Version: V1.0
 */
public interface ISmart_8StayService extends IService<Smart_8Stay> {

	public List<Smart_8Stay> selectByMainId(String mainId);
}
