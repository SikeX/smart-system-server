package org.jeecg.modules.smartPremaritalFiling.service;

import org.jeecg.modules.smartPremaritalFiling.entity.SmartPremaritalFilingApp;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 8项规定婚前报备表附表
 * @Author: jeecg-boot
 * @Date:   2021-11-02
 * @Version: V1.0
 */
public interface ISmartPremaritalFilingAppService extends IService<SmartPremaritalFilingApp> {

	public List<SmartPremaritalFilingApp> selectByMainId(String mainId);
}
