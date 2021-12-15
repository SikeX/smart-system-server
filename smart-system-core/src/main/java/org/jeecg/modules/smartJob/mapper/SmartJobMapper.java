package org.jeecg.modules.smartJob.mapper;

import java.util.List;

import org.jeecg.modules.SmartPunishPeople.entity.SmartPunishPeople;
import org.jeecg.modules.smartJob.entity.SmartJob;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.smartJob.entity.SysUser;
import org.jeecg.modules.smartPostMarriage.entity.SmartPostMarriageReport;
import org.jeecg.modules.smartPremaritalFiling.entity.SmartPremaritalFiling;

public interface SmartJobMapper extends BaseMapper<SmartJob> {

    List<SmartPunishPeople> getPunish();

    List<SysUser> getAnniversaryList();

    List<SysUser> getAllUser();

    List<SysUser> getUsers(List<String> userids);

    List<SmartJob> initGetTasks();

    String getOrgId(String from);

    SmartPremaritalFiling getPreMarrayInfo(String person);

    SmartPostMarriageReport selectPostByName(String peopleId);

    SmartPremaritalFiling getPreById(String jobBean);

    SmartPostMarriageReport selectByPreId(String preId);

    SmartJob getByBean(String id);

    List<SmartPremaritalFiling> selectNotReport();

    SysUser getPeopleInfo(String peopleId);
}
