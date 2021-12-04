package org.jeecg.modules.smartJob.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.SmartPunishPeople.entity.SmartPunishPeople;
import org.jeecg.modules.smartJob.entity.SmartJob;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.smartJob.entity.SysUser;

public interface SmartJobMapper extends BaseMapper<SmartJob> {

    List<SmartPunishPeople> getPunish();

    List<SysUser> getAnniversaryList();

    List<SysUser> getAllUser();

    List<SysUser> getUsers(List<String> userids);

    List<SmartJob> initGetTasks();

    String getOrgId(String from);
}