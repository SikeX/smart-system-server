package org.jeecg.modules.publicityEducation.service.impl;

import org.jeecg.modules.publicityEducation.entity.PublicityEducationPacpa;
import org.jeecg.modules.publicityEducation.mapper.PublicityEducationPacpaMapper;
import org.jeecg.modules.publicityEducation.service.IPublicityEducationPacpaService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 宣传教育参会人员
 * @Author: jeecg-boot
 * @Date:   2021-12-26
 * @Version: V1.0
 */
@Service
public class PublicityEducationPacpaServiceImpl extends ServiceImpl<PublicityEducationPacpaMapper, PublicityEducationPacpa> implements IPublicityEducationPacpaService {
	
	@Autowired
	private PublicityEducationPacpaMapper publicityEducationPacpaMapper;
	
	@Override
	public List<PublicityEducationPacpa> selectByMainId(String mainId) {
		return publicityEducationPacpaMapper.selectByMainId(mainId);
	}
}
