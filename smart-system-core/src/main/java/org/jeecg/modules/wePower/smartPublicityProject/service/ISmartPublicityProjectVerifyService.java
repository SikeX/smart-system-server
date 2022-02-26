package org.jeecg.modules.wePower.smartPublicityProject.service;

import org.jeecg.modules.wePower.smartPublicityProject.entity.SmartPublicityProjectVerify;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 项目审核
 * @Author: jeecg-boot
 * @Date:   2022-02-16
 * @Version: V1.0
 */
public interface ISmartPublicityProjectVerifyService extends IService<SmartPublicityProjectVerify> {

	public List<SmartPublicityProjectVerify> selectByMainId(String mainId);
}
