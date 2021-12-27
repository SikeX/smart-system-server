package org.jeecg.modules.publicityEducation.service;

import org.jeecg.modules.publicityEducation.entity.PublicityEducationAnnex;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 宣传教育附件表
 * @Author: jeecg-boot
 * @Date:   2021-12-26
 * @Version: V1.0
 */
public interface IPublicityEducationAnnexService extends IService<PublicityEducationAnnex> {

	public List<PublicityEducationAnnex> selectByMainId(String mainId);
}
