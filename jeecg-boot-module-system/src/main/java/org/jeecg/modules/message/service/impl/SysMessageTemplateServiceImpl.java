package org.jeecg.modules.message.service.impl;

import org.jeecg.common.system.base.service.impl.JeecgServiceImpl;
import org.jeecg.modules.message.entity.PersonInfo;
import org.jeecg.modules.message.entity.PunishPerson;
import org.jeecg.modules.message.entity.SmartTaskManage;
import org.jeecg.modules.message.entity.SysMessageTemplate;
import org.jeecg.modules.message.mapper.SysMessageTemplateMapper;
import org.jeecg.modules.message.service.ISysMessageTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @Description: 消息模板
 * @Author: jeecg-boot
 * @Date:  2019-04-09
 * @Version: V1.0
 */
@Service
public class SysMessageTemplateServiceImpl extends JeecgServiceImpl<SysMessageTemplateMapper, SysMessageTemplate> implements ISysMessageTemplateService {

    @Autowired
    private SysMessageTemplateMapper sysMessageTemplateMapper;


    @Override
    public List<SysMessageTemplate> selectByCode(String code) {
        return sysMessageTemplateMapper.selectByCode(code);
    }

    @Override
    public void notNull(SysMessageTemplate sysMessageTemplate) {
        if(sysMessageTemplate.getTemplateType().equals("系统")){
            sysMessageTemplate.setTemplateType("4");
        }else if(sysMessageTemplate.getTemplateType().equals("微信")){
            sysMessageTemplate.setTemplateType("3");
        }else if(sysMessageTemplate.getTemplateType().equals("邮件")){
            sysMessageTemplate.setTemplateType("2");
        }else if(sysMessageTemplate.getTemplateType().equals("短信")){
            sysMessageTemplate.setTemplateType("1");
        }
    }

    @Override
    public List<PersonInfo> getBirthList() {

        return sysMessageTemplateMapper.getBirthList();
    }

    @Override
    public List<SmartTaskManage> getTaskDetail() {

        return sysMessageTemplateMapper.getTaskDetail();
    }

    @Override
    public List<PunishPerson> getPunishList() {
        return sysMessageTemplateMapper.getPunishList();
    }
}
