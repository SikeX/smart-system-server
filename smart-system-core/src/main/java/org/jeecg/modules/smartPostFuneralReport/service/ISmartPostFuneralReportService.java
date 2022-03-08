package org.jeecg.modules.smartPostFuneralReport.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.interaction.vo.CommentVo;
import org.jeecg.modules.smartPostFuneralReport.entity.SmartPostFuneralReport;
import org.jeecg.modules.smartPostFuneralReport.vo.FuneralReport;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.smartPostMarriage.entity.SmartPostMarriageReport;

import java.util.Date;
import java.util.List;

/**
 * @Description: 丧事事后报备表
 * @Author: jeecg-boot
 * @Date:   2022-02-26
 * @Version: V1.0
 */
public interface ISmartPostFuneralReportService extends IService<SmartPostFuneralReport> {
    FuneralReport getFuneralReport(String id);
    List<FuneralReport> listByIds(List<String> ids);
    SmartPostFuneralReport getByPreId(String preId);
}
