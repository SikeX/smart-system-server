package org.jeecg.modules.smartJob.service;

import org.jeecg.modules.SmartPunishPeople.entity.SmartPunishPeople;
import org.jeecg.modules.smartJob.entity.SmartJob;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.smartJob.entity.SysUser;
import org.jeecg.modules.smartPostMarriage.entity.SmartPostMarriageReport;
import org.jeecg.modules.smartPremaritalFiling.entity.SmartPremaritalFiling;

import java.util.List;

public interface ISmartJobService extends IService<SmartJob> {

    boolean edit(SmartJob smartJob, String sendFrom);

    boolean openJob(SmartJob smartJob, String sendFrom);


    List<SmartPunishPeople> getPunish();

    List<SysUser> getAnniversaryList();

    List<SysUser> getAllUser();

    List<SysUser> selectBatchIds(String ids);

    List<SysUser> getUsers(List<String> userids);

    void updateStatus(String jobBean);

    List<SmartJob> initGetTasks();

    String getOrgId(String from);

    boolean init(SmartJob s, String createBy);

    SmartPremaritalFiling getPreMarrayInfo(String person);

    boolean selectPostByName(String peopleId);

    SmartPremaritalFiling getPreById(String jobBean);

    boolean isReport(String preId);

    SmartJob getByBean(String id);

    List<SmartPremaritalFiling> selectNotReport();

    SmartPostMarriageReport selectByPreId(String id);

    void changeJobStatus(SmartJob smartJob);

    SysUser getPeopleInfo(String peopleId);

    void updatePreIsReport(String id);
}
