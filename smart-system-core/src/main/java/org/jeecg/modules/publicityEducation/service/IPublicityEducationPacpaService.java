package org.jeecg.modules.publicityEducation.service;

import org.jeecg.modules.publicityEducation.entity.PublicityEducationPacpa;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 宣传教育参会人员
 * @Author: jeecg-boot
 * @Date:   2021-12-26
 * @Version: V1.0
 */
public interface IPublicityEducationPacpaService extends IService<PublicityEducationPacpa> {

	public List<PublicityEducationPacpa> selectByMainId(String mainId);
}
