package org.jeecg.modules.wePower.smartPublicityProject.service.impl;

import org.jeecg.modules.wePower.smartPublicityProject.entity.SmartPublicityProjectVerify;
import org.jeecg.modules.wePower.smartPublicityProject.mapper.SmartPublicityProjectVerifyMapper;
import org.jeecg.modules.wePower.smartPublicityProject.service.ISmartPublicityProjectVerifyService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 项目审核
 * @Author: jeecg-boot
 * @Date:   2021-12-26
 * @Version: V1.0
 */
@Service
public class SmartPublicityProjectVerifyServiceImpl extends ServiceImpl<SmartPublicityProjectVerifyMapper, SmartPublicityProjectVerify> implements ISmartPublicityProjectVerifyService {
	
	@Autowired
	private SmartPublicityProjectVerifyMapper smartPublicityProjectVerifyMapper;
	
	@Override
	public List<SmartPublicityProjectVerify> selectByMainId(String mainId) {
		return smartPublicityProjectVerifyMapper.selectByMainId(mainId);
	}
}
