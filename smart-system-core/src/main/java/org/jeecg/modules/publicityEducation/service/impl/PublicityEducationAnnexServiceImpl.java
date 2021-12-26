package org.jeecg.modules.publicityEducation.service.impl;

import org.jeecg.modules.publicityEducation.entity.PublicityEducationAnnex;
import org.jeecg.modules.publicityEducation.mapper.PublicityEducationAnnexMapper;
import org.jeecg.modules.publicityEducation.service.IPublicityEducationAnnexService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 宣传教育附件表
 * @Author: jeecg-boot
 * @Date:   2021-12-26
 * @Version: V1.0
 */
@Service
public class PublicityEducationAnnexServiceImpl extends ServiceImpl<PublicityEducationAnnexMapper, PublicityEducationAnnex> implements IPublicityEducationAnnexService {
	
	@Autowired
	private PublicityEducationAnnexMapper publicityEducationAnnexMapper;
	
	@Override
	public List<PublicityEducationAnnex> selectByMainId(String mainId) {
		return publicityEducationAnnexMapper.selectByMainId(mainId);
	}
}
