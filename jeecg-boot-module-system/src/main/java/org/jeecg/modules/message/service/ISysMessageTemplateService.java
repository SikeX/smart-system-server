package org.jeecg.modules.message.service;

import java.util.List;

import org.jeecg.common.system.base.service.JeecgService;
import org.jeecg.modules.message.entity.PersonInfo;
import org.jeecg.modules.message.entity.PunishPerson;
import org.jeecg.modules.message.entity.SmartTaskManage;
import org.jeecg.modules.message.entity.SysMessageTemplate;

/**
 * @Description: 消息模板
 * @Author: jeecg-boot
 * @Date:  2019-04-09
 * @Version: V1.0
 */
public interface ISysMessageTemplateService extends JeecgService<SysMessageTemplate> {
    List<SysMessageTemplate> selectByCode(String code);
    public void notNull(SysMessageTemplate sysMessageTemplate);

    public List<PersonInfo> getBirthList();

    public List<SmartTaskManage> getTaskDetail();

    List<PunishPerson> getPunishList();
}
