package org.jeecg.modules.smart_reception.service;

import org.jeecg.modules.smart_reception.entity.Smart_8List;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 接待清单
 * @Author: jeecg-boot
 * @Date:   2022-02-28
 * @Version: V1.0
 */
public interface ISmart_8ListService extends IService<Smart_8List> {

	public List<Smart_8List> selectByMainId(String mainId);
}
